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
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.io.FileInputStream;

/**
 * <p>WorldEditUtils class.</p>
 *
 * @author DELL
 * @since 2.0.1
 */
public class WorldEditUtils {
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

    /**
     * Pastes a schematic at the specified location.
     *
     * @param schematicName The name of the schematic to be pasted.
     * @param location Where its origin is.
     * @param ignoreAir Whether to ignore air when pasting.
     * @param copyEntities Whether to copy entities.
     * @param copyBiomes Whether to copy biomes.
     */
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

    /**
     * Pastes a schematic at the specified location, ignoring air and copying entities and biomes.
     *
     * @param schematicName The name of the schematic to be pasted.
     * @param location Where its origin is.
     */
    public static void pasteSchematic(String schematicName, Location location) {
        pasteSchematic(schematicName, location, true, true, true);
    }

    /**
     * Pastes a schematic at the specified location, copying entities and biomes.
     *
     * @param schematicName The name of the schematic to be pasted.
     * @param location Where its origin is.
     * @param ignoreAir Whether to ignore air when pasting.
     */
    public static void pasteSchematic(String schematicName, Location location, boolean ignoreAir) {
        pasteSchematic(schematicName, location, ignoreAir, true, true);
    }

    /**
     * Pastes a schematic at the specified location, copying biomes.
     *
     * @param schematicName The name of the schematic to be pasted.
     * @param location Where its origin is.
     * @param ignoreAir Whether to ignore air when pasting.
     * @param copyEntities Whether to copy entities.
     */
    public static void pasteSchematic(String schematicName, Location location, boolean ignoreAir, boolean copyEntities) {
        pasteSchematic(schematicName, location, ignoreAir, copyEntities, true);
    }

    /**
     * Pastes a schematic in a world at where it was copied.
     *
     * @param schematicName The name of the schematic to be pasted.
     * @param world Which world to paste into.
     * @param ignoreAir Whether to ignore air when pasting.
     * @param copyEntities Whether to copy entities.
     * @param copyBiomes Whether to copy biomes.
     */
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

    /**
     * Pastes a schematic in a world at where it was copied, ignoring air and copying entities and biomes.
     *
     * @param schematicName The name of the schematic to be pasted.
     * @param world Which world to paste into.
     */
    public static void pasteSchematicAtOriginalPosition(String schematicName, World world) {
        pasteSchematicAtOriginalPosition(schematicName, world, true, true, true);
    }

    /**
     * Pastes a schematic in a world at where it was copied, copying entities and biomes.
     *
     * @param schematicName The name of the schematic to be pasted.
     * @param world Which world to paste into.
     * @param ignoreAir Whether to ignore air when pasting.
     */
    public static void pasteSchematicAtOriginalPosition(String schematicName, World world, boolean ignoreAir) {
        pasteSchematicAtOriginalPosition(schematicName, world, ignoreAir, true, true);
    }

    /**
     * Pastes a schematic in a world at where it was copied, copying biomes.
     *
     * @param schematicName The name of the schematic to be pasted.
     * @param world Which world to paste into.
     * @param ignoreAir Whether to ignore air when pasting.
     * @param copyEntities Whether to copy entities.
     */
    public static void pasteSchematicAtOriginalPosition(String schematicName, World world, boolean ignoreAir, boolean copyEntities) {
        pasteSchematicAtOriginalPosition(schematicName, world, ignoreAir, copyEntities, true);
    }
}
