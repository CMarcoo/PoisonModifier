package me.thevipershow.poisonmodifier;

import org.bukkit.plugin.java.JavaPlugin;

public final class PoisonModifier extends JavaPlugin {

    @Override
    public void onEnable() {
        PoisonModifierPlugin.INSTANCE.start(this);
    }

    @Override
    public void onDisable() {
        PoisonModifierPlugin.INSTANCE.stop();
    }
}
