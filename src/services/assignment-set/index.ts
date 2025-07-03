import {Context} from '@loopback/context';
import {AssignmentSetImpl} from './assignment-set.impl';
import {ASSIGNMENT_SET_API} from './assignment-set.api';

export * from './assignment-set.api';

export const bindAssignmentSetApi = (service: Context) => {
  service.bind(ASSIGNMENT_SET_API).toClass(AssignmentSetImpl);
}
