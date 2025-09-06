package cc.dreamcode.tpa.config;

import cc.dreamcode.platform.bukkit.component.configuration.Configuration;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.CustomKey;
import eu.okaeri.configs.annotation.Header;

@Configuration(child = "config.yml")
@Header("## Dream-Tpa (Main-Config) ##")
public class PluginConfig extends OkaeriConfig {

    @Comment
    @Comment("Debug pokazuje dodatkowe informacje do konsoli. Lepiej wylaczyc. :P")
    @CustomKey("debug")
    public boolean debug = true;

    @Comment("Czas (sek) po jakim prośba TPA wygasa")
    public int requestExpireSeconds = 60;

    @Comment("Opóźnienie (sek) przed teleportacją po akceptacji (anty-ruch, anty-combat)")
    public int teleportDelaySeconds = 3;

    @Comment("Cooldown (sek) pomiędzy wysyłaniem kolejnych próśb do tego samego gracza")
    public int requestCooldownSeconds = 10;

    @Comment("Anuluj TPA jeśli gracz ruszy się podczas odliczania")
    public boolean cancelOnMove = true;

    @Comment("Anuluj TPA jeśli gracz otrzyma obrażenia podczas odliczania")
    public boolean cancelOnDamage = true;
}
