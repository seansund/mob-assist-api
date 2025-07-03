import {ContextResolverApi, DomainContext} from './context-resolver.api';
import {
  Assignment,
  AssignmentSet,
  Group,
  GroupMember,
  Member, MemberSignupResponse, MemberSignupResponseAssignment, Option, Signup,
} from '../../models';
import {repository, Where} from '@loopback/repository';
import {
  AssignmentRepository,
  AssignmentSetRepository,
  GroupMemberRepository,
  GroupRepository,
  MemberRepository,
  MemberSignupResponseAssignmentRepository,
  MemberSignupResponseRepository,
  OptionRepository,
  SignupRepository,
} from '../../repositories';
import {entitiesToModels, unique} from '../../util';
import {MemberSignupResponseModel} from '../../datatypes';

export class ContextResolverImpl implements ContextResolverApi {

  constructor(
    @repository('SignupRepository') protected signupRepo: SignupRepository,
    @repository('GroupRepository') protected groupRepo: GroupRepository,
    @repository('MemberRepository') protected memberRepo: MemberRepository,
    @repository('AssignmentSetRepository') protected assignmentSetRepo: AssignmentSetRepository,
    @repository('AssignmentRepository') protected assignmentRepo: AssignmentRepository,
    @repository('OptionRepository') protected optionRepo: OptionRepository,
    @repository('GroupMemberRepository') protected groupMemberRepo: GroupMemberRepository,
    @repository('MemberSignupResponseRepository') protected responseRepo: MemberSignupResponseRepository,
    @repository('MemberSignupResponseAssignmentRepository') protected responseAssignmentRepo: MemberSignupResponseAssignmentRepository,
  ) {}

  async populateSignupsContext(context: DomainContext): Promise<Signup[]> {
    if (!context.signups) {
      if (!context.responses) {
        context.signups = Promise.reject(new Error('Signups and MemberSignupResponses are missing'))

        return context.signups;
      }

      context.signups = new Promise((resolve, reject) => {
        this.populateResponsesContext(context)
          .then(responses => {
            const signupIds = responses.map(response => response.signupId.toString());

            this.signupRepo
              .find({where: {id: {inq: signupIds}}})
              .then(resolve)
              .catch(reject)
          })
          .catch(reject)
      })
    }

    return context.signups
  }

  async populateAssignmentSetsContext(context: DomainContext): Promise<AssignmentSet[]> {
    if (!context.assignmentSets) {
      context.assignmentSets = new Promise<AssignmentSet[]>((resolve, reject) => {
        this.populateSignupsContext(context)
          .then(signups => {
            const assignmentSetIds = signups
              .map(s => (s.assignmentSetId ?? '').toString())
              .filter(id => !!id)

            this.assignmentSetRepo.find({where: {id: {inq: assignmentSetIds}}})
              .then(resolve)
              .catch(reject)
          })
          .catch(reject)
      })
    }

    return context.assignmentSets
  }

  async populateAssignmentsContext(context: DomainContext): Promise<Assignment[]> {
    if (!context.assignments) {
      context.assignments = new Promise<Assignment[]>((resolve, reject) => {
        this.populateAssignmentSetsContext(context)
          .then((assignmentSets: AssignmentSet[]) => {
            const assignmentSetIds: string[] = assignmentSets.map(assignmentSet => assignmentSet.getId().toString())

            this.assignmentRepo.find({where: {assignmentSetId: {inq: assignmentSetIds}}})
              .then(resolve)
              .catch(reject)
          })
          .catch(reject)
      })
    }

    return context.assignments;
  }

  async populateGroupMembersContext(context: DomainContext): Promise<GroupMember[]> {
    if (!context.groupMembers) {
      context.groupMembers = new Promise<GroupMember[]>((resolve, reject) => {
        this.populateSignupsContext(context)
          .then(signups => {
            const groupIds = signups.map(s => s.groupId.toString())

            this.groupMemberRepo
              .find({where: {groupId: {inq: groupIds}}})
              .then(resolve)
              .catch(reject)
          })
          .catch(reject)
      })
    }

    return context.groupMembers
  }

  async populateGroupsContext(context: DomainContext): Promise<Group[]> {
    if (!context.groups) {
      context.groups = new Promise((resolve, reject) => {
        this.populateSignupsContext(context)
          .then(signups => {
            const groupIds = signups.map(s => s.groupId.toString())

            this.groupRepo
              .find({where: {id: {inq: groupIds}}})
              .then(resolve)
              .catch(reject)
          })
          .catch(reject)
      })
    }

    return context.groups
  }

  async populateMemberIdContext(context: DomainContext): Promise<string[]> {
    return new Promise<string[]>((resolve, reject) => {
      this.populateGroupMembersContext(context)
        .then(groupMembers => unique(groupMembers.map(groupMember => groupMember.memberId.toString())))
        .then(resolve)
        .catch(reject)
    })
  }

  async populateMembersContext(context: DomainContext): Promise<Member[]> {
    if (!context.members) {
      context.members = new Promise((resolve, reject) => {
        this.populateMemberIdContext(context)
          .then(memberIds => {
            this.memberRepo.find({where: {id: {inq: memberIds}}})
              .then(resolve)
              .catch(reject)
          })
          .catch(reject)
      })
    }

    return context.members;
  }

  async populateOptionsContext(context: DomainContext): Promise<Option[]> {

    if (!context.options) {
      context.options = new Promise<Option[]>((resolve, reject) => {
        this.populateSignupsContext(context)
          .then(signups => {
            const optionSetIds = signups.map(s => s.optionSetId.toString());

            this.optionRepo
              .find({
                where: {optionSetId: {inq: optionSetIds}},
                order: ['sortIndex ASC'],
              })
              .then(resolve)
              .catch(reject)
          })
          .catch(reject)
      })
    }

    return context.options
  }

  async populateResponsesContext(context: DomainContext): Promise<MemberSignupResponseModel[]> {
    const where: Where<MemberSignupResponse> = context.memberId
      ? {memberId: context.memberId}
      : {}

    if (!context.responses) {
      if (!context.signups) {
        context.responses = Promise.reject(new Error('MemberSignupResponses and Signups are missing'))

        return context.responses;
      }

      context.responses = new Promise<MemberSignupResponseModel[]>((resolve, reject) => {
        this.populateSignupsContext(context)
          .then(signups => {
            const signupIds: string[] = signups.map(s => s.getId().toString())

            this.responseRepo
              .find({where: {...where, signupId: {inq: signupIds}}})
              .then(entitiesToModels)
              .then(resolve)
              .catch(reject)
          })
          .catch(reject)
      })
    }

    return context.responses
  }

  async populateResponseAssignmentsContext(context: DomainContext): Promise<MemberSignupResponseAssignment[]> {
    if (!context.responseAssignments) {
      context.responseAssignments = new Promise<MemberSignupResponseAssignment[]>((resolve, reject) => {
        this.populateResponsesContext(context)
          .then(responses => {
            const responseIds = responses.map(response => response.id.toString())

            this.responseAssignmentRepo
              .find({where: {memberSignupResponseId: {inq: responseIds}}})
              .then(resolve)
              .catch(reject)
          })
          .catch(reject)
      })
    }

    return context.responseAssignments
  }

}
