import {inject} from '@loopback/core';
import {repository} from '@loopback/repository';
import {MemberSignupResponseApi} from './member-signup-response.api';
import {
  MemberSignupResponseFilterModel,
  MemberSignupResponseInputModel,
  MemberSignupResponseModel,
} from '../../datatypes';
import {MemberSignupResponseRepository} from '../../repositories';
import {MemberSignupResponse} from '../../models';
import {MEMBER_API, MemberApi} from '../member';
import {SIGNUP_API, SignupApi} from '../signup';

export class MemberSignupResponseImpl implements MemberSignupResponseApi {

  constructor(
    @repository('MemberSignupResponseRepository') protected repo: MemberSignupResponseRepository,
    @inject(MEMBER_API) protected memberApi: MemberApi,
    @inject(SIGNUP_API) protected signupApi: SignupApi,
  ) {}

  async create(data: MemberSignupResponseInputModel): Promise<MemberSignupResponseModel> {

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

  async delete(id: string): Promise<MemberSignupResponseModel | undefined> {

    const memberSignupResponse: MemberSignupResponse | null = await this.repo.findOne({where: {id}});

    if (memberSignupResponse) {
      await this.repo.deleteById(id);
    }

    return memberSignupResponse ?? undefined;
  }

  async get(id: string): Promise<MemberSignupResponseModel | undefined> {
    return this.repo.findOne({where: {id}})
      .then(value => value ?? undefined);
  }

  async list(filters?: MemberSignupResponseFilterModel): Promise<MemberSignupResponseModel[]> {
    // TODO filter and pagination
    const filter = filters
      ? {where: {...filters}}
      : {};

    return this.repo.find(filter);
  }

  async update(id: string, data: Partial<MemberSignupResponseModel>): Promise<MemberSignupResponseModel> {

    const memberSignupResponse: MemberSignupResponse | null = await this.repo.findOne({where: {id}});
    if (!memberSignupResponse) {
      throw new Error('MemberSignupResponse not found: ' + id);
    }

    const updatedMemberSignupResponse: MemberSignupResponse = Object.assign(memberSignupResponse, data);

    await this.repo.updateById(id, updatedMemberSignupResponse);

    return updatedMemberSignupResponse;
  }

  async listWithFillers(filters?: MemberSignupResponseFilterModel, fillersOnly?: boolean): Promise<MemberSignupResponseModel[]> {

    if (filters?.signupId && filters?.memberId) {

      const signup = await this.signupApi.get(filters?.signupId);

      if (!signup) {
        throw new Error('Signup not found: ' + filters?.signupId);
      }

      const member = await this.memberApi.get({id: filters?.memberId});

      if (!member) {
        throw new Error('Member not found: ' + filters?.memberId);
      }

      const responses = await this.list({memberId: filters?.memberId, signupId: filters?.signupId});

      if (responses.length > 0) {
        if (fillersOnly) {
          return []
        }

        return responses;
      }

      const emptyResponse: MemberSignupResponseModel = {
        signedUp: false,
        member,
        signup,
      };

      return [emptyResponse];

    } else if (filters?.signupId) {

      const signup = await this.signupApi.get(filters?.signupId);

      if (!signup) {
        throw new Error('Signup not found: ' + filters?.signupId);
      }

      const members = await this.memberApi.listForSignup(filters?.signupId);

      const responses = await this.list({signupId: filters?.signupId});

      return members.flatMap(member => {
        const memberResponses = responses
          .filter(value => (value as MemberSignupResponse).memberId === member.id);

        if (memberResponses.length > 0) {
          if (fillersOnly) {
            return []
          }

          return memberResponses;
        }

        const emptyResponse: MemberSignupResponseModel = {
          signedUp: false,
          member,
          signup,
        };

        return [emptyResponse];
      })

    } else if (filters?.memberId) {

      const member = await this.memberApi.get({id: filters?.memberId});

      if (!member) {
        throw new Error('Member not found: ' + filters?.memberId);
      }

      const signups = await this.signupApi.listForMember({id: filters?.memberId});

      const responses = await this.list({memberId: filters?.memberId});

      return signups.flatMap(signup => {
        const memberResponses = responses
          .filter(value => (value as MemberSignupResponse).signupId === signup.id);

        if (memberResponses.length > 0) {
          if (fillersOnly) {
            return []
          }

          return memberResponses;
        }

        const emptyResponse: MemberSignupResponseModel = {
          signedUp: false,
          member,
          signup,
        };

        return [emptyResponse];
      })
    }

    return [];
  }
}
