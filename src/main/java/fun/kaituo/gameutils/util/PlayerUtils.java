package fun.kaituo.gameutils.util;

import fun.kaituo.gameutils.GameUtils;
import fun.kaituo.gameutils.game.Game;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Helper class for player-related utilities.
 *
 * @author DELL
 * @since 2.0.1
 */
public class PlayerUtils {
    private static final Random RANDOM = new Random();
    private static final Color[] fireworkColors = {
            Color.AQUA, Color.BLUE, Color.FUCHSIA, Color.GREEN, Color.LIME, Color.MAROON,
            Color.NAVY, Color.OLIVE, Color.ORANGE, Color.PURPLE, Color.RED, Color.SILVER,
            Color.TEAL, Color.WHITE, Color.YELLOW
    };

    /**
     * Private constructor to prevent instantiation.
     */
    private PlayerUtils() {

    }

    /**
     * Spawns fireworks around a player.
     *
     * @param p The player to spawn fireworks around.
     */
    public static void spawnFirework(Player p) {
        Location loc = p.getLocation();
        loc.setY(loc.getY() + 0.9);
        if (loc.getWorld() == null) {
            throw new RuntimeException("Trying to spawn firework on player, but player is not in a valid world");
        }
        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK_ROCKET);
        fw.addScoreboardTag("gameFirework");
        FireworkMeta fwm = fw.getFireworkMeta();
        fwm.setPower(2);
        fwm.addEffect(FireworkEffect.builder().withColor(fireworkColors[RANDOM.nextInt(fireworkColors.length)]).flicker(true).build());
        fw.setFireworkMeta(fwm);
        fw.detonate();
    }

    /**
     * Spawns fireworks around a player from the perspective of a game.
     *
     * @param p The player to spawn fireworks around.
     * @param game The game responsible for the fireworks.
     * @return The scheduled firework detonating task ids.
     */
    @SuppressWarnings("unused")
    public static Set<Integer> spawnFireworks(Player p, Game game) {
        Set<Integer> taskIds = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            taskIds.add(Bukkit.getScheduler().runTaskLater(game, () -> spawnFirework(p), 8 * i + 1).getTaskId());
        }
        return taskIds;
    }

    /**
     * Display a countdown for the player with given title and subtitle.
     *
     * @param p The player to display the countdown to.
     * @param countdownSeconds The number of seconds to count down.
     * @param countdownTitle The title when the countdown is running. Use %time% as a placeholder for the time left.
     * @param finishTitle The title when the countdown finishes.
     * @param game The game instance that should schedule the countdown tasks.
     * @return A set of task ids that can be used to cancel the countdown display.
     */
    @SuppressWarnings("unused")
    public static Set<Integer> displayCountdown(Player p, int countdownSeconds, String countdownTitle, String finishTitle, Game game) {
        if (countdownSeconds > 5) {
            p.sendTitle(countdownTitle.replace("%time%", String.valueOf(countdownSeconds)), "", 2, 16, 2);
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1f, 1f);
        }
        Set<Integer> taskIds = new HashSet<>();
        for (int i = 5; i > 0; i--) {
            int countdownSecondsRemaining = i;
            taskIds.add(Bukkit.getScheduler().runTaskLater(game, () -> {
                if (!p.isOnline()) {
                    return;
                }
                p.sendTitle(countdownTitle.replace("%time%", String.valueOf(countdownSecondsRemaining)), "", 2, 16, 2);
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1f, 1f);
            }, 20L * countdownSeconds - 20L * i).getTaskId());
        }
        taskIds.add(Bukkit.getScheduler().runTaskLater(GameUtils.inst(), () -> {
            if (!p.isOnline()) {
                return;
            }
            p.sendTitle(finishTitle, "", 2, 16, 2);
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1f, 2f);
        }, 20L * countdownSeconds).getTaskId());
        return taskIds;
    }

    /**
     * Display a countdown for the player with given title and subtitle.
     *
     * @param p The player to display the countdown to.
     * @param countdownSeconds The number of seconds to count down.
     * @param game The game responsible for the countdown.
     * @return The scheduled countdown task ids.
     */
    @SuppressWarnings("unused")
    public static Set<Integer> displayCountdown(Player p, int countdownSeconds, Game game) {
        return displayCountdown(p, countdownSeconds, "§a游戏还有 %time% 秒开始", "§e游戏开始！", game);
    }
}
