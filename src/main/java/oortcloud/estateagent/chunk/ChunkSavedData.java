package oortcloud.estateagent.chunk;

import java.util.ArrayList;

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
		for (String i : tag.getString("players").split(" ")) {
			int[] arr = tag.getIntArray(i);
			if (arr.length != 1) {
				for (int j = 0 ; j < arr.length; j+=3) {
					ChunkCoordIntPairWithDimension chunk = new ChunkCoordIntPairWithDimension(arr[j], arr[j+1], arr[j+2]);
					if (!ChunkManager.list.containsKey(i)) {
						ChunkManager.list.put(i, new ArrayList<ChunkCoordIntPairWithDimension>());
					}
					ChunkManager.list.get(i).add(chunk);
					ChunkManager.forceChunk(chunk);
				}
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		String data = "";
		for (String i : ChunkManager.list.keySet()) {
			data+=i+" ";
			tag.setIntArray(i, ChunkManager.toIntArray(i));
		}
		tag.setString("players", data);
	}

}
