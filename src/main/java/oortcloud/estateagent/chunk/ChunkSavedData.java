package oortcloud.estateagent.chunk;

import java.util.ArrayList;

import oortcloud.estateagent.EstateAgent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;

public class ChunkSavedData extends WorldSavedData {

	public final static String key = "oortcloud.estateagent.chunksaveddata";
	
	public ChunkSavedData(String key) {
		super(key);
		this.markDirty();
	}
	
	public ChunkSavedData() {
		super(key);
		this.markDirty();
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		EstateAgent.logger.info("Loading forced chunks");
		for (String i : tag.getString("players").split(" ")) {
			int[] arr = tag.getIntArray(i);
			if (arr.length != 1) {
				for (int j = 0 ; j < arr.length; j+=3) {
					EstateAgent.logger.info("Loading forced chunks: ( " + arr[j] + ", " + arr[j+1] + ", " + arr[j+2] + " )");
					ChunkCoordIntPairWithDimension chunk = new ChunkCoordIntPairWithDimension(arr[j], arr[j+1], arr[j+2]);
					ChunkManager.getInstance().addLoadedChunkForPlayer(i, chunk, false);
				}
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		EstateAgent.logger.info("Saving forced chunks");
		String data = "";
		for (String i : ChunkManager.getInstance().getLoadedPlayers()) {
			data+=i+" ";
			tag.setIntArray(i, ChunkManager.getInstance().toIntArray(i));
		}
		tag.setString("players", data);
	}

}
