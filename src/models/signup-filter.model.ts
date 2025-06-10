import {Model, model, property} from '@loopback/repository';
import {field, inputType, registerEnumType} from '@loopback/graphql';

import {MemberIdentifier, SignupFilterModel, SignupScope} from '../datatypes';
import {MemberIdentityInput} from './member-identity-input.model';

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

  @field(() => MemberIdentityInput, {nullable: true})
  @property({
    type: 'object',
  })
  memberId?: MemberIdentifier;

  constructor(data?: Partial<SignupFilter>) {
    super(data);
  }
}
