import {Context} from '@loopback/context';
import {
  NOTIFICATION_SENDER_API,
  NOTIFICATION_SENDER_EMAIL,
  NOTIFICATION_SENDER_TEXT,
} from './notification-sender.api';
import {NotificationSenderRouter} from './notification-sender.router';
import {NotificationSenderTwilio} from './notification-sender.twilio';
import {NotificationSenderSendgrid} from './notification-sender.sendgrid';
import {
  NotificationSenderNoopEmail,
  NotificationSenderNoopText,
} from './notification-sender.noop';
import {
  EmptyConfig, GMAIL_CONFIG, GmailConfig,
  isEmptyConfig,
  TWILIO_CONFIG,
  TwilioConfig,
} from '../../config';

export * from './notification-sender.api';

export const bindNotificationSenderApi = (service: Context) => {
  service.bind(NOTIFICATION_SENDER_API).toClass(NotificationSenderRouter);

  const twilioConfig: TwilioConfig | EmptyConfig = service.getSync(TWILIO_CONFIG);
  if (isEmptyConfig(twilioConfig)) {
    service.bind(NOTIFICATION_SENDER_TEXT).toClass(NotificationSenderNoopText);
  } else {
    service.bind(NOTIFICATION_SENDER_TEXT).toClass(NotificationSenderTwilio);
  }

  const gmailConfig: GmailConfig | EmptyConfig = service.getSync(GMAIL_CONFIG);
  if (isEmptyConfig(gmailConfig)) {
    service.bind(NOTIFICATION_SENDER_EMAIL).toClass(NotificationSenderNoopEmail);
  } else {
    service.bind(NOTIFICATION_SENDER_EMAIL).toClass(NotificationSenderSendgrid);
  }
}
