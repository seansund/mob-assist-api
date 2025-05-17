import {Entity, model, property} from '@loopback/repository';
import {field, ID, objectType} from '@loopback/graphql';

import {Group} from './group.model';
import {MemberRole} from './member-role.model';
import {GroupModel, MemberModel, MemberRoleModel} from '../datatypes';

@objectType({description: 'Member of the application'})
@model()
export class Member extends Entity implements MemberModel {
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
  @field(() => [Group], {nullable: true})
  groups?: GroupModel[];

  constructor(data?: Partial<Member>) {
    super(data);
  }
}

export interface MemberRelations {
  roles?: MemberRoleModel[];
  groups?: GroupModel[];
}

export type MemberWithRelations = Member & MemberRelations;
