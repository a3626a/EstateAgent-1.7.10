package oortcloud.network;

import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.ForgeChunkManager;
import oortcloud.estateagent.EstateAgent;
import oortcloud.estateagent.chunk.ChunkCoordIntPairWithDimension;
import oortcloud.estateagent.chunk.ChunkManager;
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
			String player2 = message.getString();
			ChunkManager.getInstance().removeLoadedChunkForPlayerSynced(player2, new ChunkCoordIntPairWithDimension(message.getInt(), message.getInt(), message.getInt()));
		}
		return null;

	}
}
