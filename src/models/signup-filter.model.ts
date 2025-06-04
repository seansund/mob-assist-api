import {Model, model, property} from '@loopback/repository';
import {field, inputType, registerEnumType} from '@loopback/graphql';

import {SignupFilterModel, SignupScope} from '../datatypes';

registerEnumType(SignupScope, {
  name: 'SignupScope',
});

@inputType()
@model()
export class SignupFilter extends Model implements SignupFilterModel {
  @field(() => SignupScope)
  @property({
    type: 'object',
  })
  scope?: SignupScope;

  @field({nullable: true})
  @property({
    type: 'string',
  })
  memberId?: string;

  constructor(data?: Partial<SignupFilter>) {
    super(data);
  }
}
