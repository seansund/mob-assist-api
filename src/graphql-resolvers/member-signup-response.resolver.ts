import {inject} from '@loopback/core';
import {arg, ID, mutation, query, resolver} from '@loopback/graphql';

import {MemberSignupResponseModel} from '../datatypes';
import {
  MemberSignupResponse,
  MemberSignupResponseFilter,
  MemberSignupResponseInput,
  MemberSignupResponseUpdateInput,
} from '../models';
import {MEMBER_SIGNUP_RESPONSE_API, MemberSignupResponseApi} from '../services';

@resolver(() => MemberSignupResponse)
export class MemberSignupResponseResolver {

  constructor(
    @inject(MEMBER_SIGNUP_RESPONSE_API) protected service: MemberSignupResponseApi,
  ) {}

  @query(() => [MemberSignupResponse])
  async listResponses(
    @arg('filter', () => MemberSignupResponseFilter, {nullable: true}) filters?: MemberSignupResponseFilter
  ): Promise<MemberSignupResponseModel[]> {
    return this.service.list(filters);
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
  ) {
    return this.service.create(data);
  }

  @mutation(() => MemberSignupResponse)
  async updateMemberSignupResponse(
    @arg('id', () => ID) id: string,
    @arg('data') data: MemberSignupResponseUpdateInput,
  ): Promise<MemberSignupResponseModel> {
    return this.service.update(id, data);
  }
}
