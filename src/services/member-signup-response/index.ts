import {Context} from '@loopback/context';
import {MEMBER_SIGNUP_RESPONSE_API} from './member-signup-response.api';
import {MemberSignupResponseImpl} from './member-signup-response.impl';

export * from './member-signup-response.api';

export const bindMemberSignupResponseApi = (service: Context) => {
  service.bind(MEMBER_SIGNUP_RESPONSE_API).toClass(MemberSignupResponseImpl);
}
