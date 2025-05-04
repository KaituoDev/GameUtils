package fun.kaituo.gameutils.util;

import de.tr7zw.nbtapi.NBT;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Represents a builder for {@link org.bukkit.inventory.ItemStack} objects.
 */
@SuppressWarnings("unused")
public class ItemStackBuilder {
    private final ItemStack stack;
    private final ItemMeta meta;
    
    /**
     * <p>Constructor for ItemStackBuilder.</p>
     *
     * @param material The {@link org.bukkit.Material} of the item stack.
     */
    public ItemStackBuilder(Material material) {
        stack = new ItemStack(material);
        meta = stack.getItemMeta();
    }
    
    /**
     * Sets the amount of the item stack.
     *
     * @param amount The amount to set.
     * @return this {@link fun.kaituo.gameutils.util.ItemStackBuilder} instance.
     */
    public ItemStackBuilder setAmount(int amount) {
        this.stack.setAmount(amount);
        return this;
    }

    /**
     * Sets the display name of the item stack.
     *
     * @param name The display name to set.
     * @return this {@link fun.kaituo.gameutils.util.ItemStackBuilder} instance.
     */
    public ItemStackBuilder setDisplayName(String name) {
        this.meta.setDisplayName(name);
        return this;
    }

    /**
     * Sets the lore of the item stack.
     * @param loreText The lore text to set.
     * @return this {@link fun.kaituo.gameutils.util.ItemStackBuilder} instance.
     */
    public ItemStackBuilder setLore(String... loreText) {
        this.meta.setLore(Arrays.stream(loreText).toList());
        return this;
    }

    /**
     * Adds an enchantment to the item stack.
     * @param enchantment The enchantment to add.
     * @return this {@link fun.kaituo.gameutils.util.ItemStackBuilder} instance.
     */
    public ItemStackBuilder addEnchantment(Enchantment enchantment) {
        return this.addEnchantment(enchantment, 1);
    }
    
    /**
     * Adds an enchantment to the item stack with a specified level.
     *
     * @param enchantment The enchantment to add.
     * @param level The level of the enchantment.
     * @return this {@link fun.kaituo.gameutils.util.ItemStackBuilder} instance.
     */
    public ItemStackBuilder addEnchantment(Enchantment enchantment, int level) {
        return this.addEnchantment(enchantment, level, true);
    }
    
    /**
     * Adds an enchantment to the item stack with a specified level and level restriction.
     *
     * @param enchantment The enchantment to add.
     * @param level The level of the enchantment.
     * @param ignoreLevelRestriction Whether to ignore level restrictions.
     * @return this {@link fun.kaituo.gameutils.util.ItemStackBuilder} instance.
     */
    public ItemStackBuilder addEnchantment(Enchantment enchantment, int level, boolean ignoreLevelRestriction) {
        this.meta.addEnchant(enchantment, level, ignoreLevelRestriction);
        return this;
    }
    
    /**
     * Removes an enchantment from the item stack.
     *
     * @param enchantment The enchantment to remove.
     * @return this {@link fun.kaituo.gameutils.util.ItemStackBuilder} instance.
     */
    public ItemStackBuilder removeEnchantment(Enchantment enchantment) {
        this.meta.removeEnchant(enchantment);
        return this;
    }
    
    /**
     * Adds item flags to the item stack.
     *
     * @param itemFlags The item flags to add.
     * @return this {@link fun.kaituo.gameutils.util.ItemStackBuilder} instance.
     */
    public ItemStackBuilder addFlags(ItemFlag... itemFlags) {
        this.meta.addItemFlags(itemFlags);
        return this;
    }
    
    /**
     * Removes item flags from the item stack.
     *
     * @param itemFlags The item flags to remove.
     * @return this {@link fun.kaituo.gameutils.util.ItemStackBuilder} instance.
     */
    public ItemStackBuilder removeFlags(ItemFlag... itemFlags) {
        this.meta.removeItemFlags(itemFlags);
        return this;
    }
    
    /**
     * Sets if the item stack is unbreakable.
     *
     * @param unbreakable Whether the item stack is unbreakable.
     * @return this {@link fun.kaituo.gameutils.util.ItemStackBuilder} instance.
     */
    public ItemStackBuilder setUnbreakable(boolean unbreakable) {
        this.meta.setUnbreakable(unbreakable);
        return this;
    }

    /**
     * Sets if the item stack is droppable.
     *
     * @param droppable Whether the item stack is droppable.
     * @return this {@link fun.kaituo.gameutils.util.ItemStackBuilder} instance.
     */
    public ItemStackBuilder setDroppable(boolean droppable) {
        NBT.modify(this.stack, nbt -> {
            nbt.setBoolean("droppable", droppable);
        });
        return this;
    }

    /**
     * Sets if the item stack is clickable in the inventory.
     *
     * @param clickable Whether the item stack is clickable.
     * @return this {@link fun.kaituo.gameutils.util.ItemStackBuilder} instance.
     */
    public ItemStackBuilder setClickable(boolean clickable) {
        NBT.modify(this.stack, nbt -> {
            nbt.setBoolean("clickable", clickable);
        });
        return this;
    }

    /**
     * Builds the item stack and returns it.
     *
     * @return The built {@link org.bukkit.inventory.ItemStack}.
     */
    public ItemStack build() {
        stack.setItemMeta(meta);
        return stack;
    }
}
