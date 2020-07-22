package me.thevipershow.poisonmodifier.config;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public final class Values {
    private static Values instance = null;
    private final Plugin plugin;

    private Values(final Plugin plugin) {
        this.plugin = plugin;
    }

    public static Values getInstance(final Plugin plugin) {
        if (instance == null)
            instance = new Values(plugin);
        return instance;
    }

    private final HashMap<Integer, Double> modifiers = new HashMap<>();

    public void updateValues() {
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
