import {Model, model, property} from '@loopback/repository';
import {OptionDataModel} from '../datatypes';
import {field, inputType} from '@loopback/graphql';

@inputType({description: 'Option update input'})
@model()
export class OptionUpdateInput extends Model implements Partial<OptionDataModel> {
  @field({nullable: true})
  @property({
    type: 'string',
    required: false,
  })
  value?: string;

  @field({nullable: true})
  @property({
    type: 'string',
    required: false,
  })
  shortName?: string;

  @field(type => Number, {nullable: true})
  @property({
    type: 'number',
    required: false,
  })
  sortIndex: number;

  @field(type => Boolean, {nullable: true})
  @property({
    type: 'boolean',
  })
  declineOption?: boolean;


  constructor(data?: Partial<OptionUpdateInput>) {
    super(data);
  }
}
