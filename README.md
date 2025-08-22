# 🛫 flyingTpa

**flyingTpa** is a lightweight and configurable teleport request plugin for Spigot/Paper servers.  
It provides smooth player-to-player teleportation with delays, cooldowns, request expiry, and fully customizable messages.

---

## ✨ Features

- `/tpa` and `/tpahere` requests
- `/tpaccept`, `/tpdeny`, `/tpcancel` handling
- Configurable **teleport delay**, cancelled on **move** or **damage**
- **Cooldown** between requests
- **Auto-expire** of pending requests
- Fully **customizable messages** with placeholders
- Lightweight and optimized

---

## 🚀 Commands

| Command | Description |
|---------|-------------|
| `/tpa <player>` | Send a request to teleport to the target player |
| `/tpahere <player>` | Ask the target player to teleport to you |
| `/tpaccept [player]` | Accept a pending request (last or specified player) |
| `/tpdeny [player]` | Deny a pending request |
| `/tpcancel [player]` | Cancel your outgoing request(s) |
| `/tpareload` | Reload configuration and messages |

---

## 🔑 Permissions

| Permission | Description |
|------------|-------------|
| `flyingtpa.tpa` | Use `/tpa` |
| `flyingtpa.tpahere` | Use `/tpahere` |
| `flyingtpa.tpaccept` | Use `/tpaccept` |
| `flyingtpa.tpdeny` | Use `/tpdeny` |
| `flyingtpa.tpcancel` | Use `/tpcancel` |
| `flyingtpa.reload` | Use `/tpareload` |
| `flyingtpa.*` | Grants all permissions |

---

## ⚙️ Configuration

`config.yml` example:

```yaml
# Delay before teleport executes (seconds)
teleportDelaySeconds: 5

# Request expiration time (seconds)
requestExpireSeconds: 60

# Cooldown between sending requests to the same player (seconds)
requestCooldownSeconds: 15
```

`messages.yml` example:

```yaml
prefix: "&8[&bTPA&8] &r"

cannotDoAtMySelf: "{prefix}&cYou cannot send a request to yourself."
requestSent: "{prefix}&aRequest sent to &f{target}&a."
alreadyPending: "{prefix}&cYou already have a pending request to &f{player}&c."
onCooldown: "{prefix}&cWait &f{seconds}s &cbefore sending another request to &f{player}&c."
requestReceivedTo: "{prefix}&e{sender} wants to teleport to you. Use &a/tpaccept&e or &c/tpdeny&e."
requestReceivedHere: "{prefix}&e{sender} wants you to teleport to them. Use &a/tpaccept&e or &c/tpdeny&e."
accepted: "{prefix}&aTeleporting in &f{seconds}s&a. Don't move!"
denied: "{prefix}&cYou denied the request from &f{player}&c."
expired: "{prefix}&7Your request with &f{player}&7 has expired."
teleported: "{prefix}&aTeleport successful!"
movedCancel: "{prefix}&cTeleport cancelled because you moved."
damagedCancel: "{prefix}&cTeleport cancelled because you took damage."
```

---

## 🧱 Compatibility

- **Minecraft:** 1.8 → latest (Spigot/Paper)  
- **Java:** Compiles to Java 8 for maximum compatibility  
- For modern-only builds, set `api-version: "1.13+"` in `plugin.yml`.

---

## 📦 Installation

1. Download the latest release.
2. Place the JAR in your server’s `plugins/` folder.
3. Restart the server to generate configuration files.
4. Edit `config.yml` and `messages.yml` to your liking.
5. `/tpareload` to apply changes.


## 🤝 Contributing

Contributions are welcome!  
Please open an issue to discuss feature requests or submit a pull request.

---

## 📜 License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.
