import {SignupApi, SignupContext} from './signup.api';
import {
  formatDate, MemberIdentifier,
  SignupFilterModel,
  SignupInputModel,
  SignupModel,
  SignupScope,
  validateDate,
} from '../../datatypes';
import {repository, Where} from '@loopback/repository';
import {
  AssignmentRepository,
  GroupMemberRepository,
  GroupRepository, MemberRepository,
  MemberSignupResponseRepository,
  OptionRepository,
  SignupRepository,
} from '../../repositories';
import {
  Assignment,
  Group,
  MemberSignupResponse,
  Option,
  Signup,
  SignupWithRelations,
} from '../../models';

export class SignupImpl implements SignupApi {

  constructor(
    @repository('SignupRepository') protected repo: SignupRepository,
    @repository('GroupRepository') protected groupRepo: GroupRepository,
    @repository('MemberRepository') protected memberRepo: MemberRepository,
    @repository('AssignmentRepository') protected assignmentRepo: AssignmentRepository,
    @repository('OptionRepository') protected optionRepo: OptionRepository,
    @repository('GroupMemberRepository') protected groupMemberRepo: GroupMemberRepository,
    @repository('MemberSignupResponseRepository') protected responseRepo: MemberSignupResponseRepository,
  ) {}

  async create(signup: SignupInputModel): Promise<SignupModel> {
    validateDate(signup.date);

    return this.repo.create(signup);
  }

  async duplicate(fromSignupId: string, date: string): Promise<SignupModel> {
    validateDate(date);

    const fromSignup: SignupWithRelations | null = await this.repo.findOne({where: {id: fromSignupId}});

    if (!fromSignup) {
      throw new Error('Signup not found: ' + fromSignupId);
    }

    const signup: SignupModel = Object.assign(fromSignup, {date});
    delete signup.id;

    return this.repo.create(signup);
  }

  async delete(signupId: string): Promise<SignupModel | undefined> {
    const signup: SignupModel | null = await this.repo.findOne({where: {id: signupId}});

    if (!signup) {
      throw new Error('Signup not found: ' + signupId);
    }

    await this.repo.deleteById(signupId);

    return signup;
  }

  get(signupId: string): Promise<SignupModel | undefined> {
    return this.repo.findOne({where: {id: signupId}})
      .then(result => result as SignupModel);
  }

  async list(filter?: SignupFilterModel): Promise<SignupModel[]> {

    const groupIds = await getGroupIds(this.groupMemberRepo, filter?.memberId);

    const where: Where<Signup> = buildSignupFilterWhere(groupIds, filter);

    // TODO add pagination
    return this.repo
      .find({
        where,
        order: ['date ASC'],
      });
  }

  async update(signupId: string, data: Partial<SignupModel>): Promise<SignupModel> {

    const signup: SignupWithRelations | null = await this.repo.findOne({where: {id: signupId}});

    if (!signup) {
      throw new Error('Signup not found: ' + signupId);
    }

    const updatedSignup: SignupModel = Object.assign(signup, data);

    await this.repo.updateById(signupId, updatedSignup);

    return updatedSignup;
  }

  async getAssignments(signup: Signup, context: SignupContext = buildDefaultSignupContext(signup)): Promise<Assignment[]> {

    if (!context?.assignments) {
      context.assignments = this.assignmentRepo
        .find({
          where: {assignmentSetId: {inq: context?.assignmentSetIds || [signup.assignmentSetId.toString()]}},
        });
    }

    const assignments = await context?.assignments;

    return assignments.filter(assignment => assignment.assignmentSetId.toString() === signup.assignmentSetId.toString());
  }

  async getGroup(signup: Signup, context: SignupContext = buildDefaultSignupContext(signup)): Promise<Group> {

    if (!context.groups) {
      context.groups = this.groupRepo
        .find({
          where: {id: {inq: context.groupIds || [signup.groupId.toString()]}},
        });
    }

    const groups = await context.groups

    return groups.filter(group => group.getId() === signup.groupId)[0]
  }

  async getOptions(signup: Signup, context: SignupContext = buildDefaultSignupContext(signup)): Promise<Option[]> {

    if (!context.options) {
      context.options = this.optionRepo
        .find({
          where: {optionSetId: {inq: context.optionSetIds || [signup.optionSetId.toString()]}},
          order: ['sortIndex ASC'],
        });
    }

    const options = await context.options;

    return options.filter(option => option.optionSetId.toString() === signup.optionSetId.toString());
  }

  async getResponses(signup: Signup, context: SignupContext = buildDefaultSignupContext(signup)): Promise<MemberSignupResponse[]> {

    const where: Where<MemberSignupResponse> = context.memberId
      ? {memberId: context.memberId}
      : {}

    if (!context.responses) {
      context.responses = this.responseRepo
        .find({
          where: {
            ...where,
            signupId: {inq: context.signupIds || [signup.getId().toString()]}
          }
        })
    }

    const responses = await context.responses;

    return responses.filter(res => res.signupId.toString() === signup.getId().toString());
  }

  async listForMember(where: MemberIdentifier): Promise<SignupModel[]> {

    const member = await this.memberRepo.findOne({where});

    if (!member) {
      return [];
    }

    const groupIds = await getGroupIds(this.groupMemberRepo, member.getId());

    if (groupIds) {
      return this.repo.find({where: {groupId: {inq: groupIds}}});
    }

    return [];
  }

}

const buildDefaultSignupContext = (signup: Signup): SignupContext => {
  return {
    signupIds: signup.id ? [signup.id] : [],
    groupIds: signup.groupId ? [signup.groupId] : [],
    optionSetIds: signup.optionSetId ? [signup.optionSetId] : [],
    assignmentSetIds: signup.assignmentSetId ? [signup.assignmentSetId] : [],
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

const getGroupIds = async (groupMemberRepo: GroupMemberRepository, memberId?: string): Promise<string[] | undefined> => {
  if (!memberId) {
    return undefined;
  }

  const groupMembers = await groupMemberRepo.find({where: {memberId}});

  return groupMembers.map(groupMember => groupMember.groupId);
}
