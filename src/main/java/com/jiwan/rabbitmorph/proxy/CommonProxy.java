package com.jiwan.rabbitmorph.proxy;

import com.jiwan.rabbitmorph.network.PacketHandler;

public class CommonProxy {

    public void preInit() {
        PacketHandler.init();
    }

    public void init() {
    }

    public void postInit() {
    }

    public void registerRenderers() {
    }
}
