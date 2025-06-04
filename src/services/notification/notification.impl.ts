import {inject} from '@loopback/core';
import {repository} from '@loopback/repository';

import {NotificationApi} from './notification.api';
import {
  AssignmentGroupModel,
  AssignmentModel,
  formatDate,
  groupAssignments,
  isMemberModel,
  MemberModel,
  MemberSignupResponseModel,
  NotificationInputModel,
  NotificationModel,
  NotificationResultModel,
  OptionModel,
  SignupModel,
} from '../../datatypes';
import {NotificationRepository} from '../../repositories';
import {
  MEMBER_SIGNUP_RESPONSE_API,
  MemberSignupResponseApi,
} from '../member-signup-response';
import {SIGNUP_API, SignupApi} from '../signup';
import {
  NOTIFICATION_SENDER_API,
  NotificationSenderApi,
} from '../notification-sender';
import {isEmpty} from '../../util';
import {isMemberSignupResponse} from '../../models';
import {MEMBER_API, MemberApi} from '../member';

interface BuildMessageResult {
  subject: string;
  message: string;
  textAction: string;
}

export class NotificationImpl implements NotificationApi {
  constructor(
    @repository('NotificationRepository') protected repo: NotificationRepository,
    @inject(MEMBER_SIGNUP_RESPONSE_API) protected memberSignupResponseApi: MemberSignupResponseApi,
    @inject(SIGNUP_API) protected signupApi: SignupApi,
    @inject(MEMBER_API) protected memberApi: MemberApi,
    @inject(NOTIFICATION_SENDER_API) protected notificationSenderApi: NotificationSenderApi,
  ) {
  }

  async create(data: Omit<NotificationModel, "id">): Promise<NotificationModel> {
    return this.repo.create(data)
      .then(result => result as NotificationModel);
  }

  async delete(id: string): Promise<NotificationModel | undefined> {
    const notification: NotificationModel = await this.repo.findOne({where: {id}})
      .then(result => result as NotificationModel);

    if (notification) {
      await this.repo.deleteById(id);
    }

    return notification ?? undefined;
  }

  async get(id: string): Promise<NotificationModel | undefined> {
    const notification: NotificationModel | null = await this.repo.findOne({where: {id}})
      .then(result => result as NotificationModel);

    return notification ?? undefined;
  }

  async list(): Promise<NotificationModel[]> {
    return this.repo.find().then(result => result as NotificationModel[]);
  }

  async update(id: string, data: Partial<NotificationModel>): Promise<NotificationModel> {
    const notification: NotificationModel | null = await this.repo.findOne({where: {id}})
      .then(result => result as NotificationModel);

    if (!notification) {
      throw new Error('Notification not found: ' + id);
    }

    const updatedNotification: NotificationModel = Object.assign(notification, data);

    await this.repo.updateById(id, updatedNotification);

    return updatedNotification;
  }

  async sendAssignments(signupId: string, fromMemberId?: string): Promise<NotificationResultModel> {
    return this.sendAssignmentInternal(signupId, 'Assignment', defaultMessageSuffix, fromMemberId);
  }

  async sendCheckIn(signupId: string, fromMemberId?: string): Promise<NotificationResultModel> {
    return this.sendAssignmentInternal(signupId, 'Checkin', checkinMessageSuffix, fromMemberId);
  }

  async sendAssignmentInternal(signupId: string, type: string, messageSuffix: string, fromMemberId?: string): Promise<NotificationResultModel> {

    const signup: SignupModel | undefined = await this.signupApi.get(signupId);

    if (!signup) {
      throw new Error('Signup not found: ' + signupId);
    }

    const signupMembers: MemberModel[] = await this.memberApi.listForSignup(signupId);

    const optionIds = signup.options
      .filter(option => !option.declineOption)
      // eslint-disable-next-line
      .map<string>(option => option.id as any);

    // get signup responses for all members
    const responses: MemberSignupResponseModel[] = await this.memberSignupResponseApi
      .list({signupId, optionId: {inq: optionIds}});

    const date = formatDate(new Date());

    const newNotifications: NotificationInputModel[] = createNotifications(responses, buildCheckinRequestMessage(signup, signupMembers, responses, messageSuffix, type), type, date, fromMemberId);

    const notifications = await this.repo.createAll(newNotifications);

    return this.notificationSenderApi.send(type, notifications)
  }

