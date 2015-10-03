package oortcloud.estateagent.properties;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import oortcloud.estateagent.handler.FMLCommonEventHandler;
import oortcloud.estateagent.util.PlayerUtil;
import net.minecraft.command.ICommand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class ExtendedPropertyLand implements IExtendedEntityProperties {

	protected static String key = "ExtendedPropertiesEstateAgent";

	private int forcableChunks;
	EntityPlayer player;

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
			forcableChunks = tag.getInteger("forcableChunks");
		}
	}

	@Override
	public void init(Entity entity, World world) {
		player = (EntityPlayer) entity;
	}

	public void setForcableChunks(int value) {
		forcableChunks = value;
	}

	public int getForcableChunks() {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
			if (PlayerUtil.getInstance().hasPlayerPermission(player, 4)) {
				return Integer.MAX_VALUE;
			}
		return forcableChunks;
	}

	public void incForcableChunks() {
		forcableChunks++;
	}

}
