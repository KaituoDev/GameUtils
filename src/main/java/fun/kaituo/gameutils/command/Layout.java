package fun.kaituo.gameutils.command;

import fun.kaituo.gameutils.GameUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * Command class to teleport the executor to the layout room.
 */
@SuppressWarnings("unused")
public class Layout extends GameUtilsCommand {
    /** {@inheritDoc} */
    @Override
    public String getName() {
        return "layout";
    }

    private final Location LAYOUT_LOCATION;

    /**
     * Constructor for Layout command.
     */
    public Layout() {
        LAYOUT_LOCATION = new Location(GameUtils.inst().getMainWorld(), -21, 17, 77, 180, 0);
    }

    /** {@inheritDoc} */
    @Override
    public boolean onCommand(@Nonnull CommandSender sender, Command cmd, @Nonnull String label, @Nonnull String[] args) {
        if (!cmd.getName().equalsIgnoreCase(getName())) {
            return false;
        }
        if (!sender.hasPermission(getPermissionString())) {
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
