import {Model, model, property} from '@loopback/repository';
import {field, objectType} from '@loopback/graphql';
import {Option} from './option.model';
import {OptionModel, OptionSummaryModel} from '../datatypes';

@objectType()
@model()
export class OptionSummary extends Model implements OptionSummaryModel {
  @field(() => Number)
  @property({
    type: 'number',
    required: true,
  })
  count: number;

  @field(() => Number)
  @property({
    type: 'number',
    required: true,
  })
  assignmentCount: number;

  @field(() => Option, {nullable: true})
  @property({
    type: 'object',
    required: false,
  })
  option?: OptionModel;


  constructor(data?: Partial<OptionSummary>) {
    super(data);
  }
}
