package fun.kaituo;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BoundingBox;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class GameUtils extends JavaPlugin implements Listener {
    private static final HashMap<Game, BoundingBox> gameBoundingBoxHashMap = new HashMap<>();
    private static final HashMap<Game, String> gameNameHashMap = new HashMap<>();
    private static final HashMap<UUID, PlayerQuitData> quitDataMap = new HashMap<>();
    public static final ArrayList<ItemFrame> ROTATABLE_ITEM_FRAMES = new ArrayList<>();
    public static World world;
    
    //For global game utilities
    public static void registerGame(Game game) {
        gameBoundingBoxHashMap.put(game, game.gameBoundingBox);
        gameNameHashMap.put(game, game.getName());
    }
    
    public static void unregisterGame(Game game) {
        gameBoundingBoxHashMap.remove(game);
        gameNameHashMap.remove(game);
    }
    
    public static Game getGamePlayerIsIn(Player p) {
        if (p.getWorld().getName().equals("uhc")) {
            return getGameByName("UHC");
        }
        for (Map.Entry<Game, BoundingBox> entry : gameBoundingBoxHashMap.entrySet()) {
            if (entry.getValue().contains(p.getLocation().toVector())) {
                return entry.getKey();
            }
        }
        return null;
    }
    
    public static Game getGameByName(String name) {
        for (Map.Entry<Game, String> entry : gameNameHashMap.entrySet()) {
            if (entry.getValue().equals(name)) {
                return entry.getKey();
            }
        }
        return null;
    }
    
    public static List<?> getRegisteredGames(boolean string) {
        if (string) {
            return Arrays.asList(gameNameHashMap.values().toArray());
        } else {
            return gameNameHashMap.keySet().stream().toList();
        }
    }
    
    public static PlayerQuitData getPlayerQuitData(UUID uuid) {
        return quitDataMap.get(uuid);
    }
    
    public static void setPlayerQuitData(UUID uuid, PlayerQuitData quitData) {
        if (quitData != null) {
            quitDataMap.put(uuid, quitData);
        } else {
            quitDataMap.remove(uuid);
        }
    }
    
    public void onEnable() {
        saveDefaultConfig();
        File dir = new File("plugins/GameUtils/world");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        dir = new File("plugins/GameUtils/uhc");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File rtSave = new File("plugins/GameUtils/rtsave.yml");
        if (!rtSave.exists()) {
            try {
                rtSave.createNewFile();
            } catch (Exception e) {
                getLogger().log(Level.SEVERE, "Failed to create rtsave.yml", e);
            }
        }
        try {
            ArrayList<String> rtSaveList = new Yaml().loadAs(new FileReader(rtSave), ArrayList.class);
            if (rtSaveList != null) {
                ROTATABLE_ITEM_FRAMES.addAll(rtSaveList.stream().map(uuid -> Bukkit.getWorld("world").getEntitiesByClass(ItemFrame.class).stream().filter(e -> e.getUniqueId().toString().equals(uuid)).findFirst().orElse(null)).filter(Objects::nonNull).collect(Collectors.toCollection(ArrayList::new)));
            }
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Failed to load rtsave.yml", e);
        }
        world = Bukkit.getWorld("world");
        GameUtilsCommandExecutor executor = new GameUtilsCommandExecutor(this);
        GameUtilsTabCompleter tabCompleter = new GameUtilsTabCompleter();
        getCommand("changebiome").setExecutor(executor);
        getCommand("changebiome").setTabCompleter(tabCompleter);
        getCommand("changegame").setExecutor(executor);
        getCommand("changegame").setTabCompleter(tabCompleter);
        getCommand("rejoin").setExecutor(executor);
        getCommand("placestand").setExecutor(executor);
        getCommand("rotatable").setExecutor(executor);
        getCommand("rotatable").setTabCompleter(tabCompleter);
        Bukkit.getPluginManager().registerEvents(new GameUtilsListener(this), this);
    }
    
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        HandlerList.unregisterAll((Plugin)this);
        try {
            ArrayList<String> rtSaveList = new ArrayList<>();
            ROTATABLE_ITEM_FRAMES.forEach(e -> rtSaveList.add(e.getUniqueId().toString()));
            new Yaml().dump(rtSaveList, new FileWriter("plugins/GameUtils/rtsave.yml"));
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Failed to save rtsave.yml", e);
        }
    }
}
