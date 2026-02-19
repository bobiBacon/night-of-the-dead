package net.bobbacon2.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.bobbacon2.NightOfTheDeadClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class RefiningScreen extends HandledScreen<RefiningScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(NightOfTheDeadClient.MOD_ID,"textures/gui/refinery.png");
    private int x;
    private int y;

    public RefiningScreen(RefiningScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundHeight = 165;
    }

    @Override
    protected void init() {
        super.init();
//        titleY= y+3;
//        playerInventoryTitleY = y+;

    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        x = (width - backgroundWidth) / 2;
        y = (height - backgroundHeight) / 2;
        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
        drawProgressArrow(context, x, y);
        drawFuel(context,x,y);
//        drawForeground(context,x,y);
    }


//    @Override
//    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
//        context.drawText(this.textRenderer, this.title, this.titleX, this.titleY, 4210752, false);
//    }

    private void drawProgressArrow(DrawContext context, int x, int y) {
        if(handler.isCrafting()) {
            context.drawTexture(TEXTURE, x + 97, y + 34, 176, 14, handler.getScaledProgress(),15 );
        }
    }
    private void drawFuel(DrawContext context, int x, int y) {
        if(handler.isBurning()) {
            context.drawTexture(TEXTURE, x + 28, y + 23, 176, 30, 9,handler.getFuelValue() );
        }
    }
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
