import {inject} from '@loopback/core';
import {repository} from '@loopback/repository';
import {
  MemberSignupResponseApi,
  MemberSignupResponseContext,
} from './member-signup-response.api';
import {
  AssignmentModel,
  MemberModel,
  MemberSignupResponseFilterModel,
  MemberSignupResponseInputModel,
  MemberSignupResponseModel, OptionModel, SignupModel,
} from '../../datatypes';
import {
  AssignmentRepository,
  MemberRepository, MemberSignupResponseAssignmentRepository,
  MemberSignupResponseRepository, OptionRepository, SignupRepository,
} from '../../repositories';
import {Member, MemberSignupResponse, Option, Signup} from '../../models';
import {MEMBER_API, MemberApi} from '../member';
import {SIGNUP_API, SignupApi} from '../signup';
import {entitiesToModels, entityToModel} from '../../util';

export class MemberSignupResponseImpl implements MemberSignupResponseApi {

  constructor(
    @repository('MemberSignupResponseRepository') protected repo: MemberSignupResponseRepository,
    @repository('MemberRepository') protected memberRepo: MemberRepository,
    @repository('OptionRepository') protected optionRepo: OptionRepository,
    @repository('SignupRepository') protected signupRepo: SignupRepository,
    @repository('AssignmentRepository') protected assignmentRepo: AssignmentRepository,
    @repository('MemberSignupResponseAssignmentRepository') protected responseAssignmentRepo: MemberSignupResponseAssignmentRepository,
    @inject(MEMBER_API) protected memberApi: MemberApi,
    @inject(SIGNUP_API) protected signupApi: SignupApi,
  ) {}

  async create(data: MemberSignupResponseInputModel): Promise<MemberSignupResponseModel> {

    const memberResponsesForSignup: MemberSignupResponse[] = await this.repo.find({
      where: {
        memberId: data.memberId,
        signupId: data.signupId,
      }
    })

    const responsesForOption: MemberSignupResponse[] = memberResponsesForSignup
      .filter(res => res.optionId.toString() === data.optionId)

    const signedUpResponsesForOtherOptions: MemberSignupResponse[] = memberResponsesForSignup
      .filter(res => res.optionId.toString() !== data.optionId)
      .filter(res => res.signedUp)

    if (signedUpResponsesForOtherOptions.length > 0) {
      const declineOption: Option | undefined = await this.getDeclineOptionForSignup(data.signupId);

      if (declineOption) {
        // if matching response option is 'declineOption' then delete/decline other options
        if (isMatchingOption(data, declineOption) && data.signedUp) {
          await Promise.all(signedUpResponsesForOtherOptions
            .map<MemberSignupResponse>(resp => Object.assign(resp, {signedUp: false}))
            .map(resp => {
              console.log('  Removing signup from other options')
              return this.repo.update(resp)
            }))
        } else if (!isMatchingOption(data, declineOption) && data.signedUp) {
          // if matching response option is not 'declineOption' but nonMatching is delete/decline
          const declineResponses: MemberSignupResponse[] = signedUpResponsesForOtherOptions
            .filter(resp => isMatchingOption(resp, declineOption))

          if (declineResponses.length > 0) {
            await Promise.all(declineResponses
              .map<MemberSignupResponse>(resp => Object.assign(resp, {signedUp: false}))
              .map(resp => {
                console.log('  Removing signup from decline option')
                return this.repo.update(resp)
              }))
          }
        }
      } else {
        console.log('Decline option not found!!!')
      }
    }

    if (responsesForOption.length === 0) {
      return this.repo.create(data).then(entityToModel);
    }

    const updatedMemberSignupResponse: MemberSignupResponse = Object.assign(responsesForOption[0], data);

    await this.repo.updateById(responsesForOption[0].getId(), updatedMemberSignupResponse);

    return entityToModel(updatedMemberSignupResponse);
  }

