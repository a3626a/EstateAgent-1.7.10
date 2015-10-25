package oortcloud.estateagent.chunk;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import oortcloud.estateagent.EstateAgent;
import oortcloud.network.PacketGeneralClient;

import com.google.common.collect.ImmutableList;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ChunkManager {

	private static ChunkManager instance;

	private HashMap<String, ArrayList<ChunkCoordIntPairWithDimension>> chunksPerPlayers;
	private HashMap<Integer, Ticket> tickets;

	protected ChunkManager() {
		chunksPerPlayers = new HashMap<String, ArrayList<ChunkCoordIntPairWithDimension>>();
		tickets = new HashMap<Integer, ForgeChunkManager.Ticket>();
	}

	public static ChunkManager getInstance() {
		if (instance == null) {
			instance = new ChunkManager();
			return instance;
		}
		return instance;
	}

	public void init() {
		chunksPerPlayers = new HashMap<String, ArrayList<ChunkCoordIntPairWithDimension>>();
		tickets = new HashMap<Integer, ForgeChunkManager.Ticket>();
	}

	public void clearLoadedChunksForPlayer(String player) {
		getLoadedChunksByPlayer(player).clear();
	}

	public Set<String> getLoadedPlayers() {
		return chunksPerPlayers.keySet();
	}

	public Collection<ArrayList<ChunkCoordIntPairWithDimension>> getLoadedChunks() {
		return chunksPerPlayers.values();
	}

	private ArrayList<ChunkCoordIntPairWithDimension> getLoadedChunksByPlayer(String player) {
		ArrayList<ChunkCoordIntPairWithDimension> ret = chunksPerPlayers.get(player);
		if (ret == null) {
			ret = new ArrayList<ChunkCoordIntPairWithDimension>();
			chunksPerPlayers.put(player, ret);
			return ret;
		}
		return ret;
	}

	public boolean addLoadedChunkForPlayer(String player, ChunkCoordIntPairWithDimension chunk, boolean markDirty) {
		ArrayList<ChunkCoordIntPairWithDimension> chunks = getLoadedChunksByPlayer(player);
		if (chunks.contains(chunk))
			return false;
		chunks.add(chunk);
		forceChunk(chunk);

		if (markDirty)
			EstateAgent.chunkSaved.markDirty();

		return true;
	}

	public boolean addLoadedChunkForPlayerSynced(String player, ChunkCoordIntPairWithDimension chunk) {
		boolean flag = addLoadedChunkForPlayer(player, chunk, true);
		EntityPlayerMP reciever = MinecraftServer.getServer().getConfigurationManager().func_152612_a(player);
		if (reciever != null) {
			PacketGeneralClient msg = new PacketGeneralClient(1);
			msg.setInt(chunk.dim);
			msg.setInt(chunk.chunkXPos);
			msg.setInt(chunk.chunkZPos);
			EstateAgent.simpleChannel.sendTo(msg, reciever);
		}
		return flag;
	}

	public boolean removeLoadedChunkForPlayer(String player, ChunkCoordIntPairWithDimension chunk, boolean markDirty) {
		ArrayList<ChunkCoordIntPairWithDimension> chunks = getLoadedChunksByPlayer(player);
		if (!chunks.contains(chunk))
			return false;
		chunks.remove(chunk);
		unforceChunk(chunk);
		if (markDirty)
			EstateAgent.chunkSaved.markDirty();
		return true;
	}

	public boolean removeLoadedChunkForPlayerSynced(String player, ChunkCoordIntPairWithDimension chunk) {
		boolean flag = removeLoadedChunkForPlayer(player, chunk, true);
		EntityPlayerMP reciever = MinecraftServer.getServer().getConfigurationManager().func_152612_a(player);
		if (reciever != null) {
			PacketGeneralClient msg = new PacketGeneralClient(2);
			msg.setInt(chunk.dim);
			msg.setInt(chunk.chunkXPos);
			msg.setInt(chunk.chunkZPos);
			EstateAgent.simpleChannel.sendTo(msg, reciever);
		}
		return flag;
	}

	public boolean contains(String player, ChunkCoordIntPairWithDimension chunk) {
		return getLoadedChunksByPlayer(player).contains(chunk);
	}

	public int sizeOfLoadedChunks(String player) {
		return getLoadedChunksByPlayer(player).size();
	}

	public void requestTicket() {
		EstateAgent.logger.info("Request new tickets for every world");
		for (WorldServer i : MinecraftServer.getServer().worldServers) {
			EstateAgent.logger.info("Request a new ticket for the world " + i.provider.dimensionId);
			Ticket tic = ForgeChunkManager.requestTicket(EstateAgent.instance, i, Type.NORMAL);
			tickets.put(i.provider.dimensionId, tic);
		}
	}

	public int[] toIntArray(String player) {
		ArrayList<ChunkCoordIntPairWithDimension> list = getLoadedChunksByPlayer(player);
		if (!list.isEmpty()) {
			int[] coord = new int[list.size() * 3];

			int next = 0;
			for (ChunkCoordIntPairWithDimension i : list) {
				coord[next++] = i.dim;
				coord[next++] = i.chunkXPos;
				coord[next++] = i.chunkZPos;
			}
			return coord;

		} else {
			return new int[] { 0 };
		}
	}

	private void forceChunk(ChunkCoordIntPairWithDimension i) {
		ForgeChunkManager.forceChunk(tickets.get(i.dim), i);
	}

	public void forceEveryChunks() {
		for (ArrayList<ChunkCoordIntPairWithDimension> list : ChunkManager.getInstance().getLoadedChunks()) {
			for (ChunkCoordIntPairWithDimension i : list) {
				ChunkManager.getInstance().forceChunk(i);
			}
		}
	}

	private void unforceChunk(ChunkCoordIntPairWithDimension i) {
		ForgeChunkManager.unforceChunk(tickets.get(i.dim), i);
	}
}
