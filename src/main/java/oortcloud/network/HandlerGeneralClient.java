package oortcloud.network;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.world.ChunkCoordIntPair;
import oortcloud.estateagent.chunk.ChunkCoordIntPairWithDimension;
import oortcloud.estateagent.chunk.ChunkManager;
import oortcloud.estateagent.gui.GuiLandBook;
import oortcloud.estateagent.lib.Strings;
import oortcloud.estateagent.properties.ExtendedPropertyLand;
import scala.collection.parallel.ParIterableLike.Min;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class HandlerGeneralClient implements IMessageHandler<PacketGeneralClient, PacketGeneralServer> {

	@Override
	public PacketGeneralServer onMessage(PacketGeneralClient message, MessageContext ctx) {

		switch (message.index) {
		case 0:
			//Set loaded chunks list to the player
			int[] coord = message.getIntArray();
			String name = Minecraft.getMinecraft().thePlayer.getCommandSenderName();
			ChunkManager.getInstance().clearLoadedChunksForPlayer(name);

			if (coord.length == 1) {
				break;
			}
			for (int i = 0; i < coord.length; i += 3) {
				ChunkManager.getInstance().addLoadedChunkForPlayer(name, new ChunkCoordIntPairWithDimension(coord[i], coord[i + 1], coord[i + 2]), false);
			}

			break;
		case 1:
			//Add a chunk to loaded chunks list for the player
			//dim, chunkX, chunkZ
			ChunkManager.getInstance().addLoadedChunkForPlayer(Minecraft.getMinecraft().thePlayer.getCommandSenderName(), new ChunkCoordIntPairWithDimension(message.getInt(), message.getInt(), message.getInt()), false);
			break;
		case 2:
			//Remove a chunk from loaded chunks list for the player
			//dim, chunkX, chunkZ
			ChunkManager.getInstance().removeLoadedChunkForPlayer(Minecraft.getMinecraft().thePlayer.getCommandSenderName(), new ChunkCoordIntPairWithDimension(message.getInt(), message.getInt(), message.getInt()), false);
			break;
		case 3:
			//Synch Forcable Chunks Value
			ExtendedPropertyLand property = (ExtendedPropertyLand) Minecraft.getMinecraft().thePlayer.getExtendedProperties(Strings.extendedPropertiesKey);
			if (property != null) 
				property.setForcableChunks(message.getInt());
		}
		return null;
	}
}
