package oortcloud.estateagent.gui;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import oortcloud.estateagent.EstateAgent;
import oortcloud.estateagent.chunk.ChunkCoordIntPairWithDimension;
import oortcloud.estateagent.lib.References;
import oortcloud.network.PacketGeneralServer;

import org.lwjgl.opengl.GL11;

public class GUILandBook extends GuiScreen {

	public static ArrayList<ChunkCoordIntPairWithDimension> chunks;

	ResourceLocation backgroundimage = new ResourceLocation(References.MODID.toLowerCase() + ":" + "textures/gui/guilandbook.png");

	private int xSize;
	private int ySize;
	private String player;

	private int page;

	public GUILandBook(String player) {
		xSize = 256;
		ySize = 158;
		this.player = player;

		if (chunks == null) {
			PacketGeneralServer msg = new PacketGeneralServer(0);
			msg.setString(this.player);
			EstateAgent.simpleChannel.sendToServer(msg);
		}
	}

	@Override
	public void initGui() {

		int zeroX = (this.width - xSize) / 2;
		int zeroY = (this.height - ySize) / 2;

		for (int j = 0; j < 8; j++) {
			this.buttonList.add(new GuiButtonSell(j, zeroX + 105, zeroY + 15 + 15 * j, false));
		}
		for (int j = 0; j < 8; j++) {
			this.buttonList.add(new GuiButtonSell(8 + j, zeroX + 225, zeroY + 15 + 15 * j, false));
		}
		this.buttonList.add(new GuiButtonNext(16, zeroX + 25, zeroY + 133, false));
		this.buttonList.add(new GuiButtonNext(17, zeroX + 215, zeroY + 133, true));

		for (int i = 0; i < 16; i++) {
			if ((this.chunks == null ? 0 : this.chunks.size()) <= this.page * 16 + i) {
				((GuiButton) this.buttonList.get(i)).enabled = false;
				((GuiButton) this.buttonList.get(i)).visible = false;
			}
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) {

		switch (button.id) {
		case 16:
			if (this.page > 0)
				this.page--;
			break;
		case 17:
			if ((this.chunks.size() - 1) / 16 > this.page)
				this.page++;
			break;
		default:
			if (this.chunks.size() > this.page * 16 + button.id) {
				PacketGeneralServer msg = new PacketGeneralServer(1);
				msg.setString(player);
				msg.setInt(this.page * 16 + button.id);
				EstateAgent.simpleChannel.sendToServer(msg);
				if ((this.chunks.size() - 1) / 16 == this.page) {
					((GuiButton) this.buttonList.get((this.chunks.size() % 16) - 1)).enabled = false;
					((GuiButton) this.buttonList.get((this.chunks.size() % 16) - 1)).visible = false;
				}
			}
		}

		if ((button.id == 17) && (this.chunks.size() - 1) / 16 == this.page) {
			for (int i = this.chunks.size() % 16; i < 16; i++) {
				((GuiButton) this.buttonList.get(i)).enabled = false;
				((GuiButton) this.buttonList.get(i)).visible = false;
			}
		}
		if (button.id == 16 && ((this.chunks.size() - 1) / 16) - 1 == this.page) {
			for (int i = 0; i < 16; i++) {
				((GuiButton) this.buttonList.get(i)).enabled = true;
				((GuiButton) this.buttonList.get(i)).visible = true;
			}
		}

	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTick) {

		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		int zeroX = (this.width - xSize) / 2;
		int zeroY = (this.height - ySize) / 2;
		this.mc.getTextureManager().bindTexture(backgroundimage);
		this.drawTexturedModalRect(zeroX, zeroY, 0, 0, xSize, ySize);

		super.drawScreen(mouseX, mouseY, partialTick);

		if (this.chunks != null) {
			for (int j = 0; j < 8; j++) {
				if (this.chunks.size() > this.page * 16 + j) {
					ChunkCoordIntPairWithDimension i = this.chunks.get(this.page * 16 + j);
					this.fontRendererObj.drawString("DIM: " + i.dim + " ( " + i.chunkXPos + " , " + i.chunkZPos + " )", (zeroX + 20), (zeroY + 15 + 15 * j), 1);
				}
			}
			for (int j = 0; j < 8; j++) {
				if (this.chunks.size() > this.page * 16 + 8 + j) {
					ChunkCoordIntPairWithDimension i = this.chunks.get(this.page * 16 + 8 + j);
					this.fontRendererObj.drawString("DIM: " + i.dim + " ( " + i.chunkXPos + " , " + i.chunkZPos + " )", (zeroX + 140), (zeroY + 15 + 15 * j), 1);
				}
			}
		}

	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

}