  async getDeclineOptionForSignup(signupId: string): Promise<Option | undefined> {
    const signup: Signup = await this.signupRepo.findById(signupId);

    return this.optionRepo
      .findOne({where: {optionSetId: signup.optionSetId.toString(), declineOption: true}})
      .then(result => result ?? undefined)
  }

  async delete(id: string): Promise<MemberSignupResponseModel | undefined> {

    const memberSignupResponse: MemberSignupResponse | null = await this.repo.findOne({where: {id}});

    if (memberSignupResponse) {
      await this.repo.deleteById(id);
    }

    return entityToModel(memberSignupResponse);
  }

  async get(id: string): Promise<MemberSignupResponseModel | undefined> {
    return this.repo.findOne({where: {id}})
      .then(entityToModel);
  }

  async list(filters?: MemberSignupResponseFilterModel): Promise<MemberSignupResponseModel[]> {
    // TODO filter and pagination
    const filter = filters
      ? {where: {...filters}}
      : {};

    return this.repo
      .find(filter)
      .then(entitiesToModels);
  }

  async listWithFillers(filters?: MemberSignupResponseFilterModel, fillersOnly?: boolean): Promise<MemberSignupResponseModel[]> {

    if (filters?.signupId && filters?.memberId) {

      const signup: SignupModel | undefined = await this.signupApi.get(filters?.signupId);

      if (!signup) {
        throw new Error('Signup not found: ' + filters?.signupId);
      }

      const member: MemberModel | undefined = await this.memberApi.get({id: filters?.memberId});

      if (!member) {
        throw new Error('Member not found: ' + filters?.memberId);
      }

      const responses: MemberSignupResponseModel[] = await this.list({memberId: filters?.memberId, signupId: filters?.signupId, signedUp: true});

      if (responses.length > 0) {
        if (fillersOnly) {
          return []
        }

        return responses;
      }

      return [createEmptyResponse(member, signup)];

    } else if (filters?.signupId) {

      const signup: SignupModel | undefined = await this.signupApi.get(filters?.signupId);

      if (!signup) {
        throw new Error('Signup not found: ' + filters?.signupId);
      }

      const members: MemberModel[] = await this.memberApi.listForSignup(filters?.signupId);

      const responses: MemberSignupResponseModel[] = await this.list({signupId: filters?.signupId, signedUp: true});

      return members.flatMap(member => {
        const memberResponses = responses
          .filter(value => value.memberId.toString() === member.id.toString());

        if (memberResponses.length > 0) {
          if (fillersOnly) {
            return []
          }

          return memberResponses;
        }

        return [createEmptyResponse(member, signup)];
      })

    } else if (filters?.memberId) {

      const member: MemberModel | undefined = await this.memberApi.get({id: filters?.memberId});

      if (!member) {
        throw new Error('Member not found: ' + filters?.memberId);
      }

      const signups: SignupModel[] = await this.signupApi.listForMember({id: filters?.memberId});

      const responses: MemberSignupResponseModel[] = await this.list({memberId: filters?.memberId, signedUp: true});

      return signups.flatMap(signup => {
        const memberResponses = responses
          .filter(value => value.signupId.toString() === signup.id.toString());

        if (memberResponses.length > 0) {
          if (fillersOnly) {
            return []
          }

          return memberResponses;
        }

        return [createEmptyResponse(member, signup)];
      })
    }

    return [];
  }

  async update(id: string, data: Partial<MemberSignupResponseModel>): Promise<MemberSignupResponseModel> {

    const memberSignupResponse: MemberSignupResponse | null = await this.repo.findOne({where: {id}});
    if (!memberSignupResponse) {
      throw new Error('MemberSignupResponse not found: ' + id);
    }

    const updatedMemberSignupResponse: MemberSignupResponse = Object.assign(memberSignupResponse, data);

    await this.repo.updateById(id, updatedMemberSignupResponse);

    return entityToModel(updatedMemberSignupResponse);
  }

