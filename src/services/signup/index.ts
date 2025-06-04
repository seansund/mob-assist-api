import {Context} from '@loopback/context';
import {SIGNUP_API} from './signup.api';
import {SignupImpl} from './signup.impl';

export * from './signup.api';

export const bindSignupApi = (service: Context)=> {
  service.bind(SIGNUP_API).toClass(SignupImpl);
}
