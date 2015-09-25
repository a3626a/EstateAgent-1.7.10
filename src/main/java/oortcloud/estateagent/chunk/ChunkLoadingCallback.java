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
		/*
		EstateAgent.logger.info("Loading pre-existing chunk loading tickets:");
		
		Ticket ticket = null;
		for (Ticket t : tickets) {
			if (t.getModId().equals(References.MODID)) {
				EstateAgent.logger.info("Register a ticket for the world " + world.provider.dimensionId);
				ChunkManager.tickets.put(world.provider.dimensionId, ticket);
				return;
			}
		}
		*/
	}

}
