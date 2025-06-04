import {inject} from '@loopback/core';
import {arg, ID, mutation, Resolver} from '@loopback/graphql';

import {NotificationResultModel} from '../datatypes';
import {Notification, NotificationResult} from '../models';
import {NOTIFICATION_API, NotificationApi} from '../services';

@Resolver(() => Notification)
export class NotificationResolver {
  constructor(
    @inject(NOTIFICATION_API) private service: NotificationApi
  ) {
  }

  @mutation(() => NotificationResult)
  async sendSignupRequest(
    @arg('signupId', () => ID) id: string
  ): Promise<NotificationResultModel> {
    return this.service.sendSignupRequest(id);
  }

  @mutation(() => NotificationResult)
  async sendSignupRequestToNoResponse(
    @arg('signupId', () => ID) id: string
  ): Promise<NotificationResultModel> {
    return this.service.sendSignupRequestToNoResponse(id);
  }

  @mutation(() => NotificationResult)
  async sendSignupAssignments(
    @arg('signupId', () => ID) id: string
  ): Promise<NotificationResultModel> {
    return this.service.sendAssignments(id);
  }

  @mutation(() => NotificationResult)
  async sendSignupCheckin(
    @arg('signupId', () => ID) id: string
  ): Promise<NotificationResultModel> {
    return this.service.sendCheckIn(id);
  }
}
