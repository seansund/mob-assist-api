package com.dex.mobassist.server.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SimpleResult {
    @NonNull
    private boolean result;
}
