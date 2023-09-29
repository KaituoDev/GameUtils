package fun.kaituo.gameutils;

import fun.kaituo.gameutils.event.PlayerEndGameEvent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.*;

@SuppressWarnings({"ConstantConditions", "unused"})
public abstract class Game {
    protected List<Player> players = new ArrayList<>();
    protected List<Integer> taskIds = new ArrayList<>();
    protected Random random = new Random();
    protected JavaPlugin plugin;
    protected GameUtils gameUtils;
    protected World world = Bukkit.getWorld("world");

    protected String name;

    public String getName() {
        return name;
    }

    protected String fullName;

    public String getFullName() {
        return fullName;
    }

    protected Location hubLocation;
    protected UUID gameUUID;
    protected Runnable gameRunnable;

    protected Location startButtonLocation;
    protected BlockFace startButtonDirection;
    protected Location spectateButtonLocation;
    protected BlockFace spectateButtonDirection;

    protected Color[] fireworkColors = {Color.AQUA, Color.BLUE, Color.FUCHSIA, Color.GREEN, Color.LIME, Color.MAROON, Color.NAVY, Color.OLIVE, Color.ORANGE, Color.PURPLE, Color.RED, Color.SILVER, Color.TEAL, Color.WHITE, Color.YELLOW};


    //Initialize game
    @SuppressWarnings("SameParameterValue")
    protected void initializeGame(JavaPlugin gamePlugin, String name, String fullName, Location hubLocation) {
        this.gameUtils = (GameUtils) Bukkit.getPluginManager().getPlugin("GameUtils");
        this.plugin = gamePlugin;
        this.name = name;
        this.fullName = fullName;
        this.hubLocation = hubLocation;
        this.gameUUID = UUID.randomUUID();
    }


    protected void initializeButtons(Location startButtonLocation, BlockFace startButtonDirection, Location spectateButtonLocation, BlockFace spectateButtonDirection) {
        this.startButtonLocation = startButtonLocation;
        this.startButtonDirection = startButtonDirection;
        this.spectateButtonLocation = spectateButtonLocation;
        this.spectateButtonDirection = spectateButtonDirection;
    }


    //For game utilities
    public void startGame() {
        Bukkit.getScheduler().runTask(plugin, gameRunnable);
    }


    protected void cancelGameTasks() {
        List<Integer> taskIdsCopy = new ArrayList<>(taskIds);
        taskIds.clear();
        for (int i : taskIdsCopy) {
            Bukkit.getScheduler().cancelTask(i);
        }
    }

    protected long getTime(World world) {
        return (world.getGameTime());
    }

    protected void spawnFireworks(Player p) {
        for (int i = 0; i < 5; i++) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> spawnFirework(p), 8 * i + 1);
        }
    }

    protected void spawnFirework(Player p) {
        Location loc = p.getLocation();
        loc.setY(loc.getY() + 0.9);
        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        fw.addScoreboardTag("gameFirework");
        FireworkMeta fwm = fw.getFireworkMeta();
        fwm.setPower(2);
        fwm.addEffect(FireworkEffect.builder().withColor(fireworkColors[random.nextInt(fireworkColors.length)]).flicker(true).build());
        fw.setFireworkMeta(fwm);
        fw.detonate();
    }

    //For game logic
    protected Collection<Player> getPlayersNearHub(double xRadius, double yRadius, double zRadius) {
        Collection<Player> result = new ArrayList<>();
        for (Entity e : world.getNearbyEntities(hubLocation, xRadius, yRadius, zRadius, (e) -> e instanceof Player)) {
            result.add((Player) e);
        }
        return result;
    }


    protected void placeStartButton() {
        Block block = world.getBlockAt(startButtonLocation);
        block.setType(Material.OAK_BUTTON);
        BlockData data = block.getBlockData().clone();
        ((Directional) data).setFacing(startButtonDirection);
        block.setBlockData(data);
    }

    protected void removeStartButton() {
        world.getBlockAt(startButtonLocation).setType(Material.AIR);
    }

    protected void placeSpectateButton() {
        Block block = world.getBlockAt(spectateButtonLocation);
        block.setType(Material.OAK_BUTTON);
        BlockData data = block.getBlockData().clone();
        ((Directional) data).setFacing(spectateButtonDirection);
        block.setBlockData(data);
    }

    protected void removeSpectateButton() {
        world.getBlockAt(spectateButtonLocation).setType(Material.AIR);
    }

    protected void startCountdown(int countDownSeconds) {
        if (countDownSeconds > 5) {
            Bukkit.getScheduler().runTask(plugin, () -> {
                for (Player p : players) {
                    p.sendTitle("§a游戏还有 " + countDownSeconds + " 秒开始", null, 2, 16, 2);
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1f, 1f);
                    p.getInventory().clear();
                }
            });
        }
        for (int i = 5; i > 0; i--) {
            int finalI = i;
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                for (Player p : players) {
                    p.sendTitle("§a游戏还有 " + finalI + " 秒开始", null, 2, 16, 2);
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1f, 1f);
                    p.getInventory().clear();
                }
            }, 20L * countDownSeconds - 20L * i);
        }
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (Player p : players) {
                p.sendTitle("§e游戏开始！", null, 2, 16, 2);
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1f, 2f);
            }
        }, 20L * countDownSeconds);
    }


    //For per-game override usage

    //Save playerQuitData for rejoining
    protected abstract void quit(Player p) throws IOException;

    //Called when player uses /rejoin command
    protected abstract boolean rejoin(Player p);

    //Called when player joins this game
    protected abstract boolean join(Player p);

    protected abstract void forceStop();

    //Better messages
    protected void shout(String message) {
        Bukkit.broadcastMessage(this.fullName + " " + message);
    }
}