package cc.dreamcode.tpa.command;

import cc.dreamcode.command.CommandBase;
import cc.dreamcode.command.annotation.*;
import cc.dreamcode.tpa.service.TpaService;
import eu.okaeri.injector.annotation.Inject;
import org.bukkit.entity.Player;

@Command(name = "tpdeny", description = "Odrzuć prośbę TPA")
public class TpDenyCommand implements CommandBase {

    @Inject private TpaService tpa;

    @Executor(description = "Odrzuć jedyną oczekującą prośbę")
    void deny(Player target) {
        tpa.denyRequest(target, null);
    }

    @Executor(path = "", description = "Odrzuć prośbę od wskazanego gracza")
    void denyFrom(Player target, @Arg Player from) {
        tpa.denyRequest(target, from);
    }
}
