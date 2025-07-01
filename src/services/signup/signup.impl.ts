import {SignupApi, SignupContext} from './signup.api';
import {
  AssignmentModel,
  formatDate,
  GroupModel,
  isMemberEmail,
  isMemberId,
  isMemberPhone,
  MemberIdentifier,
  MemberModel,
  MemberSignupResponseModel,
  OptionModel,
  OptionSummaryModel,
  SignupFilterModel,
  SignupInputModel,
  SignupModel,
  SignupScope,
  validateDate,
} from '../../datatypes';
import {repository, Where} from '@loopback/repository';
import {
  GroupMemberRepository,
  MemberRepository,
  SignupRepository,
} from '../../repositories';
import {Assignment, Signup} from '../../models';
import {entitiesToModels, entityToModel, Optional, unique} from '../../util';
import {inject} from '@loopback/core';
import {CONTEXT_RESOLVER_API, ContextResolverApi} from '../context-resolver';
import dayjs from 'dayjs';

export class SignupImpl implements SignupApi {

  constructor(
    @repository('SignupRepository') protected repo: SignupRepository,
    @repository('MemberRepository') protected memberRepo: MemberRepository,
    @repository('GroupMemberRepository') protected groupMemberRepo: GroupMemberRepository,
    @inject(CONTEXT_RESOLVER_API) protected resolver: ContextResolverApi,
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

  async getAssignments(signup: Signup, context: SignupContext = buildDefaultContext(signup)): Promise<AssignmentModel[]> {

    const assignments: Assignment[] = await this.resolver.populateAssignmentsContext(context);

    // TODO generic filter function
    return assignments
      .filter(assignment => assignment.assignmentSetId.toString() === (signup.assignmentSetId ?? '').toString())
      .map(entityToModel);
  }

  async getGroup(signup: Signup, context: SignupContext = buildDefaultContext(signup)): Promise<GroupModel> {

    const groups = await this.resolver.populateGroupsContext(context)

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

  async getOptions(signup: Signup, context: SignupContext = buildDefaultContext(signup)): Promise<OptionModel[]> {

    const options = await this.resolver.populateOptionsContext(context);

    return options
      .filter(option => option.optionSetId.toString() === signup.optionSetId.toString())
      .map(entityToModel);
  }

  async getResponses(signup: Signup, context: SignupContext = buildDefaultContext(signup)): Promise<MemberSignupResponseModel[]> {

    const responses = await this.resolver.populateResponsesContext(context);

    return responses
      .filter(res => res.signupId.toString() === signup.getId().toString())
      .map(entityToModel);
  }

  async getMembers(signup: Signup, context: SignupContext = buildDefaultContext(signup)): Promise<MemberModel[]> {

    const groupMembers = await this.resolver.populateGroupMembersContext(context);
    const allMembers = await this.resolver.populateMembersContext(context);

    const signupMemberIds: string[] = groupMembers
      .filter(groupMember => groupMember.groupId.toString() === signup.groupId.toString())
      .map(groupMember => groupMember.memberId.toString())

    return allMembers
      .filter(member => signupMemberIds.includes(member.getId().toString()))
      .map(entityToModel)
  }

  async getResponseSummaries(signup: Signup, context: SignupContext = buildDefaultContext(signup)): Promise<OptionSummaryModel[]> {

    const responses = await this.resolver.populateResponsesContext(context);
    const options = await this.resolver.populateOptionsContext(context);

    const totalMembers = await this.getMembers(signup, context);

    const signupResponses = responses
      .filter(response => response.signupId.toString() === signup.getId().toString());

    const optionSummaries: OptionSummaryModel[] = options.map(option => ({count: 0, assignmentCount: 0, option: entityToModel(option)}))
    optionSummaries.push({count: totalMembers.length - countSignedUpUsers(signupResponses), assignmentCount: 0})

    return signupResponses
      .filter(response => response.signedUp)
      .reduce((partialResult: OptionSummaryModel[], current: MemberSignupResponseModel) => {
        const summary = partialResult
          .find(currentSummary => (currentSummary.option?.id ?? '').toString() === (current.optionId ?? 'x').toString())

        if (summary) {
          summary.count += 1
          summary.assignmentCount += (current.assignments ?? []).length
        } else {
          console.log('Summary not found for matching option: ', {summaries: partialResult, optionId: current.optionId})
        }

        return partialResult;
      }, optionSummaries)
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

const buildDefaultContext = (signup: Signup): SignupContext => {
  return {
    signups: Promise.resolve([signup]),
  }
}

const buildSignupFilterWhere = (groupIds?: string[], filter?: SignupFilterModel): Where<Signup> => {

  const scope: SignupScope = filter?.scope ?? SignupScope.UPCOMING;

  const and: Where<object>[] = [];

  if (groupIds && groupIds.length > 0) {
    and.push({groupId: {inq: groupIds}});
  }

  switch (scope) {
    case SignupScope.FUTURE: {
      and.push({date: {gte: formatDate()}});

      break;
    }
    case SignupScope.UPCOMING: {
      const future = dayjs().add(120, 'day');

      and.push({date: {gte: formatDate()}});
      and.push({date: {lt: formatDate(future.toDate())}});

      break;
    }
  }

  if (and.length === 0) {
    return {};
  }

  return {and};
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


const countSignedUpUsers = (responses: MemberSignupResponseModel[]): number => {
  return unique(responses
    .filter(response => response.signedUp)
    .map(response => response.memberId.toString())).length
}
