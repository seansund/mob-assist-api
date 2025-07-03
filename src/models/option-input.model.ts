import {Model, model, property} from '@loopback/repository';
import {OptionInputModel} from '../datatypes';
import {field, inputType} from '@loopback/graphql';

@inputType({description: 'Option input'})
@model()
export class OptionInput extends Model implements OptionInputModel {
  @field()
  @property({
    type: 'string',
    required: true,
  })
  value: string;

  @field()
  @property({
    type: 'string',
    required: true,
  })
  shortName: string;

  @field(type => Number)
  @property({
    type: 'number',
    required: true,
  })
  sortIndex: number;

  @field(type => Boolean, {nullable: true})
  @property({
    type: 'boolean',
  })
  declineOption?: boolean;

  @field()
  @property({
    type: 'string',
    required: true,
  })
  optionSetId: string;

  constructor(data?: Partial<OptionInput>) {
    super(data);
  }
}
