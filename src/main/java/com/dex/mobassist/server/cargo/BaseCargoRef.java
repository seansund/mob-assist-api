package com.dex.mobassist.server.cargo;

import com.dex.mobassist.server.model.ModelRef;
import lombok.Data;

@Data
public class BaseCargoRef implements ModelRef {
    private String id;

    public BaseCargoRef() {
        this(null);
    }

    public BaseCargoRef(String id) {
        this.id = id;
    }
}
