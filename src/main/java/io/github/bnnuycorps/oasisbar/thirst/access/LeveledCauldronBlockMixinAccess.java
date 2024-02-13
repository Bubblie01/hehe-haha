package io.github.bnnuycorps.oasisbar.thirst.access;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

// Duck Interface for the LeveledCauldronBlock
public interface LeveledCauldronBlockMixinAccess {
    void updateSipLevel(BlockState state, World world, BlockPos blockPos);
}
