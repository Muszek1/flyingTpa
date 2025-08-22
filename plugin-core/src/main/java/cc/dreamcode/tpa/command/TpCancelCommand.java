package cc.dreamcode.tpa.command;

import cc.dreamcode.command.CommandBase;
import cc.dreamcode.command.annotation.*;
import cc.dreamcode.tpa.service.TpaService;
import eu.okaeri.injector.annotation.Inject;
import org.bukkit.entity.Player;

@Command(name = "tpcancel", description = "Anuluj swoją wysłaną prośbę")
public class TpCancelCommand implements CommandBase {

    @Inject private TpaService tpa;

    @Executor(description = "Anuluj jedyną wysłaną prośbę")
    void cancel(Player sender) {
        tpa.cancelRequest(sender, null);
    }

    @Executor(path = "", description = "Anuluj prośbę do wskazanego gracza")
    void cancelTo(Player sender, @Arg Player target) {
        tpa.cancelRequest(sender, target);
    }
}
