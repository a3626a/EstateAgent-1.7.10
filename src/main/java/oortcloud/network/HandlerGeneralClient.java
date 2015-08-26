package oortcloud.network;

import java.util.ArrayList;

import net.minecraft.world.ChunkCoordIntPair;
import oortcloud.estateagent.chunk.ChunkCoordIntPairWithDimension;
import oortcloud.estateagent.gui.GUILandBook;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class HandlerGeneralClient implements
		IMessageHandler<PacketGeneralClient, PacketGeneralServer> {

	@Override
	public PacketGeneralServer onMessage(PacketGeneralClient message,
			MessageContext ctx) {
		
		switch (message.index) {
		case 0:
			int[] coord = message.getIntArray();
			if (coord.length == 1) {
				GUILandBook.chunks = new ArrayList<ChunkCoordIntPairWithDimension>();
			} else {
				ArrayList<ChunkCoordIntPairWithDimension> list = new ArrayList<ChunkCoordIntPairWithDimension>();
				for (int i = 0 ; i < coord.length ; i+=3) {
					list.add(new ChunkCoordIntPairWithDimension(coord[i], coord[i+1], coord[i+2]));
				}
				GUILandBook.chunks = list;
			}
			break;
		}
		return null;
		
		
	}
}
