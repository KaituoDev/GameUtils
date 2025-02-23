package fun.kaituo.gameutils.command;

import fun.kaituo.gameutils.GameUtils;
import fun.kaituo.gameutils.game.Game;
import fun.kaituo.gameutils.util.GameInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameInv implements CommandExecutor, TabCompleter {
    public static final String PERMISSION = "gameutils.command.gameinv";
    private final List<String> actions = Arrays.asList("save", "apply", "remove");

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, Command cmd, @Nonnull String label, @Nonnull String[] args) {
        if (!cmd.getName().equalsIgnoreCase("gameinv")) {
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
        if (args.length != 2) {
            sender.sendMessage("§c指令参数错误！使用方法为/gameinv <save/apply/remove> <背包ID>");
            return true;
        }
        if (!args[0].equalsIgnoreCase("save") &&
            !args[0].equalsIgnoreCase("apply") &&
            !args[0].equalsIgnoreCase("remove")) {
            sender.sendMessage("§c指令参数错误！使用方法为/gameinv <save/apply/remove> <背包ID>");
            return true;
        }
        Game game = GameUtils.inst().getGame(p);
        if (args[0].equalsIgnoreCase("apply")) {
            GameInventory inv = game.getInv(args[1]);
            if (inv == null) {
                sender.sendMessage("§c未找到ID为 " + args[1] + " 的背包！");
                return true;
            }
            inv.apply(p);
            p.sendMessage("§a成功应用ID为 " + args[1] + " 的背包！");
        } else if (args[0].equalsIgnoreCase("save")) {
            GameInventory inv = new GameInventory(p);
            game.saveInv(args[1], inv);
            p.sendMessage("§a成功将当前背包保存到 " + game.getName() + " 的背包列表，ID为 " + args[1]);
        } else if (args[0].equalsIgnoreCase("remove")) {
            if (game.removeInv(args[1])) {
                p.sendMessage("§a成功移除ID为 " + args[1] + " 的背包！");
            } else {
                p.sendMessage("§c未找到ID为 " + args[1] + " 的背包！");
            }
        }
        return true;
    }

    @Override

    public List<String> onTabComplete(@Nonnull CommandSender sender, Command command, @Nonnull String alias, @Nonnull String[] args) {
        if (!command.getName().equalsIgnoreCase("gameinv")) {
            return new ArrayList<>();
        }
        if (args.length == 1) {
            return actions;
        } else if (args.length == 2) {
            Game game = GameUtils.inst().getGame((Player) sender);
            return new ArrayList<>(game.getInvIds());
        }
        return new ArrayList<>();
    }
}
