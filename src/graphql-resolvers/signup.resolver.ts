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
  AssignmentModel,
  GroupModel, MemberModel,
  MemberSignupResponseModel,
  OptionModel, OptionSummaryModel,
  SignupModel,
} from '../datatypes';
import {
  Assignment,
  Group, Member,
  MemberSignupResponse, MemberSignupResponseInput,
  Option, OptionSummary,
  Signup,
  SignupFilter,
  SignupInput,
  SignupUpdate,
} from '../models';
import {
  MEMBER_SIGNUP_RESPONSE_API, MemberSignupResponseApi,
  SIGNUP_API,
  SignupApi,
  SignupContext,
} from '../services';
import {inject} from '@loopback/core';
import {entitiesToModels} from '../util';

@resolver(() => Signup)
export class SignupResolver implements ResolverInterface<Signup> {

  constructor(
    @inject(SIGNUP_API) protected service: SignupApi,
    @inject(MEMBER_SIGNUP_RESPONSE_API) protected responseService: MemberSignupResponseApi,
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
    @arg('data') data: SignupUpdate,
  ): Promise<SignupModel> {
    return this.service.update(signupId, data);
  }

  @query(() => Signup, {nullable: true})
  async getSignup(
    @arg('signupId', () => ID) signupId: string
  ): Promise<SignupModel | undefined> {
    return this.service.get(signupId);
  }

  @query(() => [Signup])
  async listSignups(
    @Ctx() context: SignupContext,
    @arg('filter', () => SignupFilter, {nullable: true}) filter?: SignupFilter
  ): Promise<SignupModel[]> {

    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    const result: Signup[] = await this.service.list(filter, context) as any;

    initializeContext(context, ...result);

    return entitiesToModels(result);
  }

  @mutation(() => Signup)
  async deleteSignup(
    @arg('signupId', () => ID) signupId: string
  ): Promise<SignupModel | undefined> {
    return this.service.delete(signupId);
  }

  @mutation(() => Signup, {nullable: true})
  async respondToSignup(
    @arg('data') data: MemberSignupResponseInput
  ): Promise<SignupModel | undefined> {

    validateRespondToSignupInput(data);

    await this.responseService.create(data);

    return this.service.get(data.signupId);
  }

  @fieldResolver(() => [Option])
  async options(@root() signup: Signup, @Ctx() context: SignupContext): Promise<OptionModel[]> {
    initializeContext(context, signup);

    return this.service.getOptions(signup, context);
  }

  @fieldResolver(() => [Assignment])
  async assignments(@root() signup: Signup, @Ctx() context: SignupContext): Promise<AssignmentModel[]> {
    initializeContext(context, signup);

    return this.service.getAssignments(signup, context);
  }

  @fieldResolver(() => Group)
  async group(@root() signup: Signup, @Ctx() context: SignupContext): Promise<GroupModel> {
    initializeContext(context, signup);

    return this.service.getGroup(signup, context);
  }

  @fieldResolver(() => [MemberSignupResponse])
  async responses(@root() signup: Signup, @Ctx() context: SignupContext): Promise<MemberSignupResponseModel[]> {
    initializeContext(context, signup);

    return this.service.getResponses(signup, context);
  }

  @fieldResolver(() => [OptionSummary])
  async responseSummaries(@root() signup: Signup, @Ctx() context: SignupContext): Promise<OptionSummaryModel[]> {
    initializeContext(context, signup);

    return this.service.getResponseSummaries(signup, context);
  }

  @fieldResolver(() => [Member])
  async members(@root() signup: Signup, @Ctx() context: SignupContext): Promise<MemberModel[]> {
    initializeContext(context, signup);

    return this.service.getMembers(signup, context);
  }
}

function validateRespondToSignupInput(data: MemberSignupResponseInput) {
  if (!data.memberId) {
    throw new Error('Member id is required in signup response')
  }
  if (!data.signupId) {
    throw new Error('Signup id is required in signup response')
  }
  if (!data.optionId) {
    throw new Error('Option id is required in signup response')
  }
}

const initializeContext = (context: Partial<SignupContext>, ...signups: Signup[]) => {
  if (!context.signups) {
    context.signups = Promise.resolve(signups)
  }
}
