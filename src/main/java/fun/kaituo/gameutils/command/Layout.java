package fun.kaituo.gameutils.command;

import fun.kaituo.gameutils.GameUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class Layout implements CommandExecutor {
    public static final String PERMISSION = "gameutils.command.layout";
    private final Location LAYOUT_LOCATION;

    public Layout() {
        LAYOUT_LOCATION = new Location(GameUtils.inst().getMainWorld(), -21, 17, 77, 180, 0);
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, Command cmd, @Nonnull String label, @Nonnull String[] args) {
        if (!cmd.getName().equalsIgnoreCase("layout")) {
            return false;
        }
        if (!sender.hasPermission(PERMISSION)) {
            sender.sendMessage("§c你没有权限执行这个指令！");
            return true;
        }
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§c此指令必须由玩家执行！");
            return true;
        }
        p.teleport(LAYOUT_LOCATION);
        p.sendMessage("§a成功传送到小游戏布局室！");
        return true;
    }
}
