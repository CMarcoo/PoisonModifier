package me.thevipershow.poisonmodifier.listener;

import me.thevipershow.poisonmodifier.config.Values;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
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
        final Entity entity = event.getEntity();
        if (!(entity instanceof LivingEntity)) return;
        final LivingEntity lEntity = (LivingEntity) entity;
        final PotionEffect effect = lEntity.getPotionEffect(PotionEffectType.POISON);
        if (effect == null) return;
        final int level = effect.getAmplifier();
        final Double modifier = values.getLevelDamage(level);
        if (modifier == null) return;
        if (lEntity.getHealth() - modifier <= 1.d) return;
        // System.out.println(String.format("I'm damaging %s with %.3f", lEntity.getUID(), modifier));
        event.setDamage(modifier);
    }
}
