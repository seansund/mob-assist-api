import {repository} from '@loopback/repository';
import {MemberSignupResponseRepository} from '../repositories';
import {arg, ID, mutation, query, resolver} from '@loopback/graphql';
import {
  MemberIdentityInput,
  MemberSignupResponse, MemberSignupResponseFilter,
  MemberSignupResponseInput,
  MemberSignupResponseUpdateInput,
} from '../models';
import {MemberIdentifier, MemberSignupResponseModel} from '../datatypes';

@resolver(() => MemberSignupResponse)
export class MemberSignupResponseResolver {

  constructor(
    @repository('MemberSignupResponseRepository') protected repo: MemberSignupResponseRepository
  ) {}

  @query(() => [MemberSignupResponse])
  async listResponses(
    @arg('filter', () => MemberSignupResponseFilter, {nullable: true}) filters?: MemberSignupResponseFilter
  ): Promise<MemberSignupResponseModel[]> {
    // TODO filter and pagination
    const filter = filters
      ? {where: {...filters}}
      : {};

    return this.repo.find(filter);
  }

  @query(() => MemberSignupResponse, {nullable: true})
  async getResponse(
    @arg('id', () => ID) id: string
  ): Promise<MemberSignupResponseModel | undefined> {
    return this.repo.findOne({where: {id}})
      .then(value => value ?? undefined);
  }

  @mutation(() => MemberSignupResponse)
  async signup(
    @arg('data') data: MemberSignupResponseInput,
  ) {

    const response: MemberSignupResponse | null = await this.repo.findOne({
      where: {
        memberId: data.memberId,
        signupId: data.signupId,
        optionId: data.optionId,
      }
    })

    if (!response) {
      return this.repo.create(data);
    }

    const updatedMemberSignupResponse: MemberSignupResponse = Object.assign(response, data);

    await this.repo.updateById(response.getId(), updatedMemberSignupResponse);

    return updatedMemberSignupResponse;
  }

  @mutation(() => MemberSignupResponse)
  async updateMemberSignupResponse(
    @arg('id', () => ID) id: string,
    @arg('data') data: MemberSignupResponseUpdateInput,
  ): Promise<MemberSignupResponseModel> {

    const memberSignupResponse: MemberSignupResponse | null = await this.repo.findOne({where: {id}});
    if (!memberSignupResponse) {
      throw new Error('MemberSignupResponse not found: ' + id);
    }

    const updatedMemberSignupResponse: MemberSignupResponse = Object.assign(memberSignupResponse, data);

    await this.repo.updateById(id, updatedMemberSignupResponse);

    return updatedMemberSignupResponse;
  }
}
