import {BaseApi} from '../base.api';
import {
  MemberSignupResponseFilterModel,
  MemberSignupResponseInputModel,
  MemberSignupResponseModel,
} from '../../datatypes';

export const MEMBER_SIGNUP_RESPONSE_API = 'services.MemberSignupResponseApi';

export abstract class MemberSignupResponseApi extends BaseApi<MemberSignupResponseModel, MemberSignupResponseInputModel, MemberSignupResponseFilterModel> {

  abstract listWithFillers(filter?: MemberSignupResponseFilterModel, fillersOnly?: boolean): Promise<MemberSignupResponseModel[]>;
}
