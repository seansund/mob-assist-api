package com.dex.mobassist.server.repository.mongodb.domain;

import com.dex.mobassist.server.model.AssignmentRef;
import com.dex.mobassist.server.model.AssignmentSet;
import com.dex.mobassist.server.model.AssignmentSetRef;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Document("assignmentSet")
public class MongoDBAssignmentSet extends MongoDBAssignmentSetRef implements AssignmentSet {
    @NonNull
    private String name = "";
    @NonNull
    @DBRef
    private List<? extends MongoDBAssignment> assignments = List.of();

    public MongoDBAssignmentSet() {
        this(null);
    }

    public MongoDBAssignmentSet(String id) {
        super(id);
    }

    public static MongoDBAssignmentSet createAssignmentSet(AssignmentSetRef assignmentSet) {
        if (assignmentSet == null) {
            return null;
        }
        
        if (assignmentSet instanceof MongoDBAssignmentSet) {
            return (MongoDBAssignmentSet) assignmentSet;
        }

        final MongoDBAssignmentSet result = new MongoDBAssignmentSet(assignmentSet.getId());

        if (assignmentSet instanceof AssignmentSet) {
            result.updateWith((AssignmentSet)assignmentSet);
        }

        return result;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<? extends AssignmentRef> getAssignments() {
        return assignments;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setAssignments(List<? extends AssignmentRef> assignments) {
        this.assignments = assignments.stream().map(MongoDBAssignment::createAssignment).toList();
    }

    public MongoDBAssignmentSet updateWith(AssignmentSet assignmentSet) {
        if (assignmentSet == null) {
            return this;
        }

        return this
                .withName(assignmentSet.getName())
                .withAssignments(MongoDBAssignment.createAssignments(assignmentSet.getAssignments()));
    }
}
