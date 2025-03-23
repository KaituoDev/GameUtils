package fun.kaituo.gameutils.util;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import de.tr7zw.nbtapi.NBT;
import fun.kaituo.gameutils.GameUtils;
import fun.kaituo.gameutils.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@SuppressWarnings("unused")
public class Misc {
    private static final Random RANDOM = new Random();
    private static final Color[] fireworkColors = {
            Color.AQUA, Color.BLUE, Color.FUCHSIA, Color.GREEN, Color.LIME, Color.MAROON,
            Color.NAVY, Color.OLIVE, Color.ORANGE, Color.PURPLE, Color.RED, Color.SILVER,
            Color.TEAL, Color.WHITE, Color.YELLOW
    };

    public static ItemStack getMenu() {
        return new ItemStackBuilder(Material.CLOCK).setDisplayName("§e● §b§l菜单 §e●").setLore("§f请右键打开!").build();
    }

    public static void spawnFirework(Player p) {
        Location loc = p.getLocation();
        loc.setY(loc.getY() + 0.9);
        if (loc.getWorld() == null) {
            throw new RuntimeException("Trying to spawn firework on player, but player is not in a valid world");
        }
        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK_ROCKET);
        fw.addScoreboardTag("gameFirework");
        FireworkMeta fwm = fw.getFireworkMeta();
        fwm.setPower(2);
        fwm.addEffect(FireworkEffect.builder().withColor(fireworkColors[RANDOM.nextInt(fireworkColors.length)]).flicker(true).build());
        fw.setFireworkMeta(fwm);
        fw.detonate();
    }

    @SuppressWarnings("unused")
    public static Set<Integer> spawnFireworks(Player p, Game game) {
        Set<Integer> taskIds = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            taskIds.add(Bukkit.getScheduler().runTaskLater(game, () -> spawnFirework(p), 8 * i + 1).getTaskId());
        }
        return taskIds;
    }

    /**
     * Display a countdown for the player with given title and subtitle
     * @param p The player to display the countdown to
     * @param countdownSeconds The number of seconds to count down
     * @param countdownTitle The title when the countdown is running. Use %time% as a placeholder for the time left
     * @param finishTitle The title when the countdown finishes.
     * @param game The game instance that should schedule the countdown tasks
     * @return A set of task ids that can be used to cancel the countdown display
     */
    @SuppressWarnings("unused")
    public static Set<Integer> displayCountdown(Player p, int countdownSeconds, String countdownTitle, String finishTitle, Game game) {
        if (countdownSeconds > 5) {
            p.sendTitle(countdownTitle.replace("%time%", String.valueOf(countdownSeconds)), "", 2, 16, 2);
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1f, 1f);
        }
        Set<Integer> taskIds = new HashSet<>();
        for (int i = 5; i > 0; i--) {
            int countdownSecondsRemaining = i;
            taskIds.add(Bukkit.getScheduler().runTaskLater(game, () -> {
                if (!p.isOnline()) {
                    return;
                }
                p.sendTitle(countdownTitle.replace("%time%", String.valueOf(countdownSecondsRemaining)), "", 2, 16, 2);
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1f, 1f);
            }, 20L * countdownSeconds - 20L * i).getTaskId());
        }
        taskIds.add(Bukkit.getScheduler().runTaskLater(GameUtils.inst(), () -> {
            if (!p.isOnline()) {
                return;
            }
            p.sendTitle(finishTitle, "", 2, 16, 2);
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1f, 2f);
        }, 20L * countdownSeconds).getTaskId());
        return taskIds;
    }

    @SuppressWarnings("unused")
    public static Set<Integer> displayCountdown(Player p, int countDownSeconds, Game game) {
        return displayCountdown(p, countDownSeconds, "§a游戏还有 %time% 秒开始", "§e游戏开始！", game);
    }

    public static List<String> getMatchingCompletions(String partialArg, List<String> completions) {
        return completions.stream().filter(completion ->
                completion.toLowerCase().startsWith(partialArg.toLowerCase())).toList();
    }

    public static boolean isDroppable(ItemStack item) {
        return NBT.get(item, nbt ->
            (boolean) nbt.getBoolean("droppable")
        );
    }

    public static void setDroppable(ItemStack item, boolean droppable) {
        NBT.modify(item, nbt -> {
            nbt.setBoolean("droppable", droppable);
        });
    }

    public static boolean isClickable(ItemStack item) {
        return NBT.get(item, nbt ->
            (boolean) nbt.getBoolean("clickable")
        );
    }

    public static void setClickable(ItemStack item, boolean clickable) {
        NBT.modify(item, nbt -> {
            nbt.setBoolean("clickable", clickable);
        });
    }

    private static Clipboard getSchematicClipBoard(String schematicName) {
        File file = new File("plugins/WorldEdit/schematics/" + schematicName + ".schem");
        ClipboardFormat format = ClipboardFormats.findByFile(file);
        if (format == null) {
            throw new IllegalArgumentException("Failed to find schematic file");
        }

        Clipboard clipboard;
        try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
            clipboard = reader.read();
        } catch (Exception e) {
            throw new RuntimeException("Failed to read schematic file", e);
        }
        return clipboard;
    }

    public static void pasteSchematic(String schematicName, Location location, boolean ignoreAir, boolean copyEntities, boolean copyBiomes) {
        World world = location.getWorld();
        if (world == null) {
            throw new IllegalArgumentException("Location's world is null");
        }

        Clipboard clipboard = getSchematicClipBoard(schematicName);

        try (EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder().world(BukkitAdapter.adapt(world)).build()) {
            Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(BlockVector3.at(location.getX(), location.getY(), location.getZ()))
                    .ignoreAirBlocks(ignoreAir)
                    .copyEntities(copyEntities)
                    .copyBiomes(copyBiomes)
                    .build();
            Operations.complete(operation);
        } catch (Exception e) {
            throw new RuntimeException("Failed to paste schematic", e);
        }
    }

    public static void pasteSchematic(String schematicName, Location location) {
        pasteSchematic(schematicName, location, true, true, true);
    }

    public static void pasteSchematic(String schematicName, Location location, boolean ignoreAir) {
        pasteSchematic(schematicName, location, ignoreAir, true, true);
    }

    public static void pasteSchematic(String schematicName, Location location, boolean ignoreAir, boolean copyEntities) {
        pasteSchematic(schematicName, location, ignoreAir, copyEntities, true);
    }

    public static void pasteSchematicAtOriginalPosition(String schematicName, World world, boolean ignoreAir, boolean copyEntities, boolean copyBiomes) {
        Clipboard clipboard = getSchematicClipBoard(schematicName);
        try (EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder().world(BukkitAdapter.adapt(world)).build()) {
            Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(clipboard.getOrigin())
                    .ignoreAirBlocks(ignoreAir)
                    .copyEntities(copyEntities)
                    .copyBiomes(copyBiomes)
                    .build();
            Operations.complete(operation);
        } catch (Exception e) {
            throw new RuntimeException("Failed to paste schematic", e);
        }
    }

    public static void pasteSchematicAtOriginalPosition(String schematicName, World world) {
        pasteSchematicAtOriginalPosition(schematicName, world, true, true, true);
    }

    public static void pasteSchematicAtOriginalPosition(String schematicName, World world, boolean ignoreAir) {
        pasteSchematicAtOriginalPosition(schematicName, world, ignoreAir, true, true);
    }

    public static void pasteSchematicAtOriginalPosition(String schematicName, World world, boolean ignoreAir, boolean copyEntities) {
        pasteSchematicAtOriginalPosition(schematicName, world, ignoreAir, copyEntities, true);
    }
}
