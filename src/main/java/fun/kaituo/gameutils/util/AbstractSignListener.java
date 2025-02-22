package fun.kaituo.gameutils.util;

import org.bukkit.Bukkit;
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

public abstract class AbstractSignListener implements Listener {
    protected Location location;
    protected List<String> lines = Arrays.asList("", "", "", "");
    protected JavaPlugin plugin;

    public AbstractSignListener(JavaPlugin plugin, Location location) {
        this.location = location;
        this.plugin = plugin;
        Bukkit.getScheduler().runTaskLater(plugin, this::update, 1);
    }

    @SuppressWarnings("unused")
    public void setLine(int index, @Nonnull String line) {
        if (index < 0 || index >= 4) {
            throw new IllegalArgumentException("Line index can only be 0-3");
        }
        lines.set(index, line);
        update();
    }

    @SuppressWarnings("unused")
    public @Nonnull String getLine(int index) {
        if (index < 0 || index >= 4) {
            throw new IllegalArgumentException("Line index can only be 0-3");
        }
        return lines.get(index);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
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
        Player p = e.getPlayer();
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

    public void update() {
        Sign sign = (Sign) location.getBlock().getState();
        for (int i = 0; i < 4; i++) {
            sign.getSide(Side.FRONT).setLine(i, lines.get(i));
        }
        sign.update();
    }

    public abstract void onRightClick(PlayerInteractEvent e);

    public abstract void onSneakingRightClick(PlayerInteractEvent e);


}
