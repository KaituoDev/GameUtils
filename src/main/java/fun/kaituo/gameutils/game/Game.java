package fun.kaituo.gameutils.game;

import fun.kaituo.gameutils.GameUtils;
import fun.kaituo.gameutils.util.GameInventory;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

/**
 * Represents a minigame.
 */
public abstract class Game extends JavaPlugin {

    /**
     * The default path where item data are stored in the config.
     */
    public static final String ITEM_SAVE_PATH = "items.";
    /**
     * The default path where location data are stored in the config.
     */
    public static final String LOC_SAVE_PATH = "locations.";
    /**
     * The default path where inventory data are stored in the config.
     */
    public static final String INV_SAVE_PATH = "inventories.";

    protected String displayName;
    protected Location gameTeleportLocation;
    protected GameState state;

    /**
     * Puts a player under this game's management.
     * <p>
     * This method will be called automatically when a player
     * executes the <code>/join</code> command, is invited through
     * <code>/joinall</code>, or rejoins the game.
     *
     * @param p The player joining the game.
     */
    public void addPlayer(Player p) {
        if (state != null) {
            state.addPlayer(p);
        }
    }

    /**
     * Removes a player from this game's management.
     * <p>
     * This method will be called automatically when a player
     * executes the <code>/join</code> command to join another game or leaves
     * the server.
     *
     * @param p The player leaving the game.
     */
    public void removePlayer(Player p) {
        if (state != null) {
            state.removePlayer(p);
        }
    }

    /**
     * Forcefully stop this game.
     * <p>
     * This method is called when someone executes the
     * <code>/forcestop</code> command.
     */
    public void forceStop() {
        if (state != null) {
            state.forceStop();
        }
    }

    /**
     * Performs the per tick action of the state.
     * <p>
     * This method is called every tick automatically.
     */
    public void tick() {
        if (state != null) {
            state.tick();
        }
    }

