package fun.kaituo.gameutils.util;

import fun.kaituo.gameutils.GameUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HotbarMappingManager {
    public static final String CONFIG_SECTION_NAME = "hotbar-mappings";
    public static final HotbarMappingManager INSTANCE = new HotbarMappingManager();
    private HotbarMappingManager() {}

    private final Map<UUID, HotbarMapping> mappings = new HashMap<>();

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

    public void setMapping(UUID id, String mappingString) {
        HotbarMapping mapping = new HotbarMapping(mappingString);
        mappings.put(id, mapping);
    }

    public void resetMapping(UUID id) {
        mappings.remove(id);
    }

    public @Nonnull HotbarMapping getMapping(UUID id) {
        return mappings.getOrDefault(id, new HotbarMapping("123456789"));
    }
}
