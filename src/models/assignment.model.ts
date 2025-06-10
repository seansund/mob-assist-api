import {belongsTo, Entity, model, property} from '@loopback/repository';
import {field, ID, objectType} from '@loopback/graphql';

import {AssignmentModel, AssignmentSetModel} from '../datatypes';
import {
  AssignmentSet,
  AssignmentSetWithRelations,
} from './assignment-set.model';
import {Optional} from '../util';

@objectType({description: 'Assignment definitions'})
@model({settings: {strict: false}})
export class Assignment extends Entity implements Optional<AssignmentModel, 'id'> {
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
  group: string;

  @field()
  @property({
    type: 'string',
    required: true,
  })
  name: string;

  @field(type => Number)
  @property({
    type: 'number',
    required: true,
  })
  row: number;

  @field({nullable: true})
  @property({
    type: 'string',
  })
  partnerId?: string;

  @belongsTo(() => AssignmentSet)
  assignmentSetId: string;

  @field(type => AssignmentSet, {nullable: true})
  assignmentSet?: AssignmentSetModel;

  constructor(data?: Partial<Assignment>) {
    super(data);
  }
}

export interface AssignmentRelations {
  assignmentSet?: AssignmentSetWithRelations;
}

export type AssignmentWithRelations = Assignment & AssignmentRelations;
