package cc.dreamcode.tpa.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class TpaRequest {
    private UUID sender;
    private UUID target;
    private boolean here;
    private long createdAt;
}
