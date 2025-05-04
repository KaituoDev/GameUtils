package fun.kaituo.gameutils.util;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a player's inventory.
 */
@SuppressWarnings("unused")
public class GameInventory implements ConfigurationSerializable {
    /** Size of a player's inventory. */
    public static final int PLAYER_INVENTORY_SIZE = 41;
    /** Starting index of hotbar slots. */
    public static final int HOTBAR_OFFSET = 0;
    /** Starting index of storage slots. */
    public static final int STORAGE_OFFSET = 9;
    /** Index of the head slot. */
    public static final int HEAD_SLOT = 39;
    /** Index of the chest slot. */
    public static final int CHEST_SLOT = 38;
    /** Index of the legs slot. */
    public static final int LEGS_SLOT = 37;
    /** Index of the feet slots. */
    public static final int FEET_SLOT = 36;
    /** Index of the off-hand slot. */
    public static final int OFF_HAND_SLOT = 40;

    private final ItemStack[] contents = new ItemStack[PLAYER_INVENTORY_SIZE];

    private boolean isValidItem(ItemStack item) {
        if (item == null) {
            return false;
        }
        return !item.getType().equals(Material.AIR);
    }

    /**
     * Constructs a new instance of GameInventory with empty contents.
     */
    @SuppressWarnings("unused")
    public GameInventory() {}

    /**
     * Constructs a new instance of GameInventory from a Player in game.
     *
     * @param p The {@link org.bukkit.entity.Player} from which to construct the inventory.
     */
    @SuppressWarnings("unused")
    public GameInventory(Player p) {
        HotbarMapping mapping = HotbarMappingManager.INSTANCE.getMapping(p.getUniqueId());
        for (int i = 0; i < 9; i += 1) {
            ItemStack item = p.getInventory().getItem(i);
            if (isValidItem(item)) {
                contents[mapping.reverseMap(i)] = item.clone();
            }
        }
        for (int i = 9; i < PLAYER_INVENTORY_SIZE; i += 1) {
            ItemStack item = p.getInventory().getItem(i);
            if (isValidItem(item)) {
                contents[i] = item.clone();
            }
        }
    }

    /**
     * Gets the {@link ItemStack} in the head slot of the inventory.
     *
     * @return The {@link org.bukkit.inventory.ItemStack} in the head slot, or null if empty.
     */
    public @Nullable ItemStack getHead() {
        return contents[HEAD_SLOT];
    }

    /**
     * Sets the {@link ItemStack} in the head slot of the inventory.
     *
     * @param item The {@link org.bukkit.inventory.ItemStack} to set in the head slot.
     */
    public void setHead(@Nullable ItemStack item) {
        if (isValidItem(item)) {
            contents[HEAD_SLOT] = item.clone();
        } else {
            contents[HEAD_SLOT] = null;
        }
    }

    /**
     * Gets the {@link ItemStack} in the chest slot of the inventory.
     *
     * @return The {@link org.bukkit.inventory.ItemStack} in the chest slot, or null if empty.
     */
    public @Nullable ItemStack getChest() {
        return contents[CHEST_SLOT];
    }

    /**
     * Sets the {@link ItemStack} in the chest slot of the inventory.
     *
     * @param item The {@link org.bukkit.inventory.ItemStack} to set in the chest slot.
     */
    public void setChest(@Nullable ItemStack item) {
        if (isValidItem(item)) {
            contents[CHEST_SLOT] = item.clone();
        } else {
            contents[CHEST_SLOT] = null;
        }
    }

    /**
     * Gets the {@link ItemStack} in the legs slot of the inventory.
     *
     * @return The {@link org.bukkit.inventory.ItemStack} in the legs slot, or null if empty.
     */
    public @Nullable ItemStack getLegs() {
        return contents[LEGS_SLOT];
    }

    /**
     * Sets the {@link ItemStack} in the legs slot of the inventory.
     *
     * @param item The {@link org.bukkit.inventory.ItemStack} to set in the legs slot.
     */
    public void setLegs(@Nullable ItemStack item) {
        if (isValidItem(item)) {
            contents[LEGS_SLOT] = item.clone();
        } else {
            contents[LEGS_SLOT] = null;
        }
    }

