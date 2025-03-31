package fun.kaituo.gameutils.util;

import lombok.Getter;

import java.util.Arrays;

public class HotbarMapping {
    @Getter
    private final String mapping;
    public HotbarMapping(String mapping) {
        if (!isValidMapping(mapping)) {
            throw new IllegalArgumentException("Invalid mapping string: " + mapping);
        }
        this.mapping = mapping;
    }

    /**
     * Check if a mapping string is valid.
     * A valid mapping string is a permutation of "123456789",
     * no repeated digits, and exactly 9 digits.
     * @param mapping The mapping string to check.
     * @return True if the mapping is valid, false otherwise.
     */
    public static boolean isValidMapping(String mapping) {
        if (mapping.length() != 9) {
            return false;
        }
        char[] chars = mapping.toCharArray();
        Arrays.sort(chars);
        return new String(chars).equals("123456789");
    }

    /**
     * Map a slot number to the corresponding slot in the hotbar.
     * Example: map(0) -> 4 means the ItemStack in hotbar slot 0 (the 1st slot)
     * should be put into hotbar slot 4 (the 5th slot) in game.
     * @param slot The slot number to map.
     * @return The corresponding slot in the hotbar in game.
     */
    public int map(int slot) {
        if (slot < 0 || slot > 8) {
            throw new IllegalArgumentException("Slot must be between 0 and 8");
        }
        return mapping.indexOf(String.valueOf(slot + 1));
    }

    /**
     * Reverse map a slot in the hotbar to the corresponding slot number.
     * Example: reverseMap(4) -> 0 means the ItemStack in hotbar slot 4 (the 5th slot) in game
     * should be put into GameInventory slot 0.
     * @param slot The slot in the hotbar to reverse map.
     * @return The corresponding slot number in GameInventory.
     */
    public int reverseMap(int slot) {
        if (slot < 0 || slot > 8) {
            throw new IllegalArgumentException("Slot must be between 0 and 8");
        }
        return mapping.toCharArray()[slot] - '0' - 1;
    }

    @Override
    public String toString() {
        return mapping;
    }
}
