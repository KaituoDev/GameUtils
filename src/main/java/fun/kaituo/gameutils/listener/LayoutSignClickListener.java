package fun.kaituo.gameutils.listener;

import fun.kaituo.gameutils.GameUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a listener for clicks on layout signs.
 */
public class LayoutSignClickListener implements Listener {
    private static final int MAX_X_OFFSET = 7;
    private static final int MAX_Y_OFFSET = 8;
    private static final int DEFAULT_Y = 100;
    private final Location centerSignLocation;

    /**
     * Constructor for LayoutSignClickListener.
     */
    public LayoutSignClickListener() {
        centerSignLocation = new Location(GameUtils.inst().getMainWorld(), -21, 23, 74);
    }

    /**
     * Handles player interactions with layout signs.
     *
     * @param e The {@link org.bukkit.event.player.PlayerInteractEvent} to handle.
     */
    @EventHandler
    public void onLayoutSignCLick(PlayerInteractEvent e) {
        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        Block b = e.getClickedBlock();
        assert b != null;
        if (!b.getType().name().toLowerCase().endsWith("sign")) {
            return;
        }
        if (b.getLocation().getZ() != centerSignLocation.getZ()) {
            return;
        }
        if (Math.abs(b.getLocation().getX() - centerSignLocation.getX()) > MAX_X_OFFSET) {
            return;
        }
        if (Math.abs(b.getLocation().getY() - centerSignLocation.getY()) > MAX_Y_OFFSET) {
            return;
        }
        ItemStack handItem = e.getPlayer().getInventory().getItem(EquipmentSlot.HAND);
        if (handItem != null) {
            if (!handItem.getType().equals(Material.AIR)) {
                return;
            }
        }
        e.setCancelled(true);
        int xOffset = b.getLocation().getBlockX() - centerSignLocation.getBlockX();
        int zOffset = centerSignLocation.getBlockY() - b.getLocation().getBlockY();
        Player p = e.getPlayer();
        Location targetLoc = new Location(GameUtils.inst().getMainWorld(), 0.5 + xOffset * 1000, DEFAULT_Y, 0.5 + zOffset * 1000);
        p.teleport(targetLoc);
        p.sendMessage("§a已将您传送到了 " + targetLoc.getBlockX() + ", " + targetLoc.getBlockY() + ", " + targetLoc.getBlockZ());
    }
}
