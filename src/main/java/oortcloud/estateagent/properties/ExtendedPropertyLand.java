package oortcloud.estateagent.properties;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class ExtendedPropertyLand implements IExtendedEntityProperties {

	protected static String key = "ExtendedPropertiesEstateAgent";

	private int forcableChunks;

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		NBTTagCompound tag = new NBTTagCompound();
		compound.setTag(key, tag);
		tag.setInteger("forcableChunks", forcableChunks);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		NBTTagCompound tag = (NBTTagCompound) compound.getTag(key);
		if (tag != null) {
			forcableChunks=tag.getInteger("forcableChunks");
		}
	}

	@Override
	public void init(Entity entity, World world) {
		// TODO Auto-generated method stub

	}
	
	public void setForcableChunks(int value) {
		forcableChunks=value;
	}
	
	public int getForcableChunks() {
		return forcableChunks;
	}

	public void incForcableChunks() {
		forcableChunks++;
	}
	
}
