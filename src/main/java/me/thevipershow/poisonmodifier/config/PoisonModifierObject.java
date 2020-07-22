package me.thevipershow.poisonmodifier.config;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("PoisonModifier")
public final class PoisonModifierObject implements ConfigurationSerializable {
    private final int level;
    private final double damage;

    public final int getLevel() {
        return level;
    }

    public final double getDamage() {
        return damage;
    }

    public PoisonModifierObject(final int level, final double damage) {
        this.level = level;
        this.damage = damage;
    }

    @Override
    public final Map<String, Object> serialize() {
        final Map<String, Object> map = new HashMap<>();
        map.put("level", level);
        map.put("damage", damage);
        return map;
    }

    public static PoisonModifierObject deserialize(final Map<String, Object> map) {
        final int level = (int) map.get("level");
        final double damage = (double) map.get("damage");
        return new PoisonModifierObject(level, damage);
    }
}
