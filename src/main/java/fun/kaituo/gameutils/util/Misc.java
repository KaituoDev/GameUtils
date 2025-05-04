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
     * Private constructor to prevent instantiation.
     */
    private Misc() {

    }


    /**
     * Returns a new instance of the menu item.
     *
     * @deprecated Use {@link fun.kaituo.gameutils.util.ItemUtils#getMenu()} instead.
     * @return A new instance of the menu item.
     */
    @Deprecated
    public static ItemStack getMenu() {
        return ItemUtils.getMenu();
    }

    /**
     * Spawns fireworks around a player.
     *
     * @deprecated Use {@link fun.kaituo.gameutils.util.PlayerUtils#spawnFirework(Player)} instead.
     * @param p The player to spawn fireworks around.
     */
    @Deprecated
    public static void spawnFirework(Player p) {
        PlayerUtils.spawnFirework(p);
    }

    /**
     * Spawns fireworks around a player from the perspective of a game.
     *
     * @deprecated Use {@link fun.kaituo.gameutils.util.PlayerUtils#spawnFireworks(Player, Game)} instead.
     * @param p The player to spawn fireworks around.
     * @param game The game responsible for the fireworks.
     * @return The scheduled firework detonating task ids.
     */
    @Deprecated
    @SuppressWarnings("unused")
    public static Set<Integer> spawnFireworks(Player p, Game game) {
        return PlayerUtils.spawnFireworks(p, game);
    }

    /**
     * Display a countdown for the player with given title and subtitle.
     *
     * @deprecated Use {@link fun.kaituo.gameutils.util.PlayerUtils#displayCountdown(Player, int, String, String, Game)} instead.
     * @param p The player to display the countdown to.
     * @param countdownSeconds The number of seconds to count down.
     * @param countdownTitle The title when the countdown is running. Use %time% as a placeholder for the time left.
     * @param finishTitle The title when the countdown finishes.
     * @param game The game instance that should schedule the countdown tasks.
     * @return A set of task ids that can be used to cancel the countdown display.
     */
    @Deprecated
    @SuppressWarnings("unused")
    public static Set<Integer> displayCountdown(Player p, int countdownSeconds, String countdownTitle, String finishTitle, Game game) {
        return PlayerUtils.displayCountdown(p, countdownSeconds, countdownTitle, finishTitle, game);
    }

    /**
     * Display a countdown for the player with given title and subtitle.
     *
     * @deprecated Use {@link fun.kaituo.gameutils.util.PlayerUtils#displayCountdown(Player, int, Game)} instead.
     * @param p The player to display the countdown to.
     * @param countDownSeconds The number of seconds to count down.
     * @param game The game responsible for the countdown.
     * @return The scheduled countdown task ids.
     */
    @Deprecated
    @SuppressWarnings("unused")
    public static Set<Integer> displayCountdown(Player p, int countDownSeconds, Game game) {
        return PlayerUtils.displayCountdown(p, countDownSeconds, game);
    }

    /**
     * Returns if the item is droppable.
     *
     * @deprecated Use {@link fun.kaituo.gameutils.util.ItemUtils#isDroppable(ItemStack)} instead.
     * @param item The {@link org.bukkit.inventory.ItemStack} to be checked.
     * @return Whether this item is droppable.
     */
    @Deprecated
    public static boolean isDroppable(ItemStack item) {
        return ItemUtils.isDroppable(item);
    }

    /**
     * Sets if the item is droppable.
     *
     * @deprecated Use {@link fun.kaituo.gameutils.util.ItemUtils#setDroppable(ItemStack, boolean)} instead.
     * @param item The {@link org.bukkit.inventory.ItemStack} to be set.
     * @param droppable Whether this item is droppable.
     */
    @Deprecated
    public static void setDroppable(ItemStack item, boolean droppable) {
        ItemUtils.setDroppable(item, droppable);
    }

    /**
     * Returns if the item is clickable.
     *
     * @deprecated Use {@link fun.kaituo.gameutils.util.ItemUtils#isClickable(ItemStack)} instead.
     * @param item The {@link org.bukkit.inventory.ItemStack} to be checked.
     * @return Whether this item is clickable.
     */
    @Deprecated
    public static boolean isClickable(ItemStack item) {
        return ItemUtils.isClickable(item);
    }

    /**
     * Sets if the item is clickable.
     *
     * @deprecated Use {@link fun.kaituo.gameutils.util.ItemUtils#setClickable(ItemStack, boolean)} instead.
     * @param item The {@link org.bukkit.inventory.ItemStack} to be set.
     * @param clickable Whether this item is clickable.
     */
    @Deprecated
    public static void setClickable(ItemStack item, boolean clickable) {
        ItemUtils.setClickable(item, clickable);
    }

    /**
     * Pastes a schematic at the specified location.
     *
     * @deprecated Use {@link fun.kaituo.gameutils.util.WorldEditUtils#pasteSchematic(String, Location, boolean, boolean, boolean)} instead.
     * @param schematicName The name of the schematic to be pasted.
     * @param location Where its origin is.
     * @param ignoreAir Whether to ignore air when pasting.
     * @param copyEntities Whether to copy entities.
     * @param copyBiomes Whether to copy biomes.
     */
    @Deprecated
    public static void pasteSchematic(String schematicName, Location location, boolean ignoreAir, boolean copyEntities, boolean copyBiomes) {
        WorldEditUtils.pasteSchematic(schematicName, location, ignoreAir, copyEntities, copyBiomes);
    }


    /**
     * Pastes a schematic at the specified location, ignoring air and copying entities and biomes.
     *
     * @deprecated Use {@link fun.kaituo.gameutils.util.WorldEditUtils#pasteSchematic(String, Location)} instead.
     * @param schematicName The name of the schematic to be pasted.
     * @param location Where its origin is.
     */
    @Deprecated
    public static void pasteSchematic(String schematicName, Location location) {
        WorldEditUtils.pasteSchematic(schematicName, location, true, true, true);
    }

    /**
     * Pastes a schematic at the specified location, copying entities and biomes.
     *
     * @deprecated Use {@link fun.kaituo.gameutils.util.WorldEditUtils#pasteSchematic(String, Location, boolean)} instead.
     * @param schematicName The name of the schematic to be pasted.
     * @param location Where its origin is.
     * @param ignoreAir Whether to ignore air when pasting.
     */
    @Deprecated
    public static void pasteSchematic(String schematicName, Location location, boolean ignoreAir) {
        WorldEditUtils.pasteSchematic(schematicName, location, ignoreAir, true, true);
    }

    /**
     * Pastes a schematic at the specified location, copying biomes.
     *
     * @deprecated Use {@link fun.kaituo.gameutils.util.WorldEditUtils#pasteSchematic(String, Location, boolean, boolean)} instead.
     * @param schematicName The name of the schematic to be pasted.
     * @param location Where its origin is.
     * @param ignoreAir Whether to ignore air when pasting.
     * @param copyEntities Whether to copy entities.
     */
    @Deprecated
    public static void pasteSchematic(String schematicName, Location location, boolean ignoreAir, boolean copyEntities) {
        WorldEditUtils.pasteSchematic(schematicName, location, ignoreAir, copyEntities, true);
    }

    /**
     * Pastes a schematic in a world at where it was copied.
     *
     * @deprecated Use {@link fun.kaituo.gameutils.util.WorldEditUtils#pasteSchematicAtOriginalPosition(String, World, boolean, boolean, boolean)} instead.
     * @param schematicName The name of the schematic to be pasted.
     * @param world Which world to paste into.
     * @param ignoreAir Whether to ignore air when pasting.
     * @param copyEntities Whether to copy entities.
     * @param copyBiomes Whether to copy biomes.
     */
    @Deprecated
    public static void pasteSchematicAtOriginalPosition(String schematicName, World world, boolean ignoreAir, boolean copyEntities, boolean copyBiomes) {
        WorldEditUtils.pasteSchematicAtOriginalPosition(schematicName, world, ignoreAir, copyEntities, copyBiomes);
    }

    /**
     * Pastes a schematic in a world at where it was copied, ignoring air and copying entities and biomes.
     *
     * @deprecated Use {@link fun.kaituo.gameutils.util.WorldEditUtils#pasteSchematicAtOriginalPosition(String, World)} instead.
     * @param schematicName The name of the schematic to be pasted.
     * @param world Which world to paste into.
     */
    @Deprecated
    public static void pasteSchematicAtOriginalPosition(String schematicName, World world) {
        WorldEditUtils.pasteSchematicAtOriginalPosition(schematicName, world, true, true, true);
    }

    /**
     * Pastes a schematic in a world at where it was copied, copying entities and biomes.
     *
     * @deprecated Use {@link fun.kaituo.gameutils.util.WorldEditUtils#pasteSchematicAtOriginalPosition(String, World, boolean)} instead.
     * @param schematicName The name of the schematic to be pasted.
     * @param world Which world to paste into.
     * @param ignoreAir Whether to ignore air when pasting.
     */
    @Deprecated
    public static void pasteSchematicAtOriginalPosition(String schematicName, World world, boolean ignoreAir) {
        WorldEditUtils.pasteSchematicAtOriginalPosition(schematicName, world, ignoreAir, true, true);
    }

    /**
     * Pastes a schematic in a world at where it was copied, copying biomes.
     *
     * @deprecated Use {@link fun.kaituo.gameutils.util.WorldEditUtils#pasteSchematicAtOriginalPosition(String, World, boolean, boolean)} instead.
     * @param schematicName The name of the schematic to be pasted.
     * @param world Which world to paste into.
     * @param ignoreAir Whether to ignore air when pasting.
     * @param copyEntities Whether to copy entities.
     */
    @Deprecated
    public static void pasteSchematicAtOriginalPosition(String schematicName, World world, boolean ignoreAir, boolean copyEntities) {
        WorldEditUtils.pasteSchematicAtOriginalPosition(schematicName, world, ignoreAir, copyEntities, true);
    }
}
