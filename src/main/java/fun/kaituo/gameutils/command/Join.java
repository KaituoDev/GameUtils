package fun.kaituo.gameutils.command;

import fun.kaituo.gameutils.GameUtils;
import fun.kaituo.gameutils.game.Game;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Join implements CommandExecutor, TabCompleter {
    public static final String PERMISSION = "gameutils.command.join";

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, Command cmd, @Nonnull String label, @Nonnull String[] args) {
        if (!cmd.getName().equalsIgnoreCase("join")) {
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
        if (args.length != 1) {
            sender.sendMessage("§c指令参数错误！使用方法为/join <游戏名称>");
            return true;
        }
        String gameName = args[0];
        List<Game> matchingGames = new ArrayList<>();
        for (Game game: GameUtils.getInstance().getGames()) {
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
        Game currentGame = GameUtils.getInstance().getGame(p);
        Game newGame = matchingGames.getFirst();
        if (currentGame == newGame) {
            sender.sendMessage("§c你已经在这个游戏中了！");
            return true;
        }
        currentGame.getState().removePlayer(p);
        currentGame.removePlayer(p);
        GameUtils.getInstance().join(p, newGame);
        return true;
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender commandSender, Command command, @Nonnull String alias, @Nonnull String[] args) {
        if (!command.getName().equalsIgnoreCase("join")) {
            return new ArrayList<>();
        }
        if (args.length != 1) {
            return new ArrayList<>();
        }
        List<String> matchingNames = new ArrayList<>();
        for (Game game: GameUtils.getInstance().getGames()) {
            if (game.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                matchingNames.add(game.getName());
            }
        }
        matchingNames.sort(Comparator.naturalOrder());
        return matchingNames;
    }
}
