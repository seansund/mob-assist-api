import {
  Assignment,
  AssignmentSet,
  Group,
  GroupMember,
  Member,
  MemberSignupResponse,
  MemberSignupResponseAssignment,
  Option,
  OptionSet,
  Signup,
} from '../../models';
import {MemberSignupResponseModel} from '../../datatypes';

export interface DomainContext {
  memberId?: string;

  signups?: Promise<Signup[]>
  groups?: Promise<Group[]>;
  optionSets?: Promise<OptionSet[]>;
  options?: Promise<Option[]>;
  assignmentSets?: Promise<AssignmentSet[]>;
  assignments?: Promise<Assignment[]>;
  members?: Promise<Member[]>;
  groupMembers?: Promise<GroupMember[]>;
  responses?: Promise<MemberSignupResponseModel[]>;
  responseAssignments?: Promise<MemberSignupResponseAssignment[]>;
}

export const CONTEXT_RESOLVER_API = 'services.ContextResolverApi';

export abstract class ContextResolverApi {

  abstract populateSignupsContext(context: DomainContext): Promise<Signup[]>;
  abstract populateMembersContext(context: DomainContext): Promise<Member[]>;
  abstract populateGroupMembersContext(context: DomainContext): Promise<GroupMember[]>;
  abstract populateGroupsContext(context: DomainContext): Promise<Group[]>;
  abstract populateOptionsContext(context: DomainContext): Promise<Option[]>;
  abstract populateAssignmentsContext(context: DomainContext): Promise<Assignment[]>;
  abstract populateAssignmentSetsContext(context: DomainContext): Promise<AssignmentSet[]>;
  abstract populateMemberIdContext(context: DomainContext): Promise<string[]>;
  abstract populateResponsesContext(context: DomainContext): Promise<MemberSignupResponseModel[]>;
  abstract populateResponseAssignmentsContext(context: DomainContext): Promise<MemberSignupResponseAssignment[]>;
}
