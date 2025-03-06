package fun.kaituo.gameutils.command;

import org.bukkit.command.CommandExecutor;

public abstract class GameUtilsCommand implements CommandExecutor {
    public static final String PERMISSION_PREFIX = "gameutils.command.";
    public abstract String getName();

    public String getPermissionString() {
        return PERMISSION_PREFIX + getName();
    }
}
