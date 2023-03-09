package com.dex.mobassist.server.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public abstract class BaseModelRef implements ModelRef {
    @NonNull
    private String id;
}
