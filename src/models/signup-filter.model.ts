import {Model, model, property} from '@loopback/repository';
import {field, inputType, registerEnumType} from '@loopback/graphql';

import {SignupScope} from '../datatypes';

registerEnumType(SignupScope, {
  name: 'SignupScope',
});

@inputType()
@model()
export class SignupFilter extends Model {
  @field(() => SignupScope)
  @property({
    type: 'object',
  })
  scope?: SignupScope;

  constructor(data?: Partial<SignupFilter>) {
    super(data);
  }
}
