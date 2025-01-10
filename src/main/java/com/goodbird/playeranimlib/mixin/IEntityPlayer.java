package com.goodbird.playeranimlib.mixin;

import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;

public interface IEntityPlayer {
    AnimationBuilder getBuilderByName( String name );

    void setBuilderByName( String name, AnimationBuilder builder );

    void setCurrentAnimation( String currentAnimation );

    void setCurrentLoopType( ILoopType.EDefaultLoopTypes currentLoopType );
}
