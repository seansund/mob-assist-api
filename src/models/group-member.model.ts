import {belongsTo, Entity, model, property} from '@loopback/repository';
import {Group, GroupWithRelations} from './group.model';
import {Member, MemberWithRelations} from './member.model';
import {field, ID, objectType} from '@loopback/graphql';
import {
  GroupMemberModel,
  GroupModel,
  MemberModel,
  MemberRoleModel,
} from '../datatypes';
import {Optional} from '../util';
import {MemberRole} from './member-role.model';

@objectType({description: 'Group membership'})
@model()
export class GroupMember extends Entity implements Optional<GroupMemberModel, 'id'> {
  @field(type => ID)
  @property({
    type: 'string',
    id: true,
    generated: true,
  })
  id?: string;

  @belongsTo(() => Group)
  groupId: string;

  @belongsTo(() => Member)
  memberId: string;

  @belongsTo(() => MemberRole)
  roleId?: string;

  @field(type => Group)
  group: GroupModel;

  @field(type => Member)
  member: MemberModel;

  @field(() => MemberRole, {nullable: true})
  role?: MemberRoleModel;

  constructor(data?: Partial<GroupMember>) {
    super(data);
  }
}

export interface GroupMemberRelations {
  group: GroupWithRelations;
  member: MemberWithRelations;
}

export type GroupMemberWithRelations = GroupMember & GroupMemberRelations;
