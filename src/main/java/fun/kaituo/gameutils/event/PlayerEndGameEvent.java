package fun.kaituo.gameutils.event;

import fun.kaituo.gameutils.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import javax.annotation.Nonnull;

public class PlayerEndGameEvent extends PlayerEvent {
    private static final HandlerList HANDLERS = new HandlerList();
    Game game;
    
    
    public PlayerEndGameEvent(Player p, Game game) {
        super(p);
        this.game = game;
    }
    
    @SuppressWarnings("unused")
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
    
    @Override
    @Nonnull
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    @SuppressWarnings("unused")
    public Game getGame() {
        return game;
    }
}
