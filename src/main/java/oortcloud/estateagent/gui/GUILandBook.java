package oortcloud.estateagent.gui;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import oortcloud.estateagent.EstateAgent;
import oortcloud.estateagent.chunk.ChunkCoordIntPairWithDimension;
import oortcloud.estateagent.chunk.ChunkManager;
import oortcloud.estateagent.chunk.ChunkRenderingManager;
import oortcloud.estateagent.items.ItemLandBook;
import oortcloud.estateagent.items.ModItems;
import oortcloud.estateagent.lib.References;
import oortcloud.estateagent.lib.Strings;
import oortcloud.estateagent.properties.ExtendedPropertyLand;
import oortcloud.network.PacketGeneralServer;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.ImmutableList;

public class GuiLandBook extends GuiScreen {

	ResourceLocation backgroundimage = new ResourceLocation(References.MODID.toLowerCase() + ":" + "textures/gui/guilandbook.png");

	private int xSize;
	private int ySize;
	private String player;

	private int page;
	private int radius;

	public GuiLandBook(int radius) {
		xSize = 256;
		ySize = 158;
		this.player = Minecraft.getMinecraft().thePlayer.getCommandSenderName();

		this.radius = radius;
	}

	@Override
	public void initGui() {
		int zeroX = (this.width - xSize) / 2;
		int zeroY = (this.height - ySize) / 2;

		for (int j = 0; j < 8; j++) {
			this.buttonList.add(new GuiButtonSell(j, zeroX + 105, zeroY + 15 + 15 * j));
		}
		for (int j = 0; j < 8; j++) {
			this.buttonList.add(new GuiButtonSell(8 + j, zeroX + 225, zeroY + 15 + 15 * j));
		}
		this.buttonList.add(new GuiButtonNext(16, zeroX + 25, zeroY + 133, false));
		this.buttonList.add(new GuiButtonNext(17, zeroX + 215, zeroY + 133, true));

		refresh();
	}

	private void refresh() {
		for (int i = 0; i < 16; i++) {
			if (ChunkRenderingManager.getInstance().chunks.size() > this.page * 16 + i) {
				((GuiButton) this.buttonList.get(i)).enabled = true;
				((GuiButton) this.buttonList.get(i)).visible = true;
			} else {
				((GuiButton) this.buttonList.get(i)).enabled = false;
				((GuiButton) this.buttonList.get(i)).visible = false;
			}
		}
	}

	/**
	 * Need to solve late-synch problem with "chunks" variable
	 * @param size
	 */
	private void refresh(int size) {
		for (int i = 0; i < 16; i++) {
			if (size > this.page * 16 + i) {
				((GuiButton) this.buttonList.get(i)).enabled = true;
				((GuiButton) this.buttonList.get(i)).visible = true;
			} else {
				((GuiButton) this.buttonList.get(i)).enabled = false;
				((GuiButton) this.buttonList.get(i)).visible = false;
			}
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		int size = ChunkRenderingManager.getInstance().chunks.size();
		switch (button.id) {
		case 16:
			if (this.page > 0)
				this.page--;
			refresh();
			break;
		case 17:
			if ((size - 1) / 16 > this.page)
				this.page++;
			refresh();
			break;
		default:
			if (size > this.page * 16 + button.id) {
				PacketGeneralServer msg = new PacketGeneralServer(1);
				msg.setString(player);
				ChunkCoordIntPairWithDimension chunk = ChunkRenderingManager.getInstance().chunks.get(this.page * 16 + button.id);
				msg.setInt(chunk.dim);
				msg.setInt(chunk.chunkXPos);
				msg.setInt(chunk.chunkZPos);
				EstateAgent.simpleChannel.sendToServer(msg);
				if (page == (size-1)/16 && (size % 16) == 1) {
					if (this.page > 0)
						this.page--;
				}
				refresh(size-1);
			}
			break;
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

		CopyOnWriteArrayList<ChunkCoordIntPairWithDimension> chunks = ChunkRenderingManager.getInstance().chunks;

		this.fontRendererObj.drawString("Radius : " + radius, zeroX + 45, zeroY + ySize - 20, 1);

		ExtendedPropertyLand property = (ExtendedPropertyLand) Minecraft.getMinecraft().thePlayer.getExtendedProperties(Strings.extendedPropertiesKey);
		if (property != null) {
			if (property.getForcableChunks() != Integer.MAX_VALUE) {
				this.fontRendererObj.drawString("Allowed Chunks: " + property.getForcableChunks(), zeroX + xSize / 2 + 5, zeroY + ySize - 20, 1);
			} else {
				this.fontRendererObj.drawString("Opped Player", zeroX + xSize / 2 + 5, zeroY + ySize - 20, 1);
			}
		}

		for (int j = 0; j < 8; j++) {
			if (chunks.size() > this.page * 16 + j) {
				ChunkCoordIntPairWithDimension i = chunks.get(this.page * 16 + j);
				this.fontRendererObj.drawString("DIM: " + i.dim + " ( " + i.chunkXPos + " , " + i.chunkZPos + " )", (zeroX + 20), (zeroY + 15 + 15 * j), 1);
			}
		}
		for (int j = 0; j < 8; j++) {
			if (chunks.size() > this.page * 16 + 8 + j) {
				ChunkCoordIntPairWithDimension i = chunks.get(this.page * 16 + 8 + j);
				this.fontRendererObj.drawString("DIM: " + i.dim + " ( " + i.chunkXPos + " , " + i.chunkZPos + " )", (zeroX + 140), (zeroY + 15 + 15 * j), 1);
			}
		}
	}

	/**
	 * Handles mouse input.
	 */
	public void handleMouseInput() {
		super.handleMouseInput();
		int i = Mouse.getEventDWheel();
		if (i != 0) {
			if (i > 1) {
				radius++;
			}

			if (i < -1) {
				if (radius > 0) {
					radius--;
				}
			}
		}
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		PacketGeneralServer msg = new PacketGeneralServer(2);
		msg.setString(player);
		msg.setInt(radius);
		EstateAgent.simpleChannel.sendToServer(msg);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

}
