import {BaseApi} from '../base.api';
import {
  MemberIdentifier,
  SignupFilterModel,
  SignupInputModel,
  SignupModel,
} from '../../datatypes';
import {Assignment, Group, MemberSignupResponse, Option} from '../../models';

export const SIGNUP_API = 'services.SignupApi';


export interface SignupContext {
  signupIds: string[];
  groupIds: string[];
  optionSetIds: string[];
  assignmentSetIds: string[];
  memberId?: string;

  groups?: Promise<Group[]>;
  options?: Promise<Option[]>;
  assignments?: Promise<Assignment[]>;
  responses?: Promise<MemberSignupResponse[]>;
}

export abstract class SignupApi extends BaseApi<SignupModel, SignupInputModel, SignupFilterModel> {

  abstract duplicate(fromSignupId: string, date: string): Promise<SignupModel>;

  abstract listForMember(memberId: MemberIdentifier): Promise<SignupModel[]>;

  abstract getOptions(signup: SignupModel, context?: SignupContext): Promise<Option[]>;
  abstract getAssignments(signup: SignupModel, context?: SignupContext): Promise<Assignment[]>;
  abstract getResponses(signup: SignupModel, context?: SignupContext): Promise<MemberSignupResponse[]>;
  abstract getGroup(signup: SignupModel, context?: SignupContext): Promise<Group>;
}
