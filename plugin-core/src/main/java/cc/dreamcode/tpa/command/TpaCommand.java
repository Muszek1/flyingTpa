package cc.dreamcode.tpa.command;

import cc.dreamcode.command.CommandBase;
import cc.dreamcode.command.annotation.Arg;
import cc.dreamcode.command.annotation.Command;
import cc.dreamcode.command.annotation.Executor;
import cc.dreamcode.command.annotation.Permission;
import cc.dreamcode.tpa.service.TpaService;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Command(name = "tpa")
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class TpaCommand implements CommandBase {

    private final TpaService tpaService;

    @Permission("dream-tpa.use")
    @Executor(description = "Wyślij prośbę o telepordtację do gracza.")
    void send(Player sender, @Arg Player target) {
        if (sender.equals(target)) return;
        this.tpaService.sendTpaRequest(sender, target, false);
    }
}
