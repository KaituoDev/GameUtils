package fun.kaituo.gameutils.command;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Directional;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

import javax.annotation.Nonnull;

public class PlaceStand implements CommandExecutor {
    public static final String PERMISSION = "gameutils.command.placestand";
    @Override
    public boolean onCommand(@Nonnull CommandSender sender, Command cmd, @Nonnull String label, @Nonnull String[] args) {
        if (!cmd.getName().equalsIgnoreCase("placestand")) {
            return false;
        }
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§c此指令必须由玩家执行！");
            return true;
        }
        if (!sender.hasPermission(PERMISSION)) {
            sender.sendMessage("§c你没有权限执行这个指令！");
            return true;
        }
        RayTraceResult result = p.rayTraceBlocks(5, FluidCollisionMode.NEVER);
        if (result == null) {
            sender.sendMessage("§c请面对橡木按钮！");
            return true;
        }
        Block b = result.getHitBlock();
        if (b == null) {
            sender.sendMessage("§c请面对橡木按钮！");
            return true;
        }
        if (!b.getType().equals(Material.OAK_BUTTON)) {
            sender.sendMessage("§c请面对橡木按钮！");
            return true;
        }

        Location l = b.getLocation().clone();
        l.setX(l.getX() + 0.5);
        l.setY(l.getY() - 1);
        l.setZ(l.getZ() + 0.5);
        switch (((Directional) b.getBlockData()).getFacing()) {
            case NORTH -> l.setZ(l.getZ() + 0.2);
            case SOUTH -> l.setZ(l.getZ() - 0.2);
            case EAST -> l.setX(l.getX() - 0.2);
            case WEST -> l.setX(l.getX() + 0.2);
            case UP, DOWN -> {
                sender.sendMessage("§c按钮方向不可为向上或向下！");
                return true;
            }
        }
        ArmorStand stand = (ArmorStand) p.getWorld().spawnEntity(l, EntityType.ARMOR_STAND);
        stand.setInvisible(true);
        stand.setCustomName("开始游戏");
        stand.setGravity(false);
        stand.setInvulnerable(true);
        return true;
    }
}
