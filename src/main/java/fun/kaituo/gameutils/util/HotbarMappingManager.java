package fun.kaituo.gameutils.util;

import fun.kaituo.gameutils.GameUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * <p>HotbarMappingManager class.</p>
 *
 * @author DELL
 */
public class HotbarMappingManager {
    /** Constant <code>CONFIG_SECTION_NAME="hotbar-mappings"</code> */
    public static final String CONFIG_SECTION_NAME = "hotbar-mappings";
    /** Constant <code>INSTANCE</code> */
    public static final HotbarMappingManager INSTANCE = new HotbarMappingManager();
    private HotbarMappingManager() {}

    private final Map<UUID, HotbarMapping> mappings = new HashMap<>();

    /**
     * <p>loadMappings.</p>
     *
     * @param plugin a {@link fun.kaituo.gameutils.GameUtils} object
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
     * <p>saveMappings.</p>
     *
     * @param plugin a {@link fun.kaituo.gameutils.GameUtils} object
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
     * <p>setMapping.</p>
     *
     * @param id a {@link java.util.UUID} object
     * @param mappingString a {@link java.lang.String} object
     */
    public void setMapping(UUID id, String mappingString) {
        HotbarMapping mapping = new HotbarMapping(mappingString);
        mappings.put(id, mapping);
    }

    /**
     * <p>resetMapping.</p>
     *
     * @param id a {@link java.util.UUID} object
     */
    public void resetMapping(UUID id) {
        mappings.remove(id);
    }

    /**
     * <p>getMapping.</p>
     *
     * @param id a {@link java.util.UUID} object
     * @return a {@link fun.kaituo.gameutils.util.HotbarMapping} object
     */
    public @Nonnull HotbarMapping getMapping(UUID id) {
        return mappings.getOrDefault(id, new HotbarMapping("123456789"));
    }
}
