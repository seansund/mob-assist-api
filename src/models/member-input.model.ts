import {Model, model, property} from '@loopback/repository';
import {MemberDataModel} from '../datatypes';
import {field, inputType} from '@loopback/graphql';

@inputType()
@model()
export class MemberInput extends Model implements MemberDataModel {
  @field()
  @property({
    type: 'string',
    required: true,
  })
  phone: string;

  @field()
  @property({
    type: 'string',
    required: true,
  })
  email: string;

  @field()
  @property({
    type: 'string',
    required: true,
  })
  firstName: string;

  @field()
  @property({
    type: 'string',
    required: true,
  })
  lastName: string;

  @field({nullable: true})
  @property({
    type: 'string',
    required: false,
  })
  preferredContact?: string;

  constructor(data?: Partial<MemberInput>) {
    super(data);
  }
}
