package fun.kaituo.gameutils.util;

import fun.kaituo.gameutils.GameUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a manager for hotbar mappings.
 */
public class HotbarMappingManager {
    /**
     * The name of the configuration section for hotbar mappings.
     */
    public static final String CONFIG_SECTION_NAME = "hotbar-mappings";
    /**
     * The singleton instance of the HotbarMappingManager.
     */
    public static final HotbarMappingManager INSTANCE = new HotbarMappingManager();
    private HotbarMappingManager() {}

    private final Map<UUID, HotbarMapping> mappings = new HashMap<>();

    /**
     * Loads the hotbar mappings from the configuration file.
     *
     * @param plugin The GameUtils plugin instance to load mappings from.
     */
    public void loadMappings(GameUtils plugin) {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection(CONFIG_SECTION_NAME);
        if (section == null) {
            return;
        }
        for (String key : section.getKeys(false)) {
            UUID id = UUID.fromString(key);
            String mappingString = section.getString(key);
            mappings.put(id, new HotbarMapping(mappingString));
        }
    }

    /**
     * Saves the hotbar mappings to the configuration file.
     *
     * @param plugin The GameUtils plugin instance to save mappings to.
     */
    public void saveMappings(GameUtils plugin) {
        FileConfiguration config = plugin.getConfig();
        ConfigurationSection section = config.getConfigurationSection(CONFIG_SECTION_NAME);
        if (section == null) {
            config.createSection(CONFIG_SECTION_NAME);
        }
        for (Map.Entry<UUID, HotbarMapping> entry : mappings.entrySet()) {
            String path = CONFIG_SECTION_NAME + "." + entry.getKey().toString();
            String value = entry.getValue().toString();
            config.set(path, value);
        }
        plugin.saveConfig();
    }

    /**
     * Sets the hotbar mapping for a player with the given UUID.
     *
     * @param id The UUID of the player.
     * @param mappingString The mapping string to set.
     */
    public void setMapping(UUID id, String mappingString) {
        HotbarMapping mapping = new HotbarMapping(mappingString);
        mappings.put(id, mapping);
    }

    /**
     * Resets the hotbar mapping for a player with the given UUID.
     *
     * @param id The UUID of the player.
     */
    public void resetMapping(UUID id) {
        mappings.remove(id);
    }

    /**
     * Gets the hotbar mapping for a player with the given UUID.
     *
     * @param id The UUID of the player.
     * @return The hotbar mapping for the player.
     */
    public @Nonnull HotbarMapping getMapping(UUID id) {
        return mappings.getOrDefault(id, new HotbarMapping("123456789"));
    }
}
