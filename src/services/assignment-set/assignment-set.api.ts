import {BaseApi} from '../base.api';
import {
  AssignmentModel,
  AssignmentSetInputModel,
  AssignmentSetModel,
} from '../../datatypes';
import {Assignment} from '../../models';

export const ASSIGNMENT_SET_API = 'services.AssignmentSetApi';

export interface AssignmentSetContext {
  assignmentSetIds: string[];

  assignments?: Promise<Assignment[]>;
}

export abstract class AssignmentSetApi extends BaseApi<AssignmentSetModel, AssignmentSetInputModel> {

  abstract addAssignment(id: string, assignment: Omit<AssignmentModel, 'id'>): Promise<AssignmentSetModel>;
  abstract deleteAssignment(id: string, assignmentId: string): Promise<AssignmentSetModel | undefined>;

  abstract getAssignments(assignmentSet: AssignmentSetModel, context?: AssignmentSetContext): Promise<AssignmentModel[]>;
}
