package com.merp.jump_sounds.mixins;

import com.merp.jump_sounds.client.JumpSoundsLogic;
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
		JumpSoundsLogic.jump(this);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	public void fall(CallbackInfo ci) {
		JumpSoundsLogic.fall(this);
	}
}
