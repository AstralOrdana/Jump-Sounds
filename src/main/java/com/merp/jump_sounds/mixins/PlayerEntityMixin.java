package com.merp.jump_sounds.mixins;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

	@Environment(EnvType.CLIENT)
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "jump", at = @At("TAIL"))
	public void jump(CallbackInfo ci) {
		BlockState primaryState = this.getSteppingBlockState();
		BlockState secondaryState = this.getWorld().getBlockState(this.getBlockPos().down());
		if (this.isOnGround() && !primaryState.isAir() && this.getWorld().isClient()) {
			if (primaryState.isIn(BlockTags.COMBINATION_STEP_SOUND_BLOCKS)) {
				playJumpSound(primaryState,1,1f);
				playJumpSound(secondaryState,0.5f,0.8f);
			} else {
				playJumpSound(primaryState,1,1f);
			}
		}
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void fall(CallbackInfo ci) {
		BlockState primaryState = this.getSteppingBlockState();
		BlockState secondaryState = this.getWorld().getBlockState(this.getBlockPos().down());
		if (this.isOnGround() && !primaryState.isAir() && this.getWorld().isClient()) {
			if (this.getLerpedPos(0).getY() > this.getLerpedPos(1).getY()) {
				if (primaryState.isIn(BlockTags.COMBINATION_STEP_SOUND_BLOCKS)) {
					playJumpSound(primaryState,1,0.9f);
					playJumpSound(secondaryState,0.5f,0.8f * 0.9f);
				} else {
					playJumpSound(primaryState,1,0.9f);
				}
			}
		}
	}

	@Unique
	protected void playJumpSound(BlockState state, float volMult, float pitchMult) {
		BlockSoundGroup blockSoundGroup = state.getSoundGroup();
		if (!this.isTouchingWater()) {
			this.playSound(blockSoundGroup.getFallSound(), blockSoundGroup.getVolume() * 0.075F * volMult, blockSoundGroup.getPitch() * pitchMult);
		}
	}
}
