package oortcloud.estateagent.hadler;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import oortcloud.estateagent.chunk.ChunkCoordIntPairWithDimension;
import oortcloud.estateagent.chunk.ChunkManager;
import oortcloud.estateagent.items.ModItems;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class MinecraftForgeEventHandler {

	@SubscribeEvent
	public void renderChunkBoundary(RenderWorldLastEvent event) {
		if (Minecraft.getMinecraft().thePlayer == null) {
			return;
		}

		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		ItemStack heldItem = player.getHeldItem();
		if (heldItem != null && heldItem.getItem() == ModItems.landbook) {
			Tessellator tessellator = Tessellator.instance;
			float partialTickTime = event.partialTicks;
			int dim = Minecraft.getMinecraft().theWorld.provider.dimensionId;

			int y = (int) player.posY;
			int num = 64;
			float dx = 0.25F;

			GL11.glEnable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			double px = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTickTime;
			double py = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTickTime;
			double pz = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTickTime;

			GL11.glPushMatrix();
			GL11.glTranslated(-px, 0, -pz);

			ArrayList<ChunkCoordIntPairWithDimension> list = ChunkManager.list.get(player.getCommandSenderName());

			if (list == null)
				return;

			for (ChunkCoordIntPairWithDimension i : list) {
				GL11.glPushMatrix();
				GL11.glTranslatef(i.chunkXPos * 16, 0, i.chunkZPos * 16);
				if (i.dim == dim) {
					if (!hasChunk(list, dim, i.chunkXPos, i.chunkZPos - 1)) {
						GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
						for (int j = 0; j <= num; j++) {
							GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
							GL11.glVertex3d(dx * j, -py, 0);
							GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.1F);
							GL11.glVertex3d(dx * j, 4 + 4 * Math.sin((16 * Math.PI / 64.0) * dx * j - (Minecraft.getMinecraft().theWorld.getWorldTime() + partialTickTime) / 10.0), 0);
						}
						GL11.glEnd();
					}
					if (!hasChunk(list, dim, i.chunkXPos + 1, i.chunkZPos)) {
						GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
						for (int j = 0; j <= num; j++) {
							GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
							GL11.glVertex3d(16, -py, dx * j);
							GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.1F);
							GL11.glVertex3d(16, 4 + 4 * Math.sin(4 * Math.PI + (16 * Math.PI / 64.0) * dx * j - (Minecraft.getMinecraft().theWorld.getWorldTime() + partialTickTime) / 10.0), dx * j);
						}
						GL11.glEnd();
					}
					if (!hasChunk(list, dim, i.chunkXPos, i.chunkZPos + 1)) {
						GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
						for (int j = 0; j <= num; j++) {
							GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
							GL11.glVertex3d(16 - dx * j, -py, 16);
							GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.1F);
							GL11.glVertex3d(16 - dx * j, 4 + 4 * Math.sin(8 * Math.PI + (16 * Math.PI / 64.0) * dx * j - (Minecraft.getMinecraft().theWorld.getWorldTime() + partialTickTime) / 10.0), 16);
						}
						GL11.glEnd();
					}
					if (!hasChunk(list, dim, i.chunkXPos - 1, i.chunkZPos)) {
						GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
						for (int j = 0; j <= num; j++) {
							GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
							GL11.glVertex3d(0, -py, 16 - dx * j);
							GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.1F);
							GL11.glVertex3d(0, 4 + 4 * Math.sin(12 * Math.PI + (16 * Math.PI / 64.0) * dx * j - (Minecraft.getMinecraft().theWorld.getWorldTime() + partialTickTime) / 10.0), 16 - dx * j);
						}
						GL11.glEnd();
					}
				}
				GL11.glPopMatrix();
			}

			GL11.glPopMatrix();

			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			// GL11.glEnable(GL11.GL_LIGHTING);
		}
	}

	private boolean hasChunk(ArrayList<ChunkCoordIntPairWithDimension> list, int dim, int chunkX, int chunkZ) {
		for (ChunkCoordIntPairWithDimension i : list) {
			if (i.dim == dim && i.chunkXPos == chunkX && i.chunkZPos == chunkZ) {
				return true;
			}
		}
		return false;
	}

}
