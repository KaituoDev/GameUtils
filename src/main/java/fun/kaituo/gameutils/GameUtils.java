package fun.kaituo.gameutils;

import fun.kaituo.gameutils.command.GameUtilsCommand;
import fun.kaituo.gameutils.game.Game;
import fun.kaituo.gameutils.listener.LayoutSignClickListener;
import fun.kaituo.gameutils.listener.PlayerLogInLogOutListener;
import fun.kaituo.gameutils.listener.ProtectionListener;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.util.*;


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

    private void registerCommand(Class<? extends GameUtilsCommand> commandClass) {
        try {
            Constructor<? extends GameUtilsCommand> constructor = commandClass.getConstructor();
            GameUtilsCommand command = constructor.newInstance();
            PluginCommand pluginCommand = getCommand(command.getName());
            if (pluginCommand == null) {
                getLogger().warning("Could not find command: " + command.getName() + ", did you forget to register it in plugin.yml?");
                return;
            }
            pluginCommand.setExecutor(command);
            if (command instanceof TabCompleter) {
                pluginCommand.setTabCompleter((TabCompleter) command);
            }
        } catch (Exception e) {
            getLogger().warning("Failed to register command class: " + commandClass.getSimpleName());
            throw new RuntimeException(e);
        }
    }

    private void registerCommands() {
        Reflections reflections = new Reflections("fun.kaituo.gameutils.command");
        Set<Class<? extends GameUtilsCommand>> commandClasses = reflections.getSubTypesOf(GameUtilsCommand.class);
        for (Class<? extends GameUtilsCommand> commandClass : commandClasses) {
            registerCommand(commandClass);
        }
    }

    @Override
    public void onEnable() {
        // Prepare the static instance
        GameUtils.instance = this;
        this.world = Bukkit.getWorld("world");

        saveDefaultConfig();

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
