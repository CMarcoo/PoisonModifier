package me.thevipershow.poisonmodifier.listener;

import me.thevipershow.poisonmodifier.config.Values;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class DamageListener implements Listener {

    private final Values values;

    public DamageListener(final Values values) {
        this.values = values;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onEntityDamage(EntityDamageEvent event) {
        final EntityDamageEvent.DamageCause cause = event.getCause();
        if (cause != EntityDamageEvent.DamageCause.POISON) return;
        if (!(event.getEntity() instanceof Player)) return;
        final Player player = (Player) event.getEntity();
        final PotionEffect effect = player.getPotionEffect(PotionEffectType.POISON);
        if (effect == null) return;
        final int level = effect.getAmplifier();
        final Double modifier = values.getLevelDamage(level);
        if (modifier == null) return;
        if (player.getHealth() - modifier <= 1.d) return;
        // System.out.println(String.format("I'm damaging %s with %.3f", player.getName(), modifier));
        event.setDamage(modifier);
    }
}
