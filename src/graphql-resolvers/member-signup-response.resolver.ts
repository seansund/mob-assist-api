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
  MemberModel, MemberSignupResponseInputModel,
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
    // eslint-disable-next-line
    const responses: MemberSignupResponse[] = await this.service.list(filters) as any;

    initializeContext(context, ...responses);

    return entitiesToModels(responses);
  }

  @query(() => [MemberSignupResponse])
  async listAllResponses(
    @Ctx() context: MemberSignupResponseContext,
    @arg('filter', () => MemberSignupResponseFilter, {nullable: true}) filters?: MemberSignupResponseFilter
  ): Promise<MemberSignupResponseModel[]> {
    // eslint-disable-next-line
    const responses: MemberSignupResponse[] = await this.service.listWithFillers(filters) as any;

    initializeContext(context, ...responses);

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

  @mutation(() => MemberSignupResponse)
  async setResponseAssignments(
    @arg('id', () => ID) id: string,
    @arg('assignmentIds', () => [String]) assignmentIds: string[],
  ): Promise<MemberSignupResponseModel> {
    return this.service.setAssignments(id, assignmentIds);
  }

  @fieldResolver(() => Member)
  async member(@root() response: MemberSignupResponse, @Ctx() context: MemberSignupResponseContext): Promise<MemberModel> {
    initializeContext(context, response);

    return this.service.getMember(response, context);
  }

  @fieldResolver(() => Option, {nullable: true})
  async option(@root() response: MemberSignupResponse, @Ctx() context: MemberSignupResponseContext): Promise<OptionModel | undefined> {
    initializeContext(context, response);

    return this.service.getOption(response, context);
  }

  @fieldResolver(() => Signup, {name: 'signup'})
  async resolveSignup(@root() response: MemberSignupResponse, @Ctx() context: MemberSignupResponseContext): Promise<SignupModel> {
    initializeContext(context, response);

    return this.service.getSignup(response, context);
  }

  @fieldResolver(() => Assignment, {nullable: true})
  async assignments(@root() response: MemberSignupResponse, @Ctx() context: MemberSignupResponseContext): Promise<AssignmentModel[]> {
    initializeContext(context, response);

    return this.service.getAssignments(response, context);
  }
}

const initializeContext = (context: Partial<MemberSignupResponseContext>, ...responses: MemberSignupResponse[]) => {
  if (!context.responses) {
    context.responses = Promise.resolve(entitiesToModels(responses))
  }
}
