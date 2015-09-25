package oortcloud.estateagent.hadler;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import oortcloud.estateagent.chunk.ChunkCoordIntPairWithDimension;
import oortcloud.estateagent.chunk.ChunkManager;
import oortcloud.estateagent.items.ModItems;
import oortcloud.estateagent.lib.Strings;
import oortcloud.estateagent.properties.ExtendedPropertyLand;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MinecraftForgeEventHandler {

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderChunkBoundary(RenderWorldLastEvent event) {
		if (Minecraft.getMinecraft().thePlayer == null) {
			return;
		}

		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		ItemStack heldItem = player.getHeldItem();
		if (heldItem != null && (heldItem.getItem() == ModItems.landbook || heldItem.getItem() == ModItems.landdocument)) {
			ArrayList<ChunkCoordIntPairWithDimension> list = ChunkManager.list.get(player.getCommandSenderName());
			if (list == null)
				return;

			Tessellator tessellator = Tessellator.instance;
			float partialTickTime = event.partialTicks;
			int dim = Minecraft.getMinecraft().theWorld.provider.dimensionId;

			double px = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTickTime;
			double py = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTickTime;
			double pz = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTickTime;

			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glDepthMask(false);

			GL11.glPushMatrix();
			GL11.glTranslated(-px, 0, -pz);

			int num = 64;
			float offset = 0.1F;

			for (ChunkCoordIntPairWithDimension i : list) {
				if (i.dim == dim) {
					GL11.glPushMatrix();
					GL11.glTranslatef(i.chunkXPos * 16, 0, i.chunkZPos * 16);
					boolean hasChunkSouth = hasChunk(list, dim, i.chunkXPos, i.chunkZPos + 1);
					boolean hasChunkWest = hasChunk(list, dim, i.chunkXPos - 1, i.chunkZPos);
					boolean hasChunkNorth = hasChunk(list, dim, i.chunkXPos, i.chunkZPos - 1);
					boolean hasChunkEast = hasChunk(list, dim, i.chunkXPos + 1, i.chunkZPos);
					if (!hasChunkNorth) {
						GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
						float start = 0;
						float length = 16;
						if (!hasChunkWest) {
							start = offset;
							length -= offset;
						}
						if (!hasChunkEast) {
							length -= offset;
						}
						float dx = length / (float) num;
						for (int j = 0; j <= num; j++) {
							GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
							GL11.glVertex3d(start + dx * j, -py, offset);
							GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.1F);
							GL11.glVertex3d(start + dx * j, 4 + 4 * Math.sin((4 * Math.PI / 64.0) * j - (Minecraft.getMinecraft().theWorld.getWorldTime() + partialTickTime) / 10.0), offset);
						}

						GL11.glEnd();
					}
					if (!hasChunkEast) {
						GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
						float start = 0;
						float length = 16;
						if (!hasChunkNorth) {
							start = offset;
							length -= offset;
						}
						if (!hasChunkSouth) {
							length -= offset;
						}
						float dx = length / (float) num;
						for (int j = 0; j <= num; j++) {
							GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
							GL11.glVertex3d(16 - offset, -py, start + dx * j);
							GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.1F);
							GL11.glVertex3d(16 - offset, 4 + 4 * Math.sin(4 * Math.PI + (4 * Math.PI / 64.0) * j - (Minecraft.getMinecraft().theWorld.getWorldTime() + partialTickTime) / 10.0), start + dx * j);
						}
						GL11.glEnd();
					}
					if (!hasChunkSouth) {
						GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
						float start = 0;
						float length = 16;
						if (!hasChunkEast) {
							start = offset;
							length -= offset;
						}
						if (!hasChunkWest) {
							length -= offset;
						}
						float dx = length / (float) num;
						for (int j = 0; j <= num; j++) {
							GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
							GL11.glVertex3d(16 - start - dx * j, -py, 16 - offset);
							GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.1F);
							GL11.glVertex3d(16 - start - dx * j, 4 + 4 * Math.sin(8 * Math.PI + (4 * Math.PI / 64.0) * j - (Minecraft.getMinecraft().theWorld.getWorldTime() + partialTickTime) / 10.0), 16 - offset);
						}
						GL11.glEnd();
					}
					if (!hasChunkWest) {
						GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
						float start = 0;
						float length = 16;
						if (!hasChunkSouth) {
							start = offset;
							length -= offset;
						}
						if (!hasChunkNorth) {
							length -= offset;
						}
						float dx = length / (float) num;
						for (int j = 0; j <= num; j++) {
							GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
							GL11.glVertex3d(offset, -py, 16 - start - dx * j);
							GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.1F);
							GL11.glVertex3d(offset, 4 + 4 * Math.sin(12 * Math.PI + (4 * Math.PI / 64.0) * j - (Minecraft.getMinecraft().theWorld.getWorldTime() + partialTickTime) / 10.0), 16 - start - dx * j);
						}
						GL11.glEnd();
					}
					GL11.glPopMatrix();
				}
			}
			GL11.glPopMatrix();

			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDepthMask(true);
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

	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event) {
		if (event.entity instanceof EntityPlayer) {
			event.entity.registerExtendedProperties(Strings.extendedPropertiesKey, new ExtendedPropertyLand());
		}
	}

}
