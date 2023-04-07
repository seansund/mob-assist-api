package com.dex.mobassist.server.repository.mongodb;

import com.dex.mobassist.server.model.AssignmentSet;
import com.dex.mobassist.server.repository.mongodb.domain.MongoDBAssignmentSet;
import com.dex.mobassist.server.repository.AssignmentSetRepository;
import lombok.NonNull;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository("AssignmentSetRepository")
@Profile("db-mongodb")
public class AssignmentSetRepositoryMongoDB extends AbstractRepositoryMongoDB<AssignmentSet, MongoDBAssignmentSet> implements AssignmentSetRepository {
    protected AssignmentSetRepositoryMongoDB(MongoTemplate mongoTemplate) {
        super(mongoTemplate, MongoDBAssignmentSet.class);
    }

    @Override
    protected <A extends AssignmentSet> String getId(@NonNull A cargo) {
        return cargo.getId();
    }

    @Override
    protected <A extends AssignmentSet> MongoDBAssignmentSet updateDomainWithCargo(@NonNull MongoDBAssignmentSet domain, @NonNull A cargo) {
        return domain.updateWith(cargo);
    }

    @Override
    protected Sort defaultSort() {
        return Sort.by(Sort.Direction.ASC, "name");
    }

    @Override
    protected MongoDBAssignmentSet createDomainObject(@NonNull AssignmentSet cargo) {
        return MongoDBAssignmentSet.createAssignmentSet(cargo);
    }
}
