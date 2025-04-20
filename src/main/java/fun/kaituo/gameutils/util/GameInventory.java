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
 * <p>GameInventory class.</p>
 *
 * @author DELL
 */
@SuppressWarnings("unused")
public class GameInventory implements ConfigurationSerializable {
    /** Constant <code>PLAYER_INVENTORY_SIZE=41</code> */
    public static final int PLAYER_INVENTORY_SIZE = 41;
    /** Constant <code>HOTBAR_OFFSET=0</code> */
    public static final int HOTBAR_OFFSET = 0;
    /** Constant <code>STORAGE_OFFSET=9</code> */
    public static final int STORAGE_OFFSET = 9;
    /** Constant <code>HEAD_SLOT=39</code> */
    public static final int HEAD_SLOT = 39;
    /** Constant <code>CHEST_SLOT=38</code> */
    public static final int CHEST_SLOT = 38;
    /** Constant <code>LEGS_SLOT=37</code> */
    public static final int LEGS_SLOT = 37;
    /** Constant <code>FEET_SLOT=36</code> */
    public static final int FEET_SLOT = 36;
    /** Constant <code>OFF_HAND_SLOT=40</code> */
    public static final int OFF_HAND_SLOT = 40;

    private final ItemStack[] contents = new ItemStack[PLAYER_INVENTORY_SIZE];

    private boolean isValidItem(ItemStack item) {
        if (item == null) {
            return false;
        }
        return !item.getType().equals(Material.AIR);
    }

    @SuppressWarnings("unused")
    /**
     * <p>Constructor for GameInventory.</p>
     */
    public GameInventory() {}

    @SuppressWarnings("unused")
    /**
     * <p>Constructor for GameInventory.</p>
     *
     * @param p a {@link org.bukkit.entity.Player} object
     */
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
     * <p>getHead.</p>
     *
     * @return a {@link org.bukkit.inventory.ItemStack} object
     */
    public @Nullable ItemStack getHead() {
        return contents[HEAD_SLOT];
    }

    /**
     * <p>setHead.</p>
     *
     * @param item a {@link org.bukkit.inventory.ItemStack} object
     */
    public void setHead(@Nullable ItemStack item) {
        if (isValidItem(item)) {
            contents[HEAD_SLOT] = item.clone();
        } else {
            contents[HEAD_SLOT] = null;
        }
    }

    /**
     * <p>getChest.</p>
     *
     * @return a {@link org.bukkit.inventory.ItemStack} object
     */
    public @Nullable ItemStack getChest() {
        return contents[CHEST_SLOT];
    }

    /**
     * <p>setChest.</p>
     *
     * @param item a {@link org.bukkit.inventory.ItemStack} object
     */
    public void setChest(@Nullable ItemStack item) {
        if (isValidItem(item)) {
            contents[CHEST_SLOT] = item.clone();
        } else {
            contents[CHEST_SLOT] = null;
        }
    }

    /**
     * <p>getLegs.</p>
     *
     * @return a {@link org.bukkit.inventory.ItemStack} object
     */
    public @Nullable ItemStack getLegs() {
        return contents[LEGS_SLOT];
    }

    /**
     * <p>setLegs.</p>
     *
     * @param item a {@link org.bukkit.inventory.ItemStack} object
     */
    public void setLegs(@Nullable ItemStack item) {
        if (isValidItem(item)) {
            contents[LEGS_SLOT] = item.clone();
        } else {
            contents[LEGS_SLOT] = null;
        }
    }

    /**
     * <p>getFeet.</p>
     *
     * @return a {@link org.bukkit.inventory.ItemStack} object
     */
    public @Nullable ItemStack getFeet() {
        return contents[FEET_SLOT];
    }

    /**
     * <p>setFeet.</p>
     *
     * @param item a {@link org.bukkit.inventory.ItemStack} object
     */
    public void setFeet(@Nullable ItemStack item) {
        if (isValidItem(item)) {
            contents[FEET_SLOT] = item.clone();
        } else {
            contents[FEET_SLOT] = null;
        }
    }

    /**
     * <p>getOffHand.</p>
     *
     * @return a {@link org.bukkit.inventory.ItemStack} object
     */
    public @Nullable ItemStack getOffHand() {
        return contents[OFF_HAND_SLOT];
    }

    /**
     * <p>setOffHand.</p>
     *
     * @param item a {@link org.bukkit.inventory.ItemStack} object
     */
    public void setOffHand(@Nullable ItemStack item) {
        if (isValidItem(item)) {
            contents[OFF_HAND_SLOT] = item.clone();
        } else {
            contents[OFF_HAND_SLOT] = null;
        }
    }

    /**
     * <p>getHotbar.</p>
     *
     * @param index a int
     * @return a {@link org.bukkit.inventory.ItemStack} object
     */
    public @Nullable ItemStack getHotbar(int index) {
        if (index < 0 || index >= 9) {
            throw new IllegalArgumentException("Hotbar index can only be 0-8");
        }
        return contents[index + HOTBAR_OFFSET];
    }

    /**
     * <p>setHotbar.</p>
     *
     * @param index a int
     * @param item a {@link org.bukkit.inventory.ItemStack} object
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
     * <p>getStorage.</p>
     *
     * @param index a int
     * @return a {@link org.bukkit.inventory.ItemStack} object
     */
    public @Nullable ItemStack getStorage(int index) {
        if (index < 0 || index >= 27) {
            throw new IllegalArgumentException("Storage index can only be 0-26");
        }
        return contents[index + STORAGE_OFFSET];
    }

    /**
     * <p>setStorage.</p>
     *
     * @param index a int
     * @param item a {@link org.bukkit.inventory.ItemStack} object
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
     * <p>apply.</p>
     *
     * @param p a {@link org.bukkit.entity.Player} object
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
     * <p>deserialize.</p>
     *
     * @param serialized a {@link java.util.Map} object
     * @return a {@link fun.kaituo.gameutils.util.GameInventory} object
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
