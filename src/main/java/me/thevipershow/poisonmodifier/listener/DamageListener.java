package me.thevipershow.poisonmodifier.listener;

import me.thevipershow.poisonmodifier.config.PoisonModifierObject;
import me.thevipershow.poisonmodifier.config.Values;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import static org.bukkit.event.entity.EntityPotionEffectEvent.Action.*;
import static org.bukkit.event.entity.EntityDamageEvent.DamageCause.*;

public final class DamageListener implements Listener {

    private final Values values;
    private final Plugin plugin;

    public DamageListener(final Values values, final Plugin plugin) {
        this.values = values;
        this.plugin = plugin;
    }

    private final HashMap<UUID, EntityCustomPoison> playerPoison = new HashMap<>();

    public final class EntityCustomPoison {
        private final Plugin plugin;
        private final UUID entityUUID;
        private BukkitTask task = null;
        private PoisonModifierObject poisonModifierObject;
        private boolean isRunning = false;

        public final boolean isRunning() {
            return isRunning;
        }

        public EntityCustomPoison(final Plugin plugin, final UUID entityUUID) {
            this.plugin = plugin;
            this.entityUUID = entityUUID;
        }

        public final void start(final PoisonModifierObject object) {
            if (object == null) return;
            poisonModifierObject = object;
            // System.out.println("Starting runnable.");
            task = plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
                final Entity entity = Bukkit.getEntity(entityUUID);
                if (entity == null) {
                    stop();
                    return;
                }
                if (!(entity instanceof LivingEntity)) {
                    stop();
                    return;
                }
                isRunning = true;
                final double damageToApply = object.getDamage();
                final LivingEntity livingEntity = (LivingEntity) entity;

                final int noDamageTicks = livingEntity.getNoDamageTicks();
                livingEntity.setNoDamageTicks(0);
                // System.out.println(String.format("Damaging entity: %f", damageToApply));
                livingEntity.damage(damageToApply);
                livingEntity.setNoDamageTicks(noDamageTicks);

                if (livingEntity.getHealth() - damageToApply < 0.d) {
                    stop();
                }
            }, 1L, object.getSpeed());
        }

        public final void mutate(final PoisonModifierObject object) {
            if (object == null) return;
            // System.out.println(String.format("mutated from %f to %f", poisonModifierObject.getDamage(), object.getDamage()));
            poisonModifierObject = object;
            stop();
            start(object);
        }

        public final void stop() {
            Objects.requireNonNull(task, "Cannot stop a null task!").cancel();
            isRunning = false;
            playerPoison.remove(entityUUID);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onEntityPotionEffect(final EntityPotionEffectEvent event) {

        final Entity entity = event.getEntity();
        if (!(entity instanceof LivingEntity)) return;
        final LivingEntity livingEntity = (LivingEntity) entity;
        final UUID uuid = livingEntity.getUniqueId();

        final EntityPotionEffectEvent.Action action = event.getAction();
        final EntityPotionEffectEvent.Cause cause = event.getCause();

        if (action == REMOVED || action == CLEARED) {
            final EntityCustomPoison poison = playerPoison.get(uuid);
            if (poison == null) return;
            poison.stop();
        } else {
            final PotionEffect potionEffect = event.getNewEffect();
            if (potionEffect == null) return; // this should never be null in this situation.

            final PotionEffectType potionEffectType = potionEffect.getType();
            if (!potionEffectType.equals(PotionEffectType.POISON)) return;

            final int level = potionEffect.getAmplifier();
            if (action == ADDED) {
                // System.out.println("ADDED Poison");
                values.getPoisonModifierObject(level).ifPresent(o -> {
                    // System.out.println(String.format("PoisonModifierObject present: %s", o.toString()));
                    final EntityCustomPoison entityCustomPoison = new EntityCustomPoison(plugin, uuid);
                    playerPoison.putIfAbsent(uuid, entityCustomPoison);
                    entityCustomPoison.start(o);
                });
            } else if (action == CHANGED) {
                values.getPoisonModifierObject(level).ifPresent(o -> {
                    final EntityCustomPoison entityCustomPoison = playerPoison.get(uuid);
                    if (entityCustomPoison == null) return;
                    entityCustomPoison.mutate(o);
                });
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onEntityDamage(final EntityDamageEvent event) {

        final Entity entity = event.getEntity();
        if (!(entity instanceof LivingEntity)) return;
        final LivingEntity livingEntity = (LivingEntity) entity;
        final UUID uuid = livingEntity.getUniqueId();

        final EntityDamageEvent.DamageCause cause = event.getCause();
        if (cause != POISON) return;
        final EntityCustomPoison entityCustomPoison = playerPoison.get(uuid);
        if (entityCustomPoison == null) return;
        if (entityCustomPoison.isRunning) event.setCancelled(true);
    }
}
