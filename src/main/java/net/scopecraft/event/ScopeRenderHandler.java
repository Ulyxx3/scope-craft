package net.scopecraft.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.scopecraft.config.ScopeConfig;
import net.scopecraft.gui.ScopeConfigGui;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ScopeRenderHandler {
    private static final ResourceLocation SCOPE_TEXTURE = new ResourceLocation("scopecraft", "textures/gui/scope.png");

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

    private EntityPlayerSP getPlayer(Minecraft mc) {
        if (mc == null) return null;
        try {
            return mc.thePlayer;
        } catch (NoSuchFieldError e) {
            try {
                Field f = Minecraft.class.getField("field_71439_g");
                return (EntityPlayerSP) f.get(mc);
            } catch (Throwable t) {
                try {
                    Field f = Minecraft.class.getField("h");
                    return (EntityPlayerSP) f.get(mc);
                } catch (Throwable t2) {
                    return null;
                }
            }
        }
    }

    private ItemStack getHeldItemStack(EntityPlayer player) {
        if (player == null) return null;
        try {
            return player.getHeldItem();
        } catch (NoSuchMethodError e) {
            try {
                Method m = EntityPlayer.class.getMethod("func_70694_bm");
                return (ItemStack) m.invoke(player);
            } catch (Throwable t) {
                try {
                    Method m = EntityPlayer.class.getMethod("bA");
                    return (ItemStack) m.invoke(player);
                } catch (Throwable t2) {
                    return null;
                }
            }
        }
    }

    private Item getItemFromStack(ItemStack stack) {
        if (stack == null) return null;
        try {
            return stack.getItem();
        } catch (NoSuchMethodError e) {
            try {
                Method m = ItemStack.class.getMethod("func_77973_b");
                return (Item) m.invoke(stack);
            } catch (Throwable t) {
                try {
                    Method m = ItemStack.class.getMethod("b");
                    return (Item) m.invoke(stack);
                } catch (Throwable t2) {
                    return null;
                }
            }
        }
    }

    private boolean isPlayerUsingItem(EntityPlayer player) {
        if (player == null) return false;
        try {
            return player.isUsingItem();
        } catch (NoSuchMethodError e) {
            try {
                Method m = EntityPlayer.class.getMethod("func_71039_bw");
                return (Boolean) m.invoke(player);
            } catch (Throwable t) {
                try {
                    Method m = EntityPlayer.class.getMethod("bS");
                    return (Boolean) m.invoke(player);
                } catch (Throwable t2) {
                    return false;
                }
            }
        }
    }

    private int getScaledWidth(ScaledResolution scaled) {
        if (scaled == null) return 0;
        try {
            return scaled.getScaledWidth();
        } catch (NoSuchMethodError e) {
            try {
                Method m = ScaledResolution.class.getMethod("func_78326_a");
                return (Integer) m.invoke(scaled);
            } catch (Throwable t) {
                try {
                    Method m = ScaledResolution.class.getMethod("a");
                    return (Integer) m.invoke(scaled);
                } catch (Throwable t2) {
                    return 0;
                }
            }
        }
    }

    private int getScaledHeight(ScaledResolution scaled) {
        if (scaled == null) return 0;
        try {
            return scaled.getScaledHeight();
        } catch (NoSuchMethodError e) {
            try {
                Method m = ScaledResolution.class.getMethod("func_78328_b");
                return (Integer) m.invoke(scaled);
            } catch (Throwable t) {
                try {
                    Method m = ScaledResolution.class.getMethod("b");
                    return (Integer) m.invoke(scaled);
                } catch (Throwable t2) {
                    return 0;
                }
            }
        }
    }

    private TextureManager getTextureManager(Minecraft mc) {
        if (mc == null) return null;
        try {
            return mc.getTextureManager();
        } catch (NoSuchMethodError e) {
            try {
                Method m = Minecraft.class.getMethod("func_110434_K");
                return (TextureManager) m.invoke(mc);
            } catch (Throwable t) {
                try {
                    Method m = Minecraft.class.getMethod("P");
                    return (TextureManager) m.invoke(mc);
                } catch (Throwable t2) {
                    return null;
                }
            }
        }
    }

    private void bindTexture(TextureManager tm, ResourceLocation resource) {
        if (tm == null || resource == null) return;
        try {
            tm.bindTexture(resource);
        } catch (NoSuchMethodError e) {
            try {
                Method m = TextureManager.class.getMethod("func_110577_a", ResourceLocation.class);
                m.invoke(tm, resource);
            } catch (Throwable t) {
                try {
                    Method m = TextureManager.class.getMethod("a", ResourceLocation.class);
                    m.invoke(tm, resource);
                } catch (Throwable ignored) {}
            }
        }
    }

    private VertexFormat getPositionTexFormat() {
        try {
            return DefaultVertexFormats.POSITION_TEX;
        } catch (NoSuchFieldError e) {
            try {
                Field f = DefaultVertexFormats.class.getField("field_181707_g");
                return (VertexFormat) f.get(null);
            } catch (Throwable t) {
                try {
                    Field f = DefaultVertexFormats.class.getField("g");
                    return (VertexFormat) f.get(null);
                } catch (Throwable t2) {
                    return null;
                }
            }
        }
    }

    // Tessellator & WorldRenderer reflection helpers
    private Tessellator getTessellatorInstance() {
        try {
            return Tessellator.getInstance();
        } catch (NoSuchMethodError e) {
            try {
                Method m = Tessellator.class.getMethod("func_178181_a");
                return (Tessellator) m.invoke(null);
            } catch (Throwable t) {
                try {
                    Method m = Tessellator.class.getMethod("a");
                    return (Tessellator) m.invoke(null);
                } catch (Throwable t2) {
                    return null;
                }
            }
        }
    }

    private WorldRenderer getWorldRenderer(Tessellator tessellator) {
        if (tessellator == null) return null;
        try {
            return tessellator.getWorldRenderer();
        } catch (NoSuchMethodError e) {
            try {
                Method m = Tessellator.class.getMethod("func_178180_c");
                return (WorldRenderer) m.invoke(tessellator);
            } catch (Throwable t) {
                try {
                    Method m = Tessellator.class.getMethod("c");
                    return (WorldRenderer) m.invoke(tessellator);
                } catch (Throwable t2) {
                    return null;
                }
            }
        }
    }

    private void tessellatorDraw(Tessellator tessellator) {
        if (tessellator == null) return;
        try {
            tessellator.draw();
        } catch (NoSuchMethodError e) {
            try {
                Method m = Tessellator.class.getMethod("func_78381_a");
                m.invoke(tessellator);
            } catch (Throwable t) {
                try {
                    Method m = Tessellator.class.getMethod("b");
                    m.invoke(tessellator);
                } catch (Throwable ignored) {}
            }
        }
    }

    private void worldRendererBegin(WorldRenderer wr, int mode, VertexFormat format) {
        if (wr == null) return;
        try {
            wr.begin(mode, format);
        } catch (NoSuchMethodError e) {
            try {
                Method m = WorldRenderer.class.getMethod("func_181668_a", int.class, VertexFormat.class);
                m.invoke(wr, mode, format);
            } catch (Throwable t) {
                try {
                    Method m = WorldRenderer.class.getMethod("a", int.class, VertexFormat.class);
                    m.invoke(wr, mode, format);
                } catch (Throwable ignored) {}
            }
        }
    }

    private WorldRenderer worldRendererPos(WorldRenderer wr, double x, double y, double z) {
        if (wr == null) return wr;
        try {
            return wr.pos(x, y, z);
        } catch (NoSuchMethodError e) {
            try {
                Method m = WorldRenderer.class.getMethod("func_181662_b", double.class, double.class, double.class);
                return (WorldRenderer) m.invoke(wr, x, y, z);
            } catch (Throwable t) {
                try {
                    Method m = WorldRenderer.class.getMethod("b", double.class, double.class, double.class);
                    return (WorldRenderer) m.invoke(wr, x, y, z);
                } catch (Throwable t2) {
                    return wr;
                }
            }
        }
    }

    private WorldRenderer worldRendererTex(WorldRenderer wr, double u, double v) {
        if (wr == null) return wr;
        try {
            return wr.tex(u, v);
        } catch (NoSuchMethodError e) {
            try {
                Method m = WorldRenderer.class.getMethod("func_181673_a", double.class, double.class);
                return (WorldRenderer) m.invoke(wr, u, v);
            } catch (Throwable t) {
                try {
                    Method m = WorldRenderer.class.getMethod("a", double.class, double.class);
                    return (WorldRenderer) m.invoke(wr, u, v);
                } catch (Throwable t2) {
                    return wr;
                }
            }
        }
    }

    private void worldRendererEndVertex(WorldRenderer wr) {
        if (wr == null) return;
        try {
            wr.endVertex();
        } catch (NoSuchMethodError e) {
            try {
                Method m = WorldRenderer.class.getMethod("func_181675_d");
                m.invoke(wr);
            } catch (Throwable t) {
                try {
                    Method m = WorldRenderer.class.getMethod("d");
                    m.invoke(wr);
                } catch (Throwable ignored) {}
            }
        }
    }

    // Helper de rendu OpenGL sécurisé
    private static void glDisableDepth() {
        try {
            GlStateManager.disableDepth();
        } catch (NoSuchMethodError e) {
            try {
                Method m = GlStateManager.class.getMethod("func_179097_i");
                m.invoke(null);
            } catch (Throwable t) {
                try {
                    Method m = GlStateManager.class.getMethod("i");
                    m.invoke(null);
                } catch (Throwable ignored) {}
            }
        }
    }

    private static void glEnableDepth() {
        try {
            GlStateManager.enableDepth();
        } catch (NoSuchMethodError e) {
            try {
                Method m = GlStateManager.class.getMethod("func_179126_j");
                m.invoke(null);
            } catch (Throwable t) {
                try {
                    Method m = GlStateManager.class.getMethod("j");
                    m.invoke(null);
                } catch (Throwable ignored) {}
            }
        }
    }

    private static void glDepthMask(boolean flag) {
        try {
            GlStateManager.depthMask(flag);
        } catch (NoSuchMethodError e) {
            try {
                Method m = GlStateManager.class.getMethod("func_179132_a", boolean.class);
                m.invoke(null, flag);
            } catch (Throwable t) {
                try {
                    Method m = GlStateManager.class.getMethod("a", boolean.class);
                    m.invoke(null, flag);
                } catch (Throwable ignored) {}
            }
        }
    }

    private static void glTryBlendFuncSeparate(int sFactor, int dFactor, int sFactorAlpha, int dFactorAlpha) {
        try {
            GlStateManager.tryBlendFuncSeparate(sFactor, dFactor, sFactorAlpha, dFactorAlpha);
        } catch (NoSuchMethodError e) {
            try {
                Method m = GlStateManager.class.getMethod("func_179120_a", int.class, int.class, int.class, int.class);
                m.invoke(null, sFactor, dFactor, sFactorAlpha, dFactorAlpha);
            } catch (Throwable t) {
                try {
                    Method m = GlStateManager.class.getMethod("a", int.class, int.class, int.class, int.class);
                    m.invoke(null, sFactor, dFactor, sFactorAlpha, dFactorAlpha);
                } catch (Throwable ignored) {}
            }
        }
    }

    private static void glColor(float r, float g, float b, float a) {
        try {
            GlStateManager.color(r, g, b, a);
        } catch (NoSuchMethodError e) {
            try {
                Method m = GlStateManager.class.getMethod("func_179131_c", float.class, float.class, float.class, float.class);
                m.invoke(null, r, g, b, a);
            } catch (Throwable t) {
                try {
                    Method m = GlStateManager.class.getMethod("c", float.class, float.class, float.class, float.class);
                    m.invoke(null, r, g, b, a);
                } catch (Throwable ignored) {}
            }
        }
    }

    private static void glDisableAlpha() {
        try {
            GlStateManager.disableAlpha();
        } catch (NoSuchMethodError e) {
            try {
                Method m = GlStateManager.class.getMethod("func_179118_c");
                m.invoke(null);
            } catch (Throwable t) {
                try {
                    Method m = GlStateManager.class.getMethod("c");
                    m.invoke(null);
                } catch (Throwable ignored) {}
            }
        }
    }

    private static void glEnableAlpha() {
        try {
            GlStateManager.enableAlpha();
        } catch (NoSuchMethodError e) {
            try {
                Method m = GlStateManager.class.getMethod("func_179141_d");
                m.invoke(null);
            } catch (Throwable t) {
                try {
                    Method m = GlStateManager.class.getMethod("d");
                    m.invoke(null);
                } catch (Throwable ignored) {}
            }
        }
    }

    private float getBowChargeProgress(EntityPlayer player) {
        if (player == null) return 0.0f;

        try {
            int dur = player.getItemInUseDuration();
            if (dur >= 0 && dur < 72000) {
                return Math.min(1.0f, (float) dur / 20.0f);
            }
        } catch (Throwable ignored) {}

        Class<?> clazz = player.getClass();
        while (clazz != null && clazz != Object.class) {
            for (Method m : clazz.getDeclaredMethods()) {
                m.setAccessible(true);
                if (m.getParameterTypes().length == 0 && (m.getReturnType() == int.class || m.getReturnType() == Integer.class)) {
                    String name = m.getName();
                    if (name.equals("getItemInUseDuration") || name.equals("func_71052_bv") || name.equals("bW") || name.equals("getItemInUseCount") || name.equals("func_71057_bx") || name.equals("bV")) {
                        try {
                            int val = (Integer) m.invoke(player);
                            if (val > 0 && val <= 72000) {
                                if (val <= 720) {
                                    return Math.min(1.0f, (float) val / 20.0f);
                                }
                                int dur = 72000 - val;
                                if (dur >= 0 && dur <= 720) {
                                    return Math.min(1.0f, (float) dur / 20.0f);
                                }
                            }
                        } catch (Throwable ignored) {}
                    }
                }
            }

            for (Field f : clazz.getDeclaredFields()) {
                f.setAccessible(true);
                if (f.getType() == int.class || f.getType() == Integer.class) {
                    String name = f.getName();
                    if (name.equals("itemInUseCount") || name.equals("field_71072_f") || name.equals("f") || name.equals("c") || name.equals("g")) {
                        try {
                            int val = f.getInt(player);
                            if (val > 0 && val <= 72000) {
                                int dur = 72000 - val;
                                if (dur >= 0 && dur <= 720) {
                                    return Math.min(1.0f, (float) dur / 20.0f);
                                }
                                if (val <= 720) {
                                    return Math.min(1.0f, (float) val / 20.0f);
                                }
                            }
                        } catch (Throwable ignored) {}
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }

        try {
            Class<?> pClass = player.getClass();
            while (pClass != null && pClass != Object.class) {
                for (Field f : pClass.getDeclaredFields()) {
                    if (f.getType() == int.class || f.getType() == Integer.class) {
                        f.setAccessible(true);
                        int val = f.getInt(player);
                        if (val > 71000 && val <= 72000) {
                            int dur = 72000 - val;
                            return Math.min(1.0f, (float) dur / 20.0f);
                        }
                    }
                }
                pClass = pClass.getSuperclass();
            }
        } catch (Throwable ignored) {}

        return 0.0f;
    }

    public boolean isAimingWithBow() {
        if (!ScopeConfig.INSTANCE.enabled) return false;
        Minecraft mc = getMinecraftInstance();
        EntityPlayerSP player = getPlayer(mc);
        if (player == null) return false;

        ItemStack heldItem = getHeldItemStack(player);
        if (heldItem == null) return false;

        Item item = getItemFromStack(heldItem);
        if (item == null || !(item instanceof ItemBow)) {
            return false;
        }

        if (ScopeConfig.INSTANCE.scopeOnlyWhenAiming) {
            if (!isPlayerUsingItem(player)) return false;
            float chargeProgress = getBowChargeProgress(player);
            return chargeProgress >= ScopeConfig.INSTANCE.minBowCharge;
        } else {
            return true;
        }
    }

    @SubscribeEvent
    public void onGuiDraw(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (event.gui instanceof ScopeConfigGui) {
            ((ScopeConfigGui) event.gui).renderConfigGui(event.mouseX, event.mouseY);
        }
    }

    @SubscribeEvent
    public void onFOVUpdate(FOVUpdateEvent event) {
        if (isAimingWithBow()) {
            float zoom = Math.max(1.0f, ScopeConfig.INSTANCE.zoomFactor);
            event.newfov = event.fov / zoom;
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.HELMET) return;
        if (!isAimingWithBow()) return;

        Minecraft mc = getMinecraftInstance();
        if (mc == null) return;

        ScaledResolution scaled = event.resolution;
        int width = getScaledWidth(scaled);
        int height = getScaledHeight(scaled);

        glDisableDepth();
        glDepthMask(false);
        glTryBlendFuncSeparate(770, 771, 1, 0);
        glColor(1.0F, 1.0F, 1.0F, ScopeConfig.INSTANCE.scopeOpacity);
        glDisableAlpha();

        TextureManager tm = getTextureManager(mc);
        bindTexture(tm, SCOPE_TEXTURE);

        Tessellator tessellator = getTessellatorInstance();
        WorldRenderer worldrenderer = getWorldRenderer(tessellator);
        VertexFormat format = getPositionTexFormat();
        if (worldrenderer != null && format != null) {
            worldRendererBegin(worldrenderer, 7, format);

            worldRendererEndVertex(worldRendererTex(worldRendererPos(worldrenderer, 0.0D, (double) height, -90.0D), 0.0D, 1.0D));
            worldRendererEndVertex(worldRendererTex(worldRendererPos(worldrenderer, (double) width, (double) height, -90.0D), 1.0D, 1.0D));
            worldRendererEndVertex(worldRendererTex(worldRendererPos(worldrenderer, (double) width, 0.0D, -90.0D), 1.0D, 0.0D));
            worldRendererEndVertex(worldRendererTex(worldRendererPos(worldrenderer, 0.0D, 0.0D, -90.0D), 0.0D, 0.0D));

            tessellatorDraw(tessellator);
        }

        glDepthMask(true);
        glEnableDepth();
        glEnableAlpha();
        glColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
