import {SignupApi, SignupContext} from './signup.api';
import {
  AssignmentModel,
  formatDate,
  GroupModel,
  isMemberEmail,
  isMemberId,
  isMemberPhone,
  MemberIdentifier, MemberModel,
  MemberSignupResponseModel,
  OptionModel, OptionSummaryModel,
  SignupFilterModel,
  SignupInputModel,
  SignupModel,
  SignupScope,
  validateDate,
} from '../../datatypes';
import {repository, Where} from '@loopback/repository';
import {
  AssignmentRepository, AssignmentSetRepository,
  GroupMemberRepository,
  GroupRepository,
  MemberRepository,
  MemberSignupResponseRepository,
  OptionRepository,
  SignupRepository,
} from '../../repositories';
import {
  Assignment, AssignmentSet,
  Group, GroupMember,
  Member,
  MemberSignupResponse,
  Option,
  Signup,
} from '../../models';
import {entitiesToModels, entityToModel, Optional, unique} from '../../util';

export class SignupImpl implements SignupApi {

  constructor(
    @repository('SignupRepository') protected repo: SignupRepository,
    @repository('GroupRepository') protected groupRepo: GroupRepository,
    @repository('MemberRepository') protected memberRepo: MemberRepository,
    @repository('AssignmentSetRepository') protected assignmentSetRepo: AssignmentSetRepository,
    @repository('AssignmentRepository') protected assignmentRepo: AssignmentRepository,
    @repository('OptionRepository') protected optionRepo: OptionRepository,
    @repository('GroupMemberRepository') protected groupMemberRepo: GroupMemberRepository,
    @repository('MemberSignupResponseRepository') protected responseRepo: MemberSignupResponseRepository,
  ) {}

  async create(signup: SignupInputModel): Promise<SignupModel> {
    validateDate(signup.date);

    return this.repo.create(signup).then(entityToModel);
  }

  async duplicate(fromSignupId: string, date: string): Promise<SignupModel> {
    validateDate(date);

    const fromSignup: Optional<SignupModel, 'id'> | null = await this.repo
      .findOne({where: {id: fromSignupId}});

    if (!fromSignup) {
      throw new Error('Signup not found: ' + fromSignupId);
    }

    const signup: Optional<SignupModel, 'id'> = Object.assign(fromSignup, {date});
    delete signup.id;

    return this.repo.create(signup).then(entityToModel);
  }

  async delete(signupId: string): Promise<SignupModel | undefined> {
    const signup: SignupModel | null = await this.repo
      .findOne({where: {id: signupId}})
      .then(entityToModel);

    if (!signup) {
      throw new Error('Signup not found: ' + signupId);
    }

    await this.repo.deleteById(signupId);

    return signup;
  }

  async get(signupId: string): Promise<SignupModel | undefined> {
    return this.repo.findOne({where: {id: signupId}})
      .then(result => result as SignupModel);
  }

  async list(filter?: SignupFilterModel, signupContext?: SignupContext): Promise<SignupModel[]> {

    const {memberId, groupIds} = await getGroupIds(this.groupMemberRepo, this.memberRepo, filter?.memberId);

    if (signupContext) {
      signupContext.memberId = memberId;
    }

    const where: Where<Signup> = buildSignupFilterWhere(groupIds, filter);

    // TODO add pagination
    return this.repo
      .find({
        where,
        order: ['date ASC'],
      })
      .then(entitiesToModels);
  }

  async update(signupId: string, data: Partial<SignupModel>): Promise<SignupModel> {

    const signup: SignupModel | null = await this.repo
      .findOne({where: {id: signupId}})
      .then(entityToModel);

    if (!signup) {
      throw new Error('Signup not found: ' + signupId);
    }

    const updatedSignup: SignupModel = Object.assign(signup, data);

    await this.repo.updateById(signupId, updatedSignup);

    return updatedSignup;
  }

  async getAssignments(signup: Signup, context: SignupContext = buildDefaultSignupContext(signup)): Promise<AssignmentModel[]> {

    const assignments: Assignment[] = await this.populateAssignmentsContext(signup, context);

    // TODO generic filter function
    return assignments
      .filter(assignment => assignment.assignmentSetId.toString() === signup.assignmentSetId.toString())
      .map(entityToModel);
  }

  async getGroup(signup: Signup, context: SignupContext = buildDefaultSignupContext(signup)): Promise<GroupModel> {

    const groups = await this.populateGroupsContext(signup, context)

    const matchingGroup: GroupModel | undefined = groups
      .map(entityToModel)
      .find(group => group.getId() === signup.groupId.toString())

    if (!matchingGroup) {
      console.log('Group is empty!!!')
      return {
        id: '1',
        name: 'Default'
      }
    }

    return matchingGroup
  }

