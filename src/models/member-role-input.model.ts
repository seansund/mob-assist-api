import {Model, model, property} from '@loopback/repository';
import {field, inputType} from '@loopback/graphql';

@inputType({description: 'Member role input'})
@model()
export class MemberRoleInput extends Model {
  @field()
  @property({
    type: 'string',
    required: true,
  })
  name: string;


  constructor(data?: Partial<MemberRoleInput>) {
    super(data);
  }
}
