import {inject} from '@loopback/core';
import {MailDataRequired, send, setApiKey} from '@sendgrid/mail';

import {NotificationSenderApi} from './notification-sender.api';
import {SENDGRID_CONFIG, SendgridConfig} from '../../config';
import {NotificationModel, NotificationResultModel} from '../../datatypes';

export class NotificationSenderSendgrid implements NotificationSenderApi {
  private readonly config: SendgridConfig;

  constructor(
    @inject(SENDGRID_CONFIG) config: SendgridConfig,
  ) {
    this.config = config;

    setApiKey(config.apiKey);
  }

    async send(type: string, notifications: NotificationModel[]): Promise<NotificationResultModel> {

      const [result] = await send(this.buildMailData(type, notifications))

      console.log('Email result: ', result)

      return {
        type,
        channels: [{
          channel: this.config.channel ?? 'email',
          count: notifications.length,
        }]
      }
    }

    buildMailData(type: string, notifications: NotificationModel[]): MailDataRequired[] {
      return notifications.map((n: NotificationModel) => ({
        to: n.toMember.email,
        from: n.fromMember?.email ?? this.config.fromEmail,
        subject: getSubject(type, n),
        text: n.message,
      }))
    }
}

const getSubject = (type: string, notification: NotificationModel): string => {
  return notification.subject ?? type;
}
