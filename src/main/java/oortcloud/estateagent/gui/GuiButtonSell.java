package oortcloud.estateagent.gui;

import org.lwjgl.opengl.GL11;

import oortcloud.estateagent.lib.References;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiButtonSell extends GuiButton {

	protected static final ResourceLocation buttonTextures = new ResourceLocation(References.MODID.toLowerCase() + ":" + "textures/gui/guibutton.png");

	public GuiButtonSell(int id, int x, int y) {
		super(id, x, y, 9, 9, "");
	}

	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		if (this.visible) {
			this.field_146123_n = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
			int flag1 = this.getHoverState(this.field_146123_n) - 1;
			mc.getTextureManager().bindTexture(buttonTextures);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			this.drawTexturedModalRect(this.xPosition, this.yPosition, flag1 * this.width, 26, this.width, this.height);
			this.mouseDragged(mc, mouseX, mouseY);
		}
	}

}
