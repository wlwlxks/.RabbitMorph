package com.jiwan.rabbitmorph;

import com.jiwan.rabbitmorph.proxy.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
        modid = RabbitMorph.MODID,
        name = RabbitMorph.NAME,
        version = RabbitMorph.VERSION,
        acceptedMinecraftVersions = "[1.8.9]"
)
public class RabbitMorph {

    public static final String MODID = "rabbitmorph";
    public static final String NAME = "RabbitMorph";
    public static final String VERSION = "@VERSION@";

    @Instance(MODID)
    public static RabbitMorph instance;

    @SidedProxy(
            clientSide = "com.jiwan.rabbitmorph.proxy.ClientProxy",
            serverSide = "com.jiwan.rabbitmorph.proxy.CommonProxy"
    )
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit();
        RabbitHandler handler = new RabbitHandler();
        MinecraftForge.EVENT_BUS.register(handler);
        FMLCommonHandler.instance().bus().register(handler);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
        proxy.registerRenderers();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit();
    }
}
