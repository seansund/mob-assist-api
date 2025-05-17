import {Model, model, property} from '@loopback/repository';
import {field, inputType} from '@loopback/graphql';
import {SignupDataModel, SignupRelationsIdsModel} from '../datatypes';

@inputType({description: 'Signup input'})
@model()
export class SignupInput extends Model implements SignupDataModel, SignupRelationsIdsModel {
  @field()
  @property({
    type: 'string',
    required: true,
  })
  date: string;

  @field()
  @property({
    type: 'string',
    required: true,
  })
  title: string;

  @field({nullable: true})
  @property({
    type: 'string',
  })
  description?: string;

  @field()
  @property({
    type: 'string',
    required: true,
  })
  groupId: string;

  @field()
  @property({
    type: 'string',
    required: true,
  })
  optionSetId: string;

  @field()
  @property({
    type: 'string',
    required: true,
  })
  assignmentSetId: string;


  constructor(data?: Partial<SignupInput>) {
    super(data);
  }
}
