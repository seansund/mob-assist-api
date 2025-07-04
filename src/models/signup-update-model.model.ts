import {field, inputType} from '@loopback/graphql';
import {Model, model, property} from '@loopback/repository';

import {SignupRelationsIdsModel} from '../datatypes';

@inputType({description: 'Signup update model'})
@model()
export class SignupUpdate extends Model implements Partial<SignupUpdate>, Partial<SignupRelationsIdsModel> {
  @field({nullable: true})
  @property({
    type: 'string',
  })
  date?: string;

  @field({nullable: true})
  @property({
    type: 'string',
  })
  title?: string;

  @field({nullable: true})
  @property({
    type: 'string',
  })
  description?: string;

  @field({nullable: true})
  @property({
    type: 'string',
  })
  groupId?: string;

  @field({nullable: true})
  @property({
    type: 'string',
  })
  optionSetId?: string;

  @field({nullable: true})
  @property({
    type: 'string',
  })
  assignmentSetId?: string;


  constructor(data?: Partial<SignupUpdate>) {
    super(data);
  }
}
