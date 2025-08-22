package cc.dreamcode.tpa.command;

import cc.dreamcode.command.CommandBase;
import cc.dreamcode.command.annotation.Async;
import cc.dreamcode.command.annotation.Command;
import cc.dreamcode.command.annotation.Executor;
import cc.dreamcode.command.annotation.Permission;
import cc.dreamcode.notice.bukkit.BukkitNotice;
import cc.dreamcode.tpa.config.MessageConfig;
import cc.dreamcode.tpa.config.PluginConfig;
import cc.dreamcode.utilities.TimeUtil;
import eu.okaeri.configs.exception.OkaeriException;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;

@Command(name = "tpareload")
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class TpaReloadCommand implements CommandBase {

    private final PluginConfig pluginConfig;
    private final MessageConfig messageConfig;

    @Async
    @Permission("dream-tpa.reload")
    @Executor(path = "reload", description = "Przeładowuje konfiguracje TPA.")
    BukkitNotice reload() {
        final long start = System.currentTimeMillis();
        try {
            this.messageConfig.load();
            this.pluginConfig.load();


            return this.messageConfig.reloaded
                    .with("time", TimeUtil.format(System.currentTimeMillis() - start));
        }
        catch (NullPointerException | OkaeriException e) {
            e.printStackTrace();
            return this.messageConfig.reloadError
                    .with("error", e.getMessage());
        }
    }
}
