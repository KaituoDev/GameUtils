package fun.kaituo.gameutils.util;

import de.tr7zw.nbtapi.NBT;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * <p>ItemStackBuilder class.</p>
 *
 * @author DELL
 */
@SuppressWarnings("unused")
public class ItemStackBuilder {
    private final ItemStack stack;
    private final ItemMeta meta;
    
    /**
     * <p>Constructor for ItemStackBuilder.</p>
     *
     * @param material a {@link org.bukkit.Material} object
     */
    public ItemStackBuilder(Material material) {
        stack = new ItemStack(material);
        meta = stack.getItemMeta();
    }
    
    /**
     * <p>setAmount.</p>
     *
     * @param amount a int
     * @return a {@link fun.kaituo.gameutils.util.ItemStackBuilder} object
     */
    public ItemStackBuilder setAmount(int amount) {
        this.stack.setAmount(amount);
        return this;
    }
    
    /**
     * <p>setDisplayName.</p>
     *
     * @param name a {@link java.lang.String} object
     * @return a {@link fun.kaituo.gameutils.util.ItemStackBuilder} object
     */
    public ItemStackBuilder setDisplayName(String name) {
        this.meta.setDisplayName(name);
        return this;
    }
    
    /**
     * <p>setLore.</p>
     *
     * @param loreText a {@link java.lang.String} object
     * @return a {@link fun.kaituo.gameutils.util.ItemStackBuilder} object
     */
    public ItemStackBuilder setLore(String... loreText) {
        this.meta.setLore(Arrays.stream(loreText).toList());
        return this;
    }
    
    /**
     * <p>addEnchantment.</p>
     *
     * @param enchantment a {@link org.bukkit.enchantments.Enchantment} object
     * @return a {@link fun.kaituo.gameutils.util.ItemStackBuilder} object
     */
    public ItemStackBuilder addEnchantment(Enchantment enchantment) {
        return this.addEnchantment(enchantment, 1);
    }
    
    /**
     * <p>addEnchantment.</p>
     *
     * @param enchantment a {@link org.bukkit.enchantments.Enchantment} object
     * @param level a int
     * @return a {@link fun.kaituo.gameutils.util.ItemStackBuilder} object
     */
    public ItemStackBuilder addEnchantment(Enchantment enchantment, int level) {
        return this.addEnchantment(enchantment, level, true);
    }
    
    /**
     * <p>addEnchantment.</p>
     *
     * @param enchantment a {@link org.bukkit.enchantments.Enchantment} object
     * @param level a int
     * @param ignoreLevelRestriction a boolean
     * @return a {@link fun.kaituo.gameutils.util.ItemStackBuilder} object
     */
    public ItemStackBuilder addEnchantment(Enchantment enchantment, int level, boolean ignoreLevelRestriction) {
        this.meta.addEnchant(enchantment, level, ignoreLevelRestriction);
        return this;
    }
    
    /**
     * <p>removeEnchantment.</p>
     *
     * @param enchantment a {@link org.bukkit.enchantments.Enchantment} object
     * @return a {@link fun.kaituo.gameutils.util.ItemStackBuilder} object
     */
    public ItemStackBuilder removeEnchantment(Enchantment enchantment) {
        this.meta.removeEnchant(enchantment);
        return this;
    }
    
    /**
     * <p>addFlags.</p>
     *
     * @param itemFlags a {@link org.bukkit.inventory.ItemFlag} object
     * @return a {@link fun.kaituo.gameutils.util.ItemStackBuilder} object
     */
    public ItemStackBuilder addFlags(ItemFlag... itemFlags) {
        this.meta.addItemFlags(itemFlags);
        return this;
    }
    
    /**
     * <p>removeFlags.</p>
     *
     * @param itemFlags a {@link org.bukkit.inventory.ItemFlag} object
     * @return a {@link fun.kaituo.gameutils.util.ItemStackBuilder} object
     */
    public ItemStackBuilder removeFlags(ItemFlag... itemFlags) {
        this.meta.removeItemFlags(itemFlags);
        return this;
    }
    
    /**
     * <p>setUnbreakable.</p>
     *
     * @param unbreakable a boolean
     * @return a {@link fun.kaituo.gameutils.util.ItemStackBuilder} object
     */
    public ItemStackBuilder setUnbreakable(boolean unbreakable) {
        this.meta.setUnbreakable(unbreakable);
        return this;
    }

    /**
     * <p>setDroppable.</p>
     *
     * @param droppable a boolean
     * @return a {@link fun.kaituo.gameutils.util.ItemStackBuilder} object
     * @since 2.0.1
     */
    public ItemStackBuilder setDroppable(boolean droppable) {
        NBT.modify(this.stack, nbt -> {
            nbt.setBoolean("droppable", droppable);
        });
        return this;
    }

    /**
     * <p>setClickable.</p>
     *
     * @param clickable a boolean
     * @return a {@link fun.kaituo.gameutils.util.ItemStackBuilder} object
     * @since 2.0.1
     */
    public ItemStackBuilder setClickable(boolean clickable) {
        NBT.modify(this.stack, nbt -> {
            nbt.setBoolean("clickable", clickable);
        });
        return this;
    }

    /**
     * <p>build.</p>
     *
     * @return a {@link org.bukkit.inventory.ItemStack} object
     */
    public ItemStack build() {
        stack.setItemMeta(meta);
        return stack;
    }
}
