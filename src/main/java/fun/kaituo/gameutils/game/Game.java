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

public abstract class Game extends JavaPlugin {
    public static final String ITEM_SAVE_PATH = "items.";
    public static final String LOC_SAVE_PATH = "locations.";
    public static final String INV_SAVE_PATH = "inventories.";
    protected String displayName;
    protected Location location;
    protected GameState state;

    public void addPlayer(Player p) {
        if (state != null) {
            state.addPlayer(p);
        }
    }

    public void removePlayer(Player p) {
        if (state != null) {
            state.removePlayer(p);
        }
    }
    public void forceStop() {
        if (state != null) {
            state.forceStop();
        }
    }
    public void tick() {
        if (state != null) {
            state.tick();
        }
    }

    @SuppressWarnings("unused")
    public String getDisplayName() {
        return displayName;
    }

    @SuppressWarnings("unused")
    public Location getLocation() {
        return location;
    }

    @SuppressWarnings("unused")
    protected void updateExtraInfo(String displayName, Location location) {
        this.displayName = displayName;
        this.location = location;
    }

    @SuppressWarnings("unused")
    public @Nullable GameState getState() {
        return state;
    }

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

    public void saveItem(String id, ItemStack item) {
        getConfig().createSection(ITEM_SAVE_PATH + id, item.serialize());
        saveConfig();
    }

    public @Nullable ItemStack getItem(String id) {
        ConfigurationSection section = getConfig().getConfigurationSection(ITEM_SAVE_PATH + id);
        if (section == null) {
            return null;
        }
        return ItemStack.deserialize(section.getValues(true));
    }

    public boolean removeItem(String id) {
        ConfigurationSection section = getConfig().getConfigurationSection(ITEM_SAVE_PATH + id);
        if (section == null) {
            return false;
        }
        getConfig().set(ITEM_SAVE_PATH + id, null);
        saveConfig();
        return true;
    }

    public @Nonnull Set<String> getItemIds() {
        ConfigurationSection section = getConfig().getConfigurationSection(ITEM_SAVE_PATH);
        if (section == null) {
            return Set.of();
        }
        return section.getKeys(false);
    }

    public void saveLoc(String id, Location loc) {
        getConfig().createSection(LOC_SAVE_PATH + id, loc.serialize());
        saveConfig();
    }

    public @Nullable Location getLoc(String id) {
        ConfigurationSection section = getConfig().getConfigurationSection(LOC_SAVE_PATH + id);
        if (section == null) {
            return null;
        }
        return Location.deserialize(section.getValues(true));
    }

    public boolean removeLoc(String id) {
        ConfigurationSection section = getConfig().getConfigurationSection(LOC_SAVE_PATH + id);
        if (section == null) {
            return false;
        }
        getConfig().set(LOC_SAVE_PATH + id, null);
        saveConfig();
        return true;
    }

    public @Nonnull Set<String> getLocIds() {
        ConfigurationSection section = getConfig().getConfigurationSection(LOC_SAVE_PATH);
        if (section == null) {
            return Set.of();
        }
        return section.getKeys(false);
    }

    public void saveInv(String id, GameInventory inv) {
        getConfig().createSection(INV_SAVE_PATH + id, inv.serialize());
        saveConfig();
    }

    public @Nullable GameInventory getInv(String id) {
        ConfigurationSection section = getConfig().getConfigurationSection(INV_SAVE_PATH + id);
        if (section == null) {
            return null;
        }
        return GameInventory.deserialize(section.getValues(true));
    }

    public boolean removeInv(String id) {
        ConfigurationSection section = getConfig().getConfigurationSection(INV_SAVE_PATH + id);
        if (section == null) {
            return false;
        }
        getConfig().set(INV_SAVE_PATH + id, null);
        saveConfig();
        return true;
    }

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
            throw new RuntimeException("Game already registered");
        }
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this::tick, 1, 1);

        displayName = "§f未知游戏";
        location = new Location(GameUtils.inst().getMainWorld(), 0.5, 89, 0.5, 0, 0);
    }

    @Override
    public void onDisable() {
        if (!GameUtils.inst().unregisterGame(this)) {
            throw new RuntimeException("Attempting to unregister game but not found");
        }
        Bukkit.getScheduler().cancelTasks(this);
    }
}
