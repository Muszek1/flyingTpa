package cc.dreamcode.tpa.profile;

import lombok.NonNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ProfileRepository {

    private final Map<UUID, Profile> store = new ConcurrentHashMap<>();

    public Profile findOrCreate(@NonNull UUID uuid, String profileName) {
        Profile profile = store.computeIfAbsent(uuid, Profile::new);
        if (profileName != null) profile.setName(profileName);
        return profile;
    }

    public Profile get(UUID uuid) { return store.get(uuid); }
    public void delete(UUID uuid) { if (uuid != null) store.remove(uuid); }
}
