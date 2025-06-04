import {ModelRef} from "./base.model";
import {GroupModel} from "./group.model";
import {OptionModel, OptionSetModel} from './option.model';
import {AssignmentModel, AssignmentSetModel} from './assignment.model';
import {MemberSignupResponseModel} from "./member-signup-response.model";
import {Sign} from 'node:crypto';

export const validateDate = (date: string): boolean => {
  if (date.match(/^\d{4}-\d{2}-\d{2}$/) === null) {
    throw new Error('Date format should be YYYY-MM-DD')
  }

  return true;
}

export const formatDate = (date: Date): string => {
  return date.toISOString().split('T')[0];
}

export enum SignupScope {
  UPCOMING = 'UPCOMING',
  FUTURE = 'FUTURE',
  ALL = 'all',
}

export interface SignupDataModel {
  date: string;
  title: string;
  description?: string;
}

export interface SignupInputModel extends SignupDataModel, SignupRelationsIdsModel {
}

export interface BaseSignupModel extends Partial<ModelRef>, SignupDataModel {
}

export interface SignupRelationsIdsModel {
  groupId: string;
  optionSetId: string;
  assignmentSetId: string;
}

export interface SignupRelationsModel {
  group: GroupModel;
  options: OptionModel[];
  assignments: AssignmentModel[];
  responses?: MemberSignupResponseModel[];
}

export interface SignupModel extends BaseSignupModel, SignupRelationsModel {

}

export interface SignupFilterModel {
  memberId?: string;
  scope?: SignupScope;
}
