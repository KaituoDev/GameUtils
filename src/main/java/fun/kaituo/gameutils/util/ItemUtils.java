package fun.kaituo.gameutils.util;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
public class ItemUtils {
    /**
     * Check if an inventory contains a certain item.
     * The amount of input item does not matter.
     *
     * @param inv The inventory to check
     * @param item The item to check for
     * @return True if the inventory contains the item, false otherwise
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
     * Check if an inventory contains a certain amount of a certain item.
     * The amount of input item does not matter.
     * Warning: This method serves a different purpose from {@link Inventory#contains(ItemStack, int)}
     * It does not check for the number of ItemStacks that exactly match the input ItemStack.
     * Instead, it checks for the sum of amount for all ItemStacks that are similar to the input ItemStack.
     * @param inv The inventory to check
     * @param item The item to check for
     * @param itemAmount The amount of the item to check for.
     * @return True if the inventory contains at least the specified amount of the item, false otherwise
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
     * Remove one certain item from an inventory.
     * The amount of input item does not matter.
     * @param inv The inventory to remove the item from
     * @param item The item to remove
     */
    public static void removeItem(Inventory inv, ItemStack item) {
        removeItem(inv, item, 1);
    }

    /**
     * Remove a certain amount of a certain item from an inventory.
     * The amount of input item does not matter.
     * Warning: This method does not remove the given number of ItemStacks that exactly match the input ItemStack.
     * Instead, it removes items that are similar to the input ItemStack until the specified amount is removed.
     * @param inv The inventory to remove the item from
     * @param item The item to remove
     * @param itemAmount The amount of the item to remove
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
}
