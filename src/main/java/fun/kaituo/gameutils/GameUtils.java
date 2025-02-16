package fun.kaituo.gameutils;

import fun.kaituo.gameutils.command.ChangeBiomeCommand;
import fun.kaituo.gameutils.command.ForceStopCommand;
import fun.kaituo.gameutils.command.JoinCommand;
import fun.kaituo.gameutils.command.PlaceStandCommand;
import fun.kaituo.gameutils.command.TpGameCommand;
import fun.kaituo.gameutils.game.Game;
import fun.kaituo.gameutils.util.Misc;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


public class GameUtils extends JavaPlugin implements Listener {
    private final Set<Game> games = new HashSet<>();
    private final Map<UUID, Game> uuidGameMap = new HashMap<>();

    private Game lobby;

    public Set<Game> getGames() {
        return games;
    }

    public Game getLobby() {
        return lobby;
    }

    public @Nullable Game getGame(Player p) {
        return uuidGameMap.get(p.getUniqueId());
    }

    public void join(Player p, @Nonnull Game game) {
        game.getState().join(p);
        uuidGameMap.put(p.getUniqueId(), game);
    }

    public void quit(Player p) {
        Game currentGame = getGame(p);
        if (currentGame != null) {
            currentGame.getState().quit(p);
            uuidGameMap.remove(p.getUniqueId());
        }
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

    private void registerCommands() {
        ChangeBiomeCommand changeBiomeCommand = new ChangeBiomeCommand(this);
        getCommand("changebiome").setExecutor(changeBiomeCommand);
        getCommand("changebiome").setTabCompleter(changeBiomeCommand);

        PlaceStandCommand placeStandCommand = new PlaceStandCommand();
        getCommand("placestand").setExecutor(placeStandCommand);

        TpGameCommand tpGameCommand = new TpGameCommand(this);
        getCommand("tpgame").setExecutor(tpGameCommand);
        getCommand("tpgame").setTabCompleter(tpGameCommand);

        ForceStopCommand forceStopCommand = new ForceStopCommand(this);
        getCommand("forcestop").setExecutor(forceStopCommand);
        getCommand("forcestop").setTabCompleter(forceStopCommand);

        JoinCommand joinCommand = new JoinCommand(this);
    }

    @Override
    public void onEnable() {
        registerCommands();
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
        // Inject into Misc.class to enable miscellaneous utilities
        Misc.setPlugin(this);
    }
}
