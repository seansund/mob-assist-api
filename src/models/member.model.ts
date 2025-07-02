import {Entity, model, property} from '@loopback/repository';
import {field, ID, objectType} from '@loopback/graphql';
import {MemberRole} from './member-role.model';
import {
  GroupModel,
  MemberGroupModel,
  MemberModel,
  MemberRoleModel,
  NotificationModel,
} from '../datatypes';
import {Notification} from './notification.model';
import {Optional} from '../util';
import {MemberGroup} from './member-group.model';

@objectType({description: 'Member of the application'})
@model()
export class Member extends Entity implements Optional<MemberModel, 'id'> {
  @field(() => ID)
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
  email: string;

  @field()
  @property({
    type: 'string',
    required: true,
  })
  phone: string;

  @field()
  @property({
    type: 'string',
    required: true,
  })
  firstName: string;

  @field()
  @property({
    type: 'string',
    required: true,
  })
  lastName: string;

  @field({nullable: true})
  @property({
    type: 'string',
    required: false,
  })
  preferredContact?: string;

  // @hasMany(() => MemberRole, {through: {model: () => MemberMemberRole}})
  @field(() => [MemberRole], {nullable: true})
  roles?: MemberRoleModel[];

  // @hasMany(() => Group, {through: {model: () => GroupMember}})
  @field(() => [MemberGroup], {nullable: true})
  groups?: MemberGroupModel[];

  // @hasMany(() => Notification, {through: {model: () => MemberNotification}})
  @field(() => [Notification], {nullable: true})
  notifications?: NotificationModel[];

  constructor(data?: Partial<Member>) {
    super(data);
  }
}

export interface MemberRelations {
  roles?: MemberRoleModel[];
  groups?: GroupModel[];
  notifications?: NotificationModel[];
}

export type MemberWithRelations = Member & MemberRelations;
