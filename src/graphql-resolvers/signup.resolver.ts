import {
  arg,
  Ctx,
  fieldResolver,
  ID,
  mutation,
  query,
  resolver,
  ResolverInterface,
  root,
} from '@loopback/graphql';

import {
  GroupModel,
  MemberSignupResponseModel,
  OptionModel,
  SignupModel,
} from '../datatypes';
import {
  Assignment,
  Group,
  MemberSignupResponse,
  Option,
  Signup,
  SignupFilter,
  SignupInput,
  SignupUpdateModel,
} from '../models';
import {SIGNUP_API, SignupApi, SignupContext} from '../services';
import {inject} from '@loopback/core';

@resolver(() => Signup)
export class SignupResolver implements ResolverInterface<Signup> {

  constructor(
    @inject(SIGNUP_API) protected service: SignupApi,
  ) {}

  @mutation(() => Signup)
  async createSignup(
    @arg('signup') signup: SignupInput
  ): Promise<SignupModel> {
    return this.service.create(signup);
  }

  @mutation(() => Signup)
  async duplicateSignup(
    @arg('fromSignupId', () => ID) fromSignupId: string,
    @arg('date') date: string,
  ): Promise<SignupModel> {
    return this.service.duplicate(fromSignupId, date);
  }

  @mutation(() => Signup)
  async updateSignup(
    @arg('signupId', () => ID) signupId: string,
    @arg('data') data: SignupUpdateModel,
  ): Promise<SignupModel> {
    return this.service.update(signupId, data);
  }

  @query(() => Signup, {nullable: true})
  async getSignup(@arg('signupId', () => ID) signupId: string): Promise<SignupModel | undefined> {
    return this.service.get(signupId);
  }

  @query(() => [Signup])
  async listSignups(
    @Ctx() context: SignupContext,
    @arg('filter', () => SignupFilter, {nullable: true}) filter?: SignupFilter
  ): Promise<SignupModel[]> {

    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    const result: Signup[] = await this.service.list(filter) as any;

    context.memberId = filter?.memberId;
    context.signupIds = unique(result.map(signup => signup.id as string).map(toString))
    context.groupIds = unique(result.map(signup => signup.groupId as string).map(toString))
    context.optionSetIds = unique(result.map(signup => signup.optionSetId as string).map(toString))
    context.assignmentSetIds = unique(result.map(signup => signup.assignmentSetId as string).map(toString))

    return result;
  }

  @mutation(() => Signup)
  async deleteSignup(@arg('signupId', () => ID) signupId: string): Promise<SignupModel | undefined> {
    return this.service.delete(signupId);
  }

  @fieldResolver(() => [Option])
  async options(@root() signup: Signup, @Ctx() context: SignupContext): Promise<OptionModel[]> {
    return this.service.getOptions(signup, context);
  }

  @fieldResolver(() => [Assignment])
  async assignments(@root() signup: Signup, @Ctx() context: SignupContext): Promise<Assignment[]> {
    return this.service.getAssignments(signup, context);
  }

  @fieldResolver(() => Group)
  async group(@root() signup: Signup, @Ctx() context: SignupContext): Promise<GroupModel> {
    return this.service.getGroup(signup, context);
  }

  @fieldResolver(() => [MemberSignupResponse])
  async responses(@root() signup: Signup, @Ctx() context: SignupContext): Promise<MemberSignupResponseModel[]> {
    return this.service.getResponses(signup, context);
  }
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
