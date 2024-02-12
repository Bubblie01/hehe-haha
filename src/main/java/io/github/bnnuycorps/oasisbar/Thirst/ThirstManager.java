package io.github.bnnuycorps.oasisbar.Thirst;

import io.github.bnnuycorps.oasisbar.Thirst.DrinkComponent;
import io.github.bnnuycorps.oasisbar.Thirst.Item;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;

public class ThirstManager {
    public int waterLevel = 20;
    public float watersaturationLevel = 5.0F;
    public float exhaustion;
    public int waterTickTimer;
    public int prevWaterLevel = 20;

    public ThirstManager() {
    }

    public void add(int water, float saturationModifier) {
        this.waterLevel = Math.min(water + this.waterLevel, 20);
        this.watersaturationLevel = Math.min(this.watersaturationLevel + (float) water * saturationModifier * 2.0F, (float) this.waterLevel);
    }

    public void drink(Item item, ItemStack stack) {
        if (item.isDrink()) {
            DrinkComponent drinkComponent = item.getDrinkComponent();
            this.add(drinkComponent.getThirst(), drinkComponent.getWaterSaturationModifier());
        }

    }

    public void update(PlayerEntity player) {
        Difficulty difficulty = player.getWorld().getDifficulty();
        this.prevWaterLevel = this.waterLevel;
        if (this.exhaustion > 4.0F) {
            this.exhaustion -= 4.0F;
            if (this.watersaturationLevel > 0.0F) {
                this.watersaturationLevel = Math.max(this.watersaturationLevel - 1.0F, 0.0F);
            } else if (difficulty != Difficulty.PEACEFUL) {
                this.waterLevel = Math.max(this.waterLevel - 1, 0);
            }
        }

        boolean bl = player.getWorld().getGameRules().getBoolean(GameRules.NATURAL_REGENERATION);
        if (bl && this.watersaturationLevel > 0.0F && player.canFoodHeal() && this.waterLevel >= 20) {
            ++this.waterTickTimer;
            if (this.waterTickTimer >= 10) {
                float f = Math.min(this.watersaturationLevel, 6.0F);
                player.heal(f / 6.0F);
                this.addExhaustion(f);
                this.waterTickTimer = 0;
            }
        } else if (bl && this.waterLevel >= 18 && player.canFoodHeal()) {
            ++this.waterTickTimer;
            if (this.waterTickTimer >= 80) {
                player.heal(1.0F);
                this.addExhaustion(6.0F);
                this.waterTickTimer = 0;
            }
        } else if (this.waterLevel <= 0) {
            ++this.waterTickTimer;
            if (this.waterTickTimer >= 80) {
                if (player.getHealth() > 10.0F || difficulty == Difficulty.HARD || player.getHealth() > 1.0F && difficulty == Difficulty.NORMAL) {
                    player.damage(player.getDamageSources().starve(), 1.0F);
                }

                this.waterTickTimer = 0;
            }
        } else {
            this.waterTickTimer = 0;
        }

    }

    public void readNbt(NbtCompound nbt) {
        if (nbt.contains("waterLevel", 99)) {
            this.waterLevel = nbt.getInt("waterLevel");
            this.waterTickTimer = nbt.getInt("waterTickTimer");
            this.watersaturationLevel = nbt.getFloat("waterSaturationLevel");
            this.exhaustion = nbt.getFloat("waterExhaustionLevel");
        }

    }

    public void writeNbt(NbtCompound nbt) {
        nbt.putInt("waterLevel", this.waterLevel);
        nbt.putInt("waterTickTimer", this.waterTickTimer);
        nbt.putFloat("waterSaturationLevel", this.watersaturationLevel);
        nbt.putFloat("waterExhaustionLevel", this.exhaustion);
    }

    public int getWaterLevel() {
        return this.waterLevel;
    }

    public int getPrevWaterLevel() {
        return this.prevWaterLevel;
    }

    public boolean isNotFull() {
        return this.waterLevel < 20;
    }

    public void addExhaustion(float exhaustion) {
        this.exhaustion = Math.min(this.exhaustion + exhaustion, 40.0F);
    }

    public float getExhaustion() {
        return this.exhaustion;
    }

    public float getWatersaturationLevel() {
        return this.watersaturationLevel;
    }

    public void setWaterLevel(int waterLevel) {
        this.waterLevel = waterLevel;
    }

    public void setWatersaturationLevel(float watersaturationLevel) {
        this.watersaturationLevel = watersaturationLevel;
    }

    public void setExhaustion(float exhaustion) {
        this.exhaustion = exhaustion;
    }

}