  async sendSignupRequest(signupId: string, fromMemberId?: string): Promise<NotificationResultModel> {
    return this.sendSignupResponseInternal(signupId, 'SignupRequest', fromMemberId);
  }

  async sendSignupRequestToNoResponse(signupId: string, fromMemberId?: string): Promise<NotificationResultModel> {
    return this.sendSignupResponseInternal(signupId, 'SignupRequestNoResponse', fromMemberId, true);
  }

  async sendSignupResponseInternal(signupId: string, type: string, fromMemberId?: string, fillersOnly?: boolean): Promise<NotificationResultModel> {

    const signup: SignupModel | undefined = await this.signupApi.get(signupId);

    if (!signup) {
      throw new Error('Signup not found: ' + signupId);
    }

    const responses: MemberSignupResponseModel[] = await this.memberSignupResponseApi.listWithFillers({signupId}, fillersOnly);

    const date = formatDate(new Date());

    const newNotifications = createNotifications(responses, buildSignupRequestMessage(signup, defaultMessageSuffix, type), type, date, fromMemberId);

    const notifications = await this.repo.createAll(newNotifications);

    return this.notificationSenderApi.send(type, notifications)
  }
}

const defaultMessageSuffix = `Reply STOP to unsubscribe and OPTIONS for more options.`
const checkinMessageSuffix = `Reply YES to checkin, NO if you are unable to serve.`


const buildSignupRequestMessage = (signup: SignupModel, textAction: string, type: string): (response: MemberSignupResponseModel) => BuildMessageResult => {
  return (response: MemberSignupResponseModel): BuildMessageResult => {

    if (response.signedUp && response.option) {
      // confirmation message
      return {
        subject: getSubject(type),
        message: `${signup.title} is ${signup.date}. You are signup up for ${response.option.shortName}.`,
        textAction
      }
    } else {
      // request message
      return {
        subject: getSubject(type),
        message: `${signup.title} is ${signup.date}. Sign up by responding with ${optionNames(signup.options)}.`,
        textAction
      }
    }
  }
}

const buildCheckinRequestMessage = (signup: SignupModel, signupMembers: MemberModel[], signupResponses: MemberSignupResponseModel[], messageSuffix: string, type: string): (response: MemberSignupResponseModel) => BuildMessageResult => {
  return (response: MemberSignupResponseModel): BuildMessageResult => {

    const hasAssignment: boolean = !isEmpty(response.assignments);

    const option: OptionModel | undefined = response.option;
    if (!option) {
      throw new Error('Assert: Option should not be undefined.');
    }

    return hasAssignment
      ? buildAssignmentMessage(signup, option, response.assignments ?? [], messageSuffix, signupMembers, signupResponses, type)
      : buildNoAssignmentMessage(signup, option, messageSuffix, type);
  }
}

const buildAssignmentMessage = (signup: SignupModel, option: OptionModel, assignments: AssignmentModel[], textAction: string, signupMembers: MemberModel[], signupResponses: MemberSignupResponseModel[], type: string): BuildMessageResult => {
  const assignmentDisplay: string = buildAssignmentDisplay(assignments);
  const assignmentDisplayWithPartner = buildAssignmentDisplayWithPartner(
    assignmentDisplay,
    lookupPartner(option, signup.assignments, signupMembers, signupResponses)
  )
  const assignmentDiagramUrl = buildAssignmentDiagramUrl(assignmentDisplay)

  return {
    subject: getSubject(type),
    message: `${signup.title} is ${signup.date}. You are signed up for ${option.value} and assigned to ${assignmentDisplayWithPartner}. ${assignmentDiagramUrl}`,
    textAction,
  }
}