    /**
     * Returns the display (formatted) name of the game.
     *
     * @return The formatted name of the game.
     */
    @SuppressWarnings("unused")
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns the teleport location of the game.
     *
     * @return The teleport location of the game.
     * @deprecated The naming of this method is unclear, use {@link #getGameTeleportLocation()} instead.
     */
    @Deprecated(since="2.0.1")
    public Location getLocation() {
        return getGameTeleportLocation();
    }

    /**
     * Returns the teleport location of the game.
     *
     * @return The teleport location of the game.
     */
    public Location getGameTeleportLocation() {
        return gameTeleportLocation;
    }

    /**
     * Update extra game info (display name and game teleport location).
     * Should only be called in {@link #onEnable()} once.
     *
     * @param displayName The new display name.
     * @param gameTeleportLocation The new game teleport location.
     */
    @SuppressWarnings("unused")
    protected void updateExtraInfo(String displayName, Location gameTeleportLocation) {
        this.displayName = displayName;
        this.gameTeleportLocation = gameTeleportLocation;
    }

    /**
     * Returns the state this game is in.
     *
     * @return The state this game is in.
     */
    @SuppressWarnings("unused")
    public @Nullable GameState getState() {
        return state;
    }

    /**
     * Switches the state of this game.
     *
     * @param state The state to be switched to.
     */
    @SuppressWarnings("unused")
    public void setState(@Nonnull GameState state) {
        if (this.state != null) {
            if (this.state == state) {
                throw new RuntimeException("Self-transition is not allowed");
            }
            this.state.exit();
        }
        this.state = state;
        this.state.enter();
    }

    /**
     * Saves the {@link ItemStack} data to the plugin's config file to reuse it.
     *
     * @param id The id of the {@link ItemStack} data save.
     * @param item The {@link ItemStack} to be saved.
     */
    public void saveItem(String id, ItemStack item) {
        getConfig().createSection(ITEM_SAVE_PATH + id, item.serialize());
        saveConfig();
    }

    /**
     * Returns an {@link ItemStack} based on a data save.
     *
     * @param id The id of the data save to read from.
     * @return The saved {@link ItemStack}, null if the id is not found.
     */
    public @Nullable ItemStack getItem(String id) {
        ConfigurationSection section = getConfig().getConfigurationSection(ITEM_SAVE_PATH + id);
        if (section == null) {
            return null;
        }
        return ItemStack.deserialize(section.getValues(true));
    }

    /**
     * Removes an {@link ItemStack} data save from the config file.
     *
     * @param id The id of the data save to be removed.
     * @return Whether the id exists in the config file.
     */
    public boolean removeItem(String id) {
        ConfigurationSection section = getConfig().getConfigurationSection(ITEM_SAVE_PATH + id);
        if (section == null) {
            return false;
        }
        getConfig().set(ITEM_SAVE_PATH + id, null);
        saveConfig();
        return true;
    }

    /**
     * Returns the ids of all {@link ItemStack} data saves.
     *
     * @return The ids of all {@link ItemStack} data saves.
     */
    public @Nonnull Set<String> getItemIds() {
        ConfigurationSection section = getConfig().getConfigurationSection(ITEM_SAVE_PATH);
        if (section == null) {
            return Set.of();
        }
        return section.getKeys(false);
    }

    /**
     * Saves the {@link Location} data to the plugin's config file to reuse it.
     *
     * @param id The id of the {@link Location} data save.
     * @param loc The {@link Location} to be saved.
     */
    public void saveLoc(String id, Location loc) {
        getConfig().createSection(LOC_SAVE_PATH + id, loc.serialize());
        saveConfig();
    }

    /**
     * Returns an {@link Location} based on a data save.
     *
     * @param id The id of the data save to read from.
     * @return The saved {@link Location}, null if the id is not found.
     */
    public @Nullable Location getLoc(String id) {
        ConfigurationSection section = getConfig().getConfigurationSection(LOC_SAVE_PATH + id);
        if (section == null) {
            return null;
        }
        return Location.deserialize(section.getValues(true));
    }

    /**
     * Removes an {@link Location} data save from the config file.
     *
     * @param id The id of the data save to be removed.
     * @return Whether the id exists in the config file.
     */
    public boolean removeLoc(String id) {
        ConfigurationSection section = getConfig().getConfigurationSection(LOC_SAVE_PATH + id);
        if (section == null) {
            return false;
        }
        getConfig().set(LOC_SAVE_PATH + id, null);
        saveConfig();
        return true;
    }

    /**
     * Returns the ids of all {@link Location} data saves.
     *
     * @return The ids of all {@link Location} data saves.
     */
    public @Nonnull Set<String> getLocIds() {
        ConfigurationSection section = getConfig().getConfigurationSection(LOC_SAVE_PATH);
        if (section == null) {
            return Set.of();
        }
        return section.getKeys(false);
    }

    /**
     * Saves the {@link GameInventory} data to the plugin's config file to reuse it.
     *
     * @param id The id of the {@link GameInventory} data save.
     * @param inv The {@link GameInventory} to be saved.
     */
    public void saveInv(String id, GameInventory inv) {
        getConfig().createSection(INV_SAVE_PATH + id, inv.serialize());
        saveConfig();
    }

    /**
     * Returns an {@link GameInventory} based on a data save.
     *
     * @param id The id of the data save to read from.
     * @return The saved {@link GameInventory}, null if the id is not found.
     */
    public @Nullable GameInventory getInv(String id) {
        ConfigurationSection section = getConfig().getConfigurationSection(INV_SAVE_PATH + id);
        if (section == null) {
            return null;
        }
        return GameInventory.deserialize(section.getValues(true));
    }

    /**
     * Removes an {@link GameInventory} data save from the config file.
     *
     * @param id The id of the data save to be removed.
     * @return Whether the id exists in the config file.
     */
    public boolean removeInv(String id) {
        ConfigurationSection section = getConfig().getConfigurationSection(INV_SAVE_PATH + id);
        if (section == null) {
            return false;
        }
        getConfig().set(INV_SAVE_PATH + id, null);
        saveConfig();
        return true;
    }

    /**
     * Returns the ids of all {@link GameInventory} data saves.
     *
     * @return The ids of all {@link GameInventory} data saves.
     */
    public @Nonnull Set<String> getInvIds() {
        ConfigurationSection section = getConfig().getConfigurationSection(INV_SAVE_PATH);
        if (section == null) {
            return Set.of();
        }
        return section.getKeys(false);
    }

    @Override
    public void onEnable() {
        if (!GameUtils.inst().registerGame(this)) {
            getLogger().severe("Game already registered");
            return;
        }
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this::tick, 1, 1);

        displayName = "§f未知游戏";
        gameTeleportLocation = new Location(GameUtils.inst().getMainWorld(), 0.5, 89, 0.5, 0, 0);
    }

    @Override
    public void onDisable() {
        if (!GameUtils.inst().unregisterGame(this)) {
            getLogger().severe("Attempting to unregister game but not found");
            return;
        }
        Bukkit.getScheduler().cancelTasks(this);
    }
}
