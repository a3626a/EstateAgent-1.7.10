package oortcloud.network;

import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import oortcloud.estateagent.chunk.ChunkCoordIntPairWithDimension;
import oortcloud.estateagent.chunk.ChunkManager;
import oortcloud.estateagent.items.ItemLandBook;
import oortcloud.estateagent.items.ModItems;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class HandlerGeneralServer implements IMessageHandler<PacketGeneralServer, PacketGeneralClient> {

	@Override
	public PacketGeneralClient onMessage(PacketGeneralServer message, MessageContext ctx) {

		switch (message.index) {
		case 1:
			// Remove a chunk
			// player dim x z
			String player1 = message.getString();
			ChunkManager.getInstance().removeLoadedChunkForPlayerSynced(player1, new ChunkCoordIntPairWithDimension(message.getInt(), message.getInt(), message.getInt()));
			break;
		case 2:
			// Change radius
			String player2 = message.getString();
			int radius = message.getInt();
			ItemStack stack = MinecraftServer.getServer().getConfigurationManager().func_152612_a(player2).getHeldItem();
	    	if (stack.getItem() == ModItems.landbook) {
	    		ItemLandBook.setRadius(stack, radius);
	    	}
			break;
		case 3:
			// Synchronize for initial log-in
			String player3 = message.getString();
			PacketGeneralClient msg3 = new PacketGeneralClient(0);
			msg3.setIntArray(ChunkManager.getInstance().toIntArray(player3));
			return msg3;
		}
		return null;

	}
}
