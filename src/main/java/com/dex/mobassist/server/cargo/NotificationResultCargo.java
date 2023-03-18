package com.dex.mobassist.server.cargo;

import com.dex.mobassist.server.model.NotificationResult;
import lombok.Data;

@Data
public class NotificationResultCargo implements NotificationResult {
    private String type;
    private Integer count;
}
