package fun.kaituo.gameutils.command;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static fun.kaituo.gameutils.util.Misc.*;

@SuppressWarnings("unused")
public class Droppable extends GameUtilsCommand implements TabCompleter {
    @Override
    public String getName() {
        return "droppable";
    }

    private final List<String> booleans = Arrays.asList("true", "false");

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
        if (args.length > 1) {
            sender.sendMessage("§c指令参数错误！使用方法为/droppable [true/false]");
            return true;
        }
        if (args.length == 1 && !booleans.contains(args[0].toLowerCase())) {
            sender.sendMessage("§c指令参数错误！使用方法为/droppable [true/false]");
            return true;
        }
        ItemStack item = p.getInventory().getItemInMainHand().clone();
        if (item.getType().equals(Material.AIR)) {
            p.sendMessage("§c请使用主手手持物品！");
            return true;
        }
        boolean droppable;
        if (args.length == 0) {
            droppable = !isDroppable(item);
        } else {
            droppable = Boolean.parseBoolean(args[0]);
        }
        setDroppable(item, droppable);
        p.getInventory().setItemInMainHand(item);

        if (droppable) {
            p.sendMessage("§f已设置物品为§a可丢弃");
        } else {
            p.sendMessage("§f已设置物品为§c不可丢弃");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender commandSender, Command command, @Nonnull String alias, @Nonnull String[] args) {
        if (!command.getName().equalsIgnoreCase(getName())) {
            return new ArrayList<>();
        }
        if (args.length != 1) {
            return new ArrayList<>();
        }
        return getMatchingCompletions(args[0], booleans);
    }
}
