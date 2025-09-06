package cc.dreamcode.tpa.service;

import cc.dreamcode.tpa.config.MessageConfig;
import cc.dreamcode.tpa.config.PluginConfig;
import cc.dreamcode.tpa.model.TpaRequest;
import cc.dreamcode.utilities.builder.MapBuilder;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class TpaService implements Listener {

    private static final long TICKS = 20L;

    private final PluginConfig pluginConfig;
    private final MessageConfig messageConfig;

    private final Map<UUID, List<TpaRequest>> pendingRequests = new ConcurrentHashMap<>();
    private final Map<String, Long> cooldowns = new ConcurrentHashMap<>();
    private final Set<UUID> pendingTeleports = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final Map<UUID, UUID> teleportPartners = new ConcurrentHashMap<>();

    public void sendTpaRequest(Player sender, Player target, boolean here) {
        if (sender.equals(target)) {
            messageConfig.cannotDoAtMySelf.send(sender);
            return;
        }

        List<TpaRequest> toTarget = listFor(target.getUniqueId());
        if (toTarget.stream().anyMatch(r -> r.getSender().equals(sender.getUniqueId()))) {
            messageConfig.alreadyPending.send(sender, map("player", target.getName()));
            return;
        }

        String key = sender.getUniqueId() + "-" + target.getUniqueId();
        long now = System.currentTimeMillis();
        long last = cooldowns.getOrDefault(key, 0L);
        long cdMillis = pluginConfig.requestCooldownSeconds * 1000L;

        if (now - last < cdMillis) {
            long sec = (cdMillis - (now - last)) / 1000L;
            messageConfig.onCooldown.send(sender, map("player", target.getName(), "seconds", sec));
            return;
        }
        cooldowns.put(key, now);

        TpaRequest request = new TpaRequest(sender.getUniqueId(), target.getUniqueId(), here, now);
        toTarget.add(request);

        messageConfig.requestSent.send(sender, map("target", target.getName()));
        (here ? messageConfig.requestReceivedHere : messageConfig.requestReceivedTo)
                .send(target, map("sender", sender.getName()));

        runLater(pluginConfig.requestExpireSeconds * TICKS, () -> expireRequest(target, sender, request));
    }

    public void acceptRequest(Player target, Player fromOrNull) {
        List<TpaRequest> list = listFor(target.getUniqueId());
        if (list.isEmpty()) {
            messageConfig.noRequests.send(target);
            return;
        }

        TpaRequest request = findRequest(list, fromOrNull);
        if (request == null) {
            if (fromOrNull == null && list.size() > 1) {
                messageConfig.usage.send(target, map("label", "/tpaccept <gracz>"));
            } else {
                messageConfig.noRequests.send(target);
            }
            return;
        }

        Player sender = Bukkit.getPlayer(request.getSender());
        if (sender == null) {
            removeRequest(target.getUniqueId(), request);
            messageConfig.playerNotFound.send(target);
            return;
        }

        removeRequest(target.getUniqueId(), request);

        int delay = pluginConfig.teleportDelaySeconds;
        messageConfig.accepted.send(target, map("seconds", delay));
        messageConfig.accepted.send(sender, map("seconds", delay));

        final Player teleporter = request.isHere() ? target : sender;
        final Player destination = request.isHere() ? sender : target;

        if (delay <= 0) {
            performTeleport(teleporter, destination);
            messageConfig.teleported.send(target);
            messageConfig.teleported.send(sender);
            return;
        }

        pendingTeleports.add(teleporter.getUniqueId());
        teleportPartners.put(teleporter.getUniqueId(), destination.getUniqueId());

        runLater(delay * TICKS, () -> {
            if (!pendingTeleports.remove(teleporter.getUniqueId())) return;
            teleportPartners.remove(teleporter.getUniqueId());
            performTeleport(teleporter, destination);
            messageConfig.teleported.send(target);
            messageConfig.teleported.send(sender);
        });
    }

    public void denyRequest(Player target, Player fromOrNull) {
        List<TpaRequest> list = listFor(target.getUniqueId());
        if (list.isEmpty()) {
            messageConfig.noRequests.send(target);
            return;
        }

        TpaRequest request = findRequest(list, fromOrNull);
        if (request == null) {
            if (fromOrNull == null && list.size() > 1) {
                messageConfig.usage.send(target, map("label", "/tpdeny <gracz>"));
            } else {
                messageConfig.noRequests.send(target);
            }
            return;
        }

        removeRequest(target.getUniqueId(), request);

        Player sender = Bukkit.getPlayer(request.getSender());
        if (sender != null) {
            messageConfig.wasDenied.send(sender, map("player", target.getName()));
        }
        messageConfig.denied.send(target, map("player", sender != null ? sender.getName() : "???"));
    }

    public void cancelRequest(Player sender, Player targetOrNull) {
        UUID senderId = sender.getUniqueId();

        if (targetOrNull == null) {
            boolean any = pendingRequests.values().stream().flatMap(List::stream)
                    .anyMatch(r -> r.getSender().equals(senderId));
            if (!any) {
                messageConfig.noOutgoing.send(sender);
                return;
            }
            pendingRequests.values().forEach(list -> list.removeIf(r -> r.getSender().equals(senderId)));
            messageConfig.youCancelled.send(sender, map("player", "*"));
            return;
        }

        List<TpaRequest> list = listFor(targetOrNull.getUniqueId());
        boolean removed = list.removeIf(r -> r.getSender().equals(senderId));
        if (list.isEmpty()) pendingRequests.remove(targetOrNull.getUniqueId());

        if (!removed) {
            messageConfig.noOutgoing.send(sender);
            return;
        }

        messageConfig.youCancelled.send(sender, map("player", targetOrNull.getName()));
        messageConfig.theyCancelled.send(targetOrNull, map("player", sender.getName()));
    }

    public boolean isTeleportPending(Player player) {
        return pendingTeleports.contains(player.getUniqueId());
    }

    public void cancelPendingTeleport(Player teleporter, boolean byMove) {
        UUID id = teleporter.getUniqueId();
        if (!pendingTeleports.remove(id)) return;

        UUID partnerId = teleportPartners.remove(id);
        if (byMove) messageConfig.movedCancel.send(teleporter);
        else messageConfig.damagedCancel.send(teleporter);

        if (partnerId != null) {
            Player partner = Bukkit.getPlayer(partnerId);
            if (partner != null) messageConfig.cancelled.send(partner);
        }
    }

    private void expireRequest(Player target, Player sender, TpaRequest request) {
        List<TpaRequest> list = listFor(target.getUniqueId());
        if (list.remove(request)) {
            if (list.isEmpty()) pendingRequests.remove(target.getUniqueId());
            messageConfig.expired.send(sender, map("player", target.getName()));
            messageConfig.expired.send(target, map("player", sender.getName()));
        }
    }

    private void performTeleport(Player teleporter, Player destination) {
        if (teleporter == null || destination == null) return;
        if (!teleporter.isOnline() || !destination.isOnline()) return;
        teleporter.teleport(destination.getLocation());
    }

    private List<TpaRequest> listFor(UUID targetId) {
        return pendingRequests.computeIfAbsent(targetId, u -> new ArrayList<>());
    }

    private void removeRequest(UUID targetId, TpaRequest req) {
        List<TpaRequest> list = listFor(targetId);
        list.remove(req);
        if (list.isEmpty()) pendingRequests.remove(targetId);
    }

    private TpaRequest findRequest(List<TpaRequest> list, Player fromOrNull) {
        if (fromOrNull == null) return list.size() == 1 ? list.get(0) : null;
        UUID fromId = fromOrNull.getUniqueId();
        for (TpaRequest r : list) if (r.getSender().equals(fromId)) return r;
        return null;
    }

    private Map<String, Object> map(Object... kv) {
        MapBuilder<String, Object> b = new MapBuilder<>();
        for (int i = 0; i + 1 < kv.length; i += 2) b.put(String.valueOf(kv[i]), kv[i + 1]);
        return b.build();
    }

    private void runLater(long ticks, Runnable task) {
        new BukkitRunnable() { @Override public void run() { task.run(); } }
                .runTaskLater(plugin(), ticks);
    }

    private Plugin plugin() {
        return Bukkit.getPluginManager().getPlugin("Dream-Tpa");
    }
}
