package me.thevipershow.poisonmodifier;

import me.thevipershow.poisonmodifier.config.PoisonModifierObject;
import me.thevipershow.poisonmodifier.config.Values;
import me.thevipershow.poisonmodifier.listener.DamageListener;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

public enum PoisonModifierPlugin {
    INSTANCE;

    private Plugin plugin;

    public final void start(final Plugin plugin) {
        ConfigurationSerialization.registerClass(PoisonModifierObject.class);
        this.plugin = Objects.requireNonNull(plugin, "Plugin cannot be null!");
        plugin.saveDefaultConfig();
        final Values values = new Values();
        values.updateValues(plugin);
        plugin.getServer().getPluginManager().registerEvents(new DamageListener(values), plugin);
    }

    public final void stop() {

    }
}
