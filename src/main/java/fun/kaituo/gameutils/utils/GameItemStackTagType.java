package fun.kaituo.gameutils.utils;

import fun.kaituo.gameutils.GameUtils;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;

public class GameItemStackTagType implements PersistentDataType<String, GameItemStackTag> {
    @Override
    @Nonnull
    public Class<String> getPrimitiveType() {
        return String.class;
    }

    @Override
    @Nonnull
    public Class<GameItemStackTag> getComplexType() {
        return GameItemStackTag.class;
    }

    @Override
    @Nonnull
    public String toPrimitive(@Nonnull GameItemStackTag gameItemStackTag, @Nonnull PersistentDataAdapterContext persistentDataAdapterContext) {
        return GameUtils.gson.toJson(gameItemStackTag);
    }

    @Override
    @Nonnull
    public GameItemStackTag fromPrimitive(@Nonnull String s, @Nonnull PersistentDataAdapterContext persistentDataAdapterContext) {
        return GameUtils.gson.fromJson(s, GameItemStackTag.class);
    }
}
