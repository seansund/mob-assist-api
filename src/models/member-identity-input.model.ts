import {Model, model, property} from '@loopback/repository';
import {field, ID, inputType} from '@loopback/graphql';

@inputType()
@model()
export class MemberIdentityInput extends Model {
  @field(() => ID, {nullable: true})
  @property({
    type: 'string',
    id: true,
    itemType: () => ID,
  })
  id?: string;

  @field({nullable: true})
  @property({
    type: 'string',
  })
  email?: string;

  @field({nullable: true})
  @property({
    type: 'string',
  })
  phone?: string;

  constructor(data?: Partial<MemberIdentityInput>) {
    super(data);
  }
}
