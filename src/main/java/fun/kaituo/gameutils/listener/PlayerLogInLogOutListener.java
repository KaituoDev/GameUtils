package fun.kaituo.gameutils.listener;

import fun.kaituo.gameutils.GameUtils;
import fun.kaituo.gameutils.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Represents a listener that handles {@link PlayerJoinEvent} and {@link PlayerQuitEvent}.
 * <p>
 * This class does not listen for players joining or quitting a {@link Game}!
 * It listens for players joining or quitting the server.
 */
public class PlayerLogInLogOutListener implements Listener {
    /**
     * Constructor for PlayerLogInLogOutListener.
     */
    public PlayerLogInLogOutListener() {

    }

    /**
     * Handles the {@link PlayerJoinEvent} when a player joins the server.
     *
     * @param e The {@link org.bukkit.event.player.PlayerJoinEvent} to handle.
     */
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

    /**
     * Handles the {@link PlayerQuitEvent} when a player quits the server.
     *
     * @param e The {@link org.bukkit.event.player.PlayerQuitEvent} to handle.
     */
    @EventHandler
    public void onLogOut(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        Game currentGame = GameUtils.inst().getGame(p);
        currentGame.removePlayer(p);
    }
}
