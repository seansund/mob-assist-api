package com.dex.mobassist.server.repository.mongodb.domain;

import lombok.Data;

@Data
public class MongoDBAssignmentCount {
    private String selectedOptionId;
    private Integer count;
}
