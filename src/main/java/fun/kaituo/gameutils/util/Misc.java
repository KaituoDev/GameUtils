package fun.kaituo.gameutils.util;

import fun.kaituo.gameutils.GameUtils;
import fun.kaituo.gameutils.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@SuppressWarnings("unused")
public class Misc {
    private static final Random RANDOM = new Random();
    private static final Color[] fireworkColors = {
            Color.AQUA, Color.BLUE, Color.FUCHSIA, Color.GREEN, Color.LIME, Color.MAROON,
            Color.NAVY, Color.OLIVE, Color.ORANGE, Color.PURPLE, Color.RED, Color.SILVER,
            Color.TEAL, Color.WHITE, Color.YELLOW
    };

    public static ItemStack getMenu() {
        return new ItemStackBuilder(Material.CLOCK).setDisplayName("§e● §b§l菜单 §e●").setLore("§f请右键打开!").build();
    }

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

    @SuppressWarnings("unused")
    public static Set<Integer> spawnFireworks(Player p, Game game) {
        Set<Integer> taskIds = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            taskIds.add(Bukkit.getScheduler().runTaskLater(game, () -> spawnFirework(p), 8 * i + 1).getTaskId());
        }
        return taskIds;
    }

    @SuppressWarnings("unused")
    public static Set<Integer> displayCountDown(Player p, int countDownSeconds, Game game) {
        if (countDownSeconds > 5) {
            p.sendTitle("§a游戏还有 " + countDownSeconds + " 秒开始", null, 2, 16, 2);
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1f, 1f);
        }
        Set<Integer> taskIds = new HashSet<>();
        for (int i = 5; i > 0; i--) {
            int finalI = i;
            taskIds.add(Bukkit.getScheduler().runTaskLater(game, () -> {
                if (!p.isOnline()) {
                    return;
                }
                p.sendTitle("§a游戏还有 " + finalI + " 秒开始", null, 2, 16, 2);
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1f, 1f);
                p.getInventory().clear();
            }, 20L * countDownSeconds - 20L * i).getTaskId());
        }
        taskIds.add(Bukkit.getScheduler().runTaskLater(GameUtils.inst(), () -> {
            if (!p.isOnline()) {
                return;
            }
            p.sendTitle("§e游戏开始！", null, 2, 16, 2);
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1f, 2f);
        }, 20L * countDownSeconds).getTaskId());
        return taskIds;
    }

    public static List<String> getMatchingCompletions(String partialArg, List<String> completions) {
        return completions.stream().filter(completion ->
                completion.toLowerCase().startsWith(partialArg.toLowerCase())).toList();
    }

}
