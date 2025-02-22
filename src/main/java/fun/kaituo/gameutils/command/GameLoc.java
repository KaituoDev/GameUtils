package fun.kaituo.gameutils.command;

import fun.kaituo.gameutils.GameUtils;
import fun.kaituo.gameutils.game.Game;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameLoc implements CommandExecutor, TabCompleter {

    public static final String PERMISSION = "gameutils.command.gameloc";
    private final List<String> actions = Arrays.asList("save", "tp", "remove");


    @Override
    public boolean onCommand(@Nonnull CommandSender sender, Command cmd, @Nonnull String label, @Nonnull String[] args) {
        if (!cmd.getName().equalsIgnoreCase("gameloc")) {
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
            sender.sendMessage("§c指令参数错误！使用方法为/gameloc <save/get/remove> <位置ID>");
            return true;
        }
        if (!args[0].equalsIgnoreCase("save") &&
            !args[0].equalsIgnoreCase("tp") &&
            !args[0].equalsIgnoreCase("remove")) {
            sender.sendMessage("§c指令参数错误！使用方法为/gameloc <save/get/remove> <位置ID>");
            return true;

        }
        Game game = GameUtils.inst().getGame(p);
        if (args[0].equalsIgnoreCase("tp")) {
            Location loc = game.getLoc(args[1]);
            if (loc == null) {
                sender.sendMessage("§c未找到ID为 " + args[1] + " 的位置！");
                return true;
            }
            p.teleport(loc);
            p.sendMessage("§a成功传送到ID为 " + args[1] + " 的位置！");
        } else if (args[0].equalsIgnoreCase("save")) {
            game.saveLoc(args[1], p.getLocation());
            p.sendMessage("§a成功将当前位置保存到 " + game.getName() + " 的位置列表，ID为 " + args[1]);
        } else if (args[0].equalsIgnoreCase("remove")) {
            if (game.removeLoc(args[1])) {
                p.sendMessage("§a成功移除ID为 " + args[1] + " 的位置！");
            } else {
                p.sendMessage("§c未找到ID为 " + args[1] + " 的位置！");
            }
        }
        return true;
    }

    public List<String> onTabComplete(@Nonnull CommandSender sender, Command command, @Nonnull String alias, @Nonnull String[] args) {
        if (!command.getName().equalsIgnoreCase("gameloc")) {
            return new ArrayList<>();
        }
        if (args.length == 1) {
            return actions;
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("remove")) {
                Game game = GameUtils.inst().getGame((Player) sender);
                return new ArrayList<>(game.getLocIds());
            }
        }
        return new ArrayList<>();
    }
}
