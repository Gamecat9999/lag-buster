# LagBuster

LagBuster is a lightweight Spigot/Bukkit plugin that helps keep your Minecraft server running smoothly by automatically clearing laggy entity buildup and giving server operators quick performance tools.

## 🚀 Features

- **Automatic dropped item cleanup** when a configurable threshold is reached
- **Manual lag control** via `/lagclear` to remove dropped items and/or passive mobs
- **Quick TPS reporting** via `/tps` (works on most Spigot builds)
- Fully configurable behavior via `config.yml`

## 🧩 Installation

1. Build the plugin:
   ```bash
   mvn package
   ```
2. Copy the generated JAR from:
   ```
   target/novaannouncer-1.0.0.jar
   ```
3. Paste it into your server's `plugins/` folder.
4. Start or restart your server.

## ⚙️ Configuration

After the first run, LagBuster creates a config file at:

```
plugins/LagBuster/config.yml
```

Default values:

```yaml
# LagBuster configuration
# All times are in server ticks (20 ticks = 1 second).

# How often the plugin will auto-clear dropped items (in ticks).
# Set to 0 to disable auto-clearing.
clearIntervalTicks: 6000

# Maximum number of dropped item entities allowed before clear is triggered.
# If the number of dropped items is above this threshold, they will be cleared.
maxDroppedItems: 120

# Whether to also clear passive mobs (e.g., animals) when /lagclear all is used.
# This does not run automatically.
clearMobsWithAll: true

# Log extra debug info to the server log.
debug: false
```

### Key settings

- `clearIntervalTicks` — frequency of auto-checks (20 ticks = 1 second)
- `maxDroppedItems` — threshold that triggers immediate cleanup
- `clearMobsWithAll` — controls whether `/lagclear all` also removes passive mobs
- `debug` — additional logging output

## 🗡 Commands

| Command | Description |
|--------|-------------|
| `/lagclear` | Clears dropped items (default) |
| `/lagclear drops` | Clears dropped items |
| `/lagclear mobs` | Clears passive mobs (animals, villagers, etc.) |
| `/lagclear all` | Clears drops and (optionally) passive mobs |
| `/tps` | Shows recent server TPS (1m/5m/15m) |

## 🔒 Permissions

- `lagbuster.clear` — required to use `/lagclear`
- `lagbuster.tps` — required to use `/tps`

## 🧠 Notes

- TPS reporting relies on a server implementation exposing it via reflection; some forks may not support TPS reporting.
- Clearing mobs can remove passive animals/villagers, so use `/lagclear all` carefully on live servers.

## 📜 License

This project is provided as-is, no warranty expressed or implied.
