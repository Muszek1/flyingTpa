# 🪂 flyingDrop

**flyingDrop** is a Minecraft plugin that adds new mechanics for item drops and events on your server.  
It is designed to be lightweight, easy to configure, and fun for players.  

---

## ✨ Features
- Custom drop system (configurable in YAML).
- Event-driven mechanics.
- Lightweight and optimized for survival servers.
- Easy to use commands and permissions.

---

## 📦 Installation
1. Download the latest release from [Releases](https://github.com/Muszek1/flyingDrop/releases).
2. Place the `.jar` file into your server’s `plugins` folder.
3. Restart or reload your server.
4. Configure the plugin in `plugins/flyingDrop/config.yml`.

---

## 🛠 Commands
| Command | Description |
|---------|-------------|
| `/flyingdrop reload` | Reloads the plugin configuration. |
| *(add here other commands when available)* |

---

## 🔑 Permissions
| Permission | Description |
|------------|-------------|
| `flyingdrop.reload` | Allows reloading the configuration. |
| *(add here other permissions when available)* |

---

## ⚙️ Configuration
The plugin uses a YAML configuration file (`config.yml`).  
Example:
```yaml
# Example configuration
drops:
  enabled: true
  chance: 0.25
  items:
    - DIAMOND
    - GOLD_INGOT
