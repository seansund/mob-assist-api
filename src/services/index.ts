import {Context} from '@loopback/context';
import {bindAssignmentSetApi} from './assignment-set';
import {bindMemberSignupResponseApi} from './member-signup-response';
import {bindNotificationApi} from './notification';
import {bindSignupApi} from './signup';
import {bindMemberApi} from './member';
import {bindNotificationSenderApi} from './notification-sender';
import {bindContextResolverApi} from './context-resolver';

export const bindServices = (server: Context) => {
  bindContextResolverApi(server);
  bindAssignmentSetApi(server);
  bindMemberApi(server);
  bindMemberSignupResponseApi(server);
  bindNotificationApi(server);
  bindNotificationSenderApi(server);
  bindSignupApi(server);
}

export * from './assignment-set';
export * from './context-resolver';
export * from './member';
export * from './member-signup-response';
export * from './notification';
export * from './notification-sender';
export * from './signup';
