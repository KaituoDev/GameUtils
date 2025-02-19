package fun.kaituo.gameutils.command;

import fun.kaituo.gameutils.GameUtils;
import fun.kaituo.gameutils.game.Game;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ForceStop implements CommandExecutor, TabCompleter {
    public static final String PERMISSION = "gameutils.command.forcestop";

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, Command cmd, @Nonnull String label, @Nonnull String[] args) {
        if (!cmd.getName().equalsIgnoreCase("forcestop")) {
            return false;
        }
        if (!sender.hasPermission(PERMISSION)) {
            sender.sendMessage("§c你没有权限执行这个指令！");
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage("§c指令参数错误！使用方法为/forcestop <游戏名称>");
            return true;
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
            sender.sendMessage("§c找到多个名称为" + gameName + "的游戏，强制停止未执行！可能是游戏字母相同但大小写不同！");
            return true;
        }
        matchingGames.getFirst().getState().forceStop();
        matchingGames.getFirst().forceStop();
        return true;
    }


    @Override
    public List<String> onTabComplete(@Nonnull CommandSender commandSender, Command command, @Nonnull String alias, @Nonnull String[] args) {
        if (!command.getName().equalsIgnoreCase("forcestop")) {
            return new ArrayList<>();
        }
        if (args.length != 1) {
            return new ArrayList<>();
        }
        List<String> matchingNames = new ArrayList<>();
        for (Game game: GameUtils.inst().getGames()) {
            if (game.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                matchingNames.add(game.getName());
            }
        }
        matchingNames.sort(Comparator.naturalOrder());
        return matchingNames;
    }
}
