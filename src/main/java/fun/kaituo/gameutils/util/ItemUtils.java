package fun.kaituo.gameutils.util;

import de.tr7zw.nbtapi.NBT;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
public class ItemUtils {
    /**
     * Checks if an inventory contains a certain item, regardless of the amount.
     *
     * @param inv The {@link Inventory} to check.
     * @param item The {@link ItemStack} to check for.
     *             This {@link ItemStack} instance's amount attribute will be ignored.
     * @return Whether the inventory contains the item.
     */
    public static boolean containsItem(Inventory inv, ItemStack item) {
        if (item.getType().equals(Material.AIR)) {
            throw new IllegalArgumentException("Item type cannot be air");
        }
        ItemStack[] contents = inv.getContents();
        for (ItemStack content : contents) {
            if (content == null) {
                continue;
            }
            if (content.isSimilar(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if an inventory contains a certain amount of a certain item.
     *
     * @param inv The {@link Inventory} to check.
     * @param item The {@link ItemStack} to check for.
     *             This {@link ItemStack} instance's amount attribute will be ignored.
     * @param itemAmount The amount of the item to check for.
     * @return Whether the inventory contains at least the specified amount of the item.
     *
     * @apiNote This method serves a different purpose from {@link Inventory#contains(ItemStack, int)}.
     * It does not check for the number of ItemStacks that exactly match the input ItemStack.
     * Instead, it checks for the sum of amount for all ItemStacks that are similar to the input ItemStack.
     */
    public static boolean containsItem(Inventory inv, ItemStack item, int itemAmount) {
        if (itemAmount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (item.getType().equals(Material.AIR)) {
            throw new IllegalArgumentException("Item type cannot be air");
        }
        ItemStack[] contents = inv.getContents();
        int total = 0;
        for (ItemStack content : contents) {
            if (content == null) {
                continue;
            }
            if (content.isSimilar(item)) {
                total += content.getAmount();
            }
        }
        return total >= itemAmount;
    }

    /**
     * Removes one certain item from an inventory.
     *
     * @param inv The {@link Inventory} to remove the item from.
     * @param item The {@link ItemStack} to remove.
     *             This {@link ItemStack} instance's amount attribute will be ignored.
     */
    public static void removeItem(Inventory inv, ItemStack item) {
        removeItem(inv, item, 1);
    }

    /**
     * Remove a certain amount of a certain item from an inventory.
     *
     * @param inv The {@link Inventory} to remove the item from.
     * @param item The {@link ItemStack} to remove.
     *             This {@link ItemStack} instance's amount attribute will be ignored.
     * @param itemAmount The amount of the item to remove.
     *
     * @apiNote This method does not remove the given number of ItemStacks that exactly match the input ItemStack.
     * Instead, it removes items that are similar to the input ItemStack until the specified amount is removed.
     */
    public static void removeItem(Inventory inv, @Nonnull ItemStack item, int itemAmount) {
        if (itemAmount <= 0) {
            throw new IllegalArgumentException("Cannot remove a non-positive amount of items");
        }
        if (item.getType().equals(Material.AIR)) {
            throw new IllegalArgumentException("Cannot remove air");
        }
        if (!containsItem(inv, item, itemAmount)) {
            throw new IllegalArgumentException("Inventory does not contain enough of the item");
        }
        int remaining = itemAmount;
        ItemStack[] contents = inv.getContents();
        for (int i = 0; i < inv.getSize(); i += 1) {
            ItemStack content = contents[i];
            if (content == null) {
                continue;
            }
            content = content.clone();
            if (!content.isSimilar(item)) {
                continue;
            }
            if (content.getAmount() <= remaining) {
                remaining -= content.getAmount();
                inv.setItem(i, null);
            } else {
                content.setAmount(content.getAmount() - remaining);
                inv.setItem(i, content);
                break;
            }
        }
    }

    /**
     * Returns a new instance of the menu item.
     *
     * @return A new instance of the menu item.
     */
    public static ItemStack getMenu() {
        return new ItemStackBuilder(Material.CLOCK).setDisplayName("§e● §b§l菜单 §e●").setLore("§f请右键打开!").build();
    }

    /**
     * Returns if the item is droppable.
     *
     * @param item The {@link ItemStack} to be checked.
     * @return Whether this item is droppable.
     */
    public static boolean isDroppable(ItemStack item) {
        return NBT.get(item, nbt ->
                (boolean) nbt.getBoolean("droppable")
        );
    }

    /**
     * Sets if the item is droppable.
     *
     * @param item The {@link ItemStack} to be set.
     * @param droppable Whether this item is droppable.
     */
    public static void setDroppable(ItemStack item, boolean droppable) {
        NBT.modify(item, nbt -> {
            nbt.setBoolean("droppable", droppable);
        });
    }

    /**
     * Returns if the item is clickable.
     *
     * @param item The {@link ItemStack} to be checked.
     * @return Whether this item is clickable.
     */
    public static boolean isClickable(ItemStack item) {
        return NBT.get(item, nbt ->
                (boolean) nbt.getBoolean("clickable")
        );
    }

    /**
     * Sets if the item is clickable.
     *
     * @param item The {@link ItemStack} to be set.
     * @param clickable Whether this item is clickable.
     */
    public static void setClickable(ItemStack item, boolean clickable) {
        NBT.modify(item, nbt -> {
            nbt.setBoolean("clickable", clickable);
        });
    }
}
