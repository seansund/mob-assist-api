import {Model, model, property} from '@loopback/repository';
import {field, inputType} from '@loopback/graphql';

@inputType({description: 'Member signup response input'})
@model()
export class MemberSignupResponseInput extends Model {
  @field(() => Boolean)
  @property({
    type: 'string',
    required: true,
  })
  signedUp: boolean;

  @field()
  @property({
    type: 'string',
    required: true,
  })
  memberId: string;

  @field()
  @property({
    type: 'string',
    required: true,
  })
  signupId: string;

  @field()
  @property({
    type: 'string',
    required: true,
  })
  optionId: string;

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

  constructor(data?: Partial<MemberSignupResponseInput>) {
    super(data);
  }
}
