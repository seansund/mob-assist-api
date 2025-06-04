import {field, inputType} from '@loopback/graphql';
import {Model, model, property} from '@loopback/repository';
import {MemberSignupResponseFilterModel} from '../datatypes';

@inputType({description: 'Member signup response filter'})
@model()
export class MemberSignupResponseFilter extends Model implements MemberSignupResponseFilterModel {
  @field({nullable: true})
  @property({
    type: 'string',
  })
  memberId?: string;

  @field({nullable: true})
  @property({
    type: 'string',
  })
  signupId?: string;


  constructor(data?: Partial<MemberSignupResponseFilter>) {
    super(data);
  }
}
