package fun.kaituo.gameutils;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("ConstantConditions")
public class GameUtilsTabCompleter implements TabCompleter {
    GameUtils gameUtils;
    List<String> biomeNames;
    List<String> modes;
    List<String> booleans;
    
    public GameUtilsTabCompleter(GameUtils gameUtils) {
        this.gameUtils = gameUtils;
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
    public List<String> onTabComplete(@Nonnull CommandSender commandSender, Command command, @Nonnull String alias, @Nonnull String[] args) {
        if (command.getName().equalsIgnoreCase("changebiome")) {
            if (args.length == 1) {
                return biomeNames.stream().filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase())).toList();
            } else if (args.length == 3) {
                return modes.stream().filter(s -> s.toLowerCase().startsWith(args[2].toLowerCase())).toList();
            }
        } else if (command.getName().equalsIgnoreCase("join")) {
            if (args.length == 1) {
                return gameUtils.getRegisteredGames().stream().map(Game::getName).filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList());
            }
        } else if (command.getName().equalsIgnoreCase("forcestop")) {
            if (args.length == 1) {
                return gameUtils.getRegisteredGames().stream().map(Game::getName).filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList());
            }
        } else if (command.getName().equalsIgnoreCase("rotatable")) {
            if (!(commandSender instanceof Player p)) {
                return new ArrayList<>();
            }
            RayTraceResult result = p.rayTraceBlocks(5, FluidCollisionMode.NEVER);
            assert result != null;
            Location loc = switch (result.getHitBlockFace()) {
                case EAST -> result.getHitBlock().getLocation().add(1, 0, 0);
                case WEST -> result.getHitBlock().getLocation().add(-1, 0, 0);
                case NORTH -> result.getHitBlock().getLocation().add(0, 0, -1);
                case SOUTH -> result.getHitBlock().getLocation().add(0, 0, 1);
                case UP -> result.getHitBlock().getLocation().add(0, 1, 0);
                case DOWN -> result.getHitBlock().getLocation().add(0, -1, 0);
                default -> result.getHitBlock().getLocation();
            };
            String locStr = loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ();
            List<String> locations = new ArrayList<>();
            locations.add(locStr);
            return switch (args.length) {
                case 1 -> locations.stream().filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
                case 2 ->
                        locations.stream().filter(s -> s.startsWith(args[0] + " " + args[1])).collect(Collectors.toList());
                case 3 ->
                        locations.stream().filter(s -> s.startsWith(args[0] + " " + args[1] + " " + args[2])).collect(Collectors.toList());
                case 4 -> booleans.stream().filter(s -> s.startsWith(args[3])).collect(Collectors.toList());
                default -> new ArrayList<>();
            };
        }
        return null;
    }
}
