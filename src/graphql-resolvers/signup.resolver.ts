import {repository, Where} from '@loopback/repository';
import {
  arg,
  Ctx,
  fieldResolver, ID,
  mutation,
  query,
  resolver,
  ResolverInterface,
  root,
} from '@loopback/graphql';

import {
  formatDate,
  GroupModel,
  MemberSignupResponseModel,
  OptionModel,
  SignupModel,
  SignupScope,
  validateDate,
} from '../datatypes';
import {
  Assignment,
  Group, MemberSignupResponse,
  Option,
  Signup,
  SignupFilter,
  SignupInput,
  SignupUpdateModel,
  SignupWithRelations,
} from '../models';
import {
  AssignmentRepository,
  GroupMemberRepository,
  GroupRepository, MemberSignupResponseRepository,
  OptionRepository,
  SignupRepository,
} from '../repositories';

interface SignupContext {
  signupIds: string[];
  groupIds: string[];
  optionSetIds: string[];
  assignmentSetIds: string[];
  memberId?: string;

  groups?: Promise<Group[]>;
  options?: Promise<Option[]>;
  assignments?: Promise<Assignment[]>;
  responses?: Promise<MemberSignupResponse[]>;
}

@resolver(() => Signup)
export class SignupResolver implements ResolverInterface<Signup> {

  constructor(
    @repository('SignupRepository') protected repo: SignupRepository,
    @repository('GroupRepository') protected groupRepo: GroupRepository,
    @repository('AssignmentRepository') protected assignmentRepo: AssignmentRepository,
    @repository('OptionRepository') protected optionRepo: OptionRepository,
    @repository('GroupMemberRepository') protected groupMemberRepo: GroupMemberRepository,
    @repository('MemberSignupResponseRepository') protected responseRepo: MemberSignupResponseRepository,
  ) {}

  @mutation(() => Signup)
  async createSignup(
    @arg('signup') signup: SignupInput
  ): Promise<SignupModel> {
    validateDate(signup.date);

    return this.repo.create(signup);
  }

  @mutation(() => Signup)
  async duplicateSignup(
    @arg('fromSignupId', () => ID) fromSignupId: string,
    @arg('date') date: string,
  ): Promise<SignupModel> {
    validateDate(date);

    const fromSignup: SignupWithRelations | null = await this.repo.findOne({where: {id: fromSignupId}});

    if (!fromSignup) {
      throw new Error('Signup not found: ' + fromSignupId);
    }

    const signup: SignupModel = Object.assign(fromSignup, {date});
    delete signup.id;

    return this.repo.create(signup);
  }

  @mutation(() => Signup)
  async updateSignup(
    @arg('signupId', () => ID) signupId: string,
    @arg('data') data: SignupUpdateModel,
  ): Promise<SignupModel> {

    const signup: SignupWithRelations | null = await this.repo.findOne({where: {id: signupId}});

    if (!signup) {
      throw new Error('Signup not found: ' + signupId);
    }

    const updatedSignup: SignupModel = Object.assign(signup, data);

    await this.repo.updateById(signupId, updatedSignup);

    return updatedSignup;
  }

  @query(() => Signup, {nullable: true})
  async getSignup(@arg('signupId', () => ID) signupId: string): Promise<SignupModel | null> {
    return this.repo.findOne({where: {id: signupId}});
  }

  @query(() => [Signup])
  async listSignups(
    @Ctx() context: SignupContext,
    @arg('memberId', {nullable: true}) memberId?: string,
    @arg('filter', () => SignupFilter, {nullable: true}) filter?: SignupFilter
  ): Promise<SignupModel[]> {

    const groupIds = await getGroupIds(this.groupMemberRepo, memberId);

    const where: Where<Signup> = buildSignupFilterWhere(groupIds, filter);

    // TODO add pagination
    const result = await this.repo
      .find({
        where,
        order: ['date ASC'],
      });

    context.memberId = memberId;
    context.signupIds = unique(result.map(signup => signup.id as string).map(toString))
    context.groupIds = unique(result.map(signup => signup.groupId as string).map(toString))
    context.optionSetIds = unique(result.map(signup => signup.optionSetId as string).map(toString))
    context.assignmentSetIds = unique(result.map(signup => signup.assignmentSetId as string).map(toString))

    return result;
  }

  @mutation(() => Signup)
  async deleteSignup(@arg('signupId', () => ID) signupId: string): Promise<SignupModel> {
    const signup: SignupModel | null = await this.repo.findOne({where: {id: signupId}});

    if (!signup) {
      throw new Error('Signup not found: ' + signupId);
    }

    await this.repo.deleteById(signupId);

    return signup;
  }

  @fieldResolver(() => [Option])
  async options(@root() signup: Signup, @Ctx() context: SignupContext): Promise<OptionModel[]> {

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

  @fieldResolver(() => [Assignment])
  async assignments(@root() signup: Signup, @Ctx() context: SignupContext): Promise<Assignment[]> {

    if (!context.assignments) {
      context.assignments = this.assignmentRepo
        .find({
          where: {assignmentSetId: {inq: context.assignmentSetIds || [signup.assignmentSetId.toString()]}},
        });
    }

    const assignments = await context.assignments;

    return assignments.filter(assignment => assignment.assignmentSetId.toString() === signup.assignmentSetId.toString());
  }

  @fieldResolver(() => Group)
  async group(@root() signup: Signup, @Ctx() context: SignupContext): Promise<GroupModel> {

    if (!context.groups) {
      context.groups = this.groupRepo
        .find({
          where: {id: {inq: context.groupIds || [signup.groupId.toString()]}},
        });
    }

    const groups = await context.groups

    return groups.filter(group => group.getId() === signup.groupId)[0]
  }

  @fieldResolver(() => [MemberSignupResponse])
  async responses(@root() signup: Signup, @Ctx() context: SignupContext): Promise<MemberSignupResponseModel[]> {

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
}

const buildSignupFilterWhere = (groupIds?: string[], filter?: SignupFilter): Where<Signup> => {

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

const unique = <T>(list: T[]): T[] => {

  const val = list.reduce((partialResult: T[], current: T) => {

    if (!partialResult.includes(current)) {
      partialResult.push(current);
    }

    return partialResult;
  }, [])

  return val;
}

const toString = <T extends {toString: () => string}> (value: T): string => {
  return value.toString();
}
