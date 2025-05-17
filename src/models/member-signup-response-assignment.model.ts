import {belongsTo, Entity, model, property} from '@loopback/repository';
import {field, ID, objectType} from '@loopback/graphql';

import {
  MemberSignupResponse,
  MemberSignupResponseWithRelations,
} from './member-signup-response.model';
import {Assignment, AssignmentWithRelations} from './assignment.model';
import {AssignmentModel, MemberSignupResponseModel} from '../datatypes';

@objectType({description: 'Assignment for member signup response'})
@model()
export class MemberSignupResponseAssignment extends Entity {
  @field(type => ID)
  @property({
    type: 'string',
    id: true,
    generated: true,
  })
  id?: string;

  @belongsTo(() => MemberSignupResponse)
  memberSignupResponseId: string;

  @belongsTo(() => Assignment)
  assignmentId: string;

  @field(type => MemberSignupResponse)
  memberSignupResponse: MemberSignupResponseModel;

  @field(type => Assignment)
  assignment: AssignmentModel;

  constructor(data?: Partial<MemberSignupResponseAssignment>) {
    super(data);
  }
}

export interface MemberSignupResponseAssignmentRelations {
  memberSignupResponse: MemberSignupResponseWithRelations;
  assignment: AssignmentWithRelations;
}

export type MemberSignupResponseAssignmentWithRelations = MemberSignupResponseAssignment & MemberSignupResponseAssignmentRelations;
