import {BaseApi} from '../base.api';
import {
  AssignmentModel,
  GroupModel,
  MemberIdentifier, MemberModel, MemberSignupResponseInputModel,
  MemberSignupResponseModel,
  OptionModel, OptionSummaryModel,
  SignupFilterModel,
  SignupInputModel,
  SignupModel,
} from '../../datatypes';
import {
  Assignment, AssignmentSet,
  Group, GroupMember,
  Member,
  MemberSignupResponse, MemberSignupResponseAssignment,
  Option, OptionSet, Signup,
} from '../../models';
import {Optional, RequireOne} from '../../util';
import {DomainContext} from '../context-resolver';

export const SIGNUP_API = 'services.SignupApi';


export type SignupContext = RequireOne<DomainContext, 'signups'>;

export abstract class SignupApi extends BaseApi<SignupModel, SignupInputModel, SignupFilterModel, SignupContext> {

  abstract duplicate(fromSignupId: string, date: string): Promise<SignupModel>;

  abstract listForMember(memberId: MemberIdentifier, context?: SignupContext): Promise<SignupModel[]>;

  abstract getOptions(signup: Optional<SignupModel, 'id'>, context?: SignupContext): Promise<OptionModel[]>;
  abstract getAssignments(signup: Optional<SignupModel, 'id'>, context?: SignupContext): Promise<AssignmentModel[]>;
  abstract getResponses(signup: Optional<SignupModel, 'id'>, context?: SignupContext): Promise<MemberSignupResponseModel[]>;
  abstract getGroup(signup: Optional<SignupModel, 'id'>, context?: SignupContext): Promise<GroupModel>;
  abstract getResponseSummaries(signup: Optional<SignupModel, 'id'>, context?: SignupContext): Promise<OptionSummaryModel[]>;
  abstract getMembers(signup: Optional<SignupModel, 'id'>, context: SignupContext): Promise<MemberModel[]>;
}
