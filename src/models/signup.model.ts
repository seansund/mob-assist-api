import {
  belongsTo,
  Entity,
  hasMany,
  model,
  property,
} from '@loopback/repository';
import {
  AssignmentSetModel,
  GroupModel,
  MemberSignupResponseModel,
  OptionModel,
  OptionSetModel,
  SignupModel,
} from '../datatypes';
import {Group, GroupWithRelations} from './group.model';
import {Option} from './option.model';
import {OptionSet, OptionSetWithRelations} from './option-set.model';
import {
  AssignmentSet,
  AssignmentSetWithRelations,
} from './assignment-set.model';
import {MemberSignupResponse} from './member-signup-response.model';
import {field, ID, objectType} from '@loopback/graphql';

@objectType({description: 'Signup'})
@model()
export class Signup extends Entity implements SignupModel {
  @field(() => ID)
  @property({
    type: 'string',
    id: true,
    generated: true,
  })
  id?: string;

  @field()
  @property({
    type: 'string',
    required: true,
  })
  date: string;

  @field()
  @property({
    type: 'string',
    required: true,
  })
  title: string;

  @field({nullable: true})
  @property({
    type: 'string',
  })
  description?: string;

  @belongsTo(() => Group)
  groupId: string;

  @belongsTo(() => OptionSet)
  optionSetId: string;

  @belongsTo(() => AssignmentSet)
  assignmentSetId: string;

  @field(() => [MemberSignupResponse], {nullable: true})
  @hasMany(() => MemberSignupResponse, {keyTo: 'signupId'})
  responses?: MemberSignupResponseModel[];

  @field(() => Group)
  group: GroupModel;

  @field(() => OptionSet)
  optionSet: OptionSetModel;

  @field(() => [Option])
  options: OptionModel[];

  @field(() => AssignmentSet)
  assignmentSet: AssignmentSetModel;

  constructor(data?: Partial<Signup>) {
    super(data);
  }
}

export interface SignupRelations {
  group: GroupWithRelations;
  optionSet: OptionSetWithRelations;
  assignmentSet: AssignmentSetWithRelations;
}

export type SignupWithRelations = Signup & SignupRelations;
