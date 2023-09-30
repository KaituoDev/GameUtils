package fun.kaituo.gameutils;

import fun.kaituo.gameutils.event.PlayerEndGameEvent;
import fun.kaituo.gameutils.utils.GameItemStackTagType;
import org.bukkit.*;
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
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.scoreboard.Team;

import java.io.IOException;
import java.util.List;

import static fun.kaituo.gameutils.GameUtils.ROTATABLE_ITEM_FRAME_UUID_STRINGS;

@SuppressWarnings({ "ConstantConditions"})
public class GameUtilsListener implements Listener {
    GameUtils gameUtils;
    FileConfiguration c;
    
    public GameUtilsListener(GameUtils gameUtils) {
        this.gameUtils = gameUtils;
        c = gameUtils.getConfig();
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent pje) {
        Player p = pje.getPlayer();
        Bukkit.getScheduler().runTaskLater(gameUtils, () -> {
            if (!p.isOnline()) {
                return;
            }
            p.sendMessage("§6欢迎， §e" + p.getName() + "§6！");
        }, 20);

        gameUtils.resetPlayer(p);
        Lobby.getInstance().join(p);
        gameUtils.setPlayerGame(p, Lobby.getInstance());

    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent pqe) throws IOException {
        Player p = pqe.getPlayer();
        Game game = gameUtils.getPlayerGame(p);
        if (game != null) {
            game.quit(p);
        }
        gameUtils.removePlayerGame(p);
    }
    
    @EventHandler
    public void onPlayerEndGame(PlayerEndGameEvent pege) {
        gameUtils.resetPlayer(pege.getPlayer());
    }


    @EventHandler
    public void preventIllegalPickup(PlayerPickupItemEvent e) {
        PersistentDataContainer container = e.getItem().getItemStack().getItemMeta().getPersistentDataContainer();
        if (!container.has(gameUtils.getNamespacedKey(), new GameItemStackTagType())) {
            return;
        }
        Team team = Bukkit.getScoreboardManager().getMainScoreboard().getPlayerTeam(e.getPlayer());
        if (team == null) {
            e.setCancelled(true);
            return;
        }
        List<String> allowedTeamNames = container.get(gameUtils.getNamespacedKey(), new GameItemStackTagType()).canBePickedUpByTeams;
        if (!allowedTeamNames.contains(team.getName())) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void preventIllegalDrop(PlayerDropItemEvent e) {
        PersistentDataContainer container = e.getItemDrop().getItemStack().getItemMeta().getPersistentDataContainer();
        if (!container.has(gameUtils.getNamespacedKey(), new GameItemStackTagType())) {
            return;
        }
        Team team = Bukkit.getScoreboardManager().getMainScoreboard().getPlayerTeam(e.getPlayer());
        if (team == null) {
            e.setCancelled(true);
            return;
        }
        List<String> allowedTeamNames = container.get(gameUtils.getNamespacedKey(), new GameItemStackTagType()).canBeDroppedByTeams;
        if (!allowedTeamNames.contains(team.getName())) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void preventIllegalClick(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) {
            return;
        }
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        if (e.getCurrentItem().getItemMeta() == null) {
            return;
        }
        PersistentDataContainer container = e.getCurrentItem().getItemMeta().getPersistentDataContainer();
        if (!container.has(gameUtils.getNamespacedKey(), new GameItemStackTagType())) {
            return;
        }
        Team team = Bukkit.getScoreboardManager().getMainScoreboard().getPlayerTeam((Player) e.getWhoClicked());
        if (team == null) {
            e.setCancelled(true);
            return;
        }
        List<String> allowedTeamNames = container.get(gameUtils.getNamespacedKey(), new GameItemStackTagType()).canBeClickedByTeams;
        if (!allowedTeamNames.contains(team.getName())) {
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void preventIllegalInteract(PlayerInteractEvent e) {
        if (e.getItem() == null) {
            return;
        }
        PersistentDataContainer container = e.getItem().getItemMeta().getPersistentDataContainer();
        if (!container.has(gameUtils.getNamespacedKey(), new GameItemStackTagType())) {
            return;
        }
        Team team = Bukkit.getScoreboardManager().getMainScoreboard().getPlayerTeam(e.getPlayer());
        if (team == null) {
            e.setCancelled(true);
            return;
        }
        List<String> allowedTeamNames = container.get(gameUtils.getNamespacedKey(), new GameItemStackTagType()).canBeInteractedByTeams;
        if (!allowedTeamNames.contains(team.getName())) {
            e.setCancelled(true);
        }
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
        if (((Player)hbbee.getRemover()).getGameMode().equals(GameMode.ADVENTURE)) {
            hbbee.setCancelled(true);
        }
    }
    
    @EventHandler
    public void preventManipulatingArmorStand(PlayerArmorStandManipulateEvent pasme) {
        if (!c.getBoolean("no-armourstand-manipulation")) {
            return;
        }
        if (pasme.getPlayer().getGameMode().equals(GameMode.ADVENTURE)) {
            pasme.setCancelled(true);
        }
    }
    
    @EventHandler
    public void preventItemFrameRotation(PlayerInteractEntityEvent piee) {
        if (!c.getBoolean("no-item-frame-rotation")) {
            return;
        }
        if (!piee.getPlayer().getGameMode().equals(GameMode.CREATIVE) && (piee.getRightClicked() instanceof ItemFrame)) {
            if (ROTATABLE_ITEM_FRAME_UUID_STRINGS.contains(piee.getRightClicked().getUniqueId().toString())) {
                return;
            }
            piee.setCancelled(true);
        }
    }
    
    @EventHandler
    public void preventBlockInteraction(PlayerInteractEvent pie) {
        if (!c.getBoolean("no-block-interaction")) {
            return;
        }
        if (pie.getClickedBlock() == null) {
            return;
        }
        if (!pie.getPlayer().getGameMode().equals(GameMode.ADVENTURE)) {
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
    public void setStandInvulnerable(EntitySpawnEvent ese) {
        if (!c.getBoolean("invulnerable-armourstand-on-spawn")) {
            return;
        }
        if (ese.getEntity().getType().equals(EntityType.ARMOR_STAND)) {
            ese.getEntity().setInvulnerable(true);
        }
    }
    
    @EventHandler
    public void editPaintingAndItemFrame(HangingPlaceEvent hpe) {
        if (!c.getBoolean("invulnerable-painting-on-spawn")) {
            return;
        }
        switch (hpe.getEntity().getType()) {
            case PAINTING -> hpe.getEntity().setInvulnerable(true);
            case ITEM_FRAME -> {
                if (!hpe.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
                    ROTATABLE_ITEM_FRAME_UUID_STRINGS.add(hpe.getEntity().getUniqueId().toString());
                }
            }
            default -> {
            }
        }
    }
    
    @EventHandler
    public void cancelSpawn(CreatureSpawnEvent cse) { //防止鸡蛋生成鸡
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
    

    
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent ede) {
        if (!c.getBoolean("parkour-no-fall-damage")) {
            return;
        }
        if (ede.getEntity() instanceof Player) {
            Location l = ede.getEntity().getLocation();
            if (ede.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                if (l.getX() > -500 && l.getX() < 500 && l.getZ() > -500 && l.getZ() < 500) {
                    ede.setCancelled(true);
                }
            }
        }
    }
}
