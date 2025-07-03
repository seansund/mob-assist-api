import {model, Model} from '@loopback/repository';
import {field, ID, objectType} from '@loopback/graphql';
import {MemberOfGroupModel, MemberRoleModel} from '../datatypes';
import {MemberRole} from './member-role.model';

@objectType()
@model()
export class MemberOfGroup extends Model implements MemberOfGroupModel {
  @field(() => ID)
  id: string;

  @field()
  phone: string;

  @field()
  email: string;

  @field()
  firstName: string;

  @field()
  lastName: string;

  @field()
  preferredContact?: string;

  @field(() => [MemberRole], {nullable: true})
  roles?: MemberRoleModel[];

  @field(() => ID, {nullable: true})
  roleId?: string;
  
  constructor(data?: Partial<MemberOfGroup>) {
    super(data);
  }
}