  async getOptions(signup: Signup, context: SignupContext = buildDefaultSignupContext(signup)): Promise<OptionModel[]> {

    const options = await this.populateOptionsContext(signup, context);

    return options
      .filter(option => option.optionSetId.toString() === signup.optionSetId.toString())
      .map(entityToModel);
  }

  async getResponses(signup: Signup, context: SignupContext = buildDefaultSignupContext(signup)): Promise<MemberSignupResponseModel[]> {

    const responses = await this.populateResponsesContext(signup, context);

    await this.populateMemberIdContext(signup, context);
    await this.populateOptionsContext(signup, context);

    return responses
      .filter(res => res.signupId.toString() === signup.getId().toString())
      .map(entityToModel);
  }

  async getMembers(signup: Signup, context: SignupContext): Promise<MemberModel[]> {

    const groupMembers = await this.populateGroupMemberContext(signup, context);
    const allMembers = await this.populateMembersContext(signup, context);

    const signupMemberIds: string[] = groupMembers
      .filter(groupMember => groupMember.groupId.toString() === signup.groupId.toString())
      .map(groupMember => groupMember.memberId.toString())

    return allMembers
      .filter(member => signupMemberIds.includes(member.getId().toString()))
      .map(entityToModel)
  }

  async getResponseSummaries(signup: Signup, context: SignupContext): Promise<OptionSummaryModel[]> {

    const responses = await this.populateResponsesContext(signup, context);
    const options = await this.populateOptionsContext(signup, context);

    const totalMembers = await this.getMembers(signup, context);

    const optionSummaries: OptionSummaryModel[] = options.map(option => ({count: 0, assignmentCount: 0, option: entityToModel(option)}))
    optionSummaries.push({count: totalMembers.length - countSignedUpUsers(responses), assignmentCount: 0})

    return responses
      .filter(response => response.signedUp)
      .reduce((partialResult: OptionSummaryModel[], current: MemberSignupResponse) => {
        const summary = partialResult
          .find(currentSummary => currentSummary.option?.id.toString() === current.optionId.toString())

        if (summary) {
          summary.count += 1
          summary.assignmentCount += (current.assignments ?? []).length
        } else {
          console.log('Summary not found for matching option: ', {summaries: partialResult, optionId: current.optionId})
        }

        return partialResult;
      }, optionSummaries)
  }

  async populateAssignmentSetsContext(signup: Signup, context: SignupContext): Promise<AssignmentSet[]> {
    if (!context.assignmentSets) {
      context.assignmentSets = new Promise<AssignmentSet[]>((resolve, reject) => {
        context.signups
          .then(signups => {
            const assignmentSetIds = signups.map(s => s.assignmentSetId.toString())

            this.assignmentSetRepo.find({where: {id: {inq: assignmentSetIds}}})
              .then(resolve)
              .catch(reject)
          })
          .catch(reject)
      })
    }

    return context.assignmentSets
  }

  async populateAssignmentsContext(signup: Signup, context: SignupContext = buildDefaultSignupContext(signup)): Promise<Assignment[]> {
    if (!context.assignments) {
      context.assignments = new Promise<Assignment[]>((resolve, reject) => {
        this.populateAssignmentSetsContext(signup, context)
          .then((assignmentSets: AssignmentSet[]) => {
            const assignmentSetIds: string[] = assignmentSets.map(assignmentSet => assignmentSet.getId().toString())

            this.assignmentRepo.find({where: {assignmentSetId: {inq: assignmentSetIds}}})
              .then(resolve)
              .catch(reject)
          })
          .catch(reject)
      })
    }

    return context.assignments;
  }

  async populateResponsesContext(signup: Signup, context: SignupContext): Promise<MemberSignupResponse[]> {
    const where: Where<MemberSignupResponse> = context.memberId
      ? {memberId: context.memberId}
      : {}

    if (!context.responses) {
      context.responses = new Promise<MemberSignupResponse[]>((resolve, reject) => {
        context.signups
          .then(signups => {
            const signupIds: string[] = signups.map(s => s.getId().toString())

            this.responseRepo
              .find({where: {...where, signupId: {inq: signupIds}}})
              .then(resolve)
              .catch(reject)
          })
          .catch(reject)
      })
    }

    return context.responses
  }

