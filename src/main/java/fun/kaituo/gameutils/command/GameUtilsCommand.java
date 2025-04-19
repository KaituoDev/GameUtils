package fun.kaituo.gameutils.command;

import org.bukkit.command.CommandExecutor;

import java.util.List;

/**
 * An implementation of the {@link CommandExecutor}, with custom permission strings.
 */
public abstract class GameUtilsCommand implements CommandExecutor {
    /**
     * The global permission prefix for GameUtils commands.
     */
    public static final String PERMISSION_PREFIX = "gameutils.command.";

    /**
     * Returns the command's name, used for permission check.
     *
     * @return The command's name.
     * @see #getPermissionString()
     */
    public abstract String getName();

    /**
     * Returns the permission required to execute the command based on its name.
     *
     * @return The command's required permission string.
     */
    public String getPermissionString() {
        return PERMISSION_PREFIX + getName();
    }

    protected List<String> getMatchingCompletions(String partialArg, List<String> completions) {
        return completions.stream().filter(completion ->
                completion.toLowerCase().startsWith(partialArg.toLowerCase())).toList();
    }
}
