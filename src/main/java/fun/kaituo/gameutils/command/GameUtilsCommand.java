package fun.kaituo.gameutils.command;

import org.bukkit.command.CommandExecutor;

import java.util.List;

/**
 * An implementation of the {@link org.bukkit.command.CommandExecutor}, with custom permission strings.
 *
 * @author DELL
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

    /**
     * <p>getMatchingCompletions.</p>
     *
     * @param partialArg a {@link java.lang.String} object
     * @param completions a {@link java.util.List} object
     * @return a {@link java.util.List} object
     * @since 2.0.1
     */
    protected List<String> getMatchingCompletions(String partialArg, List<String> completions) {
        return completions.stream().filter(completion ->
                completion.toLowerCase().startsWith(partialArg.toLowerCase())).toList();
    }
}
