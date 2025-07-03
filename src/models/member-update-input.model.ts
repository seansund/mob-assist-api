import {Model, model, property} from '@loopback/repository';
import {MemberDataModel} from '../datatypes';
import {field, inputType} from '@loopback/graphql';

@inputType()
@model()
export class MemberUpdateInput extends Model implements Partial<MemberDataModel> {
  @field({nullable: true})
  @property({
    type: 'string',
    required: false,
  })
  phone?: string;

  @field({nullable: true})
  @property({
    type: 'string',
    required: false,
  })
  email?: string;

  @field({nullable: true})
  @property({
    type: 'string',
    required: false,
  })
  firstName?: string;

  @field({nullable: true})
  @property({
    type: 'string',
    required: false,
  })
  lastName?: string;

  @field({nullable: true})
  @property({
    type: 'string',
    required: false,
  })
  preferredContact?: string;

  constructor(data?: Partial<MemberUpdateInput>) {
    super(data);
  }
}
