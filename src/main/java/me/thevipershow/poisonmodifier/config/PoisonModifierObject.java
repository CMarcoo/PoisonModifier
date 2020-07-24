package me.thevipershow.poisonmodifier.config;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.Map;

@SerializableAs("PoisonModifier")
public final class PoisonModifierObject implements ConfigurationSerializable {
    private final int level;
    private final double damage;
    private final int speed;

    public final int getLevel() {
        return level;
    }

    public final double getDamage() {
        return damage;
    }

    public final int getSpeed() {
        return speed;
    }

    public PoisonModifierObject(final int level, final double damage, final int speed) {
        this.level = level;
        this.damage = damage;
        this.speed = speed;
    }

    @Override
    public final Map<String, Object> serialize() {
        final Map<String, Object> map = new HashMap<>();
        map.put("level", level);
        map.put("damage", damage);
        map.put("speed", speed);
        return map;
    }

    public static PoisonModifierObject deserialize(final Map<String, Object> map) {
        final int level = (int) map.get("level");
        final double damage = (double) map.get("damage");
        final int speed = (int) map.get("speed");
        return new PoisonModifierObject(level, damage, speed);
    }
}
