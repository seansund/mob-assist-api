package com.dex.mobassist.server.repository;

import com.dex.mobassist.server.model.ModelRef;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
public abstract class BaseModelRef implements ModelRef {
    @Id
    private String id;

    public BaseModelRef() {
        this(null);
    }

    public BaseModelRef(String id) {
        this.id = id;
    }
}
