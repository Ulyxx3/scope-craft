package net.scopecraft.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.scopecraft.config.ScopeConfig;

import java.io.IOException;

public class ScopeConfigGui extends GuiScreen {
    private final GuiScreen parentScreen;

    public ScopeConfigGui(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
    }

    @Override
    public void initGui() {
        this.buttonList.clear();

        int centerX = this.width / 2;
        int startY = this.height / 4;

        // Mod Enabled Toggle
        this.buttonList.add(new GuiButton(1, centerX - 100, startY, 200, 20, getEnabledText()));

        // Trigger Mode Toggle (Only when Aiming vs Holding Bow)
        this.buttonList.add(new GuiButton(2, centerX - 100, startY + 26, 200, 20, getAimingModeText()));

        // Zoom Level Button (- / +)
        this.buttonList.add(new GuiButton(3, centerX - 100, startY + 52, 200, 20, getZoomText()));

        // Scope Opacity Button
        this.buttonList.add(new GuiButton(4, centerX - 100, startY + 78, 200, 20, getOpacityText()));

        // Done / Save Button
        this.buttonList.add(new GuiButton(200, centerX - 100, startY + 120, 200, 20, "Sauvegarder & Quitter"));
    }

    private String getEnabledText() {
        return "Mod Scope: " + (ScopeConfig.INSTANCE.enabled ? "§aACTIVÉ" : "§cDÉSACTIVÉ");
    }

    private String getAimingModeText() {
        return "Activation: " + (ScopeConfig.INSTANCE.scopeOnlyWhenAiming ? "En visant uniquement" : "Arc en main");
    }

    private String getZoomText() {
        return String.format("Zoom FOV: §e%.1fx§r (Clic pour modifier)", ScopeConfig.INSTANCE.zoomFactor);
    }

    private String getOpacityText() {
        return String.format("Opacité Scope: §e%.0f%%§r (Clic pour modifier)", ScopeConfig.INSTANCE.scopeOpacity * 100);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1) {
            ScopeConfig.INSTANCE.enabled = !ScopeConfig.INSTANCE.enabled;
            button.displayString = getEnabledText();
        } else if (button.id == 2) {
            ScopeConfig.INSTANCE.scopeOnlyWhenAiming = !ScopeConfig.INSTANCE.scopeOnlyWhenAiming;
            button.displayString = getAimingModeText();
        } else if (button.id == 3) {
            // Cycle Zoom factor: 1.5x -> 2.0x -> 3.0x -> 4.0x -> 5.0x -> 8.0x -> 1.5x
            float zoom = ScopeConfig.INSTANCE.zoomFactor;
            if (zoom >= 8.0f) zoom = 1.5f;
            else if (zoom >= 5.0f) zoom = 8.0f;
            else if (zoom >= 4.0f) zoom = 5.0f;
            else if (zoom >= 3.0f) zoom = 4.0f;
            else if (zoom >= 2.0f) zoom = 3.0f;
            else zoom = 2.0f;
            ScopeConfig.INSTANCE.zoomFactor = zoom;
            button.displayString = getZoomText();
        } else if (button.id == 4) {
            // Cycle Opacity: 100% -> 75% -> 50% -> 25% -> 100%
            float opacity = ScopeConfig.INSTANCE.scopeOpacity;
            if (opacity <= 0.3f) opacity = 1.0f;
            else opacity -= 0.25f;
            ScopeConfig.INSTANCE.scopeOpacity = opacity;
            button.displayString = getOpacityText();
        } else if (button.id == 200) {
            ScopeConfig.save();
            this.mc.displayGuiScreen(this.parentScreen);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, "§lOptions de ScopeCraft", this.width / 2, 20, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
