package oortcloud.estateagent.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import oortcloud.estateagent.lib.Strings;
import cpw.mods.fml.common.network.IGuiHandler;

public class GUIHandler implements IGuiHandler  {

public GUIHandler() {}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		if(ID == Strings.GuiLandBookID) {
			return null;
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		if(ID == Strings.GuiLandBookID) {
			return new GUILandBook(player.getCommandSenderName());
		}
		return null;
	}
	
}
