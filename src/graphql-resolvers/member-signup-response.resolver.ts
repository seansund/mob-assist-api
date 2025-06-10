import {inject} from '@loopback/core';
import {
  arg, Ctx,
  fieldResolver,
  ID,
  mutation,
  query,
  resolver, root,
} from '@loopback/graphql';

import {
  AssignmentModel,
  MemberModel,
  MemberSignupResponseModel,
  OptionModel, SignupModel,
} from '../datatypes';
import {
  Assignment,
  Member,
  MemberSignupResponse,
  MemberSignupResponseFilter,
  MemberSignupResponseInput,
  MemberSignupResponseUpdateInput, Signup,
} from '../models';
import {
  MEMBER_SIGNUP_RESPONSE_API,
  MemberSignupResponseApi,
  MemberSignupResponseContext,
} from '../services';
import {entitiesToModels} from '../util';

@resolver(() => MemberSignupResponse)
export class MemberSignupResponseResolver {

  constructor(
    @inject(MEMBER_SIGNUP_RESPONSE_API) protected service: MemberSignupResponseApi,
  ) {}

  @query(() => [MemberSignupResponse])
  async listResponses(
    @Ctx() context: MemberSignupResponseContext,
    @arg('filter', () => MemberSignupResponseFilter, {nullable: true}) filters?: MemberSignupResponseFilter
  ): Promise<MemberSignupResponseModel[]> {
    const responses: MemberSignupResponseModel[] = await this.service.list(filters);

    context.responseIds = responses.map(response => (response.id ?? '').toString()).filter(v => !!v)
    context.signupIds = responses.map(response => response.signupId.toString())
    context.memberIds = Promise.resolve(responses.map(response => (response.memberId ?? '').toString()).filter(v => !!v))
    context.optionIds = Promise.resolve(responses.map(response => (response.optionId ?? '').toString()).filter(v => !!v))

    return entitiesToModels(responses);
  }

  @query(() => [MemberSignupResponse])
  async listAllResponses(
    @Ctx() context: MemberSignupResponseContext,
    @arg('filter', () => MemberSignupResponseFilter, {nullable: true}) filters?: MemberSignupResponseFilter
  ): Promise<MemberSignupResponseModel[]> {
    const responses: MemberSignupResponseModel[] = await this.service.listWithFillers(filters);

    context.responseIds = responses.map(response => (response.id ?? '').toString()).filter(v => !!v)
    context.signupIds = responses.map(response => response.signupId.toString())
    context.memberIds = Promise.resolve(responses.map(response => (response.memberId ?? '').toString()).filter(v => !!v))
    context.optionIds = Promise.resolve(responses.map(response => (response.optionId ?? '').toString()).filter(v => !!v))

    return entitiesToModels(responses);
  }

  @query(() => MemberSignupResponse, {nullable: true})
  async getResponse(
    @arg('id', () => ID) id: string
  ): Promise<MemberSignupResponseModel | undefined> {
    return this.service.get(id);
  }

  @mutation(() => MemberSignupResponse)
  async signup(
    @arg('data') data: MemberSignupResponseInput,
  ): Promise<MemberSignupResponseModel> {
    return this.service.create(data);
  }

  @mutation(() => MemberSignupResponse)
  async updateMemberSignupResponse(
    @arg('id', () => ID) id: string,
    @arg('data') data: MemberSignupResponseUpdateInput,
  ): Promise<MemberSignupResponseModel> {
    return this.service.update(id, data);
  }

  @fieldResolver(() => Member)
  async member(@root() response: MemberSignupResponse, @Ctx() context: MemberSignupResponseContext): Promise<MemberModel> {
    return this.service.getMember(response, context);
  }

  @fieldResolver(() => Option, {nullable: true})
  async option(@root() response: MemberSignupResponse, @Ctx() context: MemberSignupResponseContext): Promise<OptionModel | undefined> {
    return this.service.getOption(response, context);
  }

  @fieldResolver(() => Signup, {name: 'signup'})
  async resolveSignup(@root() response: MemberSignupResponse, @Ctx() context: MemberSignupResponseContext): Promise<SignupModel> {
    return this.service.getSignup(response, context);
  }

  @fieldResolver(() => Assignment, {nullable: true})
  async assignments(@root() response: MemberSignupResponse, @Ctx() context: MemberSignupResponseContext): Promise<AssignmentModel[]> {
    return this.service.getAssignments(response, context);
  }
}
