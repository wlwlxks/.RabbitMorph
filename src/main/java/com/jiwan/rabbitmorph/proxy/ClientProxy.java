package com.jiwan.rabbitmorph.proxy;

import com.jiwan.rabbitmorph.RabbitKeys;
import com.jiwan.rabbitmorph.client.RabbitFirstPersonRenderer;
import com.jiwan.rabbitmorph.client.RabbitRenderer;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {
        super.preInit();
    }

    @Override
    public void init() {
        super.init();
        RabbitKeys.init();
        MinecraftForge.EVENT_BUS.register(new RabbitRenderer());
        MinecraftForge.EVENT_BUS.register(new RabbitFirstPersonRenderer());
    }

    @Override
    public void postInit() {
        super.postInit();
    }

    @Override
    public void registerRenderers() {
    }
}
