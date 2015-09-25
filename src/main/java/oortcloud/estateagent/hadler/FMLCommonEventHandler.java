package oortcloud.estateagent.hadler;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import oortcloud.estateagent.chunk.ChunkCoordIntPairWithDimension;
import oortcloud.estateagent.chunk.ChunkManager;
import oortcloud.estateagent.items.ModItems;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class FMLCommonEventHandler {

	private int tick = 0;

	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event) {
		if (tick == 0) {
			for (ArrayList<ChunkCoordIntPairWithDimension> list : ChunkManager.list.values()) {
				for (ChunkCoordIntPairWithDimension i : list) {
					ChunkManager.forceChunk(i);
				}
			}
			tick = 1200;
		}
		if (tick > 0)
			tick--;
	}

	@SubscribeEvent
	public void EventPlayerJoint(PlayerLoggedInEvent event) {
		if (!event.player.worldObj.isRemote) {
			if (!ChunkManager.list.containsKey(event.player.getCommandSenderName())) {
				ChunkManager.list.put(event.player.getCommandSenderName(), new ArrayList<ChunkCoordIntPairWithDimension>());
			}
		}
	}

}
