package io.github.bnnuycorps.oasisbar.Thirst;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.effect.StatusEffectInstance;

import java.util.List;

public class DrinkComponent {
    public final int thirst;
    private final float watersaturationModifier;
    private final boolean alwaysEdible;
    private final List<Pair<StatusEffectInstance, Float>> statusEffects;

    DrinkComponent(int thirst, float saturationModifier, boolean alwaysEdible, List<Pair<StatusEffectInstance, Float>> statusEffects){
       this.thirst = thirst;
       this.watersaturationModifier = saturationModifier;
        this.alwaysEdible = alwaysEdible;
       this.statusEffects = statusEffects;
    }

    public int getThirst() {return this.thirst; }
    public float getWaterSaturationModifier() {
        return this.watersaturationModifier;
    }
    public boolean isAlwaysEdible() {
        return this.alwaysEdible;
    }
    public List<Pair<StatusEffectInstance, Float>> getStatusEffects() {
        return this.statusEffects;
    }

    public static class Builder {
        public int thirst;
        public float saturationModifier;
        public boolean alwaysEdible;
        public final List<Pair<StatusEffectInstance, Float>> statusEffects = Lists.newArrayList();

        public Builder() {
        }

        public DrinkComponent.Builder thirst(int thirst) {
            this.thirst = thirst;
            return this;
        }

        public DrinkComponent.Builder saturationModifier(float saturationModifier) {
            this.saturationModifier = saturationModifier;
            return this;
        }

        public DrinkComponent.Builder alwaysEdible() {
            this.alwaysEdible = true;
            return this;
        }

        public DrinkComponent.Builder statusEffect(StatusEffectInstance effect, float chance) {
            this.statusEffects.add(Pair.of(effect, chance));
            return this;
        }

        public DrinkComponent build() {
            return new DrinkComponent(this.thirst, this.saturationModifier, this.alwaysEdible, this.statusEffects);
        }
    }

}