  async populateOptionsContext(signup: Signup, context: SignupContext): Promise<Option[]> {

    if (!context.options) {
      context.options = new Promise<Option[]>((resolve, reject) => {
        context.signups
          .then(signups => {
            const optionSetIds = signups.map(s => s.optionSetId.toString());

            this.optionRepo
              .find({
                where: {optionSetId: {inq: optionSetIds}},
                order: ['sortIndex ASC'],
              })
              .then(resolve)
              .catch(reject)
          })
          .catch(reject)
      })
    }

    return context.options
  }

  async populateGroupsContext(signup: Signup, context: SignupContext): Promise<Group[]> {
    if (!context.groups) {
      context.groups = new Promise((resolve, reject) => {
        context.signups
          .then(signups => {
            const groupIds = signups.map(s => s.groupId.toString())

            this.groupRepo
              .find({where: {id: {inq: groupIds}}})
              .then(resolve)
              .catch(reject)
          })
          .catch(reject)
      })
    }

    return context.groups
  }

  async populateGroupMemberContext(signup: Signup, context: SignupContext): Promise<GroupMember[]> {
    if (!context.groupMembers) {
      context.groupMembers = new Promise<GroupMember[]>((resolve, reject) => {
        context.signups
          .then(signups => {
            const groupIds = signups.map(s => s.groupId.toString())

            this.groupMemberRepo
              .find({where: {groupId: {inq: groupIds}}})
              .then(resolve)
              .catch(reject)
          })
          .catch(reject)
      })
    }

    return context.groupMembers
  }

  async populateMemberIdContext(signup: Signup, context: SignupContext): Promise<string[]> {
    return new Promise<string[]>((resolve, reject) => {
        this.populateGroupMemberContext(signup, context)
          .then(groupMembers => unique(groupMembers.map(groupMember => groupMember.memberId.toString())))
          .then(resolve)
          .catch(reject)
      })
  }

  async populateMembersContext(signup: Signup, context: SignupContext): Promise<Member[]> {
    if (!context.members) {
      const memberIds = await this.populateMemberIdContext(signup, context);

      context.members = this.memberRepo.find({where: {id: {inq: memberIds}}})
    }

    return context.members;
  }

  async listForMember(where: MemberIdentifier, context?: SignupContext): Promise<SignupModel[]> {

    const {memberId, groupIds} = await getGroupIds(this.groupMemberRepo, this.memberRepo, where);

    if (context) {
      context.memberId = memberId;
    }

    if (groupIds) {
      // eslint-disable-next-line
      return this.repo.find({where: {groupId: {inq: groupIds}}}) as Promise<any>;
    }

    return [];
  }

}

const buildDefaultSignupContext = (signup: Signup): SignupContext => {
  return {
    signups: Promise.resolve([signup]),
  }
}

const buildSignupFilterWhere = (groupIds?: string[], filter?: SignupFilterModel): Where<Signup> => {

  const scope: SignupScope = filter?.scope ?? SignupScope.UPCOMING;

  const where: Where<Signup> = groupIds && groupIds.length > 0 ?
    {groupId: {inq: groupIds}} :
    {};

  switch (scope) {
    case SignupScope.FUTURE:
      return {...where, date: {gte: formatDate(new Date())}};
    case SignupScope.UPCOMING:
      return {...where, date: {gte: formatDate(new Date()), lt: formatDate(new Date(new Date().getTime() + 90 * 24 * 60 * 60 * 1000))}};
    default:
      return where
  }
}

const getGroupIds = async (groupMemberRepo: GroupMemberRepository, memberRepo: MemberRepository, memberIdentity?: MemberIdentifier): Promise<{memberId?: string, groupIds?: string[]}> => {

  const memberId = isMemberId(memberIdentity) ? memberIdentity.id : await getMemberId(memberRepo, memberIdentity);

  if (!memberId) {
    return {};
  }

  const groupMembers = await groupMemberRepo.find({where: {memberId}});

  return {
    memberId,
    groupIds: groupMembers.map(groupMember => groupMember.groupId),
  };
}

const getMemberId = async (memberRepo: MemberRepository, memberIdentity?: MemberIdentifier): Promise<string | undefined> => {

  const where = isMemberEmail(memberIdentity)
    ? {email: memberIdentity.email}
    : isMemberPhone(memberIdentity)
      ? {phone: memberIdentity.phone}
      : undefined;

  if (!where) {
    return undefined;
  }

  const member = await memberRepo.findOne({where})

  if (!member) {
    console.log('  Member not found: ', {where})
  }

  return member?.id
}


const countSignedUpUsers = (responses: MemberSignupResponse[]): number => {
  return unique(responses
    .filter(response => response.signedUp)
    .map(response => response.memberId.toString())).length
}
