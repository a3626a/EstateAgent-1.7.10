package oortcloud.estateagent.chunk;

import java.util.List;

import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import oortcloud.estateagent.EstateAgent;
import oortcloud.estateagent.lib.References;

public class ChunkLoadingCallback implements LoadingCallback {

	@Override
	public void ticketsLoaded(List<Ticket> tickets, World world) {
		
		Ticket ticket = null;

		for (Ticket t : tickets) {
			if (t.equals(References.MODID)) {
				ticket = t;
			}
		}
		
		if (ticket != null) {
			ChunkManager.tickets.put(world.provider.dimensionId, ticket);
		} else {
				ChunkManager.tickets.put(world.provider.dimensionId,
						ForgeChunkManager.requestTicket(EstateAgent.instance,
								world, Type.NORMAL));
		}
		
	}

}
