package fun.kaituo.gameutils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Lobby extends Game {
    private static final Lobby instance = new Lobby((GameUtils) Bukkit.getPluginManager().getPlugin("GameUtils"));

    public static Lobby getInstance() {
        return instance;
    }

    private Lobby(GameUtils plugin) {
        this.plugin = plugin;
        initializeGame(plugin, "Lobby", "§3空闲", new Location(Bukkit.getWorld("world"), 0.5, 89, 0.5, 0, 0));
    }

    @Override
    protected void quit(Player p) {}

    @Override
    protected boolean rejoin(Player p) {return true;}

    @Override
    protected boolean join(Player p) {
        p.setBedSpawnLocation(hubLocation, true);
        p.teleport(hubLocation);
        return true;
    }

    @Override
    protected void forceStop() {}
}
