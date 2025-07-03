import twilio from 'twilio';

import {NotificationSenderApi} from './notification-sender.api';
import { NotificationModel, NotificationResultModel } from '../../datatypes';
import {MessageInstance} from 'twilio/lib/rest/api/v2010/account/message';
import {inject} from '@loopback/core';
import {TWILIO_CONFIG, TwilioConfig} from '../../config/twilio.config';

interface TwilioError {
  errorMessage: string;
}

export class NotificationSenderTwilio implements NotificationSenderApi {
  private readonly client: twilio.Twilio;
  private readonly defaultPhoneNumber: string;
  // TODO this should be dynamic
  private readonly messagePrefix: string;

  constructor(
    @inject(TWILIO_CONFIG) config: TwilioConfig,
  ) {
    this.client = twilio(config.accountSid, config.authToken);
    this.defaultPhoneNumber = config.phoneNumber;
    this.messagePrefix = config.messagePrefix;
  }

  async send(type: string, notifications: NotificationModel[]): Promise<NotificationResultModel> {
    const result = await Promise.all(notifications.map(n => this.sendMessage(n)));

    const errorResults = result.filter(val => !!val.errorMessage)
    if (errorResults.length > 0) {
      console.log('Errors sending notifications:' + errorResults.length)
    }

    return {
      type,
      channels: [{
        channel: 'text',
        count: result.length,
      }]
    }
  }

  async sendMessage(notification: NotificationModel): Promise<MessageInstance | TwilioError> {

    return this.client.messages
      .create({
        body: this.messagePrefix + notification.message + ' ' + notification.textAction,
        to: notification.toMember.phone,
        from: notification.fromMember?.phone ?? this.defaultPhoneNumber,
      })
      .catch(error => ({errorMessage: error.message}))
  }

}
