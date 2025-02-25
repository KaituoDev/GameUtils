package fun.kaituo.gameutils.command;

import de.tr7zw.nbtapi.NBT;
import fun.kaituo.gameutils.GameUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static fun.kaituo.gameutils.util.Misc.getMatchingCompletions;

public class Rotatable implements CommandExecutor, TabCompleter {
    public static final String PERMISSION = "gameutils.command.rotatable";
    private final List<String> booleans = Arrays.asList("true", "false");

    public static boolean isRotatable(ItemFrame frame) {
        return NBT.getPersistentData(frame, nbt -> nbt.getBoolean("rotatable"));
    }

    public static void setRotatable(ItemFrame frame, boolean rotatable) {
        NBT.modifyPersistentData(frame, nbt -> {
            nbt.setBoolean("rotatable", rotatable);
        });
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, Command cmd, @Nonnull String label, @Nonnull String[] args) {
        if (!cmd.getName().equalsIgnoreCase("rotatable")) {
            return false;
        }

        if (!(sender instanceof Player p)) {
            sender.sendMessage("§c此指令必须由玩家执行！");
            return true;
        }
        if (!sender.hasPermission(PERMISSION)) {
            sender.sendMessage("§c你没有权限执行这个指令！");
            return true;
        }
        if (args.length > 1) {
            sender.sendMessage("§c指令参数错误！使用方法为/rotatable [true/false]");
            return true;
        }
        if (args.length == 1 && !booleans.contains(args[0].toLowerCase())) {
            sender.sendMessage("§c指令参数错误！使用方法为/rotatable [true/false]");
            return true;
        }
        RayTraceResult result = GameUtils.inst().getMainWorld().rayTraceEntities(
                p.getEyeLocation(),
                p.getEyeLocation().getDirection(),
                5,
                e -> e instanceof ItemFrame
        );
        if (result == null) {
            p.sendMessage("§c请面对物品展示框！");
            return true;
        }
        if (result.getHitEntity() == null) {
            p.sendMessage("§c请面对物品展示框！");
            return true;
        }
        ItemFrame frame = (ItemFrame) result.getHitEntity();
        boolean rotatable;
        if (args.length == 0) {
            rotatable = !isRotatable(frame);
        } else {
            rotatable = Boolean.parseBoolean(args[0]);
        }
        setRotatable(frame, rotatable);

        if (rotatable) {
            p.sendMessage("§f已设置物品展示框为§a可旋转");
        } else {
            p.sendMessage("§f已设置物品展示框为§c不可旋转");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender commandSender, Command command, @Nonnull String alias, @Nonnull String[] args) {
        if (!command.getName().equalsIgnoreCase("rotatable")) {
            return new ArrayList<>();
        }
        if (args.length != 1) {
            return new ArrayList<>();
        }
        return getMatchingCompletions(args[0], booleans);
    }
}
