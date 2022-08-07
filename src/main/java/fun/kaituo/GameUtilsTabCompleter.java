package fun.kaituo;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GameUtilsTabCompleter implements TabCompleter {
    List<String> biomeNames;
    List<String> modes;
    List<String> booleans;
    
    public GameUtilsTabCompleter() {
        biomeNames = new ArrayList<>();
        modes = new ArrayList<>();
        booleans = new ArrayList<>();
        biomeNames.add("auto");
        for (Biome b : Biome.values()) {
            biomeNames.add(b.name().toLowerCase());
        }
        modes.add("circular");
        modes.add("square");
        booleans.add("true");
        booleans.add("false");
    }
    
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("changebiome")) {
            if (args.length == 1) {
                return biomeNames;
            } else if (args.length == 3) {
                return modes;
            }
        } else if (command.getName().equalsIgnoreCase("changegame")) {
            if (args.length == 1) {
                return (List<String>)GameUtils.getRegisteredGames(true);
            }
        } else if (command.getName().equalsIgnoreCase("rotatable")) {
            if (!(commandSender instanceof Player)) {
                return new ArrayList<>();
            }
            Player p = (Player)commandSender;
            RayTraceResult result = p.rayTraceBlocks(5, FluidCollisionMode.NEVER);
            Location loc;
            switch (result.getHitBlockFace()) {
                case EAST:
                    loc = result.getHitBlock().getLocation().add(1, 0, 0);
                    break;
                case WEST:
                    loc = result.getHitBlock().getLocation().add(-1, 0, 0);
                    break;
                case NORTH:
                    loc = result.getHitBlock().getLocation().add(0, 0, -1);
                    break;
                case SOUTH:
                    loc = result.getHitBlock().getLocation().add(0, 0, 1);
                    break;
                case UP:
                    loc = result.getHitBlock().getLocation().add(0, 1, 0);
                    break;
                case DOWN:
                    loc = result.getHitBlock().getLocation().add(0, -1, 0);
                    break;
                default:
                    loc = result.getHitBlock().getLocation();
                    break;
            }
            String locStr = loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ();
            List<String> locations = new ArrayList<>();
            locations.add(locStr);
            switch (args.length) {
                case 1:
                    return locations.stream().filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
                case 2:
                    return locations.stream().filter(s -> s.startsWith(args[0] + " " + args[1])).collect(Collectors.toList());
                case 3:
                    return locations.stream().filter(s -> s.startsWith(args[0] + " " + args[1] + " " + args[2])).collect(Collectors.toList());
                case 4:
                    return booleans.stream().filter(s -> s.startsWith(args[3])).collect(Collectors.toList());
                default:
                    return new ArrayList<>();
            }
        }
        return null;
    }
}
