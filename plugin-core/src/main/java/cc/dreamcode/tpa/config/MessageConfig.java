package cc.dreamcode.tpa.config;

import cc.dreamcode.notice.bukkit.BukkitNotice;
import cc.dreamcode.platform.bukkit.component.configuration.Configuration;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.CustomKey;
import eu.okaeri.configs.annotation.Header;
import eu.okaeri.configs.annotation.Headers;

@Configuration(child = "message.yml")
@Headers({
        @Header("## Dream-Tpa (Message-Config) ##"),
        @Header("Dostepne type: (DO_NOT_SEND, CHAT, ACTION_BAR, SUBTITLE, TITLE, TITLE_SUBTITLE)")
})
public class MessageConfig extends OkaeriConfig {

    @CustomKey("command-usage")
    public BukkitNotice usage = BukkitNotice.chat("&7Przyklady uzycia komendy: &c{label}");
    @CustomKey("command-usage-help")
    public BukkitNotice usagePath = BukkitNotice.chat("&f{usage} &8- &7{description}");

    @CustomKey("command-usage-not-found")
    public BukkitNotice usageNotFound = BukkitNotice.chat("&cNie znaleziono pasujacych do kryteriow komendy.");
    @CustomKey("command-path-not-found")
    public BukkitNotice pathNotFound = BukkitNotice.chat("&cTa komenda jest pusta lub nie posiadasz dostepu do niej.");
    @CustomKey("command-no-permission")
    public BukkitNotice noPermission = BukkitNotice.chat("&cNie posiadasz uprawnien.");
    @CustomKey("command-not-player")
    public BukkitNotice notPlayer = BukkitNotice.chat("&cTa komende mozna tylko wykonac z poziomu gracza.");
    @CustomKey("command-not-console")
    public BukkitNotice notConsole = BukkitNotice.chat("&cTa komende mozna tylko wykonac z poziomu konsoli.");
    @CustomKey("command-invalid-format")
    public BukkitNotice invalidFormat = BukkitNotice.chat("&cPodano nieprawidlowy format argumentu komendy. ({input})");

    @CustomKey("player-not-found")
    public BukkitNotice playerNotFound = BukkitNotice.chat("&cPodanego gracza nie znaleziono.");
    @CustomKey("world-not-found")
    public BukkitNotice worldNotFound = BukkitNotice.chat("&cPodanego swiata nie znaleziono.");
    @CustomKey("cannot-do-at-my-self")
    public BukkitNotice cannotDoAtMySelf = BukkitNotice.chat("&cNie mozesz tego zrobic na sobie.");
    @CustomKey("number-is-not-valid")
    public BukkitNotice numberIsNotValid = BukkitNotice.chat("&cPodana liczba nie jest cyfra.");

    @CustomKey("config-reloaded")
    public BukkitNotice reloaded = BukkitNotice.chat("&aPrzeladowano! &7({time})");
    @CustomKey("config-reload-error")
    public BukkitNotice reloadError = BukkitNotice.chat("&cZnaleziono problem w konfiguracji: &6{error}");

    @CustomKey("no-requests")
    public BukkitNotice noRequests = BukkitNotice.chat("&cNie masz zadnych oczekujacych prosb.");
    @CustomKey("no-outgoing")
    public BukkitNotice noOutgoing = BukkitNotice.chat("&cNie masz zadnych wyslanych prosb.");

    @CustomKey("request-sent")
    public BukkitNotice requestSent = BukkitNotice.chat("&7Wyslales prosbe TPA do &d{target}&7. &8(&f/tpaccept&8, &f/tpdeny&8)");
    @CustomKey("request-received-to")
    public BukkitNotice requestReceivedTo = BukkitNotice.chat("&d{sender} &7chce sie do Ciebie przeteleportowac. &8(&f/tpaccept&8, &f/tpdeny&8)");
    @CustomKey("request-received-here")
    public BukkitNotice requestReceivedHere = BukkitNotice.chat("&d{sender} &7prosi, abys teleportowal sie do niego. &8(&f/tpaccept&8, &f/tpdeny&8)");

    @CustomKey("already-pending")
    public BukkitNotice alreadyPending = BukkitNotice.chat("&cMasz juz oczekujaca prosbe z &d{player}&c.");
    @CustomKey("on-cooldown")
    public BukkitNotice onCooldown = BukkitNotice.chat("&7Poczekaj jeszcze &d{seconds}s&7, zanim wyślesz kolejna prosbe do &d{player}&7.");

    @CustomKey("expired")
    public BukkitNotice expired = BukkitNotice.chat("&cProsba TPA z &d{player} &cwygasla.");
    @CustomKey("cancelled")
    public BukkitNotice cancelled = BukkitNotice.chat("&cProsba TPA zostala anulowana.");
    @CustomKey("you-cancelled")
    public BukkitNotice youCancelled = BukkitNotice.chat("&7Anulowales prosbe TPA do &d{player}&7.");
    @CustomKey("they-cancelled")
    public BukkitNotice theyCancelled = BukkitNotice.chat("&d{player} &7anulowal prosbe TPA.");

    @CustomKey("accepted")
    public BukkitNotice accepted = BukkitNotice.chat("&7Za &d{seconds}s &7nastapi teleportacja...");
    @CustomKey("denied")
    public BukkitNotice denied = BukkitNotice.chat("&7Odrzuciles prosbe TPA od &d{player}&7.");
    @CustomKey("was-denied")
    public BukkitNotice wasDenied = BukkitNotice.chat("&d{player} &7odrzucil Twoja prosbe TPA.");

    @CustomKey("moved-cancel")
    public BukkitNotice movedCancel = BukkitNotice.chat("&cPoruszyles sie — teleportacja anulowana.");
    @CustomKey("damaged-cancel")
    public BukkitNotice damagedCancel = BukkitNotice.chat("&cOtrzymales obrazenia — teleportacja anulowana.");

    @CustomKey("teleported")
    public BukkitNotice teleported = BukkitNotice.chat("&aTeleportacja zakonczona.");

}
