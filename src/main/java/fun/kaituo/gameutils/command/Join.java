package fun.kaituo.gameutils.command;

import fun.kaituo.gameutils.GameUtils;
import fun.kaituo.gameutils.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Join class.</p>
 *
 * @author DELL
 */
@SuppressWarnings("unused")
public class Join extends GameUtilsCommand implements TabCompleter {
    /** {@inheritDoc} */
    @Override
    public String getName() {
        return "join";
    }

    /**
     * <p>getOthersPermissionString.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getOthersPermissionString() {
        return getPermissionString() + ".others";
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
        if (args.length != 1 && args.length != 2) {
            sender.sendMessage("§c指令参数错误！使用方法为/join <游戏名称> [玩家]");
            return true;
        }
        Player target;
        if (args.length == 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§c此指令必须由玩家执行！");
                return true;
            }
            target = (Player) sender;
        } else {
            if (!sender.hasPermission(getOthersPermissionString())) {
                sender.sendMessage("§c你没有权限执行这个指令！");
                return true;
            }
            target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage("§c未找到玩家 " + args[1] + "！");
                return true;
            }
        }
        String gameName = args[0];
        List<Game> matchingGames = new ArrayList<>();
        for (Game game: GameUtils.inst().getGames()) {
            if (game.getName().equalsIgnoreCase(gameName)) {
                matchingGames.add(game);
            }
        }
        if (matchingGames.isEmpty()) {
            sender.sendMessage("§c未找到名称为" + gameName + "的游戏！");
            return true;
        }
        if (matchingGames.size() > 1) {
            sender.sendMessage("§c找到多个名称为" + gameName + "的游戏，加入已取消！可能是游戏字母相同但大小写不同！");
            return true;
        }
        Game currentGame = GameUtils.inst().getGame(target);
        Game newGame = matchingGames.getFirst();
        if (currentGame == newGame) {
            sender.sendMessage("§c你/目标玩家已经在这个游戏中了！");
            return true;
        }
        currentGame.removePlayer(target);
        GameUtils.inst().join(target, newGame);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public List<String> onTabComplete(@Nonnull CommandSender commandSender, Command command, @Nonnull String alias, @Nonnull String[] args) {
        if (!command.getName().equalsIgnoreCase(getName())) {
            return new ArrayList<>();
        }
        if (args.length == 1) {
            List<String> gameNames = GameUtils.inst().getGames().stream().map(Game::getName).toList();
            return getMatchingCompletions(args[0], gameNames);
        } else if (args.length == 2) {
            List<String> playerNames = Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
            return getMatchingCompletions(args[1], playerNames);
        } else {
            return new ArrayList<>();
        }
    }
}
