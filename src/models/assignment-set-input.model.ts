import {Model, model, property} from '@loopback/repository';
import {field, inputType} from '@loopback/graphql';
import {AssignmentInput} from './assignment-input.model';

@inputType({description: 'Assignment set input for creating an assignment set'})
@model()
export class AssignmentSetInput extends Model {
  @field()
  @property({
    type: 'string',
    required: true,
  })
  name: string;

  @field(type => [AssignmentInput])
  @property({
    type: 'array',
    itemType: 'object',
    required: true,
  })
  assignments: AssignmentInput[];

  constructor(data?: Partial<AssignmentSetInput>) {
    super(data);
  }
}
