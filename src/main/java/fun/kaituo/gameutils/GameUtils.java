package fun.kaituo.gameutils;

import fun.kaituo.gameutils.command.GameUtilsCommand;
import fun.kaituo.gameutils.game.Game;
import fun.kaituo.gameutils.listener.LayoutSignClickListener;
import fun.kaituo.gameutils.listener.PlayerLogInLogOutListener;
import fun.kaituo.gameutils.listener.ProtectionListener;
import fun.kaituo.gameutils.util.HotbarMappingManager;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.util.*;


/**
 * The base toolset for constructing our minigames.
 */
public class GameUtils extends JavaPlugin {
    private static GameUtils instance = null;

    /**
     * Constructor for GameUtils.
     */
    public GameUtils() {
        super();
    }

    /**
     * Returns the GameUtils plugin instance.
     *
     * @return The GameUtils instance.
     * @throws java.lang.IllegalStateException If GameUtils hasn't been enabled.
     */
    public @Nonnull static GameUtils inst() {
        if (instance == null) {
            throw new IllegalStateException("GameUtils instance has not been enabled");
        }
        return instance;
    }

    private final Set<Game> games = new HashSet<>();

    /**
     * Returns registered games.
     *
     * @return Registered games.
     */
    public Set<Game> getGames() {
        return games;
    }

    private final Map<UUID, Game> uuidGameMap = new HashMap<>();

    private World world;

    /**
     * Returns the main world of the server, namely the world named 'world'.
     *
     * @return The main world.
     */
    public World getMainWorld() {
        return world;
    }

    private Game lobby;

    /**
     * Returns the lobby 'game'.
     *
     * @see <a href="https://github.com/KaituoDev/Lobby">Lobby</a>
     * @return The lobby 'game'.
     */
    public Game getLobby() {
        return lobby;
    }

    /**
     * Returns the game that a player is a participant of.
     *
     * @param p The player to be queried.
     * @return The game the player is in.
     */
    public @Nonnull Game getGame(Player p) {
        return uuidGameMap.get(p.getUniqueId());
    }

    /**
     * Adds the player to a game.
     *
     * @param p The player to add.
     * @param game The game to join.
     */
    public void join(Player p, @Nonnull Game game) {
        game.addPlayer(p);
        uuidGameMap.put(p.getUniqueId(), game);
    }

    /**
     * Checks if the player is joining the server for the first time.
     *
     * @param p The player to be checked.
     * @return Whether the player hasn't joined the server before.
     */
    public boolean isPlayerFirstTimeJoin(Player p) {
        return !uuidGameMap.containsKey(p.getUniqueId());
    }

    /**
     * Registers a game to GameUtils' registry.
     *
     * @param game The game to be registered.
     * @return Whether the registration is successful.
     */
    public boolean registerGame(Game game) {
        if (games.contains(game)) {
            getLogger().warning(String.format("Attempting to register duplicated game: %s", game.getName()));
            return false;
        }
        games.add(game);
        return true;
    }

    /**
     * Unregisters a game to GameUtils' registry.
     *
     * @param game The game to be unregistered.
     * @return Whether the unregistration is successful.
     */
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
        try (ScanResult scanResult = new ClassGraph()
                .acceptPackages("fun.kaituo.gameutils.command") // 指定扫描的包
                .enableClassInfo()
                .scan()) {

            Set<Class<? extends GameUtilsCommand>> commandClasses = new HashSet<>(scanResult
                    .getSubclasses(GameUtilsCommand.class.getName()) // 获取子类
                    .loadClasses(GameUtilsCommand.class));

            commandClasses.forEach(this::registerCommand);
        } catch (Exception e) {
            getLogger().warning("Failed to scan for command classes");
            throw new RuntimeException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void onEnable() {
        // Prepare the static instance
        GameUtils.instance = this;
        this.world = Bukkit.getWorld("world");

        saveDefaultConfig();
        HotbarMappingManager.INSTANCE.loadMappings(this);

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

    /** {@inheritDoc} */
    public void onDisable() {
        HotbarMappingManager.INSTANCE.saveMappings(this);
    }
}
