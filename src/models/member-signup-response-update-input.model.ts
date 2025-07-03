import {Model, model, property} from '@loopback/repository';
import {field, inputType} from '@loopback/graphql';

@inputType({description: 'Member signup response update'})
@model()
export class MemberSignupResponseUpdateInput extends Model {
  @field(() => Boolean, {nullable: true})
  @property({
    type: 'boolean',
  })
  signedUp?: boolean;

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

  @field({nullable: true})
  @property({
    type: 'string',
  })
  optionId?: string;

  @field({nullable: true})
  @property({
    type: 'string',
    required: false,
  })
  message?: string;

  @field(() => Boolean, {nullable: true})
  @property({
    type: 'boolean',
    required: false,
  })
  checkedIn?: boolean;

  constructor(data?: Partial<MemberSignupResponseUpdateInput>) {
    super(data);
  }
}
