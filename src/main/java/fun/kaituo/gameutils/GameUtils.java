package fun.kaituo.gameutils;

import fun.kaituo.gameutils.command.ChangeBiome;
import fun.kaituo.gameutils.command.ForceStop;
import fun.kaituo.gameutils.command.JoinCommand;
import fun.kaituo.gameutils.command.PlaceStandCommand;
import fun.kaituo.gameutils.command.TpGameCommand;
import fun.kaituo.gameutils.game.Game;
import fun.kaituo.gameutils.listener.PlayerLogInLogOutListener;
import fun.kaituo.gameutils.listener.ProtectionListener;
import fun.kaituo.gameutils.util.Misc;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


public class GameUtils extends JavaPlugin {
    private final Set<Game> games = new HashSet<>();
    private final Map<UUID, Game> uuidGameMap = new HashMap<>();

    private Game lobby;

    public Set<Game> getGames() {
        return games;
    }

    public Game getLobby() {
        return lobby;
    }

    public @Nonnull Game getGame(Player p) {
        return uuidGameMap.get(p.getUniqueId());
    }

    public void join(Player p, @Nonnull Game game) {
        game.getState().addPlayer(p);
        uuidGameMap.put(p.getUniqueId(), game);
    }

    public boolean isPlayerFirstTimeJoin(Player p) {
        return !uuidGameMap.containsKey(p.getUniqueId());
    }

    public boolean registerGame(Game game) {
        if (games.contains(game)) {
            getLogger().warning(String.format("Attempting to register duplicated game: %s", game.getName()));
            return false;
        }
        games.add(game);
        return true;
    }

    public boolean unregisterGame(Game game) {
        if (!games.contains(game)) {
            getLogger().warning(String.format("Attempting to unregister non-existent game: %s", game.getName()));
            return false;
        }
        games.remove(game);
        return true;
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new PlayerLogInLogOutListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ProtectionListener(this), this);
    }

    private void registerCommands() {
        PluginCommand joinCommand = getCommand("join");
        if (joinCommand == null) {
            getLogger().warning("Command not found: join. Did you add it to plugin.yml?");
        } else {
            JoinCommand join = new JoinCommand(this);
            joinCommand.setExecutor(join);
            joinCommand.setTabCompleter(join);
        }

        PluginCommand tpgameCommand = getCommand("tpgame");
        if (tpgameCommand == null) {
            getLogger().warning("Command not found: tpgame. Did you add it to plugin.yml?");
        } else {
            TpGameCommand tpgame = new TpGameCommand(this);
            tpgameCommand.setExecutor(tpgame);
            tpgameCommand.setTabCompleter(tpgame);
        }

        PluginCommand forceStopCommand = getCommand("forcestop");
        if (forceStopCommand == null) {
            getLogger().warning("Command not found: forcestop. Did you add it to plugin.yml?");
        } else {
            ForceStop forceStop = new ForceStop(this);
            forceStopCommand.setExecutor(forceStop);
            forceStopCommand.setTabCompleter(forceStop);
        }

        PluginCommand placeStandCommand = getCommand("placestand");
        if (placeStandCommand == null) {
            getLogger().warning("Command not found: placestand. Did you add it to plugin.yml?");
        } else {
            PlaceStandCommand placeStand = new PlaceStandCommand();
            placeStandCommand.setExecutor(placeStand);
        }

        PluginCommand changeBiomeCommand = getCommand("changebiome");
        if (changeBiomeCommand == null) {
            getLogger().warning("Command not found: changebiome. Did you add it to plugin.yml?");
        } else {
            ChangeBiome changeBiome = new ChangeBiome(this);
            changeBiomeCommand.setExecutor(changeBiome);
            changeBiomeCommand.setTabCompleter(changeBiome);
        }
    }

    @Override
    public void onEnable() {
        registerCommands();
        registerEvents();

        // Get the lobby plugin after startup finishes
        Bukkit.getScheduler().runTaskLater(this, () -> {
            Plugin lobby = Bukkit.getPluginManager().getPlugin("Lobby");
            if (lobby == null) {
                getLogger().warning("Lobby plugin not found!");
                return;
            }
            if (!(lobby instanceof Game)) {
                getLogger().warning("Lobby plugin is not a Game!");
                return;
            }
            this.lobby = (Game) lobby;
        }, 1);

        // Inject into Misc class to enable miscellaneous utilities
        Misc.setPlugin(this);
    }
}
