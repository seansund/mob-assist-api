import {
  arg, fieldResolver, ID,
  mutation,
  query,
  resolver,
  ResolverInterface, root,
} from '@loopback/graphql';
import {repository} from '@loopback/repository';

import {AssignmentSetModel} from '../datatypes';
import {
  Assignment,
  AssignmentInput,
  AssignmentSet,
  AssignmentSetInput,
} from '../models';
import {AssignmentRepository} from '../repositories';

@resolver(() => AssignmentSet)
export class AssignmentSetResolver implements ResolverInterface<AssignmentSet> {

  constructor(
    @repository('AssignmentSetRepository') protected repo: AssignmentRepository,
    @repository('AssignmentRepository') protected assignmentRepo: AssignmentRepository,
  ) {}

  @mutation(() => AssignmentSet)
  async createAssignmentSet(
    @arg('assignmentSet') data: AssignmentSetInput,
  ): Promise<AssignmentSetModel> {
    const assignmentSet = await this.repo.create({name: data.name});

    const optionInputs = data.assignments.map((assignment: AssignmentInput) => ({...assignment, assignmentSetId: assignmentSet.getId()}))

    const assignments: Assignment[] = await this.assignmentRepo.createAll(optionInputs);

    return {
      id: assignmentSet.getId(),
      name: assignmentSet.name,
      assignments
    }
  }

  @mutation(() => AssignmentSet)
  async updateAssignmentSet(
    @arg('assignmentSetId', () => ID) assignmentSetId: string,
    @arg('name') name: string,
  ): Promise<AssignmentSetModel> {
    const assignmentSet: AssignmentSetModel | null = await this.repo.findOne({where: {id: assignmentSetId}});

    if (!assignmentSet) {
      throw new Error('AssignmentSet not found: ' + assignmentSetId);
    }

    const updatedAssignmentSet: AssignmentSetModel = Object.assign(assignmentSet, {name});

    await this.repo.updateById(assignmentSetId, updatedAssignmentSet);

    return updatedAssignmentSet;
  }

  @mutation(() => AssignmentSet)
  async addAssignment(
    @arg('assignmentSetId', () => ID) assignmentSetId: string,
    @arg('assignment') data: AssignmentInput,
  ): Promise<AssignmentSetModel> {
    const assignmentSet: AssignmentSet | null = await this.repo.findOne({where: {id: assignmentSetId}});
    if (!assignmentSet) {
      throw new Error('AssignmentSet not found: ' + assignmentSetId);
    }

    await this.assignmentRepo.create({...data, assignmentSetId: assignmentSetId});

    return {
      id: assignmentSet.getId(),
      name: assignmentSet.name,
    }
  }

  @mutation(() => AssignmentSet, {nullable: true})
  async deleteAssignment(
    @arg('assignmentSetId', () => ID) assignmentSetId: string,
    @arg('assignmentId') assignmentId: string,
  ): Promise<AssignmentSetModel | null> {
    const assignmentSet: AssignmentSet | null = await this.repo.findOne({where: {id: assignmentSetId}});

    if (assignmentSet) {
      await this.assignmentRepo.deleteAll({id: assignmentId, assignmentSetId: assignmentSetId});
    }

    return assignmentSet;
  }

  @query(() => [AssignmentSet])
  async listAssignmentSets(): Promise<AssignmentSetModel[]> {
    return this.repo.find();
  }

  @query(() => AssignmentSet, {nullable: true})
  async getAssignmentSet(@arg('assignmentSetId') assignmentSetId: string): Promise<AssignmentSetModel | null> {
    return this.repo.findOne({where: {id: assignmentSetId}});
  }

  @mutation(() => AssignmentSet)
  async deleteAssignmentSet(@arg('assignmentSetId', () => ID) assignmentSetId: string): Promise<AssignmentSetModel> {
    const assignmentSet: AssignmentSetModel | null = await this.repo.findOne({where: {id: assignmentSetId}});

    if (!assignmentSet) {
      throw new Error('AssignmentSet not found: ' + assignmentSetId);
    }

    await this.repo.deleteById(assignmentSetId);

    return assignmentSet;
  }

  @fieldResolver(() => [Assignment])
  async assignments(
    @root() assignmentSet: AssignmentSet
  ): Promise<Assignment[]> {
    return this.assignmentRepo.find({where: {assignmentSetId: assignmentSet.getId()}});
  }
}
