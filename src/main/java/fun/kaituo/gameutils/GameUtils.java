package fun.kaituo.gameutils;

import fun.kaituo.gameutils.command.ChangeBiome;
import fun.kaituo.gameutils.command.ForceStop;
import fun.kaituo.gameutils.command.GameItem;
import fun.kaituo.gameutils.command.Join;
import fun.kaituo.gameutils.command.Layout;
import fun.kaituo.gameutils.command.PlaceStand;
import fun.kaituo.gameutils.command.TpGame;
import fun.kaituo.gameutils.game.Game;
import fun.kaituo.gameutils.listener.LayoutSignClickListener;
import fun.kaituo.gameutils.listener.PlayerLogInLogOutListener;
import fun.kaituo.gameutils.listener.ProtectionListener;
import org.bukkit.Bukkit;
import org.bukkit.World;
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
    private static GameUtils instance;
    public static GameUtils inst() {
        return instance;
    }

    private final Set<Game> games = new HashSet<>();
    private final Map<UUID, Game> uuidGameMap = new HashMap<>();

    private World world;

    public World getMainWorld() {
        return world;
    }

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
        game.addPlayer(p);
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
        Bukkit.getPluginManager().registerEvents(new PlayerLogInLogOutListener(), this);
        Bukkit.getPluginManager().registerEvents(new ProtectionListener(), this);
        Bukkit.getPluginManager().registerEvents(new LayoutSignClickListener(), this);
    }

    private void registerCommands() {
        PluginCommand joinCommand = getCommand("join");
        if (joinCommand == null) {
            getLogger().warning("Command not found: join. Did you add it to plugin.yml?");
        } else {
            Join join = new Join();
            joinCommand.setExecutor(join);
            joinCommand.setTabCompleter(join);
        }

        PluginCommand tpgameCommand = getCommand("tpgame");
        if (tpgameCommand == null) {
            getLogger().warning("Command not found: tpgame. Did you add it to plugin.yml?");
        } else {
            TpGame tpgame = new TpGame();
            tpgameCommand.setExecutor(tpgame);
            tpgameCommand.setTabCompleter(tpgame);
        }

        PluginCommand forceStopCommand = getCommand("forcestop");
        if (forceStopCommand == null) {
            getLogger().warning("Command not found: forcestop. Did you add it to plugin.yml?");
        } else {
            ForceStop forceStop = new ForceStop();
            forceStopCommand.setExecutor(forceStop);
            forceStopCommand.setTabCompleter(forceStop);
        }

        PluginCommand placeStandCommand = getCommand("placestand");
        if (placeStandCommand == null) {
            getLogger().warning("Command not found: placestand. Did you add it to plugin.yml?");
        } else {
            PlaceStand placeStand = new PlaceStand();
            placeStandCommand.setExecutor(placeStand);
        }

        PluginCommand changeBiomeCommand = getCommand("changebiome");
        if (changeBiomeCommand == null) {
            getLogger().warning("Command not found: changebiome. Did you add it to plugin.yml?");
        } else {
            ChangeBiome changeBiome = new ChangeBiome();
            changeBiomeCommand.setExecutor(changeBiome);
            changeBiomeCommand.setTabCompleter(changeBiome);
        }

        PluginCommand layoutCommand = getCommand("layout");
        if (layoutCommand == null) {
            getLogger().warning("Command not found: layout. Did you add it to plugin.yml?");
        } else {
            Layout layout = new Layout();
            layoutCommand.setExecutor(layout);
        }

        PluginCommand gameItemCommand = getCommand("gameitem");
        if (gameItemCommand == null) {
            getLogger().warning("Command not found: gameitem. Did you add it to plugin.yml?");
        } else {
            GameItem gameItem = new GameItem();
            gameItemCommand.setExecutor(gameItem);
            gameItemCommand.setTabCompleter(gameItem);
        }

    }

    @Override
    public void onEnable() {
        // Prepare the static instance
        GameUtils.instance = this;
        this.world = Bukkit.getWorld("world");

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
    }
}
