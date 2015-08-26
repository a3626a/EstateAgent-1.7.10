package oortcloud.estateagent.chunk;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

public class ChunkManager {
	public static HashMap<String, ArrayList<ChunkCoordIntPairWithDimension>> list;
	public static HashMap<Integer, Ticket> tickets;

	public static void init() {
		list = new HashMap<String, ArrayList<ChunkCoordIntPairWithDimension>>();
		tickets = new HashMap<Integer, ForgeChunkManager.Ticket>();
	}

	public static int[] toIntArray(String player) {

		ArrayList<ChunkCoordIntPairWithDimension> list = ChunkManager.list
				.get(player);
		if (list == null) {
			ChunkManager.list.put(player,
					new ArrayList<ChunkCoordIntPairWithDimension>());
			return new int[] { 0 };
		} else {
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
	}

	public static void forceChunk(ChunkCoordIntPairWithDimension i) {
		ForgeChunkManager.forceChunk(ChunkManager.tickets.get(i.dim), i);
	}
}
