package net.scopecraft.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

public class GuiSliderButton extends GuiButton {
    public interface ISliderResponder {
        void onValueChanged(GuiSliderButton slider, float value);
    }

    private final float minValue;
    private final float maxValue;
    private final String prefix;
    private final String suffix;
    private float sliderValue; // 0.0 à 1.0
    public boolean dragging = false;
    private final ISliderResponder responder;

    public GuiSliderButton(int id, int x, int y, int width, int height, String prefix, String suffix, float minVal, float maxVal, float currentVal, ISliderResponder responder) {
        super(id, x, y, width, height, "");
        this.prefix = prefix;
        this.suffix = suffix;
        this.minValue = minVal;
        this.maxValue = maxVal;
        this.responder = responder;
        setValue(currentVal);
    }

    public float getValue() {
        return minValue + (maxValue - minValue) * sliderValue;
    }

    public void setValue(float val) {
        float clamped = Math.max(minValue, Math.min(maxValue, val));
        this.sliderValue = (clamped - minValue) / (maxValue - minValue);
        updateDisplayString();
    }

    private void updateDisplayString() {
        float val = getValue();
        if ("%".equals(suffix)) {
            this.displayString = String.format("%s: §e%.0f%%§r", prefix, val * 100.0f);
        } else if ("x".equals(suffix)) {
            this.displayString = String.format("%s: §e%.1fx§r", prefix, val);
        } else {
            this.displayString = String.format("%s: §e%.2f%s§r", prefix, val, suffix);
        }
    }

    @Override
    protected int getHoverState(boolean mouseOver) {
        return 0;
    }

    @Override
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible && this.dragging) {
            this.sliderValue = (float) (mouseX - (this.xPosition + 4)) / (float) (this.width - 8);
            if (this.sliderValue < 0.0F) this.sliderValue = 0.0F;
            if (this.sliderValue > 1.0F) this.sliderValue = 1.0F;

            updateDisplayString();
            if (responder != null) {
                responder.onValueChanged(this, getValue());
            }
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (super.mousePressed(mc, mouseX, mouseY)) {
            this.sliderValue = (float) (mouseX - (this.xPosition + 4)) / (float) (this.width - 8);
            if (this.sliderValue < 0.0F) this.sliderValue = 0.0F;
            if (this.sliderValue > 1.0F) this.sliderValue = 1.0F;

            updateDisplayString();
            if (responder != null) {
                responder.onValueChanged(this, getValue());
            }
            this.dragging = true;
            return true;
        }
        return false;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        this.dragging = false;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (!this.visible) return;

        super.drawButton(mc, mouseX, mouseY);

        // Texture du curseur (knob)
        mc.getTextureManager().bindTexture(buttonTextures);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        int knobX = this.xPosition + (int) (this.sliderValue * (float) (this.width - 8));
        int knobState = this.hovered ? 2 : 1;
        this.drawTexturedModalRect(knobX, this.yPosition, 0, 46 + knobState * 20, 4, 20);
        this.drawTexturedModalRect(knobX + 4, this.yPosition, 196, 46 + knobState * 20, 4, 20);
    }
}
