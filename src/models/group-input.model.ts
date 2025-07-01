import {field, inputType} from '@loopback/graphql';
import {Model, model, property} from '@loopback/repository';
import {GroupDataModel} from '../datatypes';

@inputType({description: 'Group update'})
@model()
export class GroupInput extends Model implements Partial<GroupDataModel> {
  @field({nullable: true})
  @property({
    type: 'string',
    required: false,
  })
  name?: string;

  constructor(data?: Partial<GroupInput>) {
    super(data);
  }
}
