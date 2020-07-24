package me.thevipershow.poisonmodifier.config;

import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

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

    private final HashSet<PoisonModifierObject> modifiers = new HashSet<>();

    public void updateValues() {
        plugin.reloadConfig();
        modifiers.clear();
        for (final Map<?, ?> map : plugin.getConfig().getConfigurationSection("poison").getMapList("modifiers")) {
            final PoisonModifierObject p = PoisonModifierObject.deserialize((Map<String, Object>) map);
            modifiers.add(p);
        }
    }

    public final Optional<PoisonModifierObject> getPoisonModifierObject(final int level) {
        for (final PoisonModifierObject modifier : modifiers)
            if (modifier.getLevel() == level) return Optional.of(modifier);
        return Optional.empty();
    }

    public final Optional<Double> getLevelDamage(final int level) {
        for (final PoisonModifierObject modifier : modifiers)
            if (modifier.getLevel() == level)
                return Optional.of(modifier.getDamage());
        return Optional.empty();
    }

    public final Optional<Integer> getLevelSpeed(final int level) {
        for (final PoisonModifierObject modifier : modifiers)
            if (modifier.getLevel() == level)
                return Optional.of(modifier.getSpeed());
        return Optional.empty();
    }
}
