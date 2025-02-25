package fun.kaituo.gameutils.command;

import fun.kaituo.gameutils.GameUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static fun.kaituo.gameutils.util.Misc.getMatchingCompletions;

public class ChangeBiome implements CommandExecutor, TabCompleter {
    public static final String PERMISSION = "gameutils.command.changebiome";
    private final List<String> biomeNames = new ArrayList<>();
    private final List<String> modes = Arrays.asList("circular", "square");
    private final List<String> exampleRadii = Arrays.asList("16", "32", "64", "128", "256");

    public ChangeBiome() {
        biomeNames.add("auto");
        for (Biome b : Biome.values()) {
            biomeNames.add(b.name().toLowerCase());
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(@Nonnull CommandSender sender, Command cmd, @Nonnull String label, @Nonnull String[] args) {
        if (!cmd.getName().equalsIgnoreCase("changebiome")) {
            return false;
        }
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§c此指令必须由玩家执行！");
            return true;
        }
        if (!sender.hasPermission(PERMISSION)) {
            sender.sendMessage("§c你没有权限执行这个指令！");
            return true;
        }
        if (args.length != 3) {
            sender.sendMessage("§c指令参数错误！使用方法为/changebiome <biome>/auto radius <circular/square>");
            return true;
        }
        Location l = p.getLocation();
        int x = (int) Math.floor(l.getX());
        int z = (int) Math.floor(l.getZ());
        int radius = Integer.parseInt(args[1]);
        int r = radius - 1;
        if (!args[2].equalsIgnoreCase("circular") && !args[2].equalsIgnoreCase("square")) {
            sender.sendMessage("§c指令参数错误！使用方法为/changebiome <biome>/auto radius <circular/square> ！");
            return true;
        }
        boolean isCircular = args[2].equalsIgnoreCase("circular");

        if (!args[0].equalsIgnoreCase("auto")) {
            for (int xOffset = -r; xOffset <= r; xOffset++) {
                if (xOffset == Math.round(-r / 2D)) {
                    Bukkit.broadcastMessage("§a[changebiome] §f服务器主线程冻结中，生物群系设置操作进度为 25%");
                } else if (xOffset == 0) {
                    Bukkit.broadcastMessage("§a[changebiome] §f服务器主线程冻结中，生物群系设置操作进度为 50%");
                } else if (xOffset == Math.round(r / 2D)) {
                    Bukkit.broadcastMessage("§a[changebiome] §f服务器主线程冻结中，生物群系设置操作进度为 75%");
                }
                for (int zOffset = -r; zOffset <= r; zOffset++) {
                    if (isCircular) {
                        if (Math.sqrt(Math.pow(xOffset, 2) + Math.pow(zOffset, 2)) > r) {
                            continue;
                        }
                    }
                    p.getWorld().setBiome((x + xOffset), (z + zOffset), Biome.valueOf(args[0].toUpperCase()));
                }
            }
            Bukkit.broadcastMessage("§a[changebiome] §f生物群系设置操作完成！");
            return true;
        } else {
            for (int xOffset = -r; xOffset <= r; xOffset++) {
                if (xOffset == Math.round(-r / 2D)) {
                    Bukkit.broadcastMessage("§a[changebiome] §f服务器主线程冻结中，生物群系设置操作进度为 25%");
                } else if (xOffset == 0) {
                    Bukkit.broadcastMessage("§a[changebiome] §f服务器主线程冻结中，生物群系设置操作进度为 50%");
                } else if (xOffset == Math.round(r / 2D)) {
                    Bukkit.broadcastMessage("§a[changebiome] §f服务器主线程冻结中，生物群系设置操作进度为 75%");
                }
                for (int zOffset = -r; zOffset <= r; zOffset++) {
                    if (isCircular) {
                        if (Math.sqrt(Math.pow(xOffset, 2) + Math.pow(zOffset, 2)) > r) {
                            continue;
                        }
                    }
                    int k = 319;
                    boolean isSurfaceBlockObtained = false;
                    while (!isSurfaceBlockObtained) {
                        if (p.getWorld().getBlockAt((x + xOffset), k, (z + zOffset)).getType().equals(Material.WATER)) {
                            isSurfaceBlockObtained = true;
                            p.getWorld().setBiome((x + xOffset), (z + zOffset), Biome.RIVER);
                        } else if (p.getWorld().getBlockAt((x + xOffset), k, (z + zOffset)).getType().equals(Material.LAVA)) {
                            isSurfaceBlockObtained = true;
                            p.getWorld().setBiome((x + xOffset), (z + zOffset), Biome.BADLANDS);
                        } else if (p.getWorld().getBlockAt((x + xOffset), k, (z + zOffset)).getType().isSolid()) {
                            isSurfaceBlockObtained = true;
                            FileConfiguration config = GameUtils.inst().getConfig();
                            Material material = p.getWorld().getBlockAt((x + xOffset), k, (z + zOffset)).getType();
                            if (config.contains("change-biome-settings." + material.toString().toLowerCase())) {
                                String biomeName = config.getString("change-biome-settings." + material.toString().toLowerCase());
                                assert biomeName != null;
                                p.getWorld().setBiome((x + xOffset), (z + zOffset), Biome.valueOf(biomeName.toUpperCase()));
                            } else {
                                String defaultBiomeName = config.getString("change-biome-settings.default");
                                assert defaultBiomeName != null;
                                p.getWorld().setBiome((x + xOffset), (z + zOffset), Biome.valueOf(defaultBiomeName.toUpperCase()));
                            }
                        }
                        k--;
                        if (k == -65) {
                            break;
                        }
                    }
                }
            }
            Bukkit.broadcastMessage("§a[changebiome] §f生物群系自动设置操作完成！");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender commandSender, Command command, @Nonnull String alias, @Nonnull String[] args) {
        if (!command.getName().equalsIgnoreCase("changebiome")) {
            return new ArrayList<>();
        }
        if (args.length == 1) {
            return getMatchingCompletions(args[0], biomeNames);
        } else if (args.length == 2) {
            return getMatchingCompletions(args[1], exampleRadii);
        } else if (args.length == 3) {
            return getMatchingCompletions(args[2], modes);
        }
        return new ArrayList<>();
    }
}
