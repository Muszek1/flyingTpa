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
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class TpaService implements Listener {

    private final PluginConfig pluginConfig;
    private final MessageConfig messageConfig;

    private final Map<UUID, List<TpaRequest>> pendingRequests = new ConcurrentHashMap<>();
    private final Map<String, Long> cooldowns = new ConcurrentHashMap<>();

    private final Set<UUID> pendingTeleports = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final Map<UUID, UUID> teleportPartners = new ConcurrentHashMap<>();


    public void sendTpaRequest(Player sender, Player target, boolean here) {
        if (sender.equals(target)) {
            this.messageConfig.cannotDoAtMySelf.send(sender);
            return;
        }

        List<TpaRequest> toTarget = this.pendingRequests.getOrDefault(target.getUniqueId(), new ArrayList<>());
        boolean hasDuplicate = toTarget.stream().anyMatch(r -> r.getSender().equals(sender.getUniqueId()));
        if (hasDuplicate) {
            this.messageConfig.alreadyPending.send(sender, new MapBuilder<String, Object>()
                    .put("player", target.getName())
                    .build());
            return;
        }

        String key = sender.getUniqueId() + "-" + target.getUniqueId();
        long now = System.currentTimeMillis();
        long last = this.cooldowns.getOrDefault(key, 0L);
        long cdMillis = pluginConfig.requestCooldownSeconds * 1000L;
        if (now - last < cdMillis) {
            long sec = (cdMillis - (now - last)) / 1000L;
            this.messageConfig.onCooldown.send(sender, new MapBuilder<String, Object>()
                    .put("player", target.getName())
                    .put("seconds", sec)
                    .build());
            return;
        }
        this.cooldowns.put(key, now);

        TpaRequest request = new TpaRequest(sender.getUniqueId(), target.getUniqueId(), here, now);
        this.pendingRequests.computeIfAbsent(target.getUniqueId(), u -> new ArrayList<>()).add(request);

        this.messageConfig.requestSent.send(sender, new MapBuilder<String, Object>()
                .put("target", target.getName()).build());

        (here ? messageConfig.requestReceivedHere : messageConfig.requestReceivedTo)
                .send(target, new MapBuilder<String, Object>()
                        .put("sender", sender.getName()).build());

        new BukkitRunnable() {
            @Override
            public void run() {
                List<TpaRequest> list = pendingRequests.getOrDefault(target.getUniqueId(), new ArrayList<>());
                if (list.remove(request)) {
                    messageConfig.expired.send(sender, new MapBuilder<String, Object>()
                            .put("player", target.getName()).build());
                    messageConfig.expired.send(target, new MapBuilder<String, Object>()
                            .put("player", sender.getName()).build());
                }
            }
        }.runTaskLater(
                Bukkit.getPluginManager().getPlugin("Dream-Tpa"),
                pluginConfig.requestExpireSeconds * 20L
        );
    }

    public void acceptRequest(Player target, Player fromOrNull) {
        List<TpaRequest> list = this.pendingRequests.getOrDefault(target.getUniqueId(), new ArrayList<>());
        if (list.isEmpty()) {
            this.messageConfig.noRequests.send(target);
            return;
        }

        TpaRequest request;
        if (fromOrNull == null) {
            if (list.size() > 1) {
                this.messageConfig.usage.send(target, new MapBuilder<String, Object>()
                        .put("label", "/tpaccept <gracz>").build());
                return;
            }
            request = list.get(0);
        } else {
            request = list.stream()
                    .filter(r -> r.getSender().equals(fromOrNull.getUniqueId()))
                    .findFirst().orElse(null);
        }

        if (request == null) {
            this.messageConfig.noRequests.send(target);
            return;
        }

        Player sender = Bukkit.getPlayer(request.getSender());
        if (sender == null) {
            list.remove(request);
            this.messageConfig.playerNotFound.send(target);
            return;
        }

        list.remove(request);
        if (list.isEmpty()) this.pendingRequests.remove(target.getUniqueId());

        int delay = this.pluginConfig.teleportDelaySeconds;
        this.messageConfig.accepted.send(target, new MapBuilder<String, Object>()
                .put("seconds", delay).build());
        this.messageConfig.accepted.send(sender, new MapBuilder<String, Object>()
                .put("seconds", delay).build());

        final Player teleporter = request.isHere() ? target : sender;
        final Player destination = request.isHere() ? sender : target;

        if (delay <= 0) {
            performTeleport(teleporter, destination);
            this.messageConfig.teleported.send(target);
            this.messageConfig.teleported.send(sender);
            return;
        }

        this.pendingTeleports.add(teleporter.getUniqueId());
        this.teleportPartners.put(teleporter.getUniqueId(), destination.getUniqueId());

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!pendingTeleports.remove(teleporter.getUniqueId())) return;
                teleportPartners.remove(teleporter.getUniqueId());

                performTeleport(teleporter, destination);
                messageConfig.teleported.send(target);
                messageConfig.teleported.send(sender);
            }
        }.runTaskLater(
                Bukkit.getPluginManager().getPlugin("Dream-Tpa"),
                delay * 20L
        );
    }

    public void denyRequest(Player target, Player fromOrNull) {
        List<TpaRequest> list = this.pendingRequests.getOrDefault(target.getUniqueId(), new ArrayList<>());
        if (list.isEmpty()) {
            this.messageConfig.noRequests.send(target);
            return;
        }
        TpaRequest request;
        if (fromOrNull == null) {
            if (list.size() > 1) {
                this.messageConfig.usage.send(target, new MapBuilder<String, Object>()
                        .put("label", "/tpdeny <gracz>").build());
                return;
            }
            request = list.get(0);
        } else {
            request = list.stream()
                    .filter(r -> r.getSender().equals(fromOrNull.getUniqueId()))
                    .findFirst().orElse(null);
        }
        if (request == null) {
            this.messageConfig.noRequests.send(target);
            return;
        }

        list.remove(request);
        if (list.isEmpty()) this.pendingRequests.remove(target.getUniqueId());

        Player sender = Bukkit.getPlayer(request.getSender());
        if (sender != null) {
            this.messageConfig.wasDenied.send(sender, new MapBuilder<String, Object>()
                    .put("player", target.getName()).build());
        }
        this.messageConfig.denied.send(target, new MapBuilder<String, Object>()
                .put("player", sender != null ? sender.getName() : "???").build());
    }

    public void cancelRequest(Player sender, Player targetOrNull) {
        UUID senderId = sender.getUniqueId();

        if (targetOrNull == null) {
            boolean anyOutgoing = this.pendingRequests.values().stream()
                    .flatMap(List::stream)
                    .anyMatch(r -> r.getSender().equals(senderId));
            if (!anyOutgoing) {
                this.messageConfig.noOutgoing.send(sender);
                return;
            }

            this.pendingRequests.values().forEach(list -> list.removeIf(r -> r.getSender().equals(senderId)));
            this.messageConfig.youCancelled.send(sender, new MapBuilder<String, Object>()
                    .put("player", "*").build());
            return;
        }

        List<TpaRequest> list = this.pendingRequests.getOrDefault(targetOrNull.getUniqueId(), new ArrayList<>());
        boolean removedAny = list.removeIf(r -> r.getSender().equals(senderId));
        if (list.isEmpty()) this.pendingRequests.remove(targetOrNull.getUniqueId());

        if (!removedAny) {
            this.messageConfig.noOutgoing.send(sender);
            return;
        }

        this.messageConfig.youCancelled.send(sender, new MapBuilder<String, Object>()
                .put("player", targetOrNull.getName()).build());
        this.messageConfig.theyCancelled.send(targetOrNull, new MapBuilder<String, Object>()
                .put("player", sender.getName()).build());
    }

    public boolean isTeleportPending(Player player) {
        return this.pendingTeleports.contains(player.getUniqueId());
    }

    public void cancelPendingTeleport(Player teleporter, boolean byMove) {
        UUID id = teleporter.getUniqueId();
        if (this.pendingTeleports.remove(id)) {
            UUID partnerId = this.teleportPartners.remove(id);

            if (byMove) this.messageConfig.movedCancel.send(teleporter);
            else this.messageConfig.damagedCancel.send(teleporter);

            if (partnerId != null) {
                Player partner = Bukkit.getPlayer(partnerId);
                if (partner != null) this.messageConfig.cancelled.send(partner);
            }
        }
    }


    private void performTeleport(Player teleporter, Player destination) {
        if (teleporter == null || destination == null) return;
        if (!teleporter.isOnline() || !destination.isOnline()) return;
        teleporter.teleport(destination.getLocation());
    }
}
