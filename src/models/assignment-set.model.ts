import {Entity, hasMany, model, property} from '@loopback/repository';
import {AssignmentModel, AssignmentSetModel} from '../datatypes';
import {Assignment} from './assignment.model';
import {field, ID, objectType} from '@loopback/graphql';

@objectType({description: 'Collection of assignments'})
@model()
export class AssignmentSet extends Entity implements AssignmentSetModel {
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

  @field(type => [Assignment], {nullable: true})
  @hasMany(() => Assignment, {keyTo: 'assignmentSetId'})
  assignments?: AssignmentModel[];


  constructor(data?: Partial<AssignmentSet>) {
    super(data);
  }
}

export interface AssignmentSetRelations {
  // describe navigational properties here
}

export type AssignmentSetWithRelations = AssignmentSet & AssignmentSetRelations;
