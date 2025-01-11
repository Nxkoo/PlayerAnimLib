package com.goodbird.playeranimlib.mixin.impl;

import com.goodbird.playeranimlib.client.renderer.GeoPlayerRenderer;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPlayer.class)
public abstract class MixinRenderPlayer extends RendererLivingEntity {

    public MixinRenderPlayer(ModelBase p_i1261_1_, float p_i1261_2_) {
        super(p_i1261_1_, p_i1261_2_);
    }

    @Inject(method = "Lnet/minecraft/client/renderer/entity/RenderPlayer;doRender(Lnet/minecraft/client/entity/AbstractClientPlayer;DDDFF)V", at = @At("HEAD"), cancellable = true)
    public void doRender(AbstractClientPlayer player, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo info) {
        if(player != null) {
            info.cancel();
            GeoPlayerRenderer.INSTANCE.doRender((Entity) player, x, y - 1.5, z, entityYaw, partialTicks);
        }
    }
}
