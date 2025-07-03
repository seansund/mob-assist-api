import {model, Model} from '@loopback/repository';
import {MemberGroupModel} from '../datatypes';
import {field, ID, objectType} from '@loopback/graphql';

@objectType()
@model()
export class MemberGroup extends Model implements MemberGroupModel {
  @field(() => ID)
  id: string;

  @field()
  name: string;

  @field(() => ID, {nullable: true})
  roleId?: string;

  constructor(data?: Partial<MemberGroup>) {
    super(data);
  }
}
