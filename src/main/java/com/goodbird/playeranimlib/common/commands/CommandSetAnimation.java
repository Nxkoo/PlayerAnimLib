package com.goodbird.playeranimlib.common.commands;

import com.goodbird.playeranimlib.PlayerAnimLib;
import com.goodbird.playeranimlib.common.network.NetworkWrapper;
import com.goodbird.playeranimlib.common.network.SyncPlayerAnimation;
import com.goodbird.playeranimlib.mixin.IEntityPlayer;
import com.google.common.collect.Lists;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.file.AnimationFile;
import software.bernie.geckolib3.resource.GeckoLibCache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandSetAnimation extends CommandBase {
    @Override
    public String getCommandName()
    {
        return "animation";
    }

    @Override
    public String getCommandUsage( ICommandSender sender )
    {
        return "/animation <animation_name>";
    }

    @Override
    public void processCommand( ICommandSender sender, String[] args )
    {
        if ( args.length < 1 ) {
            sender.addChatMessage(new ChatComponentText("§cUsage: /animation <animation_name>"));
            return;
        }

        if ( sender instanceof EntityPlayer ) {
            EntityPlayer player = (EntityPlayer) sender;
            String animationName = args[0];

            if ( !animations("").contains(animationName) ) {
                sender.addChatMessage(new ChatComponentText("§cAnimation '" + animationName + "' does not exist."));
                return;
            }

            String loopType = "LOOP";
            if ( args.length > 1 ) {
                try {
                    loopType = args[1].toUpperCase();
                } catch ( IllegalArgumentException e ) {
                    sender.addChatMessage(new ChatComponentText("§cInvalid loop type. Valid types: HOLD_ON_LAST_FRAME, PLAY_ONCE, LOOP"));
                    return;
                }
            }

            ((IEntityPlayer) player).setCurrentAnimation(animationName);
            ((IEntityPlayer) player).setCurrentLoopType(ILoopType.EDefaultLoopTypes.valueOf(loopType));

            NetworkWrapper.sendToPlayer(new SyncPlayerAnimation(animationName, loopType), player);

            sender.addChatMessage(new ChatComponentText("§aAnimation set to: " + animationName));
        } else {
            sender.addChatMessage(new ChatComponentText("§4Only players can use this command."));
        }
    }

    @Override
    public List addTabCompletionOptions( ICommandSender sender, String[] args )
    {
        if ( args.length == 1 ) {
            String prefix = args[0];
            return animations(prefix);
        } else if (args.length == 2) {
            return Stream.of("HOLD_ON_LAST_FRAME", "PLAY_ONCE", "LOOP")
                .filter(loop -> loop.startsWith(args[1].toUpperCase()))
                .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private List<String> animations( String prefix )
    {
        List<String> animationsName = new ArrayList<>();
        Map<ResourceLocation, AnimationFile> animations = GeckoLibCache.getInstance().getAnimations();

        animations.forEach(( resourceLocation, animationFile ) -> {
            animationFile.getAllAnimations().forEach(animation -> {
                if ( resourceLocation.getResourceDomain().equalsIgnoreCase(PlayerAnimLib.MODID) )
                    animationsName.add(animation.animationName);
            });
        });

        return animationsName.stream()
            .filter(name -> name.startsWith(prefix))
            .collect(Collectors.toList());
    }
}
