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
        if (GameUtils.inst().isPlayerFirstTimeJoin(p)) {
            GameUtils.inst().join(p, GameUtils.inst().getLobby());
        } else {
            Game currentGame = GameUtils.inst().getGame(p);
            currentGame.addPlayer(p);
        }

        // Send a welcome message to the player after 1 second
        Bukkit.getScheduler().runTaskLater(GameUtils.inst(), () -> {
            if (!p.isOnline()) {
                return;
            }
            p.sendMessage("§6欢迎， §e" + p.getName() + "§6！");
        }, 20);
    }

    @EventHandler
    public void onLogOut(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        Game currentGame = GameUtils.inst().getGame(p);
        currentGame.removePlayer(p);
    }
}
