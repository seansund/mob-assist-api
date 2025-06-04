import {BaseApi} from '../base.api';
import {NotificationModel, NotificationResultModel} from '../../datatypes';

export const NOTIFICATION_API = 'services.NotificationApi';

export abstract class NotificationApi extends BaseApi<NotificationModel> {
  abstract sendSignupRequest(signupId: string, fromMemberId?: string): Promise<NotificationResultModel>;
  abstract sendSignupRequestToNoResponse(signupId: string, fromMemberId?: string): Promise<NotificationResultModel>;
  abstract sendAssignments(signupId: string, fromMemberId?: string): Promise<NotificationResultModel>;
  abstract sendCheckIn(signupId: string, fromMemberId?: string): Promise<NotificationResultModel>;
}
