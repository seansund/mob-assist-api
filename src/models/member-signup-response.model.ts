import {
  belongsTo,
  Entity,
  hasMany,
  model,
  property,
} from '@loopback/repository';
import {
  AssignmentModel,
  MemberModel, MemberSignupResponseModel,
  OptionModel,
  SignupModel,
} from '../datatypes';
import {Member, MemberWithRelations} from './member.model';
import {Signup} from './signup.model';
import {Option} from './option.model';
import {Assignment} from './assignment.model';
import {
  MemberSignupResponseAssignment
} from './member-signup-response-assignment.model';
import {field, ID, objectType} from '@loopback/graphql';

@objectType({description: 'Member response to signup'})
@model()
export class MemberSignupResponse extends Entity implements MemberSignupResponseModel {
  @field(type => ID)
  @property({
    type: 'string',
    id: true,
    generated: true,
  })
  id?: string;

  @field(() => Boolean)
  @property({
    type: 'boolean',
    required: true,
  })
  signedUp: boolean;

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

  @belongsTo(() => Member)
  memberId: string;

  @belongsTo(() => Signup)
  signupId: string;

  @belongsTo(() => Option)
  optionId: string;

  @field(type => [Assignment], {nullable: true})
  @hasMany(() => Assignment, {through: {model: () => MemberSignupResponseAssignment}})
  assignments?: AssignmentModel[];

  @field(type => Member)
  member: MemberModel;

  @field(type => Signup)
  signup: SignupModel;

  @field(type => Option)
  option: OptionModel;

  constructor(data?: Partial<MemberSignupResponse>) {
    super(data);
  }
}

export interface MemberSignupResponseRelations {
  member: MemberWithRelations;
  signup: Signup;
  option: Option;
}

export type MemberSignupResponseWithRelations = MemberSignupResponse & MemberSignupResponseRelations;
