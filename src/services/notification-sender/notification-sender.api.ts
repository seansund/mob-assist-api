import {NotificationModel, NotificationResultModel} from '../../datatypes';

export const NOTIFICATION_SENDER_API = 'services.NotificationSenderApi';
export const NOTIFICATION_SENDER_TEXT = 'services.NotificationSenderText';
export const NOTIFICATION_SENDER_EMAIL = 'services.NotificationSenderEmail';

export abstract class NotificationSenderApi {
  abstract send(type: string, notifications: NotificationModel[]): Promise<NotificationResultModel>;
}
