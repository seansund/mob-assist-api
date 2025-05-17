import {ModelRef} from "./base.model";

export interface AssignmentSetModel extends Partial<ModelRef> {
    name: string;
    assignments?: AssignmentModel[];
}

export interface AssignmentDataModel {
  group: string;
  name: string;
  row: number;
  partnerId?: string;
}

export interface AssignmentModel extends Partial<ModelRef>, AssignmentDataModel {
}
