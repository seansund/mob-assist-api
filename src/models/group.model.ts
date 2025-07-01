import {Entity, hasMany, model, property} from '@loopback/repository';
import {field, ID, objectType} from '@loopback/graphql';

import {GroupMember} from './group-member.model';
import {GroupSummary} from './group-summary.model';
import {Member} from './member.model';
import {GroupModel, GroupSummaryModel, MemberModel} from '../datatypes';
import {Optional} from '../util';

@objectType({description: 'Group of members'})
@model()
export class Group extends Entity implements Optional<GroupModel, 'id'> {
  @field(type => ID)
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
  name: string;

  @field(() => GroupSummary, {nullable: true})
  summary?: GroupSummaryModel;

  @field(type => [Member], {nullable: true})
  @hasMany(() => Member, {through: {model: () => GroupMember}})
  members: MemberModel[];

  constructor(data?: Partial<Group>) {
    super(data);
  }
}

export interface GroupRelations {
  summary?: GroupSummaryModel;
  members: MemberModel[];
}

export type GroupWithRelations = Group & GroupRelations;
