import {Entity, hasMany, Model, model, property} from '@loopback/repository';
import {OptionModel, OptionSetModel, OptionSetSummaryModel} from '../datatypes';
import {Option} from './option.model';
import {field, ID, objectType} from '@loopback/graphql';
import {Optional} from '../util';

@objectType({description: 'Collection of options'})
@model()
export class OptionSet extends Entity implements Optional<OptionSetModel, 'id'> {
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

  @field(type => [Option], {nullable: true})
  @hasMany(() => Option, {keyTo: 'optionSetId'})
  options?: OptionModel[];

  @field(type => OptionSetSummary, {nullable: true})
  summary?: OptionSetSummaryModel;


  constructor(data?: Partial<OptionSet>) {
    super(data);
  }
}

export interface OptionSetRelations {
  // describe navigational properties here
}

export type OptionSetWithRelations = OptionSet & OptionSetRelations;

@objectType({description: 'Summary of an option set'})
@model()
export class OptionSetSummary extends Model implements OptionSetSummaryModel {
  @field(() => Number)
  signupCount: number;
}
