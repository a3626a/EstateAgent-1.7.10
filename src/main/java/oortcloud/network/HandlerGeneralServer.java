package oortcloud.network;

import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.ForgeChunkManager;
import oortcloud.estateagent.EstateAgent;
import oortcloud.estateagent.chunk.ChunkManager;
import oortcloud.estateagent.items.ModItems;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class HandlerGeneralServer implements IMessageHandler<PacketGeneralServer, PacketGeneralClient> {

	@Override
	public PacketGeneralClient onMessage(PacketGeneralServer message, MessageContext ctx) {

		switch (message.index) {
		case 0:
			String player1 = message.getString();
			PacketGeneralClient msg1 = new PacketGeneralClient(0);
			msg1.setIntArray(ChunkManager.toIntArray(player1));
			EstateAgent.simpleChannel.sendTo(msg1, MinecraftServer.getServer().getConfigurationManager().func_152612_a(player1));
			break;
		case 1:
			String player2 = message.getString();
			int index = message.getInt();
			ForgeChunkManager.unforceChunk(ChunkManager.tickets.get(ChunkManager.list.get(player2).get(index).dim), ChunkManager.list.get(player2).get(index));
			ChunkManager.list.get(player2).remove(index);
			EstateAgent.chunkSaved.markDirty();

			if (!MinecraftServer.getServer().getConfigurationManager().func_152612_a(player2).inventory.addItemStackToInventory(new ItemStack(ModItems.landdocument))) {
				MinecraftServer.getServer().getConfigurationManager().func_152612_a(player2).dropItem(ModItems.landdocument, 1);
			}

			PacketGeneralClient msg2 = new PacketGeneralClient(0);
			msg2.setIntArray(ChunkManager.toIntArray(player2));
			return msg2;
		}
		return null;

	}
}
