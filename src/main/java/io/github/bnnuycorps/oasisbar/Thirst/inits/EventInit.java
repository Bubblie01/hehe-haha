package io.github.bnnuycorps.oasisbar.Thirst.inits;

import io.github.bnnuycorps.oasisbar.Thirst.interfaces.PlayerInt;
import io.github.bnnuycorps.oasisbar.Thirst.interfaces.ServerPlayerInt;
import io.github.bnnuycorps.oasisbar.Thirst.interfaces.ThirstManagerInt;
import net.dehydration.thirst.ThirstManager;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.BinomialLootNumberProvider;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

public class EventInit {

    public static void init() {
        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register((player, origin, destination) -> {
            ((ServerPlayerInt) player).compatSync();
        });

        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, supplier, setter) -> {
            if (id.equals(new Identifier(LootTables.SPAWN_BONUS_CHEST.toString()))) {
                LootPool pool = LootPool.builder().with(ItemEntry.builder(Items.GLASS_BOTTLE).build()).rolls(BinomialLootNumberProvider.create(5, 0.9F)).build();
                supplier.pool(pool);
            }
        });

        UseBlockCallback.EVENT.register((player, world, hand, result) -> {
            if (!player.isCreative() && !player.isSpectator() && player.isSneaking() && (player.getMainHandStack().isEmpty() )) {
                HitResult hitResult = player.raycast(1.5D, 0.0F, true);
                BlockPos blockPos = ((BlockHitResult) hitResult).getBlockPos();
                if (world.canPlayerModifyAt(player, blockPos) && world.getFluidState(blockPos).isIn(FluidTags.WATER)) {
                    if (world.getFluidState(blockPos).isStill() || ConfigInit.CONFIG.allow_non_flowing_water_sip) {
                        ThirstManager thirstManager = ((ThirstManagerInt) player).getThirstManager();
                        if (thirstManager.isNotFull()) {
                            int drinkTime = ((PlayerInt) player).getDrinkTime();
                            if (world.isClient() && drinkTime % 3 == 0)
                                player.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 0.5f, world.getRandom().nextFloat() * 0.1f + 0.9f);

                            if (drinkTime > 20) {
                                if (!world.isClient()) {
                                    if (!ConfigInit.CONFIG.allow_non_flowing_water_sip && world.getFluidState(blockPos).isStill())
                                        if (world.getBlockState(blockPos).contains(Properties.WATERLOGGED)) {
                                            world.setBlockState(blockPos, world.getBlockState(blockPos).with(Properties.WATERLOGGED, false));
                                        } else {
                                            world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
                                        }
                                    thirstManager.add(ConfigInit.CONFIG.water_souce_quench);
                                } else {
                                    world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundInit.WATER_SIP_EVENT, SoundCategory.PLAYERS, 1.0F,
                                            0.9F + (world.getRandom().nextFloat() / 5F));
                                }
                                ((PlayerInt) player).setDrinkTime(0);
                                return ActionResult.SUCCESS;
                            }

                            ((PlayerInt) player).setDrinkTime(drinkTime + 1);
                        }
                    }
                }
                return ActionResult.PASS;
            }
            return ActionResult.PASS;
        });
    }
}
