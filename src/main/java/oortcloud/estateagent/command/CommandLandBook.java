package oortcloud.estateagent.command;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import oortcloud.estateagent.items.ModItems;

public class CommandLandBook implements ICommand {

	private List aliases;

	public CommandLandBook() {
		this.aliases = new ArrayList();
		this.aliases.add("landbook");
		this.aliases.add("lb");
	}

	@Override
	public String getCommandName() {
		return "landbook";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "landbook : will give you item Land Book";
	}

	@Override
	public void processCommand(ICommandSender player, String[] strArr) {
		MinecraftServer.getServer().getConfigurationManager().func_152612_a(player.getCommandSenderName()).inventory.addItemStackToInventory(new ItemStack(ModItems.landbook));
	}

	@Override
	public int compareTo(Object arg0) {
		return 0;
	}

	@Override
	public List getCommandAliases() {
		return aliases;
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
		return true;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender p_71516_1_,
			String[] p_71516_2_) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
		return false;
	}

}
