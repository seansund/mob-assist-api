import {BaseApi} from '../base.api';
import {
  AssignmentModel,
  MemberModel,
  MemberSignupResponseFilterModel,
  MemberSignupResponseInputModel,
  MemberSignupResponseModel, OptionModel, SignupModel,
} from '../../datatypes';
import {
  Assignment,
  Member,
  MemberSignupResponse,
  MemberSignupResponseAssignment,
  Option, Signup,
} from '../../models';

export const MEMBER_SIGNUP_RESPONSE_API = 'services.MemberSignupResponseApi';

export interface MemberSignupResponseContext {

  responses: Promise<MemberSignupResponse[]>;

  signups?: Promise<Signup[]>;
  members?: Promise<Member[]>;
  options?: Promise<Option[]>;
  assignments?: Promise<Assignment[]>;
  responseAssignments?: Promise<MemberSignupResponseAssignment[]>;
}

export abstract class MemberSignupResponseApi extends BaseApi<MemberSignupResponseModel, MemberSignupResponseInputModel, MemberSignupResponseFilterModel> {

  abstract listWithFillers(filter?: MemberSignupResponseFilterModel, fillersOnly?: boolean): Promise<MemberSignupResponseModel[]>;

  abstract getAssignments(response: Omit<MemberSignupResponseModel, 'id'>, context: MemberSignupResponseContext): Promise<AssignmentModel[]>;
  abstract getMember(response: Omit<MemberSignupResponseModel, 'id'>, context?: MemberSignupResponseContext): Promise<MemberModel>;
  abstract getOption(response: Omit<MemberSignupResponseModel, 'id'>, context?: MemberSignupResponseContext): Promise<OptionModel | undefined>;
  abstract getSignup(response: Omit<MemberSignupResponseModel, 'id'>, context: MemberSignupResponseContext): Promise<SignupModel>;
}
