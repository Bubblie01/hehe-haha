package io.github.bnnuycorps.oasisbar.Thirst.items;


import io.github.bnnuycorps.oasisbar.Thirst.ThirstManager;
import io.github.bnnuycorps.oasisbar.Thirst.inits.ConfigInit;
import io.github.bnnuycorps.oasisbar.Thirst.inits.EffectInit;
import io.github.bnnuycorps.oasisbar.Thirst.inits.SoundInit;
import io.github.bnnuycorps.oasisbar.Thirst.interfaces.ThirstManagerInt;
import io.github.bnnuycorps.oasisbar.Thirst.interfaces.ThirstTooltipData;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.*;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.List;
import java.util.Optional;

public class Canteen extends Item {
    public final int addition;

    public Canteen(int waterAddition, Settings settings) {
        super(settings);
        this.addition = waterAddition;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        ItemStack itemStack = context.getStack();
        BlockPos pos = context.getBlockPos();
        BlockState state = context.getWorld().getBlockState(pos);
        NbtCompound tags = itemStack.getNbt();

        if (state.getBlock() instanceof LeveledCauldronBlock || state.getBlock() instanceof CauldronBlock ) {

            // Empty flask
            if (player.isSneaking()) {
                if ((itemStack.hasNbt() && tags.getInt("canteen") > 0) || !itemStack.hasNbt()) {
                    if (!player.getWorld().isClient()) {
                        if (state.getBlock() instanceof AbstractCauldronBlock) {
                            if (state.getBlock() instanceof LeveledCauldronBlock) {
                                if (((LeveledCauldronBlock) state.getBlock()).isFull(state)) {
                                    return super.useOnBlock(context);
                                }
                                player.getWorld().setBlockState(pos, (BlockState) state.cycle(LeveledCauldronBlock.LEVEL));
                            } else {
                                player.getWorld().setBlockState(pos, Blocks.WATER_CAULDRON.getDefaultState());
                                player.getWorld().emitGameEvent(null, GameEvent.BLOCK_CHANGE, pos);
                            }
                        }
                        player.getWorld().playSound((PlayerEntity) null, pos, SoundInit.EMPTY_FLASK_EVENT, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        player.incrementStat(Stats.USE_CAULDRON);

                        if (itemStack.hasNbt())
                            tags.putInt("canteen", tags.getInt("canteen") - 1);
                        else {
                            tags = new NbtCompound();
                            tags.putInt("canteen", 1 + this.addition);
                        }
                        itemStack.setNbt(tags);
                    }
                    return ActionResult.success(player.getWorld().isClient());
                }
            } else if (state.getBlock() instanceof LeveledCauldronBlock && state.get(LeveledCauldronBlock.LEVEL) > 0 && itemStack.hasNbt() && tags.getInt("canteen") < 2 + this.addition) {
                // Fill up flask
                if (!player.getWorld().isClient()) {
                    player.getWorld().playSound((PlayerEntity) null, pos, SoundInit.FILL_FLASK_EVENT, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    player.incrementStat(Stats.USE_CAULDRON);
                    LeveledCauldronBlock.decrementFluidLevel(state, player.getWorld(), pos);
                    tags.putInt("canteen", tags.getInt("canteen") + 1);
                    itemStack.setNbt(tags);
                }
                return ActionResult.success(player.getWorld().isClient());
            }
        }
        return super.useOnBlock(context);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        NbtCompound tags = itemStack.getNbt();
        HitResult hitResult = raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);
        BlockPos blockPos = ((BlockHitResult) hitResult).getBlockPos();

        if (hitResult.getType() == HitResult.Type.BLOCK && world.canPlayerModifyAt(user, blockPos) && world.getFluidState(blockPos).isIn(FluidTags.WATER) && itemStack.hasNbt()) {
            if (user.isSneaking() && tags.getInt("canteen") != 0) {
                tags.putInt("canteen", 0);
                world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundInit.EMPTY_FLASK_EVENT, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                return TypedActionResult.consume(itemStack);
            }
            if (tags.getInt("canteen") < 2 + addition) {
                int fillLevel = 2 + addition;
                int waterPurity = 2;

                boolean isEmpty = tags.getInt("leather_flask") == 0;
                boolean isDirtyWater = tags.getInt("purified_water") == 2;
                if (!isEmpty && !isDirtyWater)
                    waterPurity = 1;


                boolean riverWater = world.getBiome(blockPos).isIn(BiomeTags.IS_RIVER);
                if (riverWater && (isEmpty || (!isEmpty && !isDirtyWater)))
                    waterPurity = 0;

                world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundInit.FILL_FLASK_EVENT, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                tags.putInt("purified_water", waterPurity);
                tags.putInt("leather_flask", fillLevel);
                return TypedActionResult.consume(itemStack);
            }
        }
        if (itemStack.hasNbt() && tags.getInt("leather_flask") == 0)
            return TypedActionResult.pass(itemStack);
        else
            return ItemUsage.consumeHeldItem(world, user, hand);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity) user : null;
        NbtCompound tags = stack.getNbt();
        if (!stack.hasNbt() || tags != null && tags.getInt("leather_flask") > 0) {
            if (playerEntity instanceof ServerPlayerEntity) {
                Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity) playerEntity, stack);
            }
            if (playerEntity != null) {
                playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
                if (!playerEntity.isCreative()) {
                    if (!stack.hasNbt()) {
                        tags = new NbtCompound();

                        tags.putInt("leather_flask", 2 + addition);
                        tags.putInt("purified_water", 0);
                        stack.setNbt(tags);
                    }
                    tags.putInt("leather_flask", tags.getInt("leather_flask") - 1);
                    ThirstManager thirstManager = ((ThirstManagerInt) playerEntity).getThirstManager();
                    thirstManager.add(ConfigInit.CONFIG.flask_thirst_quench);
                    if (!world.isClient)
                        if (tags.getInt("purified_water") == 2 && world.random.nextFloat() <= ConfigInit.CONFIG.flask_dirty_thirst_chance)
                            playerEntity.addStatusEffect(new StatusEffectInstance(EffectInit.DEHYDRATION, ConfigInit.CONFIG.flask_dirty_thirst_duration, 1, false, false, true));
                        else if (tags.getInt("purified_water") == 1 && world.random.nextFloat() <= ConfigInit.CONFIG.flask_dirty_thirst_chance * 0.5F)
                            playerEntity.addStatusEffect(new StatusEffectInstance(EffectInit.DEHYDRATION, ConfigInit.CONFIG.flask_dirty_thirst_duration, 0, false, false, true));
                }
            }
        }
        return stack;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 32;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        NbtCompound tags = stack.getNbt();
        if (!stack.hasNbt() || (tags != null && tags.getInt("leather_flask") > 0)) {
            return UseAction.DRINK;
        } else
            return UseAction.NONE;
    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        if (!stack.hasNbt()) {
            NbtCompound tags = new NbtCompound();
            tags.putInt("leather_flask", 0);
            stack.setNbt(tags);
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound tags = stack.getNbt();
        if (tags != null) {
            tooltip.add(Text.translatable("item.dehydration.leather_flask.tooltip", tags.getInt("leather_flask"), addition + 2).formatted(Formatting.GRAY));
            if (tags.getInt("leather_flask") > 0) {
                String string = "dirty";
                if (tags.getInt("purified_water") == 1) {
                    string = "impurified";
                } else if (tags.getInt("purified_water") == 0) {
                    string = "purified";
                }
                tooltip.add(Text.translatable("item.dehydration.leather_flask.tooltip3." + string));
            }
        } else {
            tooltip.add(Text.translatable("item.dehydration.leather_flask.tooltip2", addition + 2).formatted(Formatting.GRAY));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        if (ConfigInit.CONFIG.thirst_preview) {
            if (stack.hasNbt() && stack.getNbt().contains("leather_flask")) {
                if (stack.getNbt().getInt("leather_flask") == 0) {
                    return Optional.empty();
                }
            }
            return Optional.of(new ThirstTooltipData(0, (2 + this.addition) * ConfigInit.CONFIG.flask_thirst_quench));
        }
        return super.getTooltipData(stack);
    }

    public static void fillFlask(ItemStack itemStack, int quench) {
        NbtCompound nbt = new NbtCompound();
        if (!itemStack.hasNbt()) {
            nbt.putInt("leather_flask", 0);
        } else {
            nbt = itemStack.getNbt().copy();
            if (nbt.getInt("leather_flask") == 0) {
            }
        }
        int fillQuench = nbt.getInt("leather_flask") + quench;
        int addition = ((Canteen) itemStack.getItem()).addition;
        nbt.putInt("leather_flask", fillQuench > 2 + addition ? 2 + addition : fillQuench);
        itemStack.setNbt(nbt);
    }

    public static boolean isFlaskEmpty(ItemStack stack) {
        NbtCompound tags = stack.getNbt();
        if (tags != null) {
            if (tags.getInt("leather_flask") != 0) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public static boolean isFlaskFull(ItemStack stack) {
        NbtCompound tags = stack.getNbt();
        if (tags != null) {
            if (tags.getInt("leather_flask") >= ((Canteen) stack.getItem()).addition + 2) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    // purified_water: 0 = purified, 1 impurified, 2 dirty
}
