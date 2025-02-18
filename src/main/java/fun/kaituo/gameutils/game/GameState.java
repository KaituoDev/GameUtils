package fun.kaituo.gameutils.game;

import org.bukkit.entity.Player;

public interface GameState {
    // Called once when this state is entered
    void enter();
    // Called once when exiting from this state
    void exit();

    // Called every tick. Use this function to transit to another state
    void tick();

    void addPlayer(Player p);

    void removePlayer(Player p);

    void forceStop();
}
