package net.scopecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.scopecraft.config.ScopeConfig;
import net.scopecraft.event.ScopeRenderHandler;
import net.scopecraft.gui.ScopeConfigGui;
import org.lwjgl.input.Keyboard;

@Mod(modid = ScopeCraft.MODID, name = ScopeCraft.NAME, version = ScopeCraft.VERSION, clientSideOnly = true)
public class ScopeCraft {
    public static final String MODID = "scopecraft";
    public static final String NAME = "ScopeCraft";
    public static final String VERSION = "1.0.0";

    public static KeyBinding openMenuKey;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ScopeConfig.init(event.getModConfigurationDirectory());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // Enregistrement des événements Forge
        MinecraftForge.EVENT_BUS.register(new ScopeRenderHandler());
        MinecraftForge.EVENT_BUS.register(this);

        // Touche de raccourci pour ouvrir les paramètres (Touche 'O' par défaut)
        openMenuKey = new KeyBinding("Ouvrir Menu ScopeCraft", Keyboard.KEY_O, "ScopeCraft");
        ClientRegistry.registerKeyBinding(openMenuKey);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (openMenuKey.isPressed()) {
            Minecraft.getMinecraft().displayGuiScreen(new ScopeConfigGui(null));
        }
    }
}
