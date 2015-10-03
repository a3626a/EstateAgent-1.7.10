package oortcloud.estateagent.gui;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import oortcloud.estateagent.EstateAgent;
import oortcloud.estateagent.chunk.ChunkCoordIntPairWithDimension;
import oortcloud.estateagent.chunk.ChunkManager;
import oortcloud.estateagent.lib.References;
import oortcloud.estateagent.lib.Strings;
import oortcloud.estateagent.properties.ExtendedPropertyLand;
import oortcloud.network.PacketGeneralServer;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.ImmutableList;

public class GuiLandBook extends GuiScreen {

	ResourceLocation backgroundimage = new ResourceLocation(References.MODID.toLowerCase() + ":" + "textures/gui/guilandbook.png");

	private int xSize;
	private int ySize;
	private String player;

	private int page;

	public GuiLandBook() {
		xSize = 256;
		ySize = 158;
		this.player = Minecraft.getMinecraft().thePlayer.getCommandSenderName();
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
			if (ChunkManager.getInstance().sideOfLoadedChunks(player) <= this.page * 16 + i) {
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
			if ((ChunkManager.getInstance().sideOfLoadedChunks(player) - 1) / 16 > this.page)
				this.page++;
			break;
		default:
			if (ChunkManager.getInstance().sideOfLoadedChunks(player) > this.page * 16 + button.id) {
				PacketGeneralServer msg = new PacketGeneralServer(1);
				msg.setString(player);
				ImmutableList<ChunkCoordIntPairWithDimension> chunks = ChunkManager.getInstance().getLoadedChunksByPlayerImmutable(player);
				ChunkCoordIntPairWithDimension chunk = chunks.get(this.page * 16 + button.id);
				msg.setInt(chunk.dim);
				msg.setInt(chunk.chunkXPos);
				msg.setInt(chunk.chunkZPos);
				EstateAgent.simpleChannel.sendToServer(msg);
				if ((ChunkManager.getInstance().sideOfLoadedChunks(player) - 1) / 16 == this.page) {
					((GuiButton) this.buttonList.get((ChunkManager.getInstance().sideOfLoadedChunks(player) % 16) - 1)).enabled = false;
					((GuiButton) this.buttonList.get((ChunkManager.getInstance().sideOfLoadedChunks(player) % 16) - 1)).visible = false;
				}
			}
		}

		if ((button.id == 17) && (ChunkManager.getInstance().sideOfLoadedChunks(player) - 1) / 16 == this.page) {
			for (int i = ChunkManager.getInstance().sideOfLoadedChunks(player) % 16; i < 16; i++) {
				((GuiButton) this.buttonList.get(i)).enabled = false;
				((GuiButton) this.buttonList.get(i)).visible = false;
			}
		}
		if (button.id == 16 && ((ChunkManager.getInstance().sideOfLoadedChunks(player) - 1) / 16) - 1 == this.page) {
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

		ImmutableList<ChunkCoordIntPairWithDimension> chunks = ChunkManager.getInstance().getLoadedChunksByPlayerImmutable(player);

		ExtendedPropertyLand property = (ExtendedPropertyLand) Minecraft.getMinecraft().thePlayer.getExtendedProperties(Strings.extendedPropertiesKey);
		if (property != null) {
			if (property.getForcableChunks() != Integer.MAX_VALUE) {
				this.fontRendererObj.drawString("Allowed Chunks: " + property.getForcableChunks(), zeroX + xSize / 2 + 5, zeroY + ySize - 20, 1);
			} else {
				this.fontRendererObj.drawString("Opped Player", zeroX + xSize / 2 + 5, zeroY + ySize - 20, 1);
			}
		}

		for (int j = 0; j < 8; j++) {
			if (ChunkManager.getInstance().sideOfLoadedChunks(player) > this.page * 16 + j) {
				ChunkCoordIntPairWithDimension i = chunks.get(this.page * 16 + j);
				this.fontRendererObj.drawString("DIM: " + i.dim + " ( " + i.chunkXPos + " , " + i.chunkZPos + " )", (zeroX + 20), (zeroY + 15 + 15 * j), 1);
			}
		}
		for (int j = 0; j < 8; j++) {
			if (ChunkManager.getInstance().sideOfLoadedChunks(player) > this.page * 16 + 8 + j) {
				ChunkCoordIntPairWithDimension i = chunks.get(this.page * 16 + 8 + j);
				this.fontRendererObj.drawString("DIM: " + i.dim + " ( " + i.chunkXPos + " , " + i.chunkZPos + " )", (zeroX + 140), (zeroY + 15 + 15 * j), 1);
			}
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

}
