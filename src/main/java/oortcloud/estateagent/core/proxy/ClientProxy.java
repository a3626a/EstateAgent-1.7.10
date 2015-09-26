package oortcloud.estateagent.core.proxy;

import net.minecraft.client.Minecraft;
import oortcloud.estateagent.gui.GuiLandBook;

public class ClientProxy extends CommonProxy {
	@Override
	public void modInit() {
	}
	
	@Override
	public void openGUI(GuiLandBook guiLandBook) {
		Minecraft.getMinecraft().displayGuiScreen(guiLandBook);;
	}
}