    /**
     * Gets the {@link ItemStack} in the feet slot of the inventory.
     *
     * @return The {@link org.bukkit.inventory.ItemStack} in the feet slot, or null if empty.
     */
    public @Nullable ItemStack getFeet() {
        return contents[FEET_SLOT];
    }

    /**
     * Sets the {@link ItemStack} in the feet slot of the inventory.
     *
     * @param item The {@link org.bukkit.inventory.ItemStack} to set in the feet slot.
     */
    public void setFeet(@Nullable ItemStack item) {
        if (isValidItem(item)) {
            contents[FEET_SLOT] = item.clone();
        } else {
            contents[FEET_SLOT] = null;
        }
    }

    /**
     * Gets the {@link ItemStack} in the off-hand slot of the inventory.
     *
     * @return The {@link org.bukkit.inventory.ItemStack} in the off-hand slot, or null if empty.
     */
    public @Nullable ItemStack getOffHand() {
        return contents[OFF_HAND_SLOT];
    }

    /**
     * Sets the {@link ItemStack} in the off-hand slot of the inventory.
     *
     * @param item The {@link org.bukkit.inventory.ItemStack} to set in the off-hand slot.
     */
    public void setOffHand(@Nullable ItemStack item) {
        if (isValidItem(item)) {
            contents[OFF_HAND_SLOT] = item.clone();
        } else {
            contents[OFF_HAND_SLOT] = null;
        }
    }

    /**
     * Gets the {@link ItemStack} in the hotbar slot at the given index of the inventory.
     *
     * @param index The index of the hotbar slot (0-8).
     * @return The {@link org.bukkit.inventory.ItemStack} in the hotbar slot, or null if empty.
     */
    public @Nullable ItemStack getHotbar(int index) {
        if (index < 0 || index >= 9) {
            throw new IllegalArgumentException("Hotbar index can only be 0-8");
        }
        return contents[index + HOTBAR_OFFSET];
    }

    /**
     * Sets the {@link ItemStack} in the hotbar slot at the given index of the inventory.
     *
     * @param index The index of the hotbar slot (0-8).
     * @param item The {@link org.bukkit.inventory.ItemStack} to set in the hotbar slot.
     */
    public void setHotbar(int index, @Nullable ItemStack item) {
        if (index < 0 || index >= 9) {
            throw new IllegalArgumentException("Hotbar index can only be 0-8");
        }
        if (isValidItem(item)) {
            contents[index + HOTBAR_OFFSET] = item.clone();
        } else {
            contents[index + HOTBAR_OFFSET] = null;
        }
    }

    /**
     * Gets the {@link ItemStack} in the storage slot at the given index of the inventory.
     *
     * @param index The index of the storage slot (0-26).
     * @return The {@link org.bukkit.inventory.ItemStack} in the storage slot, or null if empty.
     */
    public @Nullable ItemStack getStorage(int index) {
        if (index < 0 || index >= 27) {
            throw new IllegalArgumentException("Storage index can only be 0-26");
        }
        return contents[index + STORAGE_OFFSET];
    }

    /**
     * Sets the {@link ItemStack} in the storage slot at the given index of the inventory.
     *
     * @param index The index of the storage slot (0-26).
     * @param item The {@link org.bukkit.inventory.ItemStack} to set in the storage slot.
     */
    public void setStorage(int index, @Nullable ItemStack item) {
        if (index < 0 || index >= 27) {
            throw new IllegalArgumentException("Storage index can only be 0-26");
        }
        if (isValidItem(item)) {
            contents[index + STORAGE_OFFSET] = item.clone();
        } else {
            contents[index + STORAGE_OFFSET] = null;
        }
    }

