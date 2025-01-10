package com.goodbird.playeranimlib.common.network;

import com.goodbird.playeranimlib.mixin.IEntityPlayer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import software.bernie.geckolib3.core.builder.ILoopType;

public class SyncPlayerAnimation implements IMessage, IMessageHandler<SyncPlayerAnimation, IMessage> {

    private String animation;
    private String loopType;

    public SyncPlayerAnimation()
    {
    }

    public SyncPlayerAnimation( String animation, String loopType )
    {
        this.animation = animation;
        this.loopType = loopType;
    }

    @Override
    public void toBytes( ByteBuf buf )
    {
        buf.writeInt(animation.length());
        buf.writeBytes(animation.getBytes());
        buf.writeInt(loopType.length());
        buf.writeBytes(loopType.getBytes());
    }

    @Override
    public void fromBytes( ByteBuf buf )
    {
        int length = buf.readInt();
        byte[] bytes = new byte[length];
        buf.readBytes(bytes);
        this.animation = new String(bytes);

        int loopTypeLength = buf.readInt();
        byte[] loopTypeBytes = new byte[loopTypeLength];
        buf.readBytes(loopTypeBytes);
        this.loopType = new String(loopTypeBytes);
    }

    @Override
    public IMessage onMessage( SyncPlayerAnimation message, MessageContext ctx )
    {
        Minecraft.getMinecraft().func_152344_a(() -> {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            if ( player instanceof IEntityPlayer ) {
                ((IEntityPlayer) player).setCurrentAnimation(message.animation);

                ILoopType.EDefaultLoopTypes loopType = ILoopType.EDefaultLoopTypes.valueOf(message.loopType.toUpperCase());
                ((IEntityPlayer) player).setCurrentLoopType(loopType);
            }
        });
        return null;
    }
}
