package fun.kaituo.gameutils.command;

import fun.kaituo.gameutils.GameUtils;
import fun.kaituo.gameutils.game.Game;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Command class to manage game locations.
 */
@SuppressWarnings("unused")
public class GameLoc extends GameUtilsCommand implements TabCompleter {
    /**
     * Constructor for GameLoc command.
     */
    public GameLoc() {

    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return "gameloc";
    }

    private final List<String> actions = Arrays.asList("save", "tp", "remove");
    private final List<String> saveModes = Arrays.asList("block", "player");

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
        if (args.length != 2 && args.length != 3) {
            sender.sendMessage("§c指令参数错误！使用方法为/gameloc <save/tp/remove> <位置ID> [block/player]");
            return true;
        }
        if (!args[0].equalsIgnoreCase("save") &&
            !args[0].equalsIgnoreCase("tp") &&
            !args[0].equalsIgnoreCase("remove")) {
            sender.sendMessage("§c指令参数错误！使用方法为/gameloc <save/tp/remove> <位置ID> [block/player]");
            return true;
        }
        Game game = GameUtils.inst().getGame(p);
        if (args[0].equalsIgnoreCase("tp")) {
            if (args.length == 3) {
                sender.sendMessage("§c指令参数错误！使用方法为/gameloc tp <位置ID>");
                return true;
            }
            Location loc = game.getLoc(args[1]);
            if (loc == null) {
                sender.sendMessage("§c未找到ID为 " + args[1] + " 的位置！");
                return true;
            }
            p.teleport(loc);
            p.sendMessage("§a成功传送到ID为 " + args[1] + " 的位置！");
        } else if (args[0].equalsIgnoreCase("save")) {
            boolean blockMode = false;
            if (args.length == 3) {
                if (args[2].equalsIgnoreCase("block")) {
                    blockMode = true;
                }
            }
            Location loc;
            if (blockMode) {
                Block b = p.getTargetBlockExact(5);
                if (b == null) {
                    p.sendMessage("§c未找到目标方块！");
                    return true;
                }
                loc = b.getLocation();
                p.sendMessage("§f目标方块为 " + b.getType().name());
            } else {
                loc = p.getLocation();
            }
            game.saveLoc(args[1], loc);
            p.sendMessage("§a成功将当前位置保存到 " + game.getName() + " 的位置列表，ID为 " + args[1]);
        } else if (args[0].equalsIgnoreCase("remove")) {
            if (args.length == 3) {
                sender.sendMessage("§c指令参数错误！使用方法为/gameloc remove <位置ID>");
                return true;
            }
            if (game.removeLoc(args[1])) {
                p.sendMessage("§a成功移除ID为 " + args[1] + " 的位置！");
            } else {
                p.sendMessage("§c未找到ID为 " + args[1] + " 的位置！");
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
            return getMatchingCompletions(args[1], new ArrayList<>(game.getLocIds()));
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("save")) {
                return getMatchingCompletions(args[2], saveModes);
            }
        }
        return new ArrayList<>();
    }
}
