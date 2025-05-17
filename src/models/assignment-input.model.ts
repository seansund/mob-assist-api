import {Model, model, property} from '@loopback/repository';
import {field, inputType} from '@loopback/graphql';
import {AssignmentDataModel} from '../datatypes';

@inputType({description: 'Assignment input'})
@model()
export class AssignmentInput extends Model implements AssignmentDataModel {
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


  constructor(data?: Partial<AssignmentInput>) {
    super(data);
  }
}
