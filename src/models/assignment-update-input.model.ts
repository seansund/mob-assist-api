import {Model, model, property} from '@loopback/repository';
import {field, inputType} from '@loopback/graphql';
import {AssignmentDataModel} from '../datatypes';

@inputType({description: 'Assignment update input'})
@model()
export class AssignmentUpdateInput extends Model implements Partial<AssignmentDataModel> {
  @field({nullable: true})
  @property({
    type: 'string',
    required: false,
  })
  group?: string;

  @field({nullable: true})
  @property({
    type: 'string',
    required: false,
  })
  name?: string;

  @field(type => Number, {nullable: true})
  @property({
    type: 'number',
    required: false,
  })
  row: number;

  @field({nullable: true})
  @property({
    type: 'string',
    required: false,
  })
  partnerId?: string;


  constructor(data?: Partial<AssignmentUpdateInput>) {
    super(data);
  }
}
