package me.thevipershow.poisonmodifier.config;

import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public final class Values {

    private final HashMap<Integer, Double> modifiers = new HashMap<>();

    public void updateValues(final Plugin plugin) {
        plugin.reloadConfig();
        modifiers.clear();
        for (final Map<?, ?> map : plugin.getConfig().getConfigurationSection("poison").getMapList("modifiers")) {
            final PoisonModifierObject p = PoisonModifierObject.deserialize((Map<String, Object>) map);
            modifiers.put(p.getLevel(), p.getDamage());
        }
    }

    public final Double getLevelDamage(final int level) {
        return modifiers.get(level);
    }
}
