import {BaseApi} from '../base.api';
import {
  AssignmentModel,
  MemberModel,
  MemberSignupResponseFilterModel,
  MemberSignupResponseInputModel,
  MemberSignupResponseModel,
  OptionModel,
  SignupModel,
} from '../../datatypes';
import {DomainContext} from '../context-resolver';
import {RequireOne} from '../../util';

export const MEMBER_SIGNUP_RESPONSE_API = 'services.MemberSignupResponseApi';

export type MemberSignupResponseContext = RequireOne<DomainContext, 'responses'>;

export abstract class MemberSignupResponseApi extends BaseApi<MemberSignupResponseModel, MemberSignupResponseInputModel, MemberSignupResponseFilterModel> {

  abstract listWithFillers(filter?: MemberSignupResponseFilterModel, fillersOnly?: boolean): Promise<MemberSignupResponseModel[]>;

  abstract setAssignments(id: string, assignmentIds: string[]): Promise<MemberSignupResponseModel>;

  abstract getAssignments(response: Omit<MemberSignupResponseModel, 'id'>, context: MemberSignupResponseContext): Promise<AssignmentModel[]>;
  abstract getMember(response: Omit<MemberSignupResponseModel, 'id'>, context?: MemberSignupResponseContext): Promise<MemberModel>;
  abstract getOption(response: Omit<MemberSignupResponseModel, 'id'>, context?: MemberSignupResponseContext): Promise<OptionModel | undefined>;
  abstract getSignup(response: Omit<MemberSignupResponseModel, 'id'>, context: MemberSignupResponseContext): Promise<SignupModel>;
}
