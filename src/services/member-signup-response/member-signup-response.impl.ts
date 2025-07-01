import {inject} from '@loopback/core';
import {repository} from '@loopback/repository';
import {
  MemberSignupResponseApi,
  MemberSignupResponseContext,
} from './member-signup-response.api';
import {
  AssignmentModel,
  MemberModel, MemberSignupResponseAssignmentDataModel,
  MemberSignupResponseFilterModel,
  MemberSignupResponseInputModel,
  MemberSignupResponseModel,
  OptionModel,
  SignupModel,
} from '../../datatypes';
import {
  MemberRepository,
  MemberSignupResponseAssignmentRepository,
  MemberSignupResponseRepository,
  OptionRepository,
  SignupRepository,
} from '../../repositories';
import {
  Member,
  MemberSignupResponse,
  MemberSignupResponseAssignment,
  Option,
  Signup,
} from '../../models';
import {MEMBER_API, MemberApi} from '../member';
import {SIGNUP_API, SignupApi} from '../signup';
import {entitiesToModels, entityToModel} from '../../util';
import {CONTEXT_RESOLVER_API, ContextResolverApi} from '../context-resolver';

export class MemberSignupResponseImpl implements MemberSignupResponseApi {

  constructor(
    @repository('MemberSignupResponseRepository') protected repo: MemberSignupResponseRepository,
    @repository('MemberSignupResponseAssignmentRepository') protected responseAssignmentRepo: MemberSignupResponseAssignmentRepository,
    @repository('MemberRepository') protected memberRepo: MemberRepository,
    @repository('OptionRepository') protected optionRepo: OptionRepository,
    @repository('SignupRepository') protected signupRepo: SignupRepository,
    @inject(MEMBER_API) protected memberApi: MemberApi,
    @inject(SIGNUP_API) protected signupApi: SignupApi,
    @inject(CONTEXT_RESOLVER_API) protected resolver: ContextResolverApi,
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

  async setAssignments(memberSignupResponseId: string, updateAssignmentIds: string[]): Promise<MemberSignupResponseModel> {

    const currentAssignments: MemberSignupResponseAssignment[] = await this.responseAssignmentRepo
      .find({where: {memberSignupResponseId}});

    const {newAssignments, deleteAssignments} = handleAssignments(memberSignupResponseId, updateAssignmentIds, currentAssignments);

    if (newAssignments.length > 0) {
      await this.responseAssignmentRepo.createAll(newAssignments)
    }

    if (deleteAssignments.length > 0) {
      const deleteIds: string[] = deleteAssignments.map(assignment => assignment.getId())

      await this.responseAssignmentRepo.deleteAll({id: {inq: deleteIds}})
    }

    return this.repo.findById(memberSignupResponseId)
      .then(entityToModel);
  }

  async getMember(response: MemberSignupResponse, context: MemberSignupResponseContext = buildDefaultContext(response)): Promise<MemberModel> {

    const members = await this.resolver.populateMembersContext(context)

    const matchingMember: MemberModel | undefined = members
      .map(entityToModel)
      .find(matchResponseMember(response))

    if (!matchingMember) {
      console.log('Member is empty!!!')
      return {
        id: '1',
        email: 'unknown',
        // eslint-disable-next-line
      } as any
    }

    return matchingMember
  }

  async getOption(response: MemberSignupResponse, context: MemberSignupResponseContext = buildDefaultContext(response)): Promise<OptionModel | undefined> {

    if (!response.optionId) {
      return undefined;
    }

    const options = await this.resolver.populateOptionsContext(context);

    const matchingOption: OptionModel | undefined = options
      .map(entityToModel)
      .find(matchResponseOption(response))

    if (!matchingOption) {
      console.log('Option not found!!!')
    }

    return matchingOption
  }

  async getAssignments(response: MemberSignupResponse, context: MemberSignupResponseContext = buildDefaultContext(response)): Promise<AssignmentModel[]> {

    const responseAssignments = await this.resolver.populateResponseAssignmentsContext(context);
    const assignments = await this.resolver.populateAssignmentsContext(context);

    return responseAssignments
      .filter(ra => ra.memberSignupResponseId.toString() === response.getId().toString())
      .flatMap(ra => assignments.filter(a => a.getId().toString() === ra.assignmentId.toString()))
      .map(entityToModel)
  }

  async getSignup(response: MemberSignupResponse, context: MemberSignupResponseContext = buildDefaultContext(response)): Promise<SignupModel> {

    const signups = await this.resolver.populateSignupsContext(context);

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

const buildDefaultContext = (response: MemberSignupResponse): MemberSignupResponseContext => {
  return {
    responses: Promise.resolve(entitiesToModels([response]))
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

interface HandleAssignmentResult {
  newAssignments: MemberSignupResponseAssignmentDataModel[];
  deleteAssignments: MemberSignupResponseAssignment[];
}

const handleAssignments = (memberSignupResponseId: string, updateAssignmentIds: string[], currentAssignments: MemberSignupResponseAssignment[]): HandleAssignmentResult => {

  const currentAssignmentIds: string[] = currentAssignments.map(assignment => assignment.assignmentId.toString());

  const newAssignments: MemberSignupResponseAssignmentDataModel[] = updateAssignmentIds
    .filter(assignmentId => !currentAssignmentIds.includes(assignmentId))
    .map(assignmentId => ({memberSignupResponseId, assignmentId}));

  const deleteAssignments: MemberSignupResponseAssignment[] = currentAssignments
    .filter(assignment => !updateAssignmentIds.includes(assignment.getId()));

  return {newAssignments, deleteAssignments};
}
