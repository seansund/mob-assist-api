import {inject} from '@loopback/core';

import {
  NOTIFICATION_SENDER_EMAIL,
  NOTIFICATION_SENDER_TEXT,
  NotificationSenderApi,
} from './notification-sender.api';
import {
  NotificationChannelResultModel,
  NotificationModel,
  NotificationResultModel,
} from '../../datatypes';

export class NotificationSenderRouter implements NotificationSenderApi {

  constructor(
    @inject(NOTIFICATION_SENDER_TEXT) protected textService: NotificationSenderApi,
    @inject(NOTIFICATION_SENDER_EMAIL) protected emailService: NotificationSenderApi,
  ) { }

  async send(type: string, notifications: NotificationModel[]): Promise<NotificationResultModel> {

    if (notifications.length === 0) {
      return {
        type,
        channels: [{
          channel: 'email',
          count: 0,
        }, {
          channel: 'text',
          count: 0,
        }]
      };
    }

    const textNotifications = notifications.filter(n => n.textChannel);
    const emailNotifications = notifications.filter(n => n.emailChannel);

    const channels: NotificationChannelResultModel[] = [
        await this.textService.send(type, textNotifications),
        await this.emailService.send(type, emailNotifications),
      ]
      .flatMap(result => result.channels)
      .reduce((partialResult: NotificationChannelResultModel[], currentResult: NotificationChannelResultModel) => {
          const existingResult: NotificationChannelResultModel | undefined = partialResult.find(r => r.channel === currentResult.channel);

          if (existingResult) {
            partialResult.push({
              channel: currentResult.channel,
              count: currentResult.count + existingResult.count,
            })
          } else {
            partialResult.push(currentResult);
          }

          return partialResult;
        }, []
      )

    return {
      type,
      channels,
    }
  }

}