    /**
     * Applies this inventory to the target player.
     *
     * @param p The {@link org.bukkit.entity.Player} to apply the inventory to.
     */
    @SuppressWarnings("unused")
    public void apply(Player p) {
        HotbarMapping mapping = HotbarMappingManager.INSTANCE.getMapping(p.getUniqueId());
        for (int i = 0; i < 9; i += 1) {
            ItemStack item = getHotbar(i);
            if (isValidItem(item)) {
                p.getInventory().setItem(mapping.map(i), item.clone());
                continue;
            }
            p.getInventory().setItem(mapping.map(i), null);
        }
        for (int i = 9; i < PLAYER_INVENTORY_SIZE; i += 1) {
            if (isValidItem(contents[i])) {
                p.getInventory().setItem(i, contents[i].clone());
                continue;
            }
            p.getInventory().setItem(i, null);
        }
    }

    /** {@inheritDoc} */
    @Override
    public @Nonnull Map<String, Object> serialize() {
        Map<String, Object> serialized = new HashMap<>();
        if (isValidItem(getHead())) {
            serialized.put("head", getHead().serialize());
        }
        if (isValidItem(getChest())) {
            serialized.put("chest", getChest().serialize());
        }
        if (isValidItem(getLegs())) {
            serialized.put("legs", getLegs().serialize());
        }
        if (isValidItem(getFeet())) {
            serialized.put("feet", getFeet().serialize());
        }
        if (isValidItem(getOffHand())) {
            serialized.put("off-hand", getOffHand().serialize());
        }
        Map<String, Object> hotbar = new HashMap<>();
        for (int i = 0; i < 9; i += 1) {
            ItemStack item = getHotbar(i);
            if (isValidItem(item)) {
                hotbar.put(String.valueOf(i), item.serialize());
            }
        }
        if (!hotbar.isEmpty()) {
            serialized.put("hotbar", hotbar);
        }
        Map<String, Object> storage = new HashMap<>();
        for (int i = 0; i < 27; i += 1) {
            ItemStack item = getStorage(i);
            if (isValidItem(item)) {
                storage.put(String.valueOf(i), item.serialize());
            }
        }
        if (!storage.isEmpty()) {
            serialized.put("storage", storage);
        }
        return serialized;
    }

    /**
     * Deserializes a {@link GameInventory} object from a {@link java.util.Map} object.
     *
     * @param serialized The {@link java.util.Map} object to deserialize from.
     * @return The deserialized {@link GameInventory} object.
     */
    @SuppressWarnings({"unused"})
    public static GameInventory deserialize(Map<String, Object> serialized) {
        GameInventory deserialized = new GameInventory();
        ConfigurationSection head = (ConfigurationSection) serialized.get("head");
        if (head != null) {
            deserialized.setHead(ItemStack.deserialize(head.getValues(true)));
        }
        ConfigurationSection chest = (ConfigurationSection) serialized.get("chest");
        if (chest != null) {
            deserialized.setChest(ItemStack.deserialize(chest.getValues(true)));
        }
        ConfigurationSection legs = (ConfigurationSection) serialized.get("legs");
        if (legs != null) {
            deserialized.setLegs(ItemStack.deserialize(legs.getValues(true)));
        }
        ConfigurationSection feet = (ConfigurationSection) serialized.get("feet");
        if (feet != null) {
            deserialized.setFeet(ItemStack.deserialize(feet.getValues(true)));
        }
        ConfigurationSection offHand = (ConfigurationSection) serialized.get("off-hand");
        if (offHand != null) {
            deserialized.setOffHand(ItemStack.deserialize(offHand.getValues(true)));
        }

        ConfigurationSection hotbar = (ConfigurationSection) serialized.get("hotbar");
        if (hotbar != null) {
            for (int i = 0; i < 9; i += 1) {
                ConfigurationSection item = (ConfigurationSection) hotbar.get(String.valueOf(i));
                if (item == null) {
                    continue;
                }
                deserialized.setHotbar(i, ItemStack.deserialize(item.getValues(true)));
            }
        }

        ConfigurationSection storage = (ConfigurationSection) serialized.get("storage");
        if (storage != null) {
            for (int i = 0; i < 27; i += 1) {
                ConfigurationSection item = (ConfigurationSection) storage.get(String.valueOf(i));
                if (item == null) {
                    continue;
                }
                deserialized.setStorage(i, ItemStack.deserialize(item.getValues(true)));
            }
        }

        return deserialized;
    }
}
