package oortcloud.estateagent.handler;

import oortcloud.estateagent.chunk.ChunkManager;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FMLCommonEventHandler {

	private int tick = 0;

	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event) {
		if (tick == 0) {
			ChunkManager.getInstance().forceEveryChunks();
			tick = 1200;
		}
		if (tick > 0)
			tick--;
	}

}
