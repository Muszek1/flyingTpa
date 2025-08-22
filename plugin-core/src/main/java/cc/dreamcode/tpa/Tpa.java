package cc.dreamcode.tpa;

import cc.dreamcode.command.bukkit.BukkitCommandProvider;
import cc.dreamcode.menu.bukkit.BukkitMenuProvider;
import cc.dreamcode.menu.serializer.MenuBuilderSerializer;
import cc.dreamcode.notice.serializer.BukkitNoticeSerializer;
import cc.dreamcode.platform.DreamVersion;
import cc.dreamcode.platform.bukkit.DreamBukkitConfig;
import cc.dreamcode.platform.bukkit.DreamBukkitPlatform;
import cc.dreamcode.platform.bukkit.component.ConfigurationResolver;
import cc.dreamcode.platform.bukkit.serializer.ItemMetaSerializer;
import cc.dreamcode.platform.bukkit.serializer.ItemStackSerializer;
import cc.dreamcode.platform.component.ComponentService;
import cc.dreamcode.platform.other.component.DreamCommandExtension;
import cc.dreamcode.platform.persistence.DreamPersistence;
import cc.dreamcode.platform.persistence.component.DocumentPersistenceResolver;
import cc.dreamcode.platform.persistence.component.DocumentRepositoryResolver;
import cc.dreamcode.tpa.command.*;
import cc.dreamcode.tpa.command.handler.InvalidInputHandlerImpl;
import cc.dreamcode.tpa.command.handler.InvalidPermissionHandlerImpl;
import cc.dreamcode.tpa.command.handler.InvalidSenderHandlerImpl;
import cc.dreamcode.tpa.command.handler.InvalidUsageHandlerImpl;
import cc.dreamcode.tpa.command.result.BukkitNoticeResolver;
import cc.dreamcode.tpa.config.MessageConfig;
import cc.dreamcode.tpa.config.PluginConfig;
import cc.dreamcode.tpa.profile.ProfileRepository;
import cc.dreamcode.tpa.service.TpaService;
import cc.dreamcode.utilities.adventure.AdventureProcessor;
import cc.dreamcode.utilities.adventure.AdventureUtil;
import cc.dreamcode.utilities.bukkit.StringColorUtil;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import eu.okaeri.persistence.document.DocumentPersistence;
import eu.okaeri.tasker.bukkit.BukkitTasker;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class Tpa extends DreamBukkitPlatform implements DreamBukkitConfig, DreamPersistence {

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
        componentService.registerComponent(MessageConfig.class);


        componentService.registerComponent(BukkitNoticeResolver.class);
        componentService.registerComponent(InvalidInputHandlerImpl.class);
        componentService.registerComponent(InvalidPermissionHandlerImpl.class);
        componentService.registerComponent(InvalidSenderHandlerImpl.class);
        componentService.registerComponent(InvalidUsageHandlerImpl.class);

        componentService.registerComponent(PluginConfig.class, pluginConfig -> {
            // register persistence + repositories
            this.registerInjectable(pluginConfig.storageConfig);

            componentService.registerResolver(DocumentPersistenceResolver.class);
            componentService.registerComponent(DocumentPersistence.class);
            componentService.registerResolver(DocumentRepositoryResolver.class);

            // enable additional logs and debug messages
            componentService.setDebug(pluginConfig.debug);
        });

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

    @Override
    public @NonNull OkaeriSerdesPack getPersistenceSerdesPack() {
        return registry -> {
            registry.register(new SerdesBukkit());

            registry.registerExclusive(ItemStack.class, new ItemStackSerializer());
            registry.registerExclusive(ItemMeta.class, new ItemMetaSerializer());
        };
    }

}
