package oortcloud.network;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.world.ChunkCoordIntPair;
import oortcloud.estateagent.chunk.ChunkCoordIntPairWithDimension;
import oortcloud.estateagent.chunk.ChunkManager;
import oortcloud.estateagent.gui.GuiLandBook;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class HandlerGeneralClient implements IMessageHandler<PacketGeneralClient, PacketGeneralServer> {

	@Override
	public PacketGeneralServer onMessage(PacketGeneralClient message, MessageContext ctx) {

		switch (message.index) {
		case 0:
			int[] coord = message.getIntArray();
			String name = Minecraft.getMinecraft().thePlayer.getCommandSenderName();
			ArrayList<ChunkCoordIntPairWithDimension> list = ChunkManager.list.get(name);
			if (list == null) {
				list = new ArrayList<ChunkCoordIntPairWithDimension>();
				ChunkManager.list.put(name, list);
			}

			list.clear();

			if (coord.length == 1) {
				break;
			}
			for (int i = 0; i < coord.length; i += 3) {
				list.add(new ChunkCoordIntPairWithDimension(coord[i], coord[i + 1], coord[i + 2]));
			}

			break;
		}
		return null;
	}
}
