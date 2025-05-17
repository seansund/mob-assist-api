import {belongsTo, Entity, model, property} from '@loopback/repository';
import {OptionModel, OptionSetModel} from '../datatypes';
import {OptionSet, OptionSetWithRelations} from './option-set.model';
import {field, ID, objectType} from '@loopback/graphql';

@objectType({description: 'Signup option'})
@model()
export class Option extends Entity implements OptionModel {
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
  value: string;

  @field()
  @property({
    type: 'string',
    required: true,
  })
  shortName: string;

  @field(type => Number)
  @property({
    type: 'number',
    required: true,
  })
  sortIndex: number;

  @field(type => Boolean, {nullable: true})
  @property({
    type: 'boolean',
  })
  declineOption?: boolean;

  @belongsTo(() => OptionSet)
  optionSetId: string;

  @field(type => OptionSet, {nullable: true})
  optionSet: OptionSetModel;

  constructor(data?: Partial<Option>) {
    super(data);
  }
}

export interface OptionRelations {
  optionSet?: OptionSetWithRelations
}

export type OptionWithRelations = Option & OptionRelations;
