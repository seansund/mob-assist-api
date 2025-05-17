import {Entity, hasMany, model, property} from '@loopback/repository';
import {GroupModel, MemberModel} from '../datatypes';
import {GroupMember} from './group-member.model';
import {Member} from './member.model';
import {field, ID, objectType} from '@loopback/graphql';

@objectType({description: 'Group of members'})
@model()
export class Group extends Entity implements GroupModel {
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

  @field(type => [Member], {nullable: true})
  @hasMany(() => Member, {through: {model: () => GroupMember}})
  members: MemberModel[];

  constructor(data?: Partial<Group>) {
    super(data);
  }
}

export interface GroupRelations {
  // describe navigational properties here
}

export type GroupWithRelations = Group & GroupRelations;
