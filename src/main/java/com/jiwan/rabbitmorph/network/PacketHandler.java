package com.jiwan.rabbitmorph.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("rabbitmorph");
    private static int packetId = 0;

    public static void init() {
        INSTANCE.registerMessage(PacketToggleRabbit.Handler.class, PacketToggleRabbit.class, packetId++, Side.SERVER);
        INSTANCE.registerMessage(PacketRabbitSettings.Handler.class, PacketRabbitSettings.class, packetId++, Side.SERVER);
        INSTANCE.registerMessage(PacketOpenInventory.Handler.class, PacketOpenInventory.class, packetId++, Side.SERVER);
        INSTANCE.registerMessage(PacketSyncRabbitData.Handler.class, PacketSyncRabbitData.class, packetId++, Side.CLIENT);
    }
}
