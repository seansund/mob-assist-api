import {belongsTo, Entity, model, property} from '@loopback/repository';
import {Member, MemberWithRelations} from './member.model';
import {MemberRole, MemberRoleWithRelations} from './member-role.model';
import {MemberMemberRoleModel} from '../datatypes';

@model()
export class MemberMemberRole extends Entity implements MemberMemberRoleModel {
  @property({
    type: 'string',
    id: true,
    generated: true,
  })
  id?: string;

  @belongsTo(() => Member)
  memberId: string;

  @belongsTo(() => MemberRole)
  memberRoleId: string;

  constructor(data?: Partial<MemberMemberRole>) {
    super(data);
  }
}

export interface MemberMemberRoleRelations {
  member: MemberWithRelations;
  memberRole: MemberRoleWithRelations;
}

export type MemberMemberRoleWithRelations = MemberMemberRole & MemberMemberRoleRelations;
