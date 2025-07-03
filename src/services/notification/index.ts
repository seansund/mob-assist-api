import {Context} from '@loopback/context';
import {NOTIFICATION_API} from './notification.api';
import {NotificationImpl} from './notification.impl';

export * from './notification.api';

export const bindNotificationApi = (service: Context) => {
  service.bind(NOTIFICATION_API).toClass(NotificationImpl);
}
