package cc.dreamcode.tpa;

import cc.dreamcode.command.bukkit.BukkitCommandProvider;
import cc.dreamcode.menu.bukkit.BukkitMenuProvider;
import cc.dreamcode.menu.serializer.MenuBuilderSerializer;
import cc.dreamcode.notice.serializer.BukkitNoticeSerializer;
import cc.dreamcode.platform.DreamVersion;
import cc.dreamcode.platform.bukkit.DreamBukkitConfig;
import cc.dreamcode.platform.bukkit.DreamBukkitPlatform;
import cc.dreamcode.platform.bukkit.component.ConfigurationResolver;
import cc.dreamcode.platform.component.ComponentService;
import cc.dreamcode.platform.other.component.DreamCommandExtension;
import cc.dreamcode.tpa.command.*;
import cc.dreamcode.tpa.command.handler.InvalidInputHandlerImpl;
import cc.dreamcode.tpa.command.handler.InvalidPermissionHandlerImpl;
import cc.dreamcode.tpa.command.handler.InvalidSenderHandlerImpl;
import cc.dreamcode.tpa.command.handler.InvalidUsageHandlerImpl;
import cc.dreamcode.tpa.command.result.BukkitNoticeResolver;
import cc.dreamcode.tpa.profile.ProfileRepository;
import cc.dreamcode.tpa.service.TpaService;
import cc.dreamcode.utilities.adventure.AdventureProcessor;
import cc.dreamcode.utilities.adventure.AdventureUtil;
import cc.dreamcode.utilities.bukkit.StringColorUtil;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.tasker.bukkit.BukkitTasker;
import lombok.Getter;
import lombok.NonNull;

public final class Tpa extends DreamBukkitPlatform implements DreamBukkitConfig {

    @Getter private static Tpa instance;

    @Override
    public void load(@NonNull ComponentService componentService) {
        instance = this;

        AdventureUtil.setRgbSupport(true);
        StringColorUtil.setColorProcessor(new AdventureProcessor());
    }

    @Override
    public void enable(@NonNull ComponentService componentService) {

        componentService.setDebug(false);

        this.registerInjectable(BukkitTasker.newPool(this));
        this.registerInjectable(BukkitMenuProvider.create(this));
        this.registerInjectable(BukkitCommandProvider.create(this));

        componentService.registerExtension(DreamCommandExtension.class);
        componentService.registerResolver(ConfigurationResolver.class);

        componentService.registerComponent(cc.dreamcode.tpa.config.PluginConfig.class);
        componentService.registerComponent(cc.dreamcode.tpa.config.MessageConfig.class);


        componentService.registerComponent(BukkitNoticeResolver.class);
        componentService.registerComponent(InvalidInputHandlerImpl.class);
        componentService.registerComponent(InvalidPermissionHandlerImpl.class);
        componentService.registerComponent(InvalidSenderHandlerImpl.class);
        componentService.registerComponent(InvalidUsageHandlerImpl.class);
        componentService.registerComponent(TpaService.class);
        componentService.registerComponent(ProfileRepository.class);
        componentService.registerComponent(TpaCommand.class);
        componentService.registerComponent(TpAcceptCommand.class);
        componentService.registerComponent(TpaHereCommand.class);
        componentService.registerComponent(TpCancelCommand.class);
        componentService.registerComponent(TpDenyCommand.class);
        componentService.registerComponent(TpaReloadCommand.class);
        componentService.registerComponent(cc.dreamcode.tpa.listener.TpaListener.class);

    }

    @Override
    public void disable() {
        // features need to be call when server is stopping
    }

    @Override
    public @NonNull DreamVersion getDreamVersion() {
        return DreamVersion.create("Dream-Tpa", "1.0-InDEV", "Muszek_");
    }

    @Override
    public @NonNull OkaeriSerdesPack getConfigSerdesPack() {
        return registry -> {
            registry.register(new BukkitNoticeSerializer());
            registry.register(new MenuBuilderSerializer());
        };
    }

}
