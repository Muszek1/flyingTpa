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

@Command(name = "tpaccept")
@Permission("dream-tpa.use")
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class TpAcceptCommand implements CommandBase {

    private final TpaService tpaService;

    @Executor(description = "Akceptuj jedną oczekującą prośbę (jeśli jest tylko jedna).")
    void accept(Player target) {
        this.tpaService.acceptRequest(target, null);
    }

    @Executor(path = "", description = "Akceptuj prośbę od wskazanego gracza.")
    void acceptFrom(Player target, @Arg Player from) {
        this.tpaService.acceptRequest(target, from);
    }
}
