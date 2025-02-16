package fun.kaituo.gameutils.game;

import fun.kaituo.gameutils.GameUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

public abstract class Game extends JavaPlugin {
    protected GameUtils gameUtils;

    protected String displayName;
    protected Location location;
    protected GameState state;

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
    public GameState getState() {
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

    @Override
    public void onEnable() {
        gameUtils = (GameUtils) Bukkit.getPluginManager().getPlugin("GameUtils");
        if (gameUtils == null) {
            throw new RuntimeException("GameUtils not found");
        }
        if (!gameUtils.registerGame(this)) {
            throw new RuntimeException("Game already registered");
        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this.state::tick, 1, 1);

        displayName = "§f未知游戏";
        location = new Location(Bukkit.getWorld("world"), 0.5, 89, 0.5, 0, 0);
    }

    @Override
    public void onDisable() {
        if (!gameUtils.unregisterGame(this)) {
            throw new RuntimeException("Attempting to unregister game but not found");
        }
        Bukkit.getScheduler().cancelTasks(this);
    }
}
