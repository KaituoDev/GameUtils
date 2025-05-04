package fun.kaituo.gameutils.listener;

import fun.kaituo.gameutils.GameUtils;
import fun.kaituo.gameutils.util.ItemUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import static fun.kaituo.gameutils.command.Rotatable.isRotatable;

/**
 * Represents a listener for various events for protection purposes.
 */
public class ProtectionListener implements Listener {

    private final FileConfiguration c;

    /**
     * The constructor for the ProtectionListener class.
     */
    public ProtectionListener() {
        this.c = GameUtils.inst().getConfig();
    }

    /**
     * Prevents firework damage.
     *
     * @param edbee The {@link org.bukkit.event.entity.EntityDamageByEntityEvent} to handle.
     */
    @EventHandler
    public void preventFireworkDamage(EntityDamageByEntityEvent edbee) {
        if (!c.getBoolean("no-firework-damage")) {
            return;
        }
        if (edbee.getDamager() instanceof Firework) {
            if (edbee.getDamager().getScoreboardTags().contains("gameFirework")) {
                edbee.setCancelled(true);
            }
        }
    }

    /**
     * Prevents destroying paintings unless in creative mode.
     *
     * @param hbbee The {@link org.bukkit.event.hanging.HangingBreakByEntityEvent} to handle.
     */
    @EventHandler
    public void preventDestroyingPainting(HangingBreakByEntityEvent hbbee) {
        if (!c.getBoolean("no-destroy-painting")) {
            return;
        }
        if (!(hbbee.getRemover() instanceof Player)) {
            return;
        }
        if (!((Player)hbbee.getRemover()).getGameMode().equals(GameMode.CREATIVE)) {
            hbbee.setCancelled(true);
        }
    }

    /**
     * Prevents manipulating armor stands unless in creative mode.
     *
     * @param pasme The {@link org.bukkit.event.player.PlayerArmorStandManipulateEvent} to handle.
     */
    @EventHandler
    public void preventManipulatingArmorStand(PlayerArmorStandManipulateEvent pasme) {
        if (!c.getBoolean("no-armourstand-manipulation")) {
            return;
        }
        if (!pasme.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            pasme.setCancelled(true);
        }
    }

    /**
     * Prevents rotating item frames unless in creative mode.
     *
     * @param piee The {@link org.bukkit.event.player.PlayerInteractEntityEvent} to handle.
     */
    @EventHandler
    public void preventItemFrameRotation(PlayerInteractEntityEvent piee) {
        if (!c.getBoolean("no-item-frame-rotation")) {
            return;
        }
        if (!(piee.getRightClicked() instanceof ItemFrame frame)) {
            return;
        }
        if (!piee.getPlayer().getGameMode().equals(GameMode.CREATIVE) && !isRotatable(frame)) {
            piee.setCancelled(true);
        }
    }

    /**
     * Prevents dropping items unless the item is droppable or the player is in creative mode.
     *
     * @param pdie The {@link org.bukkit.event.player.PlayerDropItemEvent} to handle.
     */
    @EventHandler
    public void preventItemDrop(PlayerDropItemEvent pdie) {
        if (!c.getBoolean("no-item-drop")) {
            return;
        }
        ItemStack item = pdie.getItemDrop().getItemStack();
        if (item.getType().equals(Material.AIR)) {
            return;
        }
        if (item.getAmount() == 0) {
            return;
        }
        if (!pdie.getPlayer().getGameMode().equals(GameMode.CREATIVE) && !ItemUtils.isDroppable(item)) {
            pdie.setCancelled(true);
        }
    }

    /**
     * Prevents clicking on items in inventories unless the item is clickable or the player is in creative mode.
     *
     * @param ice The {@link org.bukkit.event.inventory.InventoryClickEvent} to handle.
     */
    @EventHandler
    public void preventItemClick(InventoryClickEvent ice) {
        if (!c.getBoolean("no-item-click")) {
            return;
        }
        ItemStack item = ice.getCurrentItem();
        if (item == null) {
            return;
        }
        if (item.getType().equals(Material.AIR)) {
            return;
        }
        if (item.getAmount() == 0) {
            return;
        }
        if (!ice.getWhoClicked().getGameMode().equals(GameMode.CREATIVE) && !ItemUtils.isClickable(item)) {
            ice.setCancelled(true);
        }
    }

