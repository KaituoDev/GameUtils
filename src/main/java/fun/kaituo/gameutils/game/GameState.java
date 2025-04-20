package fun.kaituo.gameutils.game;

import org.bukkit.entity.Player;

/**
 * Represents a state that the game can be in.
 *
 * @author DELL
 */
public interface GameState {

    /**
     * Enters the state.
     * <p>
     * This method is called once automatically when the game enters
     * the state using {@link fun.kaituo.gameutils.game.Game#setState(GameState)}.
     */
    void enter();

    /**
     * Exits from the state.
     * <p>
     * This method is called once automatically when the game exits
     * from the state using {@link fun.kaituo.gameutils.game.Game#setState(GameState)}.
     */
    void exit();

    /**
     * Performs the per tick action of the state.
     * <p>
     * This method is called every tick automatically when the state
     * is active.
     */
    void tick();

    /**
     * Puts a player under this state's management.
     * <p>
     * When the state is active, this method will be called
     * automatically when a player executes the <code>/join</code> command,
     * is invited through <code>/joinall</code>, or rejoins the game.
     * <p>
     * This method <b><u>will not</u></b> be called automatically when the
     * game enters the state.
     *
     * @param p The player to be controlled.
     */
    void addPlayer(Player p);

    /**
     * Removes a player from this state's management.
     * <p>
     * When the state is active, this method will be called
     * automatically when a player executes the <code>/join</code> command to
     * join another game or leaves the server.
     * <p>
     * This method <b><u>will not</u></b> be called automatically when the
     * game exits from the state.
     *
     * @param p The player to be freed.
     */
    void removePlayer(Player p);

    /**
     * Forcefully stops the state from performing any action.
     * <p>
     * This method is called when someone executes the
     * <code>/forcestop</code> command.
     */
    void forceStop();
}
