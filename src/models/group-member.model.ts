import {belongsTo, Entity, model, property} from '@loopback/repository';
import {Group, GroupWithRelations} from './group.model';
import {Member, MemberWithRelations} from './member.model';
import {field, ID, objectType} from '@loopback/graphql';
import {GroupMemberModel, GroupModel, MemberModel} from '../datatypes';

@objectType({description: 'Group membership'})
@model()
export class GroupMember extends Entity implements GroupMemberModel {
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

  @field(type => Group)
  group: GroupModel;

  @field(type => Member)
  member: MemberModel;

  constructor(data?: Partial<GroupMember>) {
    super(data);
  }
}

export interface GroupMemberRelations {
  group: GroupWithRelations;
  member: MemberWithRelations;
}

export type GroupMemberWithRelations = GroupMember & GroupMemberRelations;
