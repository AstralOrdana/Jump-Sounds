package com.merp.jump_sounds.client;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.BlockSoundGroup;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class JumpSoundsLogic {


	public static void fall(LivingEntity entity) {
		BlockState primaryState = entity.getSteppingBlockState();
		BlockState secondaryState = entity.getWorld().getBlockState(entity.getBlockPos().down());


		if (!entity.isOnGround() || primaryState.isAir() || !entity.getWorld().isClient) return;

			if (entity.getLerpedPos(0).getY() > entity.getLerpedPos(1).getY()) {
				playJumpSound(primaryState, 1, 1f, entity);
				if (primaryState.isIn(BlockTags.COMBINATION_STEP_SOUND_BLOCKS)) playJumpSound(secondaryState, 0.5f, 0.8f, entity);
			}
	}

	public static void jump(LivingEntity entity) {
		BlockState primaryState = entity.getSteppingBlockState();
		BlockState secondaryState = entity.getWorld().getBlockState(entity.getBlockPos().down());

		if (!entity.isOnGround() || primaryState.isAir() || !entity.getWorld().isClient) return;

		playJumpSound(primaryState, 1, 1f, entity);
		if (primaryState.isIn(BlockTags.COMBINATION_STEP_SOUND_BLOCKS)) playJumpSound(secondaryState, 0.5f, 0.8f, entity);
	}

	@Unique
	protected static void playJumpSound(BlockState state, float volMult, float pitchMult, LivingEntity entity) {
		if (entity.isTouchingWater()) return;

		BlockSoundGroup blockSoundGroup = state.getSoundGroup();
		entity.playSound(blockSoundGroup.getFallSound(), blockSoundGroup.getVolume() * 0.075F * volMult, blockSoundGroup.getPitch() * pitchMult);
	}
}
