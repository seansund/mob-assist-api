package com.dex.mobassist.server.repository.mongodb.domain;

import com.dex.mobassist.server.model.Assignment;
import com.dex.mobassist.server.model.AssignmentRef;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Objects;

@Data
@EqualsAndHashCode(callSuper = true)
@Document("assignment")
public class MongoDBAssignment extends MongoDBAssignmentRef implements Assignment {
    @NonNull
    private String group = "";
    @NonNull
    @Indexed(unique = true)
    private String name = "";
    @NonNull
    private Integer row = 0;
    private String partnerId = "";

    public MongoDBAssignment() {
        this(null);
    }

    public MongoDBAssignment(String id) {
        super(id);
    }

    public static MongoDBAssignment createAssignment(AssignmentRef assignmentRef) {
        if (assignmentRef == null) {
            return null;
        }

        if (assignmentRef instanceof MongoDBAssignment) {
            return (MongoDBAssignment) assignmentRef;
        }

        final MongoDBAssignment result = new MongoDBAssignment(assignmentRef.getId());

        if (assignmentRef instanceof final Assignment assignment) {
            result.updateWith(assignment);
        }

        return result;
    }

    public static List<? extends MongoDBAssignment> createAssignments(List<? extends AssignmentRef> refs) {
        if (refs == null) {
            return null;
        }

        return refs.stream().map(MongoDBAssignment::createAssignment).filter(Objects::nonNull).toList();
    }

    public MongoDBAssignment updateWith(Assignment assignment) {
        if (assignment == null) {
            return this;
        }

        return this
                .withGroup(assignment.getGroup())
                .withName(assignment.getName())
                .withRow(assignment.getRow())
                .withPartnerId(assignment.getPartnerId());
    }
}
