package fun.kaituo.gameutils.game;

import org.bukkit.entity.Player;

public interface GameState {
    // Called once when this state is entered
    void enter();
    // Called once when exiting from this state
    void exit();

    // Called when a player joins the game
    void join(Player p);
    // Called when a player quits the game
    void quit(Player p);

    void forceStop();

    // Called every tick. Use this function to transit to another state
    void tick();
}
