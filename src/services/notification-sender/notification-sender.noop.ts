import {NotificationSenderApi} from './notification-sender.api';
import {NotificationResultModel} from '../../datatypes';

class NotificationSenderNoop implements NotificationSenderApi {
  constructor(private readonly channel: string) {
  }

  async send(type: string): Promise<NotificationResultModel> {
    return {
      type,
      channels: [{
        channel: this.channel,
        count: 0,
      }]
    };
  }
}

export class NotificationSenderNoopEmail extends NotificationSenderNoop {
  constructor() {
    super('email');
  }
}

export class NotificationSenderNoopText extends NotificationSenderNoop {
  constructor() {
    super('text');
  }
}
