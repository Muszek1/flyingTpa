package cc.dreamcode.tpa.command;

import cc.dreamcode.command.CommandBase;
import cc.dreamcode.command.annotation.*;
import cc.dreamcode.tpa.service.TpaService;
import eu.okaeri.injector.annotation.Inject;
import org.bukkit.entity.Player;

@Command(name = "tpahere", description = "Poproś gracza, by teleportował się do Ciebie")
public class TpaHereCommand implements CommandBase {

    @Inject private TpaService tpa;

    @Executor(description = "Wyślij prośbę TPA-HERE do gracza")
    void here(Player sender, @Arg Player target) {
        if (sender.equals(target)) return;
        tpa.sendTpaRequest(sender, target, true);
    }
}
