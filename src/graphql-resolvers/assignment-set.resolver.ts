import {inject} from '@loopback/core';
import {
  arg,
  fieldResolver,
  ID,
  mutation,
  query,
  resolver,
  ResolverInterface,
  root,
} from '@loopback/graphql';

import {AssignmentModel, AssignmentSetModel} from '../datatypes';
import {
  Assignment,
  AssignmentInput,
  AssignmentSet,
  AssignmentSetInput,
} from '../models';
import {ASSIGNMENT_SET_API, AssignmentSetApi} from '../services';

@resolver(() => AssignmentSet)
export class AssignmentSetResolver implements ResolverInterface<AssignmentSet> {

  constructor(
    @inject(ASSIGNMENT_SET_API) protected service: AssignmentSetApi,
  ) {}

  @mutation(() => AssignmentSet)
  async createAssignmentSet(
    @arg('assignmentSet') data: AssignmentSetInput,
  ): Promise<AssignmentSetModel> {
    return this.service.create(data);
  }

  @mutation(() => AssignmentSet)
  async updateAssignmentSet(
    @arg('assignmentSetId', () => ID) assignmentSetId: string,
    @arg('name') name: string,
  ): Promise<AssignmentSetModel> {
    return this.service.update(assignmentSetId, {name});
  }

  @mutation(() => AssignmentSet)
  async addAssignment(
    @arg('assignmentSetId', () => ID) assignmentSetId: string,
    @arg('assignment') data: AssignmentInput,
  ): Promise<AssignmentSetModel> {
    return this.service.addAssignment(assignmentSetId, data);
  }

  @mutation(() => AssignmentSet, {nullable: true})
  async deleteAssignment(
    @arg('assignmentSetId', () => ID) assignmentSetId: string,
    @arg('assignmentId') assignmentId: string,
  ): Promise<AssignmentSetModel | undefined> {
    return this.service.deleteAssignment(assignmentSetId, assignmentId);
  }

  @query(() => [AssignmentSet])
  async listAssignmentSets(): Promise<AssignmentSetModel[]> {
    return this.service.list();
  }

  @query(() => AssignmentSet, {nullable: true})
  async getAssignmentSet(@arg('assignmentSetId') assignmentSetId: string): Promise<AssignmentSetModel | undefined> {
    return this.service.get(assignmentSetId)
      .then(result => result);
  }

  @mutation(() => AssignmentSet)
  async deleteAssignmentSet(@arg('assignmentSetId', () => ID) assignmentSetId: string): Promise<AssignmentSetModel | undefined> {
    return this.service.delete(assignmentSetId);
  }

  @fieldResolver(() => [Assignment])
  async assignments(
    @root() assignmentSet: AssignmentSet
  ): Promise<AssignmentModel[]> {
    return this.service.getAssignments(assignmentSet);
  }
}
