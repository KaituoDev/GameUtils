package fun.kaituo.gameutils.command;

import fun.kaituo.gameutils.util.HotbarMapping;
import fun.kaituo.gameutils.util.HotbarMappingManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Command class to let the executor set their hotbar mapping.
 */
@SuppressWarnings("unused")
public class MapHotbar extends GameUtilsCommand implements TabCompleter {
    private final List<String> completions = Arrays.asList("reset", "123456789");

    /**
     * Constructor for MapHotbar command.
     */
    public MapHotbar() {

    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return "maphotbar";
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
        if (args.length != 1) {
            sender.sendMessage("§c指令参数错误！使用方法为/maphotbar <映射(例如123456789)/reset>");
            return true;
        }
        if (args[0].equals("reset")) {
            HotbarMappingManager.INSTANCE.resetMapping(p.getUniqueId());
            sender.sendMessage("§a成功重置快捷栏映射！");
            return true;
        }
        String mapping = args[0];
        if (!HotbarMapping.isValidMapping(mapping)) {
            sender.sendMessage("§c映射不合法！映射应为1-9的数字组合！例如：123456789");
            return true;
        }
        HotbarMappingManager.INSTANCE.setMapping(p.getUniqueId(), mapping);
        sender.sendMessage("§a成功设置快捷栏映射为" + mapping + "！");
        return true;
    }
    /** {@inheritDoc} */
    @Override
    public List<String> onTabComplete(@Nonnull CommandSender commandSender, Command command, @Nonnull String alias, @Nonnull String[] args) {
        if (!command.getName().equalsIgnoreCase(getName())) {
            return new ArrayList<>();
        }
        if (args.length == 1) {
            return getMatchingCompletions(args[0], completions);
        }
        return new ArrayList<>();
    }
}
