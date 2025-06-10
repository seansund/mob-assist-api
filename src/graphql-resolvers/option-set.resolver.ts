import {
  arg,
  fieldResolver,
  ID,
  mutation,
  query,
  resolver,
  ResolverInterface,
  root,
} from '@loopback/graphql';
import {repository} from '@loopback/repository';

import {OptionModel, OptionSetModel} from '../datatypes';
import {
  Option,
  OptionInput,
  OptionSet,
  OptionSetInput,
  OptionSetWithRelations,
} from '../models';
import {OptionRepository, OptionSetRepository} from '../repositories';
import {entitiesToModels, entityToModel} from '../util';

@resolver(() => OptionSet)
export class OptionSetResolver implements ResolverInterface<OptionSet> {

  constructor(
    @repository('OptionSetRepository') protected repo: OptionSetRepository,
    @repository('OptionRepository') protected optionRepo: OptionRepository,
  ) {}

  @mutation(() => OptionSet)
  async createOptionSet(
    @arg('optionSet') data: OptionSetInput,
  ): Promise<OptionSetModel> {
    const optionSet = await this.repo.create({name: data.name});

    const optionInputs = data.options.map((option: OptionInput) => ({...option, optionSetId: optionSet.getId()}))

    const options: Option[] = await this.optionRepo.createAll(optionInputs);

    return {
      id: optionSet.getId(),
      name: optionSet.name,
      options: entitiesToModels(options)
    }
  }

  @mutation(() => OptionSet)
  async updateOptionSet(
    @arg('optionSetId', () => ID) optionSetId: string,
    @arg('name') name: string,
  ): Promise<OptionSetModel> {
    const optionSet: OptionSetModel | null = await this.repo
      .findOne({where: {id: optionSetId}})
      .then(entityToModel);

    if (!optionSet) {
      throw new Error('OptionSet not found: ' + optionSetId);
    }

    const updatedOptionSet: OptionSetModel = Object.assign(optionSet, {name});

    await this.repo.updateById(optionSetId, updatedOptionSet);

    return updatedOptionSet;
  }

  @query(() => [OptionSet])
  async listOptionSets(): Promise<OptionSetModel[]> {
    console.log('listOptionSets');
    return this.repo.find().then(entitiesToModels);
  }

  @query(() => OptionSet, {nullable: true})
  async getOptionSet(@arg('optionSetId', () => ID) optionSetId: string): Promise<OptionSetModel | null> {
    return this.repo
      .findOne({where: {id: optionSetId}})
      .then(entityToModel);
  }

  @mutation(() => OptionSet)
  async deleteOptionSet(@arg('optionSetId', () => ID) optionSetId: string): Promise<OptionSetModel> {
    const optionSet: OptionSetModel | null = await this.repo
      .findOne({where: {id: optionSetId}})
      .then(entityToModel);

    if (!optionSet) {
      throw new Error('OptionSet not found: ' + optionSetId);
    }

    await this.repo.deleteById(optionSetId);

    return optionSet;
  }

  @mutation(() => OptionSet)
  async addOption(
    @arg('optionSetId', () => ID) optionSetId: string,
    @arg('option') data: OptionInput,
  ): Promise<OptionSetModel> {
    const optionSet: OptionSetWithRelations | null = await this.repo.findOne({where: {id: optionSetId}});

    if (!optionSet) {
      throw new Error('OptionSet not found: ' + optionSetId);
    }

    const option: Option = await this.optionRepo.create({...data, optionSetId: optionSetId});

    return {
      id: optionSet.getId(),
      name: optionSet.name,
      options: entitiesToModels([...(optionSet.options ?? []), option]),
    }
  }

  @mutation(() => OptionSet)
  async removeOption(
    @arg('optionSetId', () => ID) optionSetId: string,
    @arg('optionId') optionId: string,
  ): Promise<OptionSetModel | null> {
    await this.optionRepo.deleteById(optionId);

    return this.repo.findOne({where: {id: optionSetId}}).then(entityToModel);
  }

  @fieldResolver(() => [Option])
  async options(@root() optionSet: OptionSet): Promise<OptionModel[]> {
    return this.optionRepo
      .find({where: {optionSetId: optionSet.getId()}})
      .then(entitiesToModels);
  }
}
