import {belongsTo, Entity, model, property} from '@loopback/repository';
import {field, ID, objectType} from '@loopback/graphql';
import {Member} from './member.model';
import {
  MemberModel, NotificationChannelResultModel,
  NotificationModel,
  NotificationResultModel,
} from '../datatypes';
import {Optional} from '../util';

@objectType({description: 'Notification'})
@model()
export class Notification extends Entity implements Optional<NotificationModel, 'id'> {
  @field(() => ID, {nullable: true})
  @property({
    type: 'string',
    id: true,
    generated: true,
  })
  id?: string;

  @field()
  @property({
    type: 'string',
    required: true,
  })
  subject: string;

  @field()
  @property({
    type: 'string',
    required: true,
  })
  textAction: string;

  @field()
  @property({
    type: 'string',
    required: true,
  })
  message: string;

  @field()
  @property({
    type: 'string',
    required: true,
  })
  type: string;

  @field()
  @property({
    type: 'string',
    required: true,
  })
  date: string;

  @field(() => Boolean, {nullable: true})
  @property({
    type: 'boolean',
    required: false,
  })
  read?: boolean;

  @field(() => Boolean)
  @property({
    type: 'boolean',
    required: true,
  })
  textChannel: boolean;

  @field(() => Boolean)
  @property({
    type: 'boolean',
    required: true,
  })
  emailChannel: boolean;

  @field(() => Boolean)
  @property({
    type: 'boolean',
    required: true,
  })
  webChannel: boolean;

  @belongsTo(() => Member, {keyTo: 'id'})
  fromMemberId?: string;

  @field(() => Member, {nullable: true})
  fromMember?: MemberModel;

  @belongsTo(() => Member, {keyTo: 'id'})
  toMemberId: string;

  @field(() => Member)
  toMember: MemberModel;


  constructor(data?: Partial<Notification>) {
    super(data);
  }
}

export interface NotificationRelations {
  toMember?: MemberModel;
  fromMember?: MemberModel;
}

export type NotificationWithRelations = Notification & NotificationRelations;


@objectType()
export class NotificationChannelResult implements NotificationChannelResultModel {
  @field()
  channel: string;
  @field(() => Number)
  count: number;
}

@objectType()
export class NotificationResult implements NotificationResultModel {
  @field(() => [NotificationChannelResult])
  channels: NotificationChannelResultModel[];
  @field()
  type: string;
}
