package fun.kaituo.gameutils.listener;

import fun.kaituo.gameutils.GameUtils;
import fun.kaituo.gameutils.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

// Note: This class does not listen for joining or quitting a GAME!
// It listens for players joining or quitting the SERVER.
public class PlayerLogInLogOutListener implements Listener {

    @EventHandler
    public void onLogIn(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (GameUtils.getInstance().isPlayerFirstTimeJoin(p)) {
            GameUtils.getInstance().join(p, GameUtils.getInstance().getLobby());
        } else {
            Game currentGame = GameUtils.getInstance().getGame(p);
            currentGame.getState().addPlayer(p);
        }

        GameUtils.getInstance().join(p, GameUtils.getInstance().getLobby());
        // Send a welcome message to the player after 1 second
        Bukkit.getScheduler().runTaskLater(GameUtils.getInstance(), () -> {
            if (!p.isOnline()) {
                return;
            }
            p.sendMessage("§6欢迎， §e" + p.getName() + "§6！");
        }, 20);
    }

    @EventHandler
    public void onLogOut(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        Game currentGame = GameUtils.getInstance().getGame(p);
        currentGame.getState().removePlayer(p);
    }
}
