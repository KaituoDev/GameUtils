package fun.kaituo.gameutils;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;
import java.util.logging.Level;

@SuppressWarnings("ConstantConditions")
public class GameUtils extends JavaPlugin implements Listener {

    ItemStack menu;

    private final HashMap<Player, Game> playerGameMap = new HashMap<>();
    private final HashMap<String, Game> nameGameMap = new HashMap<>();
    private final HashMap<UUID, PlayerQuitData> quitDataMap = new HashMap<>();
    public static final ArrayList<String> ROTATABLE_ITEM_FRAME_UUID_STRINGS = new ArrayList<>();
    private World world;

    public World getWorld() {
        return world;
    }

    //For global game utilities
    public void registerGame(Game game) {
        nameGameMap.put(game.getName(), game);
    }

    @SuppressWarnings("unused")
    public void unregisterGame(Game game) {
        nameGameMap.remove(game.getName());
    }
    
    public Game getPlayerGame(Player p) {
        return playerGameMap.get(p);
    }
    
    public Game getGame(String name) {
        return nameGameMap.get(name);
    }

    public void setPlayerGame(Player p, Game game) {
        playerGameMap.put(p, game);
    }

    public void removePlayerGame(Player p) {
        playerGameMap.remove(p);
    }
    
    public List<Game> getRegisteredGames() {
        return nameGameMap.values().stream().toList();
    }
    
    public PlayerQuitData getPlayerQuitData(UUID uuid) {
        return quitDataMap.get(uuid);
    }

    @SuppressWarnings({"unused"})
    public void setPlayerQuitData(UUID uuid, PlayerQuitData quitData) {
        if (quitData != null) {
            quitDataMap.put(uuid, quitData);
        } else {
            quitDataMap.remove(uuid);
        }
    }
    @SuppressWarnings("deprecation")
    public void resetPlayer(Player p) {
        if (!p.getGameMode().equals(GameMode.CREATIVE)) {
            p.setGameMode(GameMode.ADVENTURE);
            p.getInventory().clear();
            p.getInventory().setItem(0, menu);
            for (PotionEffect effect : p.getActivePotionEffects()) {
                p.removePotionEffect(effect.getType());
            }
        }
        p.resetPlayerTime();
        p.resetPlayerWeather();
        p.setMaximumNoDamageTicks(20);
        p.resetMaxHealth();
        p.setHealth(20);
        p.setLevel(0);
        p.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        for (Team team : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()) {
            team.removePlayer(p);
        }
        p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 1, 0, false, false));
        p.setInvisible(true);
        Bukkit.getScheduler().runTaskLater(this, () -> p.setInvisible(false), 1);
    }
    
    public void onEnable() {
        saveDefaultConfig();

        this.menu = new ItemStack(Material.CLOCK, 1);
        ItemMeta itemMeta = menu.getItemMeta();
        itemMeta.setDisplayName("§e● §b§l菜单 §e●");
        itemMeta.setLore(List.of("§f请右键打开!"));
        menu.setItemMeta(itemMeta);

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
                ROTATABLE_ITEM_FRAME_UUID_STRINGS.addAll(rtSaveList);
            }
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Failed to load rtsave.yml", e);
        }
        world = Bukkit.getWorld("world");
        GameUtilsCommandExecutor executor = new GameUtilsCommandExecutor(this);
        GameUtilsTabCompleter tabCompleter = new GameUtilsTabCompleter(this);
        getCommand("changebiome").setExecutor(executor);
        getCommand("changebiome").setTabCompleter(tabCompleter);
        getCommand("join").setExecutor(executor);
        getCommand("join").setTabCompleter(tabCompleter);
        getCommand("rejoin").setExecutor(executor);
        getCommand("placestand").setExecutor(executor);
        getCommand("forcestop").setExecutor(executor);
        getCommand("forcestop").setTabCompleter(tabCompleter);
        getCommand("rotatable").setExecutor(executor);
        getCommand("rotatable").setTabCompleter(tabCompleter);
        Bukkit.getPluginManager().registerEvents(new GameUtilsListener(this), this);

        registerGame(Lobby.getInstance());
    }

    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        HandlerList.unregisterAll((Plugin)this);
        try {
            new Yaml().dump(ROTATABLE_ITEM_FRAME_UUID_STRINGS, new FileWriter("plugins/GameUtils/rtsave.yml"));
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Failed to save rtsave.yml", e);
        }
    }
}