    /**
     * Prevents interaction with flower pots and composters unless the player is in creative mode.
     *
     * @param pie The {@link org.bukkit.event.player.PlayerInteractEvent} to handle.
     */
    @EventHandler
    public void preventSpecialBlockInteraction(PlayerInteractEvent pie) {
        if (!c.getBoolean("no-special-block-interaction")) {
            return;
        }
        if (pie.getClickedBlock() == null) {
            return;
        }
        if (pie.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }
        Block block = pie.getClickedBlock();
        if (block.getType().name().startsWith("POTTED_") || block.getType() == Material.FLOWER_POT) {
            pie.setCancelled(true);
            return;
        }
        if (block.getType().equals(Material.COMPOSTER)) {
            pie.setCancelled(true);
        }
    }


    /**
     * Sets a painting to be invulnerable when placed.
     *
     * @param hpe The {@link org.bukkit.event.hanging.HangingPlaceEvent} to handle.
     */
    @EventHandler
    public void invulnerablePainting(HangingPlaceEvent hpe) {
        if (!c.getBoolean("invulnerable-painting-on-spawn")) {
            return;
        }
        if (hpe.getEntity().getType().equals(EntityType.PAINTING)) {
            hpe.getEntity().setInvulnerable(true);
        }
    }

    /**
     * Sets an armor stand to be invulnerable when placed.
     *
     * @param ese The {@link org.bukkit.event.entity.EntitySpawnEvent} to handle.
     */
    @EventHandler
    public void invulnerableStand(EntitySpawnEvent ese) {
        if (!c.getBoolean("invulnerable-armourstand-on-spawn")) {
            return;
        }
        if (ese.getEntity().getType().equals(EntityType.ARMOR_STAND)) {
            ese.getEntity().setInvulnerable(true);
        }
    }

    /**
     * Sets an item frame to be invulnerable when placed.
     *
     * @param ese The {@link org.bukkit.event.entity.EntitySpawnEvent} to handle.
     */
    @EventHandler
    public void invulnerableItemFrame(EntitySpawnEvent ese) {
        if (!c.getBoolean("invulnerable-item-frame-on-spawn")) {
            return;
        }
        if (ese.getEntity().getType().equals(EntityType.ITEM_FRAME)
                || ese.getEntity().getType().equals(EntityType.GLOW_ITEM_FRAME)) {
            ese.getEntity().setInvulnerable(true);
        }
    }


    /**
     * Prevents chickens spawning from eggs.
     *
     * @param cse The {@link org.bukkit.event.entity.CreatureSpawnEvent} to handle.
     */
    @EventHandler
    public void cancelEggSpawn(CreatureSpawnEvent cse) { //防止鸡蛋生成鸡
        if (!c.getBoolean("no-chicken-from-egg")) {
            return;
        }
        if (cse.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.EGG)) {
            cse.setCancelled(true);
        }
    }

    /**
     * Prevents snow and ice formation.
     *
     * @param bfe The {@link org.bukkit.event.block.BlockFormEvent} to handle.
     */
    @EventHandler
    public void preventSnowFormation(BlockFormEvent bfe) {
        if (!c.getBoolean("no-snow-and-ice-formation")) {
            return;
        }
        if (bfe.getNewState().getType().equals(Material.SNOW) || bfe.getNewState().getType().equals(Material.ICE)) {
            bfe.setCancelled(true);
        }
    }

    /**
     * Prevents silver fish from burrowing into blocks.
     *
     * @param ecbe The {@link org.bukkit.event.entity.EntityChangeBlockEvent} to handle.
     */
    @EventHandler
    public void noSilverfishBurrow(EntityChangeBlockEvent ecbe) {
        if (!c.getBoolean("no-silverfish-burrow")) {
            return;
        }
        if (ecbe.getEntityType().equals(EntityType.SILVERFISH)) {
            ecbe.setCancelled(true);
        }
    }
}
