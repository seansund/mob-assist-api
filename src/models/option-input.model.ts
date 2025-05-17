import {Model, model, property} from '@loopback/repository';
import {OptionDataModel} from '../datatypes';
import {field, inputType} from '@loopback/graphql';

@inputType({description: 'Option input'})
@model()
export class OptionInput extends Model implements OptionDataModel {
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


  constructor(data?: Partial<OptionInput>) {
    super(data);
  }
}
