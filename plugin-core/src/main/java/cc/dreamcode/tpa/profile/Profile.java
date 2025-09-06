package cc.dreamcode.tpa.profile;

import lombok.Data;

import java.util.UUID;

@Data
public class Profile {
    private final UUID id;
    private String name;

    public void setName(String profileName) {
        if (this.name == null) {
            this.name = profileName;
        }
    }
}
