package com.dex.mobassist.server.repository.mongodb;

import com.dex.mobassist.server.model.Assignment;
import com.dex.mobassist.server.repository.mongodb.domain.MongoDBAssignment;
import com.dex.mobassist.server.repository.AssignmentRepository;
import lombok.NonNull;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository("AssignmentRepository")
@Profile("mongodb")
public class AssignmentRepositoryMongoDB extends AbstractRepositoryMongoDB<Assignment, MongoDBAssignment> implements AssignmentRepository {

    public AssignmentRepositoryMongoDB(MongoTemplate mongoTemplate) {
        super(mongoTemplate, MongoDBAssignment.class);
    }

    @Override
    public List<? extends Assignment> findAllById(List<String> assignmentIds) {
        return mongoTemplate().find(
                query(where("id").in(assignmentIds)),
                MongoDBAssignment.class
        );
    }

    @Override
    protected <A extends Assignment> String getId(@NonNull A cargo) {
        return cargo.getId();
    }

    @Override
    protected MongoDBAssignment updateDomainWithCargo(@NonNull MongoDBAssignment domain, @NonNull Assignment cargo) {
        return domain.updateWith(cargo);
    }

    @Override
    protected Sort defaultSort() {
        return Sort.by(Sort.Direction.ASC, "group", "row", "name");
    }

    @Override
    protected MongoDBAssignment createDomainObject(@NonNull Assignment cargo) {
        return MongoDBAssignment.createAssignment(cargo);
    }
}
