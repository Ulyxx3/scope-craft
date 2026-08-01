package net.scopecraft.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.scopecraft.config.ScopeConfig;

public class ScopeRenderHandler {
    private static final ResourceLocation SCOPE_TEXTURE = new ResourceLocation("scopecraft", "textures/gui/scope.png");
    private final Minecraft mc = Minecraft.getMinecraft();

    /**
     * Vérifie si le joueur est actuellement en train de viser avec l'arc
     */
    public boolean isAimingWithBow() {
        if (!ScopeConfig.INSTANCE.enabled) return false;
        if (mc.thePlayer == null) return false;

        EntityPlayer player = mc.thePlayer;
        ItemStack heldItem = player.getHeldItem();

        if (heldItem == null || !(heldItem.getItem() instanceof ItemBow)) {
            return false;
        }

        if (ScopeConfig.INSTANCE.scopeOnlyWhenAiming) {
            // Le joueur doit utiliser l'arc (clic droit maintenu)
            return player.isUsingItem();
        } else {
            // Actif dès qu'il tient un arc
            return true;
        }
    }

    /**
     * Gestion du Zoom (modification dynamique du FOV)
     */
    @SubscribeEvent
    public void onFOVUpdate(EntityViewRenderEvent.FOVModifier event) {
        if (isAimingWithBow()) {
            float currentFov = event.getFov();
            float zoom = Math.max(1.0f, ScopeConfig.INSTANCE.zoomFactor);
            event.setFov(currentFov / zoom);
        }
    }

    /**
     * Affichage de la superposition Scope PNG à l'écran
     */
    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.HELMET) return;
        if (!isAimingWithBow()) return;

        ScaledResolution scaled = event.resolution;
        int width = scaled.getScaledWidth();
        int height = scaled.getScaledHeight();

        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1.0F, 1.0F, 1.0F, ScopeConfig.INSTANCE.scopeOpacity);
        GlStateManager.disableAlpha();

        mc.getTextureManager().bindTexture(SCOPE_TEXTURE);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);

        worldrenderer.pos(0.0D, (double) height, -90.0D).tex(0.0D, 1.0D).endVertex();
        worldrenderer.pos((double) width, (double) height, -90.0D).tex(1.0D, 1.0D).endVertex();
        worldrenderer.pos((double) width, 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
        worldrenderer.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();

        tessellator.draw();

        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
