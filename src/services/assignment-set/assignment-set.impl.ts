import {AssignmentSetApi, AssignmentSetContext} from './assignment-set.api';
import {repository} from '@loopback/repository';
import {
  AssignmentRepository,
  AssignmentSetRepository,
} from '../../repositories';
import {AssignmentModel, AssignmentSetModel} from '../../datatypes';
import {Assignment, AssignmentSet} from '../../models';

export class AssignmentSetImpl implements AssignmentSetApi {
  constructor(
    @repository('AssignmentSetRepository') protected repo: AssignmentSetRepository,
    @repository('AssignmentRepository') protected assignmentRepo: AssignmentRepository,
  ) {
  }

  async create(data: Omit<AssignmentSetModel, "id">): Promise<AssignmentSetModel> {
    const assignmentSet = await this.repo.create({name: data.name});

    const optionInputs = (data.assignments ?? []).map((assignment: AssignmentModel) => ({...assignment, assignmentSetId: assignmentSet.getId()}))

    const assignments: Assignment[] = await this.assignmentRepo.createAll(optionInputs);

    return {
      id: assignmentSet.getId(),
      name: assignmentSet.name,
      assignments
    }
  }

  async delete(id: string): Promise<AssignmentSetModel> {
    const assignmentSet: AssignmentSetModel | null = await this.repo.findOne({where: {id}});

    if (!assignmentSet) {
      throw new Error('AssignmentSet not found: ' + id);
    }

    await this.repo.deleteById(id);

    return assignmentSet;
  }

  async get(id: string): Promise<AssignmentSetModel> {
    return await this.repo.findOne({where: {id}}) as AssignmentSetModel;
  }

  async update(id: string, data: Partial<AssignmentSetModel>): Promise<AssignmentSetModel> {
    const assignmentSet: AssignmentSetModel | null = await this.repo.findOne({where: {id}});

    if (!assignmentSet) {
      throw new Error('AssignmentSet not found: ' + id);
    }

    const updatedAssignmentSet: AssignmentSetModel = Object.assign(assignmentSet, data);

    await this.repo.updateById(id, updatedAssignmentSet);

    return updatedAssignmentSet;
  }

  async list(): Promise<AssignmentSetModel[]> {
    return this.repo.find();
  }


  async addAssignment(assignmentSetId: string, data: Omit<AssignmentModel, 'id'>): Promise<AssignmentSetModel> {
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

  async deleteAssignment(assignmentSetId: string, assignmentId: string): Promise<AssignmentSetModel | undefined> {
    const assignmentSet: AssignmentSet | null = await this.repo.findOne({where: {id: assignmentSetId}});

    if (assignmentSet) {
      await this.assignmentRepo.deleteAll({id: assignmentId, assignmentSetId: assignmentSetId});
    }

    return assignmentSet ?? undefined;
  }

  async getAssignments(
    assignmentSet: AssignmentSetModel,
    context: AssignmentSetContext = {assignmentSetIds: assignmentSet.id ? [assignmentSet.id] : []}
  ): Promise<AssignmentModel[]> {

    if (!context.assignments) {
      context.assignments = this.assignmentRepo
        .find({where: {assignmentSetId: {inq: context.assignmentSetIds}}});
    }

    const assignments = await context.assignments;

    return assignments.filter(assignment => assignment.assignmentSetId === assignmentSet.id);
  }

}