const buildAssignmentDisplay = (assignments: AssignmentModel[]): string => {
  const groups: AssignmentGroupModel[] = groupAssignments(assignments);

  return groups.reduce((partialResult: string, current: AssignmentGroupModel) => {

    if (partialResult.length === 0) {
      partialResult = current.group + ' - ';
    } else {
      partialResult += ', ';
    }

    partialResult += current.assignments
      .map(assignment => assignment.name)
      .join(', ');

    return partialResult;
  }, '')
}

const buildAssignmentDisplayWithPartner = (assignmentDisplay: string, partner?: MemberModel): string => {
  return partner
    ? `${assignmentDisplay} with ${partner.firstName + ' ' + partner.lastName}`
    : assignmentDisplay;
}

const lookupPartner = (option: OptionModel, assignments: AssignmentModel[], members: MemberModel[], signupResponses: MemberSignupResponseModel[]): MemberModel | undefined => {
  const partnerAssignmentIds: string[] = assignments
    .filter(assignment => !!assignment.partnerId)
    .map(assignment => assignment.partnerId as string)

  const partners: MemberSignupResponseModel[] = signupResponses
    .filter(res => res.option?.id === option.id)
    .filter(res => (res.assignments ?? [])
      .map(assignment => assignment.id as string)
      .some(id => partnerAssignmentIds.includes(id)));

  if (partners.length === 0) {
    return undefined;
  }

  if (partners.length > 1) {
    console.log('Multiple partners found for assignment list: ' + assignments);
  }

  const partnerResponse: MemberSignupResponseModel = partners[0];

  const memberRef: {id: string} | MemberModel = isMemberSignupResponse(partnerResponse) ? {id: partnerResponse.memberId} : partnerResponse.member;
  return getMemberFromMemberRef(memberRef, members);
}

const getMemberFromMemberRef = (memberRef: {id: string} | MemberModel, members: MemberModel[]): MemberModel | undefined => {
  if (!memberRef) {
    return undefined;
  }

  if (isMemberModel(memberRef)) {
    return memberRef as MemberModel;
  }

  return members.find(m => m.id === memberRef.id);
}

const buildAssignmentDiagramUrl = (assignmentDisplay: string): string => {
  const urlAssignment = assignmentDisplay
    .replace(' ', '')
    .replace('-', ',')
    .toLowerCase()

  // TODO lookup this url from signup
  return `https://bit.ly/deacon-assn#${urlAssignment}`
}

const buildNoAssignmentMessage = (signup: SignupModel, option: OptionModel, textAction: string, type: string): BuildMessageResult => {
  return {
    subject: getSubject(type),
    message: `${signup.title} is ${signup.date}. You are signed up for ${option.value}.`,
    textAction,
  }
}

const optionNames = (options: OptionModel[]): string => {
  return options
    .sort((a: OptionModel, b: OptionModel) => a.sortIndex - b.sortIndex)
    .map(option => option.shortName)
    .join(', ');
}

const createNotifications = (responses: MemberSignupResponseModel[], buildMessage: (response: MemberSignupResponseModel) => BuildMessageResult, type: string, date: string, fromMemberId?: string): NotificationInputModel[] => {
  return responses.map(response => {
    const channels = memberChannels(response.member.preferredContact);

    const {message, textAction, subject} = buildMessage(response);

    const notification: NotificationInputModel = {
      type,
      date,
      fromMemberId,
      toMemberId: response.member.id as string,
      subject,
      message,
      textAction,
      webChannel: channels.webChannel,
      textChannel: channels.textChannel,
      emailChannel: channels.emailChannel,
    };

    return notification;
  });
}

interface MemberChannels {
  webChannel: boolean;
  emailChannel: boolean;
  textChannel: boolean;
}

const memberChannels = (preferredContact?: string): MemberChannels => {
  const channels: MemberChannels = {
    webChannel: true,
    emailChannel: false,
    textChannel: false,
  }

  switch (preferredContact) {
    case 'email':
      channels.emailChannel = true;
    // eslint-disable-next-line no-fallthrough
    case 'text':
      channels.textChannel = true;
  }

  return channels;
}

const getSubject = (type: string): string => {
  // TODO implement
  return type;
}
