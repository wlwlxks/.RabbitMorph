package com.jiwan.rabbitmorph;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class RabbitKeys {

    public static KeyBinding keyToggleRabbit;
    public static KeyBinding keyOpenSettings;
    public static KeyBinding keyInteract;

    public static void init() {
        keyToggleRabbit = new KeyBinding("key.rabbitmorph.toggle", Keyboard.KEY_R, "key.categories.rabbitmorph");
        keyOpenSettings = new KeyBinding("key.rabbitmorph.settings", Keyboard.KEY_B, "key.categories.rabbitmorph");
        keyInteract = new KeyBinding("key.rabbitmorph.interact", Keyboard.KEY_E, "key.categories.rabbitmorph");

        ClientRegistry.registerKeyBinding(keyToggleRabbit);
        ClientRegistry.registerKeyBinding(keyOpenSettings);
        ClientRegistry.registerKeyBinding(keyInteract);
    }
}
