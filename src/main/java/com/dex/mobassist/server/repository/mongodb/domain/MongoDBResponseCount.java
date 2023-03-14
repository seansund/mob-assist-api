package com.dex.mobassist.server.repository.mongodb.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class MongoDBResponseCount {
    @Id
    private String selectedOptionId;
    private Integer count;
}
