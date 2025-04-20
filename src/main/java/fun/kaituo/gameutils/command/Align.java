package fun.kaituo.gameutils.command;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class Align extends GameUtilsCommand {
    public static final float PITCH_ALIGN_THRESHOLD = 20f;
    @Override
    public String getName() {
        return "align";
    }


    @Override
    public boolean onCommand(@Nonnull CommandSender sender, Command cmd, @Nonnull String label, @Nonnull String[] args) {
        if (!cmd.getName().equalsIgnoreCase(getName())) {
            return false;
        }

        if (!(sender instanceof Player p)) {
            sender.sendMessage("§c此指令必须由玩家执行！");
            return true;
        }
        if (!sender.hasPermission(getPermissionString())) {
            sender.sendMessage("§c你没有权限执行这个指令！");
            return true;
        }
        if (args.length != 0) {
            sender.sendMessage("§c指令参数错误！使用方法为/align");
            return true;
        }
        Location loc = p.getLocation();
        double targetX = Math.floor(loc.getX()) + 0.5;
        double targetY = loc.getY();
        double targetZ = Math.floor(loc.getZ()) + 0.5;

        float targetYaw = loc.getYaw();
        targetYaw = Math.round(targetYaw / 45f) * 45f;

        float targetPitch = loc.getPitch();
        if (targetPitch < -90 + PITCH_ALIGN_THRESHOLD) {
            targetPitch = -90;
        } else if (targetPitch > 90 - PITCH_ALIGN_THRESHOLD) {
            targetPitch = 90;
        } else {
            targetPitch = 0;
        }
        p.teleport(new Location(loc.getWorld(), targetX, targetY, targetZ, targetYaw, targetPitch));
        p.sendMessage("§a已对齐当前位置");
        return true;
    }
}
