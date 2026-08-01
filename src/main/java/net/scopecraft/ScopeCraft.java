package net.scopecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.scopecraft.config.ScopeConfig;
import net.scopecraft.event.ScopeRenderHandler;
import net.scopecraft.gui.ScopeConfigGui;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

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
        MinecraftForge.EVENT_BUS.register(new ScopeRenderHandler());
        MinecraftForge.EVENT_BUS.register(this);

        openMenuKey = new KeyBinding("Ouvrir Menu ScopeCraft", Keyboard.KEY_O, "ScopeCraft");
        ClientRegistry.registerKeyBinding(openMenuKey);
    }

    private boolean isKeyBindingPressed(KeyBinding keyBinding) {
        if (keyBinding == null) return false;
        try {
            return keyBinding.isPressed();
        } catch (NoSuchMethodError e) {
            try {
                Method m = KeyBinding.class.getMethod("func_151468_f");
                return (Boolean) m.invoke(keyBinding);
            } catch (Throwable t) {
                try {
                    Method m = KeyBinding.class.getMethod("f");
                    return (Boolean) m.invoke(keyBinding);
                } catch (Throwable t2) {
                    return false;
                }
            }
        }
    }

    private GuiScreen getCurrentScreen(Minecraft mc) {
        if (mc == null) return null;
        try {
            return mc.currentScreen;
        } catch (NoSuchFieldError e) {
            try {
                Field f = Minecraft.class.getField("field_71462_r");
                return (GuiScreen) f.get(mc);
            } catch (Throwable t) {
                try {
                    Field f = Minecraft.class.getField("m");
                    return (GuiScreen) f.get(mc);
                } catch (Throwable t2) {
                    return null;
                }
            }
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (isKeyBindingPressed(openMenuKey)) {
            Minecraft mc = getMinecraftInstance();
            if (mc != null && getCurrentScreen(mc) == null) {
                displayGui(new ScopeConfigGui(null));
            }
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (isKeyBindingPressed(openMenuKey)) {
            Minecraft mc = getMinecraftInstance();
            if (mc != null && getCurrentScreen(mc) == null) {
                displayGui(new ScopeConfigGui(null));
            }
        }
    }

    private void displayGui(GuiScreen screen) {
        Minecraft mc = getMinecraftInstance();
        if (mc == null) return;
        try {
            mc.displayGuiScreen(screen);
        } catch (Throwable t) {
            try {
                Method m = Minecraft.class.getMethod("func_147108_a", GuiScreen.class);
                m.invoke(mc, screen);
            } catch (Throwable t2) {
                try {
                    Method m = Minecraft.class.getMethod("a", GuiScreen.class);
                    m.invoke(mc, screen);
                } catch (Throwable ignored) {}
            }
        }
    }

    private Minecraft getMinecraftInstance() {
        try {
            return Minecraft.getMinecraft();
        } catch (NoSuchMethodError e) {
            try {
                Method m = Minecraft.class.getMethod("func_71410_x");
                return (Minecraft) m.invoke(null);
            } catch (Throwable t) {
                try {
                    Method m = Minecraft.class.getMethod("A");
                    return (Minecraft) m.invoke(null);
                } catch (Throwable t2) {
                    return null;
                }
            }
        }
    }
}
