package oortcloud.estateagent.chunk;

import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.client.Minecraft;
import oortcloud.estateagent.EstateAgent;
import oortcloud.network.PacketGeneralServer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ChunkRenderingManager {
	private static ChunkRenderingManager instance;

	public CopyOnWriteArrayList<ChunkCoordIntPairWithDimension> chunks;

	protected ChunkRenderingManager() {
		init();
	}

	public static ChunkRenderingManager getInstance() {
		if (instance == null) {
			instance = new ChunkRenderingManager();
			return instance;
		}
		return instance;
	}

	public void init() {
		chunks = new CopyOnWriteArrayList<ChunkCoordIntPairWithDimension>();
		PacketGeneralServer msg = new PacketGeneralServer(3);
		msg.setString(Minecraft.getMinecraft().thePlayer.getCommandSenderName());
		EstateAgent.simpleChannel.sendToServer(msg);
	}

}
