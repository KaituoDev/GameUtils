package fun.kaituo.gameutils.util;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

/**
 * Simple wrapper for sign interactions and text alternation.
 */
public abstract class AbstractSignListener implements Listener {
    /**
     * The location of the sign.
     */
    protected final Location location;
    /**
     * The lines of text on the sign.
     */
    protected final List<String> lines = Arrays.asList("", "", "", "");
    /**
     * The {@link org.bukkit.plugin.java.JavaPlugin} to be used to run tasks.
     */
    protected final JavaPlugin plugin;

    /**
     * <p>Constructor for AbstractSignListener.</p>
     *
     * @param plugin The {@link org.bukkit.plugin.java.JavaPlugin} to be used to run tasks.
     * @param location The {@link org.bukkit.Location} of the sign.
     */
    public AbstractSignListener(JavaPlugin plugin, Location location) {
        this.location = location;
        this.plugin = plugin;
        Bukkit.getScheduler().runTaskLater(this.plugin, this::update, 1);
    }

    /**
     * Changes the text at the corresponding line on the front.
     *
     * @param index The line index (0-3).
     * @param line The new line text.
     */
    @SuppressWarnings("unused")
    public void setLine(int index, @Nonnull String line) {
        if (index < 0 || index >= 4) {
            throw new IllegalArgumentException("Line index can only be 0-3");
        }
        lines.set(index, line);
        update();
    }

    /**
     * Returns the text at the corresponding line on the front.
     *
     * @param index The line index (0-3).
     * @return The text at the corresponding line.
     */
    @SuppressWarnings("unused")
    public @Nonnull String getLine(int index) {
        if (index < 0 || index >= 4) {
            throw new IllegalArgumentException("Line index can only be 0-3");
        }
        return lines.get(index);
    }

    /**
     * Handles when a player interacts with the sign.
     *
     * @param e The {@link org.bukkit.event.player.PlayerInteractEvent} to handle.
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (p.getGameMode().equals(GameMode.SPECTATOR)) {
            return;
        }
        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        Block b = e.getClickedBlock();
        assert b != null;
        if (!b.getLocation().equals(location)) {
            return;
        }
        if (!(b.getState() instanceof Sign)) {
            e.getPlayer().sendMessage("§c此位置应当是告示牌！");
            return;
        }
        update();
        // Allow use of ink sac and glow ink sac
        ItemStack handItem = p.getInventory().getItem(EquipmentSlot.HAND);
        if (handItem != null) {
            if (handItem.getType().equals(Material.INK_SAC) || handItem.getType().equals(Material.GLOW_INK_SAC)) {
                return;
            }
        }
        e.setCancelled(true);
        if (e.getPlayer().isSneaking()) {
            onSneakingRightClick(e);
        } else {
            onRightClick(e);
        }
        p.playSound(p, Sound.UI_BUTTON_CLICK, 0.5f, 1);
    }

    /**
     * Updates the sign according to internal changes.
     */
    public void update() {
        Sign sign = (Sign) location.getBlock().getState();
        for (int i = 0; i < 4; i++) {
            sign.getSide(Side.FRONT).setLine(i, lines.get(i));
        }
        sign.update();
    }

    /**
     * Performs actions when the sign is right-clicked.
     *
     * @param e The {@link org.bukkit.event.player.PlayerInteractEvent} as a reference.
     */
    public abstract void onRightClick(PlayerInteractEvent e);

    /**
     * Performs actions when the sign is shift + right-clicked.
     *
     * @param e The {@link org.bukkit.event.player.PlayerInteractEvent} as a reference.
     */
    public abstract void onSneakingRightClick(PlayerInteractEvent e);


}
