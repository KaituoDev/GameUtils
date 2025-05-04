package fun.kaituo.gameutils.command;

import org.bukkit.command.CommandExecutor;

import java.util.List;

/**
 * Abstract parent class for all GameUtils commands.
 */
public abstract class GameUtilsCommand implements CommandExecutor {
    /**
     * Constructor for GameUtilsCommand.
     */
    public GameUtilsCommand() {}

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
     * Returns the permission required to execute the command based on its name, as a string.
     *
     * @return The command's required permission string.
     */
    public String getPermissionString() {
        return PERMISSION_PREFIX + getName();
    }

    /**
     * Returns the matching completions for a given partial argument from a list of completions.
     *
     * @param partialArg The partial argument to match against.
     * @param completions A list of possible completions.
     * @return A list of completions that match the partial argument.
     * @since 2.0.1
     */
    protected List<String> getMatchingCompletions(String partialArg, List<String> completions) {
        return completions.stream().filter(completion ->
                completion.toLowerCase().startsWith(partialArg.toLowerCase())).toList();
    }
}
