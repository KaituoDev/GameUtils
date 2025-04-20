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
 * @author DELL
 */
@Deprecated
@SuppressWarnings("unused")
public class Misc {

    /**
     * <p>getMenu.</p>
     *
     * @deprecated Use {@link fun.kaituo.gameutils.util.ItemUtils#getMenu()} instead.
     * @return a {@link org.bukkit.inventory.ItemStack} object
     */
    @Deprecated
    public static ItemStack getMenu() {
        return ItemUtils.getMenu();
    }

    /**
     * <p>spawnFirework.</p>
     *
     * @deprecated Use {@link fun.kaituo.gameutils.util.PlayerUtils#spawnFirework(Player)} instead.
     * @param p a {@link org.bukkit.entity.Player} object
     */
    @Deprecated
    public static void spawnFirework(Player p) {
        PlayerUtils.spawnFirework(p);
    }

    /**
     * <p>spawnFireworks.</p>
     *
     * @deprecated Use {@link fun.kaituo.gameutils.util.PlayerUtils#spawnFireworks(Player, Game)} instead.
     * @param p a {@link org.bukkit.entity.Player} object
     * @param game a {@link fun.kaituo.gameutils.game.Game} object
     * @return a {@link java.util.Set} object
     */
    @Deprecated
    @SuppressWarnings("unused")
    public static Set<Integer> spawnFireworks(Player p, Game game) {
        return PlayerUtils.spawnFireworks(p, game);
    }

    /**
     * <p>displayCountdown.</p>
     *
     * @deprecated Use {@link fun.kaituo.gameutils.util.PlayerUtils#displayCountdown(Player, int, String, String, Game)} instead.
     * @param p a {@link org.bukkit.entity.Player} object
     * @param countdownSeconds a int
     * @param countdownTitle a {@link java.lang.String} object
     * @param finishTitle a {@link java.lang.String} object
     * @param game a {@link fun.kaituo.gameutils.game.Game} object
     * @return a {@link java.util.Set} object
     */
    @Deprecated
    @SuppressWarnings("unused")
    public static Set<Integer> displayCountdown(Player p, int countdownSeconds, String countdownTitle, String finishTitle, Game game) {
        return PlayerUtils.displayCountdown(p, countdownSeconds, countdownTitle, finishTitle, game);
    }

    /**
     * <p>displayCountdown.</p>
     *
     * @deprecated Use {@link fun.kaituo.gameutils.util.PlayerUtils#displayCountdown(Player, int, Game)} instead.
     * @param p a {@link org.bukkit.entity.Player} object
     * @param countDownSeconds a int
     * @param game a {@link fun.kaituo.gameutils.game.Game} object
     * @return a {@link java.util.Set} object
     */
    @Deprecated
    @SuppressWarnings("unused")
    public static Set<Integer> displayCountdown(Player p, int countDownSeconds, Game game) {
        return PlayerUtils.displayCountdown(p, countDownSeconds, game);
    }

    /**
     * <p>isDroppable.</p>
     *
     * @deprecated Use {@link fun.kaituo.gameutils.util.ItemUtils#isDroppable(ItemStack)} instead.
     * @param item a {@link org.bukkit.inventory.ItemStack} object
     * @return a boolean
     */
    @Deprecated
    public static boolean isDroppable(ItemStack item) {
        return ItemUtils.isDroppable(item);
    }

    /**
     * <p>setDroppable.</p>
     *
     * @deprecated Use {@link fun.kaituo.gameutils.util.ItemUtils#setDroppable(ItemStack, boolean)} instead.
     * @param item a {@link org.bukkit.inventory.ItemStack} object
     * @param droppable a boolean
     */
    @Deprecated
    public static void setDroppable(ItemStack item, boolean droppable) {
        ItemUtils.setDroppable(item, droppable);
    }

    /**
     * <p>isClickable.</p>
     *
     * @deprecated Use {@link fun.kaituo.gameutils.util.ItemUtils#isClickable(ItemStack)} instead.
     * @param item a {@link org.bukkit.inventory.ItemStack} object
     * @return a boolean
     */
    @Deprecated
    public static boolean isClickable(ItemStack item) {
        return ItemUtils.isClickable(item);
    }

    /**
     * <p>setClickable.</p>
     *
     * @deprecated Use {@link fun.kaituo.gameutils.util.ItemUtils#setClickable(ItemStack, boolean)} instead.
     * @param item a {@link org.bukkit.inventory.ItemStack} object
     * @param clickable a boolean
     */
    @Deprecated
    public static void setClickable(ItemStack item, boolean clickable) {
        ItemUtils.setClickable(item, clickable);
    }

