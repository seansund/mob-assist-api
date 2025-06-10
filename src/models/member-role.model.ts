import {Entity, model, property} from '@loopback/repository';
import {MemberRoleModel} from '../datatypes';
import {field, ID, objectType} from '@loopback/graphql';
import {Optional} from '../util';

@objectType({description: 'Member role'})
@model()
export class MemberRole extends Entity implements Optional<MemberRoleModel, 'id'> {
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
  name: string;


  constructor(data?: Partial<MemberRole>) {
    super(data);
  }
}

export interface MemberRoleRelations {
  // describe navigational properties here
}

export type MemberRoleWithRelations = MemberRole & MemberRoleRelations;
