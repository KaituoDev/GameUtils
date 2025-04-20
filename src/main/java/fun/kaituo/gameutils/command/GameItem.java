package fun.kaituo.gameutils.command;

import fun.kaituo.gameutils.GameUtils;
import fun.kaituo.gameutils.game.Game;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>GameItem class.</p>
 *
 * @author DELL
 */
@SuppressWarnings("unused")
public class GameItem extends GameUtilsCommand implements TabCompleter {
    /** {@inheritDoc} */
    @Override
    public String getName() {
        return "gameitem";
    }

    private final List<String> actions = Arrays.asList("save", "get", "remove");

    /** {@inheritDoc} */
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
        if (args.length != 2) {
            sender.sendMessage("§c指令参数错误！使用方法为/gameitem <save/get/remove> <物品ID>");
            return true;
        }
        if (!args[0].equalsIgnoreCase("save") &&
            !args[0].equalsIgnoreCase("get") &&
            !args[0].equalsIgnoreCase("remove")) {
            sender.sendMessage("§c指令参数错误！使用方法为/gameitem <save/get/remove> <物品ID>");
            return true;

        }
        Game game = GameUtils.inst().getGame(p);
        if (args[0].equalsIgnoreCase("get")) {
            ItemStack item = game.getItem(args[1]);
            if (item == null) {
                sender.sendMessage("§c未找到ID为 " + args[1] + " 的物品！");
                return true;
            }
            p.getInventory().addItem(item);
            p.sendMessage("§a成功获取ID为 " + args[1] + " 的物品！");
        } else if (args[0].equalsIgnoreCase("save")) {
            ItemStack handItem = p.getInventory().getItem(EquipmentSlot.HAND);
            if (handItem == null) {
                sender.sendMessage("§c你的手上没有物品！");
                return true;
            }
            if (handItem.getType().equals(Material.AIR)) {
                sender.sendMessage("§c你的手上没有物品！");
                return true;
            }
            game.saveItem(args[1], handItem);
            p.sendMessage("§a成功将手中物品保存到 " + game.getName() + " 的物品列表，ID为 " + args[1]);
        } else if (args[0].equalsIgnoreCase("remove")) {
            if (game.removeItem(args[1])) {
                p.sendMessage("§a成功移除ID为 " + args[1] + " 的物品！");
            } else {
                p.sendMessage("§c未找到ID为 " + args[1] + " 的物品！");
            }
        }
        return true;
    }

    /**
     * <p>onTabComplete.</p>
     *
     * @param sender a {@link org.bukkit.command.CommandSender} object
     * @param command a {@link org.bukkit.command.Command} object
     * @param alias a {@link java.lang.String} object
     * @param args an array of {@link java.lang.String} objects
     * @return a {@link java.util.List} object
     */
    public List<String> onTabComplete(@Nonnull CommandSender sender, Command command, @Nonnull String alias, @Nonnull String[] args) {
        if (!command.getName().equalsIgnoreCase(getName())) {
            return new ArrayList<>();
        }
        if (args.length == 1) {
            return getMatchingCompletions(args[0], actions);
        } else if (args.length == 2) {
            Game game = GameUtils.inst().getGame((Player) sender);
            return getMatchingCompletions(args[1], new ArrayList<>(game.getItemIds()));
        }
        return new ArrayList<>();
    }
}
