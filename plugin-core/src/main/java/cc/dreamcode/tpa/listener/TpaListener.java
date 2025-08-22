package cc.dreamcode.tpa.listener;

import cc.dreamcode.tpa.config.PluginConfig;
import cc.dreamcode.tpa.service.TpaService;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class TpaListener implements Listener {

    private final PluginConfig pluginConfig;
    private final TpaService tpaService;

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (!pluginConfig.cancelOnMove) return;

        Player p = e.getPlayer();
        if (!tpaService.isTeleportPending(p)) return;

        if (e.getFrom().getBlockX() != e.getTo().getBlockX()
                || e.getFrom().getBlockY() != e.getTo().getBlockY()
                || e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {
            tpaService.cancelPendingTeleport(p, true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!pluginConfig.cancelOnDamage) return;
        if (!(e.getEntity() instanceof Player)) return;

        Player p = (Player) e.getEntity();
        if (!tpaService.isTeleportPending(p)) return;

        if (e.getFinalDamage() > 0) {
            tpaService.cancelPendingTeleport(p, false);
        }
    }
}
