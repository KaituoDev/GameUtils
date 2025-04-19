package fun.kaituo.gameutils.util;

import fun.kaituo.gameutils.game.Game;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

/**
 * Various utility methods.
 *
 * @deprecated This class is a mess. Its functions are now split into small util classes.
 * @see ItemUtils
 * @see PlayerUtils
 * @see WorldEditUtils
 * @see ItemStackBuilder
 * @see fun.kaituo.gameutils.command.GameUtilsCommand GameUtilsCommand
 */
@Deprecated
@SuppressWarnings("unused")
public class Misc {

    /**
     * @deprecated Use {@link ItemUtils#getMenu()} instead.
     */
    @Deprecated
    public static ItemStack getMenu() {
        return ItemUtils.getMenu();
    }

    /**
     * @deprecated Use {@link PlayerUtils#spawnFirework(Player)} instead.
     */
    @Deprecated
    public static void spawnFirework(Player p) {
        PlayerUtils.spawnFirework(p);
    }

    /**
     * @deprecated Use {@link PlayerUtils#spawnFireworks(Player, Game)} instead.
     */
    @Deprecated
    @SuppressWarnings("unused")
    public static Set<Integer> spawnFireworks(Player p, Game game) {
        return PlayerUtils.spawnFireworks(p, game);
    }

    /**
     * @deprecated Use {@link PlayerUtils#displayCountdown(Player, int, String, String, Game)} instead.
     */
    @Deprecated
    @SuppressWarnings("unused")
    public static Set<Integer> displayCountdown(Player p, int countdownSeconds, String countdownTitle, String finishTitle, Game game) {
        return PlayerUtils.displayCountdown(p, countdownSeconds, countdownTitle, finishTitle, game);
    }

    /**
     * @deprecated Use {@link PlayerUtils#displayCountdown(Player, int, Game)} instead.
     */
    @Deprecated
    @SuppressWarnings("unused")
    public static Set<Integer> displayCountdown(Player p, int countDownSeconds, Game game) {
        return PlayerUtils.displayCountdown(p, countDownSeconds, game);
    }

    /**
     * @deprecated Use {@link ItemUtils#isDroppable(ItemStack)} instead.
     */
    @Deprecated
    public static boolean isDroppable(ItemStack item) {
        return ItemUtils.isDroppable(item);
    }

    /**
     * @deprecated Use {@link ItemUtils#setDroppable(ItemStack, boolean)} instead.
     */
    @Deprecated
    public static void setDroppable(ItemStack item, boolean droppable) {
        ItemUtils.setDroppable(item, droppable);
    }

    /**
     * @deprecated Use {@link ItemUtils#isClickable(ItemStack)} instead.
     */
    @Deprecated
    public static boolean isClickable(ItemStack item) {
        return ItemUtils.isClickable(item);
    }

    /**
     * @deprecated Use {@link ItemUtils#setClickable(ItemStack, boolean)} instead.
     */
    @Deprecated
    public static void setClickable(ItemStack item, boolean clickable) {
        ItemUtils.setClickable(item, clickable);
    }

    /**
     * @deprecated Use {@link WorldEditUtils#pasteSchematic(String, Location, boolean, boolean, boolean)} instead.
     */
    @Deprecated
    public static void pasteSchematic(String schematicName, Location location, boolean ignoreAir, boolean copyEntities, boolean copyBiomes) {
        WorldEditUtils.pasteSchematic(schematicName, location, ignoreAir, copyEntities, copyBiomes);
    }

    /**
     * @deprecated Use {@link WorldEditUtils#pasteSchematic(String, Location)} instead.
     */
    @Deprecated
    public static void pasteSchematic(String schematicName, Location location) {
        WorldEditUtils.pasteSchematic(schematicName, location, true, true, true);
    }

    /**
     * @deprecated Use {@link WorldEditUtils#pasteSchematic(String, Location, boolean)} instead.
     */
    @Deprecated
    public static void pasteSchematic(String schematicName, Location location, boolean ignoreAir) {
        WorldEditUtils.pasteSchematic(schematicName, location, ignoreAir, true, true);
    }

    /**
     * @deprecated Use {@link WorldEditUtils#pasteSchematic(String, Location, boolean, boolean)} instead.
     */
    @Deprecated
    public static void pasteSchematic(String schematicName, Location location, boolean ignoreAir, boolean copyEntities) {
        WorldEditUtils.pasteSchematic(schematicName, location, ignoreAir, copyEntities, true);
    }

    /**
     * @deprecated Use {@link WorldEditUtils#pasteSchematicAtOriginalPosition(String, World, boolean, boolean, boolean)} instead.
     */
    @Deprecated
    public static void pasteSchematicAtOriginalPosition(String schematicName, World world, boolean ignoreAir, boolean copyEntities, boolean copyBiomes) {
        WorldEditUtils.pasteSchematicAtOriginalPosition(schematicName, world, ignoreAir, copyEntities, copyBiomes);
    }

    /**
     * @deprecated Use {@link WorldEditUtils#pasteSchematicAtOriginalPosition(String, World)} instead.
     */
    @Deprecated
    public static void pasteSchematicAtOriginalPosition(String schematicName, World world) {
        WorldEditUtils.pasteSchematicAtOriginalPosition(schematicName, world, true, true, true);
    }

    /**
     * @deprecated Use {@link WorldEditUtils#pasteSchematicAtOriginalPosition(String, World, boolean)} instead.
     */
    @Deprecated
    public static void pasteSchematicAtOriginalPosition(String schematicName, World world, boolean ignoreAir) {
        WorldEditUtils.pasteSchematicAtOriginalPosition(schematicName, world, ignoreAir, true, true);
    }

    /**
     * @deprecated Use {@link WorldEditUtils#pasteSchematicAtOriginalPosition(String, World, boolean, boolean)} instead.
     */
    @Deprecated
    public static void pasteSchematicAtOriginalPosition(String schematicName, World world, boolean ignoreAir, boolean copyEntities) {
        WorldEditUtils.pasteSchematicAtOriginalPosition(schematicName, world, ignoreAir, copyEntities, true);
    }
}
