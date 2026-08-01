package net.scopecraft.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.scopecraft.config.ScopeConfig;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ScopeConfigGui extends GuiScreen {
    private static final ResourceLocation WIDGETS_TEXTURE = new ResourceLocation("textures/gui/widgets.png");
    private final GuiScreen parentScreen;

    private int lastMouseX = 0;
    private int lastMouseY = 0;
    private long lastClickTime = 0;

    private abstract static class CustomWidget {
        int id;
        int x, y, width, height;
        boolean enabled = true;

        CustomWidget(int id, int x, int y, int width, int height) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        boolean isMouseOver(int mouseX, int mouseY) {
            return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
        }

        abstract void render(Minecraft mc, int mouseX, int mouseY);
        void onClick(int mouseX, int mouseY) {}
        void onDrag(int mouseX, int mouseY) {}
        void onRelease() {}
    }

    private static class CustomButtonWidget extends CustomWidget {
        String label;
        Runnable action;

        CustomButtonWidget(int id, int x, int y, int width, int height, String label, Runnable action) {
            super(id, x, y, width, height);
            this.label = label;
            this.action = action;
        }

        @Override
        void render(Minecraft mc, int mouseX, int mouseY) {
            boolean hovered = isMouseOver(mouseX, mouseY);
            renderButtonBox(mc, x, y, width, height, label, hovered, enabled);
        }

        @Override
        void onClick(int mouseX, int mouseY) {
            if (enabled && action != null) {
                action.run();
            }
        }
    }

    public interface ISliderValueChange {
        void onChange(float val);
    }

    private static class CustomSliderWidget extends CustomWidget {
        String prefix;
        String suffix;
        float minVal, maxVal;
        float sliderValue;
        boolean dragging = false;
        ISliderValueChange callback;

        CustomSliderWidget(int id, int x, int y, int width, int height, String prefix, String suffix, float minVal, float maxVal, float currentVal, ISliderValueChange callback) {
            super(id, x, y, width, height);
            this.prefix = prefix;
            this.suffix = suffix;
            this.minVal = minVal;
            this.maxVal = maxVal;
            this.callback = callback;
            this.sliderValue = (currentVal - minVal) / (maxVal - minVal);
            if (this.sliderValue < 0.0f) this.sliderValue = 0.0f;
            if (this.sliderValue > 1.0f) this.sliderValue = 1.0f;
        }

        float getValue() {
            return minVal + (maxVal - minVal) * sliderValue;
        }

        String getDisplayString() {
            float v = getValue();
            if ("%".equals(suffix)) {
                return String.format("%s: \u00A7e%.0f%%\u00A7r", prefix, v * 100.0f);
            } else if ("x".equals(suffix)) {
                return String.format("%s: \u00A7e%.1fx\u00A7r", prefix, v);
            } else {
                return String.format("%s: \u00A7e%.2f%s\u00A7r", prefix, v, suffix);
            }
        }

        @Override
        void render(Minecraft mc, int mouseX, int mouseY) {
            boolean hovered = isMouseOver(mouseX, mouseY);
            renderButtonBox(mc, x, y, width, height, getDisplayString(), hovered, true);

            // Render Slider Knob
            try {
                ensureGLState();
                bindWidgetsTexture(mc);

                int knobX = x + (int) (sliderValue * (float) (width - 8));
                int knobState = hovered ? 2 : 1;
                drawTexturedRect(knobX, y, 0, 46 + knobState * 20, 4, 20);
                drawTexturedRect(knobX + 4, y, 196, 46 + knobState * 20, 4, 20);
            } catch (Throwable ignored) {}
        }

        @Override
        void onClick(int mouseX, int mouseY) {
            updateFromMouse(mouseX);
            this.dragging = true;
        }

        @Override
        void onDrag(int mouseX, int mouseY) {
            if (this.dragging) {
                updateFromMouse(mouseX);
            }
        }

        @Override
        void onRelease() {
            this.dragging = false;
        }

        private void updateFromMouse(int mouseX) {
            this.sliderValue = (float) (mouseX - (x + 4)) / (float) (width - 8);
            if (this.sliderValue < 0.0f) this.sliderValue = 0.0f;
            if (this.sliderValue > 1.0f) this.sliderValue = 1.0f;
            if (callback != null) {
                callback.onChange(getValue());
            }
        }
    }

    private final List<CustomWidget> widgets = new ArrayList<>();

    public ScopeConfigGui(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
    }

    private Minecraft getMinecraftInstance() {
        try {
            if (this.mc != null) return this.mc;
        } catch (Throwable ignored) {}
        try {
            return Minecraft.getMinecraft();
        } catch (Throwable t) {
            try {
                Method m = Minecraft.class.getMethod("func_71410_x");
                return (Minecraft) m.invoke(null);
            } catch (Throwable t2) {
                return null;
            }
        }
    }

    private int getGuiScreenWidth() {
        try {
            if (this.width > 0) return this.width;
        } catch (Throwable ignored) {}
        try {
            Field f = GuiScreen.class.getField("field_146294_l");
            return f.getInt(this);
        } catch (Throwable t) {
            try {
                Field f = GuiScreen.class.getField("l");
                return f.getInt(this);
            } catch (Throwable t2) {
                return 800;
            }
        }
    }

    private int getGuiScreenHeight() {
        try {
            if (this.height > 0) return this.height;
        } catch (Throwable ignored) {}
        try {
            Field f = GuiScreen.class.getField("field_146295_m");
            return f.getInt(this);
        } catch (Throwable t) {
            try {
                Field f = GuiScreen.class.getField("m");
                return f.getInt(this);
            } catch (Throwable t2) {
                return 600;
            }
        }
    }

    private void displayScreen(GuiScreen screen) {
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

    private void rebuildWidgets() {
        widgets.clear();
        int width = getGuiScreenWidth();
        int height = getGuiScreenHeight();
        int centerX = width / 2;
        int startY = Math.max(30, height / 4);

        // 1. Mod Enable Toggle
        CustomButtonWidget btnEnabled = new CustomButtonWidget(1, centerX - 100, startY, 200, 20, getEnabledText(), null);
        btnEnabled.action = () -> {
            ScopeConfig.INSTANCE.enabled = !ScopeConfig.INSTANCE.enabled;
            btnEnabled.label = getEnabledText();
        };
        widgets.add(btnEnabled);

        // 2. Aiming Mode Toggle
        CustomButtonWidget btnAiming = new CustomButtonWidget(2, centerX - 100, startY + 26, 200, 20, getAimingModeText(), null);
        btnAiming.action = () -> {
            ScopeConfig.INSTANCE.scopeOnlyWhenAiming = !ScopeConfig.INSTANCE.scopeOnlyWhenAiming;
            btnAiming.label = getAimingModeText();
        };
        widgets.add(btnAiming);

        // 3. Zoom FOV Slider
        CustomSliderWidget sliderZoom = new CustomSliderWidget(3, centerX - 100, startY + 52, 200, 20,
                "Zoom FOV", "x", 1.0f, 10.0f, ScopeConfig.INSTANCE.zoomFactor,
                val -> ScopeConfig.INSTANCE.zoomFactor = Math.round(val * 10.0f) / 10.0f);
        widgets.add(sliderZoom);

        // 4. Bow Charge Trigger Slider
        CustomSliderWidget sliderBowTrigger = new CustomSliderWidget(4, centerX - 100, startY + 78, 200, 20,
                "Declenchement Arc", "%", 0.0f, 1.0f, ScopeConfig.INSTANCE.minBowCharge,
                val -> ScopeConfig.INSTANCE.minBowCharge = Math.round(val * 100.0f) / 100.0f);
        widgets.add(sliderBowTrigger);

        // 5. Opacity Toggle
        CustomButtonWidget btnOpacity = new CustomButtonWidget(5, centerX - 100, startY + 104, 200, 20, getOpacityText(), null);
        btnOpacity.action = () -> {
            float opacity = ScopeConfig.INSTANCE.scopeOpacity;
            if (opacity <= 0.3f) opacity = 1.0f;
            else opacity -= 0.25f;
            ScopeConfig.INSTANCE.scopeOpacity = opacity;
            btnOpacity.label = getOpacityText();
        };
        widgets.add(btnOpacity);

        // 6. Save & Exit
        CustomButtonWidget btnSave = new CustomButtonWidget(200, centerX - 100, startY + 138, 200, 20, "Sauvegarder & Quitter", () -> {
            ScopeConfig.save();
            displayScreen(parentScreen);
        });
        widgets.add(btnSave);
    }

    private String getEnabledText() {
        return "Mod Scope: " + (ScopeConfig.INSTANCE.enabled ? "\u00A7aACTIVE" : "\u00A7cDESACTIVE");
    }

    private String getAimingModeText() {
        return "Mode Visee: " + (ScopeConfig.INSTANCE.scopeOnlyWhenAiming ? "Bander l'arc" : "Arc en main");
    }

    private String getOpacityText() {
        return String.format("Opacite Scope: \u00A7e%.0f%%\u00A7r (Clic pour modifier)", ScopeConfig.INSTANCE.scopeOpacity * 100);
    }

    // --- Lifecycle Overrides (MCP + Searge + Notch Aliases) ---
    @Override
    public void setWorldAndResolution(Minecraft mc, int width, int height) {
        this.mc = mc;
        this.width = width;
        this.height = height;
        rebuildWidgets();
    }
    public void func_102015_a(Minecraft mc, int width, int height) {
        setWorldAndResolution(mc, width, height);
    }
    public void a(Minecraft mc, int width, int height) {
        setWorldAndResolution(mc, width, height);
    }

    @Override
    public void initGui() {
        rebuildWidgets();
    }
    public void func_73866_w() {
        rebuildWidgets();
    }
    public void b() {
        rebuildWidgets();
    }

    private void handleMouseClick(int mouseX, int mouseY, int button) {
        long now = System.currentTimeMillis();
        if (now - lastClickTime < 150) return;
        if (button == 0) {
            for (CustomWidget w : widgets) {
                if (w.isMouseOver(mouseX, mouseY)) {
                    w.onClick(mouseX, mouseY);
                    lastClickTime = now;
                    break;
                }
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        handleMouseClick(mouseX, mouseY, mouseButton);
    }
    public void func_73864_a(int mouseX, int mouseY, int mouseButton) {
        handleMouseClick(mouseX, mouseY, mouseButton);
    }
    public void a(int mouseX, int mouseY, int mouseButton) {
        handleMouseClick(mouseX, mouseY, mouseButton);
    }

    private void handleMouseDrag(int mouseX, int mouseY) {
        for (CustomWidget w : widgets) {
            w.onDrag(mouseX, mouseY);
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (clickedMouseButton == 0) {
            handleMouseDrag(mouseX, mouseY);
        }
    }
    public void func_146273_a(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (clickedMouseButton == 0) {
            handleMouseDrag(mouseX, mouseY);
        }
    }
    public void a(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (clickedMouseButton == 0) {
            handleMouseDrag(mouseX, mouseY);
        }
    }

    private void handleMouseRelease() {
        for (CustomWidget w : widgets) {
            w.onRelease();
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        handleMouseRelease();
    }
    public void func_146286_b(int mouseX, int mouseY, int state) {
        handleMouseRelease();
    }
    public void b(int mouseX, int mouseY, int state) {
        handleMouseRelease();
    }

    private void handleKeyPress(int keyCode) {
        if (keyCode == 1 || keyCode == Keyboard.KEY_ESCAPE) {
            ScopeConfig.save();
            displayScreen(this.parentScreen);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        handleKeyPress(keyCode);
    }
    public void func_73869_a(char typedChar, int keyCode) {
        handleKeyPress(keyCode);
    }

    @Override
    public void handleInput() throws IOException {
        processInputEvents();
    }
    public void func_146269_k() {
        processInputEvents();
    }
    public void k() {
        processInputEvents();
    }
    public void p() {
        processInputEvents();
    }
    public void m() {
        processInputEvents();
    }

    private void processInputEvents() {
        int mouseX = this.lastMouseX;
        int mouseY = this.lastMouseY;

        while (Mouse.next()) {
            int btn = Mouse.getEventButton();
            if (btn >= 0) {
                if (Mouse.getEventButtonState()) {
                    handleMouseClick(mouseX, mouseY, btn);
                } else {
                    handleMouseRelease();
                }
            }
        }

        if (Mouse.isButtonDown(0)) {
            handleMouseDrag(mouseX, mouseY);
        }

        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                handleKeyPress(Keyboard.getEventKey());
            }
        }
    }

    // --- Core Rendering Method ---
    public void renderConfigGui(int mouseX, int mouseY) {
        this.lastMouseX = mouseX;
        this.lastMouseY = mouseY;

        Minecraft mc = getMinecraftInstance();
        int width = getGuiScreenWidth();
        int height = getGuiScreenHeight();

        if (widgets.isEmpty()) {
            rebuildWidgets();
        }

        ensureGLState();
        drawRectOverlay(-100, -100, width + 500, height + 500, 0xD0101010);

        ensureGLState();
        drawString(mc, "\u00A7lOptions de ScopeCraft", (width - getStringWidth(mc, "Options de ScopeCraft")) / 2, 12, 0xFFFFFF);

        for (CustomWidget w : widgets) {
            ensureGLState();
            w.render(mc, mouseX, mouseY);
        }

        ensureGLState();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        renderConfigGui(mouseX, mouseY);
    }
    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        renderConfigGui(mouseX, mouseY);
    }
    public void a(int mouseX, int mouseY, float partialTicks) {
        renderConfigGui(mouseX, mouseY);
    }

    // --- Safe GL & Rendering Helpers ---
    private static void ensureGLState() {
        try {
            GlStateManager.enableTexture2D();
            GlStateManager.enableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        } catch (Throwable t) {
            try {
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            } catch (Throwable ignored) {}
        }
    }

    private static void bindWidgetsTexture(Minecraft mc) {
        if (mc == null) return;
        try {
            mc.getTextureManager().bindTexture(WIDGETS_TEXTURE);
        } catch (Throwable t) {
            try {
                Method m = Minecraft.class.getMethod("func_110434_K");
                Object tm = m.invoke(mc);
                Method m2 = tm.getClass().getMethod("func_110577_a", ResourceLocation.class);
                m2.invoke(tm, WIDGETS_TEXTURE);
            } catch (Throwable ignored) {}
        }
    }

    private static void drawRectOverlay(int left, int top, int right, int bottom, int color) {
        float a = (float) (color >> 24 & 255) / 255.0F;
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;

        try {
            GlStateManager.disableTexture2D();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.color(r, g, b, a);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION);
            worldrenderer.pos((double) left, (double) bottom, 0.0D).endVertex();
            worldrenderer.pos((double) right, (double) bottom, 0.0D).endVertex();
            worldrenderer.pos((double) right, (double) top, 0.0D).endVertex();
            worldrenderer.pos((double) left, (double) top, 0.0D).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();
        } catch (Throwable t) {
            try {
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glColor4f(r, g, b, a);
                GL11.glBegin(GL11.GL_QUADS);
                GL11.glVertex2d(left, bottom);
                GL11.glVertex2d(right, bottom);
                GL11.glVertex2d(right, top);
                GL11.glVertex2d(left, top);
                GL11.glEnd();
                GL11.glEnable(GL11.GL_TEXTURE_2D);
            } catch (Throwable ignored) {}
        }
    }

    private static void renderButtonBox(Minecraft mc, int x, int y, int width, int height, String displayString, boolean hovered, boolean enabled) {
        try {
            ensureGLState();
            bindWidgetsTexture(mc);

            int hoverState = !enabled ? 0 : (hovered ? 2 : 1);
            int halfW = width / 2;

            drawTexturedRect(x, y, 0, 46 + hoverState * 20, halfW, height);
            drawTexturedRect(x + halfW, y, 200 - halfW, 46 + hoverState * 20, halfW, height);

            if (displayString != null) {
                int color = !enabled ? 0xA0A0A0 : (hovered ? 0xFFFFA0 : 0xE0E0E0);
                int textW = getStringWidth(mc, displayString);
                drawString(mc, displayString, x + (width - textW) / 2, y + (height - 8) / 2, color);
            }
        } catch (Throwable ignored) {}
    }

    private static void drawTexturedRect(int x, int y, int u, int v, int width, int height) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        try {
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldrenderer.pos((double) (x + 0), (double) (y + height), 0.0D).tex((double) ((float) (u + 0) * f), (double) ((float) (v + height) * f1)).endVertex();
            worldrenderer.pos((double) (x + width), (double) (y + height), 0.0D).tex((double) ((float) (u + width) * f), (double) ((float) (v + height) * f1)).endVertex();
            worldrenderer.pos((double) (x + width), (double) (y + 0), 0.0D).tex((double) ((float) (u + width) * f), (double) ((float) (v + 0) * f1)).endVertex();
            worldrenderer.pos((double) (x + 0), (double) (y + 0), 0.0D).tex((double) ((float) (u + 0) * f), (double) ((float) (v + 0) * f1)).endVertex();
            tessellator.draw();
        } catch (Throwable t) {
            try {
                GL11.glBegin(GL11.GL_QUADS);
                GL11.glTexCoord2f((float) u * f, (float) (v + height) * f1);
                GL11.glVertex2f(x, y + height);
                GL11.glTexCoord2f((float) (u + width) * f, (float) (v + height) * f1);
                GL11.glVertex2f(x + width, y + height);
                GL11.glTexCoord2f((float) (u + width) * f, (float) v * f1);
                GL11.glVertex2f(x + width, y);
                GL11.glTexCoord2f((float) u * f, (float) v * f1);
                GL11.glVertex2f(x, y);
                GL11.glEnd();
            } catch (Throwable ignored) {}
        }
    }

    private static void drawString(Minecraft mc, String text, int x, int y, int color) {
        if (mc == null || text == null) return;
        FontRenderer fr = getFontRendererInstance(mc);
        if (fr == null) return;

        ensureGLState();

        try {
            fr.drawStringWithShadow(text, (float) x, (float) y, color);
            return;
        } catch (Throwable ignored) {}

        Class<?> clazz = fr.getClass();
        while (clazz != null && clazz != Object.class) {
            for (Method m : clazz.getDeclaredMethods()) {
                m.setAccessible(true);
                Class<?>[] params = m.getParameterTypes();
                if (params.length == 4 || params.length == 5) {
                    if (params[0] == String.class) {
                        try {
                            if (params.length == 4) {
                                Number nX = (params[1] == float.class || params[1] == Float.class) ? (float) x : x;
                                Number nY = (params[2] == float.class || params[2] == Float.class) ? (float) y : y;
                                m.invoke(fr, text, nX, nY, color);
                                return;
                            } else if (params.length == 5) {
                                Number nX = (params[1] == float.class || params[1] == Float.class) ? (float) x : x;
                                Number nY = (params[2] == float.class || params[2] == Float.class) ? (float) y : y;
                                Boolean bShadow = params[4] == boolean.class || params[4] == Boolean.class ? true : false;
                                m.invoke(fr, text, nX, nY, color, bShadow);
                                return;
                            }
                        } catch (Throwable ignored) {}
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    private static FontRenderer getFontRendererInstance(Minecraft mc) {
        if (mc == null) return null;
        try {
            if (mc.fontRendererObj != null) return mc.fontRendererObj;
        } catch (Throwable ignored) {}
        try {
            Field f = Minecraft.class.getField("field_71466_p");
            return (FontRenderer) f.get(mc);
        } catch (Throwable t) {
            try {
                Field f = Minecraft.class.getField("fontRendererObj");
                return (FontRenderer) f.get(mc);
            } catch (Throwable t2) {
                return null;
            }
        }
    }

    private static int getStringWidth(Minecraft mc, String text) {
        if (mc == null || text == null) return 0;
        FontRenderer fr = getFontRendererInstance(mc);
        if (fr == null) return text.length() * 6;

        try {
            return fr.getStringWidth(text);
        } catch (Throwable ignored) {}

        Class<?> clazz = fr.getClass();
        while (clazz != null && clazz != Object.class) {
            for (Method m : clazz.getDeclaredMethods()) {
                m.setAccessible(true);
                Class<?>[] params = m.getParameterTypes();
                if (params.length == 1 && params[0] == String.class && (m.getReturnType() == int.class || m.getReturnType() == Integer.class)) {
                    try {
                        return (Integer) m.invoke(fr, text);
                    } catch (Throwable ignored) {}
                }
            }
            clazz = clazz.getSuperclass();
        }
        return text.length() * 6;
    }
}
