package fun.kaituo.gameutils.util;


import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import javax.annotation.Nonnull;
import java.util.*;

@SuppressWarnings("unused")
public class PlayerMap<V> implements Map<OfflinePlayer, V> {
    private final Map<UUID, V> map = new HashMap<>();

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof OfflinePlayer offlinePlayer)) {
            return false;
        }
        return map.containsKey(offlinePlayer.getUniqueId());
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public V get(Object key) {
        if (!containsKey(key)) {
            return null;
        }
        return map.get(((OfflinePlayer) key).getUniqueId());
    }

    @Override
    public V put(OfflinePlayer key, V value) {
        V oldValue = get(key);
        map.put(key.getUniqueId(), value);
        return oldValue;
    }

    @Override
    public V remove(Object key) {
        if (!containsKey(key)) {
            return null;
        }
        return map.remove(((OfflinePlayer) key).getUniqueId());
    }

    @Override
    public void putAll(@Nonnull Map<? extends OfflinePlayer, ? extends V> m) {

    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public @Nonnull Set<OfflinePlayer> keySet() {
        Set<OfflinePlayer> keySet = new HashSet<>();
        for (UUID uuid: map.keySet()) {
            keySet.add(Bukkit.getOfflinePlayer(uuid));
        }
        return keySet;
    }

    @Override
    public @Nonnull Collection<V> values() {
        return map.values();
    }

    @Override
    public @Nonnull Set<Entry<OfflinePlayer, V>> entrySet() {
        Set<Entry<OfflinePlayer, V>> entrySet = new HashSet<>();
        for (UUID uuid: map.keySet()) {
            entrySet.add(new AbstractMap.SimpleEntry<>(Bukkit.getOfflinePlayer(uuid), map.get(uuid)));
        }
        return entrySet;
    }
}
