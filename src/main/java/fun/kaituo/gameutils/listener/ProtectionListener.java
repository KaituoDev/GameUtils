package fun.kaituo.gameutils.listener;

import fun.kaituo.gameutils.GameUtils;
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
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ProtectionListener implements Listener {

    private final FileConfiguration c;

    public ProtectionListener() {
        this.c = GameUtils.inst().getConfig();
    }

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

    @EventHandler
    public void preventManipulatingArmorStand(PlayerArmorStandManipulateEvent pasme) {
        if (!c.getBoolean("no-armourstand-manipulation")) {
            return;
        }
        if (!pasme.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            pasme.setCancelled(true);
        }
    }

    @EventHandler
    public void preventItemFrameRotation(PlayerInteractEntityEvent piee) {
        if (!c.getBoolean("no-item-frame-rotation")) {
            return;
        }
        if (!(piee.getRightClicked() instanceof ItemFrame)) {
            return;
        }
        if (!piee.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            piee.setCancelled(true);
        }
    }


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


    @EventHandler
    public void invulnerablePainting(HangingPlaceEvent hpe) {
        if (!c.getBoolean("invulnerable-painting-on-spawn")) {
            return;
        }
        if (hpe.getEntity().getType().equals(EntityType.PAINTING)) {
            hpe.getEntity().setInvulnerable(true);
        }
    }

    @EventHandler
    public void invulnerableStand(EntitySpawnEvent ese) {
        if (!c.getBoolean("invulnerable-armourstand-on-spawn")) {
            return;
        }
        if (ese.getEntity().getType().equals(EntityType.ARMOR_STAND)) {
            ese.getEntity().setInvulnerable(true);
        }
    }


    @EventHandler
    public void cancelEggSpawn(CreatureSpawnEvent cse) { //防止鸡蛋生成鸡
        if (!c.getBoolean("no-chicken-from-egg")) {
            return;
        }
        if (cse.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.EGG)) {
            cse.setCancelled(true);
        }
    }

    @EventHandler
    public void preventSnowFormation(BlockFormEvent bfe) {
        if (!c.getBoolean("no-snow-and-ice-formation")) {
            return;
        }
        if (bfe.getNewState().getType().equals(Material.SNOW) || bfe.getNewState().getType().equals(Material.ICE)) {
            bfe.setCancelled(true);
        }
    }



}