  async getMember(response: MemberSignupResponse, context: MemberSignupResponseContext = buildDefaultMemberSignupResponseContext(response)): Promise<MemberModel> {

    if (!context.members) {
      context.members = new Promise<Member[]>((resolve, reject) => {
        context.memberIds
          .then(memberIds => this.memberRepo
            .find({where: {id: {inq: memberIds || [response.memberId.toString()]}}})
          )
          .then(resolve)
          .catch(reject)
      })
    }

    const members = await context.members

    const matchingMembers: MemberModel[] = members
      .filter(matchResponseMember(response))
      .map(entityToModel)

    if (matchingMembers.length === 0) {
      console.log('Member is empty!!!')
      return {
        id: '1',
        email: 'unknown',
        // eslint-disable-next-line
      } as any
    }

    return matchingMembers[0]
  }

  async getOption(response: MemberSignupResponse, context: MemberSignupResponseContext = buildDefaultMemberSignupResponseContext(response)): Promise<OptionModel | undefined> {

    if (!response.optionId) {
      return undefined;
    }

    if (!context.options) {
      context.options = new Promise<Option[]>((resolve, reject) => {
        context.optionIds
          .then(optionIds => this.optionRepo
            .find({
              where: {id: {inq: optionIds || [response.optionId.toString()]}},
              order: ['sortIndex ASC'],
            })
          )
          .then(resolve)
          .catch(reject)
      })
    }

    const options = await context.options

    const matchingOption: OptionModel | undefined = options
      .map(entityToModel)
      .find(matchResponseOption(response))

    if (!matchingOption) {
      console.log('Option not found!!!')
    }

    return matchingOption
  }

  async getAssignments(response: MemberSignupResponse, context: MemberSignupResponseContext): Promise<AssignmentModel[]> {

    if (!context.responseAssignments) {
      context.responseAssignments = this.responseAssignmentRepo
        .find({
          where: {
            memberSignupResponseId: {inq: context.responseIds || [response.getId().toString()]}
          }
        })
    }

    if (!context.assignments) {
      context.assignments = this.assignmentRepo.find()
    }

    const responseAssignments = await context.responseAssignments
    const assignments = await context.assignments

    return responseAssignments
      .filter(ra => ra.memberSignupResponseId.toString() === response.getId().toString())
      .flatMap(ra => assignments.filter(a => a.getId().toString() === ra.assignmentId.toString()))
      .map(entityToModel)
  }

  async getSignup(response: MemberSignupResponse, context: MemberSignupResponseContext): Promise<SignupModel> {

    if (!context.signups) {
      context.signups = this.signupRepo.find({where: {id: {inq: context.signupIds || [response.signupId.toString()]}}})
    }

    const signups = await context.signups;

    const signup: Signup | undefined = signups.find(s => s.getId().toString() === response.signupId.toString())

    if (!signup) {
      throw new Error('Signup not found: ' + response.signupId.toString())
    }

    return entityToModel(signup);
  }
}


const createEmptyResponse = (member: MemberModel, signup: SignupModel): MemberSignupResponseModel => ({
  id: `${member.id}-${signup.id}`,
  signedUp: true,
  member,
  memberId: member.id,
  signup,
  signupId: signup.id,
});

const buildDefaultMemberSignupResponseContext = (response: MemberSignupResponse): MemberSignupResponseContext => {
  return {
    memberIds: Promise.resolve([response.memberId.toString()]),
    optionIds: Promise.resolve([response.optionId.toString()]),
    responseIds: [response.getId().toString()],
    signupIds: [response.signupId.toString()],
  }
}

const matchResponseMember = (response: MemberSignupResponse) => {
  return (member: Member): boolean => {
    return member.getId() === response.memberId.toString()
  }
}

const matchResponseOption = (response: MemberSignupResponse) => {
  return (option: Option): boolean => {
    return option.getId() === (response.optionId ?? '').toString()
  }
}

const isMatchingOption = (data: {optionId: string}, declineOption: Option): boolean => {
  return (data.optionId ?? '').toString() === declineOption.getId().toString();
}