    /**
     * <p>pasteSchematic.</p>
     *
     * @deprecated Use {@link fun.kaituo.gameutils.util.WorldEditUtils#pasteSchematic(String, Location, boolean, boolean, boolean)} instead.
     * @param schematicName a {@link java.lang.String} object
     * @param location a {@link org.bukkit.Location} object
     * @param ignoreAir a boolean
     * @param copyEntities a boolean
     * @param copyBiomes a boolean
     */
    @Deprecated
    public static void pasteSchematic(String schematicName, Location location, boolean ignoreAir, boolean copyEntities, boolean copyBiomes) {
        WorldEditUtils.pasteSchematic(schematicName, location, ignoreAir, copyEntities, copyBiomes);
    }

    /**
     * <p>pasteSchematic.</p>
     *
     * @deprecated Use {@link fun.kaituo.gameutils.util.WorldEditUtils#pasteSchematic(String, Location)} instead.
     * @param schematicName a {@link java.lang.String} object
     * @param location a {@link org.bukkit.Location} object
     */
    @Deprecated
    public static void pasteSchematic(String schematicName, Location location) {
        WorldEditUtils.pasteSchematic(schematicName, location, true, true, true);
    }

    /**
     * <p>pasteSchematic.</p>
     *
     * @deprecated Use {@link fun.kaituo.gameutils.util.WorldEditUtils#pasteSchematic(String, Location, boolean)} instead.
     * @param schematicName a {@link java.lang.String} object
     * @param location a {@link org.bukkit.Location} object
     * @param ignoreAir a boolean
     */
    @Deprecated
    public static void pasteSchematic(String schematicName, Location location, boolean ignoreAir) {
        WorldEditUtils.pasteSchematic(schematicName, location, ignoreAir, true, true);
    }

    /**
     * <p>pasteSchematic.</p>
     *
     * @deprecated Use {@link fun.kaituo.gameutils.util.WorldEditUtils#pasteSchematic(String, Location, boolean, boolean)} instead.
     * @param schematicName a {@link java.lang.String} object
     * @param location a {@link org.bukkit.Location} object
     * @param ignoreAir a boolean
     * @param copyEntities a boolean
     */
    @Deprecated
    public static void pasteSchematic(String schematicName, Location location, boolean ignoreAir, boolean copyEntities) {
        WorldEditUtils.pasteSchematic(schematicName, location, ignoreAir, copyEntities, true);
    }

    /**
     * <p>pasteSchematicAtOriginalPosition.</p>
     *
     * @deprecated Use {@link fun.kaituo.gameutils.util.WorldEditUtils#pasteSchematicAtOriginalPosition(String, World, boolean, boolean, boolean)} instead.
     * @param schematicName a {@link java.lang.String} object
     * @param world a {@link org.bukkit.World} object
     * @param ignoreAir a boolean
     * @param copyEntities a boolean
     * @param copyBiomes a boolean
     */
    @Deprecated
    public static void pasteSchematicAtOriginalPosition(String schematicName, World world, boolean ignoreAir, boolean copyEntities, boolean copyBiomes) {
        WorldEditUtils.pasteSchematicAtOriginalPosition(schematicName, world, ignoreAir, copyEntities, copyBiomes);
    }

    /**
     * <p>pasteSchematicAtOriginalPosition.</p>
     *
     * @deprecated Use {@link fun.kaituo.gameutils.util.WorldEditUtils#pasteSchematicAtOriginalPosition(String, World)} instead.
     * @param schematicName a {@link java.lang.String} object
     * @param world a {@link org.bukkit.World} object
     */
    @Deprecated
    public static void pasteSchematicAtOriginalPosition(String schematicName, World world) {
        WorldEditUtils.pasteSchematicAtOriginalPosition(schematicName, world, true, true, true);
    }

    /**
     * <p>pasteSchematicAtOriginalPosition.</p>
     *
     * @deprecated Use {@link fun.kaituo.gameutils.util.WorldEditUtils#pasteSchematicAtOriginalPosition(String, World, boolean)} instead.
     * @param schematicName a {@link java.lang.String} object
     * @param world a {@link org.bukkit.World} object
     * @param ignoreAir a boolean
     */
    @Deprecated
    public static void pasteSchematicAtOriginalPosition(String schematicName, World world, boolean ignoreAir) {
        WorldEditUtils.pasteSchematicAtOriginalPosition(schematicName, world, ignoreAir, true, true);
    }

    /**
     * <p>pasteSchematicAtOriginalPosition.</p>
     *
     * @deprecated Use {@link fun.kaituo.gameutils.util.WorldEditUtils#pasteSchematicAtOriginalPosition(String, World, boolean, boolean)} instead.
     * @param schematicName a {@link java.lang.String} object
     * @param world a {@link org.bukkit.World} object
     * @param ignoreAir a boolean
     * @param copyEntities a boolean
     */
    @Deprecated
    public static void pasteSchematicAtOriginalPosition(String schematicName, World world, boolean ignoreAir, boolean copyEntities) {
        WorldEditUtils.pasteSchematicAtOriginalPosition(schematicName, world, ignoreAir, copyEntities, true);
    }
}
