import {field, objectType} from '@loopback/graphql';
import {Model, model, property} from '@loopback/repository';
import {GroupSummaryModel} from '../datatypes';

@objectType()
@model()
export class GroupSummary extends Model implements GroupSummaryModel {

  @field(type => Number)
  @property({
    type: 'number',
    required: true,
  })
  memberCount: number;
}
