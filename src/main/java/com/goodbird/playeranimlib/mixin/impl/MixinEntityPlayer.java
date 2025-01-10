package com.goodbird.playeranimlib.mixin.impl;

import com.goodbird.playeranimlib.PlayerAnimLib;
import com.goodbird.playeranimlib.mixin.IEntityPlayer;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer extends EntityLivingBase implements IAnimationTickable, IAnimatable, IEntityPlayer {
    @Unique
    private final AnimationFactory factory = new AnimationFactory(this);

    @Unique
    private String currentAnimation = "idle";

    @Unique
    private ILoopType.EDefaultLoopTypes currentLoopType = ILoopType.EDefaultLoopTypes.LOOP;

    public MixinEntityPlayer( World p_i1594_1_ )
    {
        super(p_i1594_1_);
    }

    private <E extends IAnimatable> PlayState predicate( AnimationEvent<E> event )
    {
        EntityPlayer player = (EntityPlayer) event.getAnimatable();

        if ( currentAnimation != null ) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(currentAnimation, currentLoopType));
//            return PlayState.CONTINUE;
        }

//        if ( !player.onGround ) {
//            setCurrentAnimation("idle");
//            return PlayState.CONTINUE;
//        }
        if ( event.isMoving() ) {
            setCurrentAnimation("walk2");
        } else {
            setCurrentAnimation("idle");
        }
        if ( player.isSprinting() ) {
            setCurrentAnimation("run");
        }

        return PlayState.CONTINUE;
    }

    public void registerControllers( AnimationData data )
    {
        data.addAnimationController(new AnimationController(this, "controller", 0.5F, this::predicate));
    }

    public AnimationFactory getFactory()
    {
        return this.factory;
    }

    @Override
    public int tickTimer()
    {
        return this.ticksExisted;
    }

    @Unique
    public void setCurrentAnimation( String currentAnimation )
    {
        this.currentAnimation = currentAnimation;
    }

    @Unique
    public void setCurrentLoopType( ILoopType.EDefaultLoopTypes currentLoopType )
    {
        this.currentLoopType = currentLoopType;
    }
}
