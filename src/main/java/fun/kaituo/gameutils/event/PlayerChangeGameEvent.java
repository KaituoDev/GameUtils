package fun.kaituo.gameutils.event;

import fun.kaituo.gameutils.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
public class PlayerChangeGameEvent extends PlayerEvent {
    private static final HandlerList HANDLERS = new HandlerList();
    Game fromGame;
    Game toGame;
    
    
    public PlayerChangeGameEvent(Player p, Game fromGame, Game toGame) {
        super(p);
        this.fromGame = fromGame;
        this.toGame = toGame;
    }
    
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
    
    @Override
    @Nonnull
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    
    public Game getFromGame() {
        return fromGame;
    }
    
    public Game getToGame() {
        return toGame;
    }
}
