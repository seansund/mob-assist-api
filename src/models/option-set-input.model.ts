import {field, inputType} from '@loopback/graphql';
import {Model, model, property} from '@loopback/repository';

import {OptionInput} from './option-input.model';

@inputType({description: 'Option set input'})
@model()
export class OptionSetInput extends Model {
  @field()
  @property({
    type: 'string',
    required: true,
  })
  name: string;

  @field(type => [OptionInput])
  @property({
    type: 'array',
    itemType: 'object',
    required: true,
  })
  options: OptionInput[];

  constructor(data?: Partial<OptionSetInput>) {
    super(data);
  }
}
