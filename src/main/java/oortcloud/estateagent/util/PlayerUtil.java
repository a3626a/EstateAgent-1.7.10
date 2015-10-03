package oortcloud.estateagent.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.server.management.UserListOpsEntry;

public class PlayerUtil {

	private static PlayerUtil instance;

	private MinecraftServer server;
	private ServerConfigurationManager configManager;
	
	private PlayerUtil() {
		server = MinecraftServer.getServer();
		configManager = server.getConfigurationManager();
	}
	
	public static PlayerUtil getInstance() {
		if (instance == null) {
			instance = new PlayerUtil();
			return instance;
		}
		return instance;
	}

	public boolean hasPlayerPermission(EntityPlayer player, int permission) {
		if (configManager.func_152596_g(player.getGameProfile())) {
			UserListOpsEntry userlistopsentry = (UserListOpsEntry) configManager.func_152603_m().func_152683_b(player.getGameProfile());
			return userlistopsentry != null ? userlistopsentry.func_152644_a() >= permission : server.getOpPermissionLevel() >= permission;
		} else {
			return false;
		}
	}

	public EntityPlayerMP getEntityPlayerMPFromPlayerName(String player) {
		return configManager.func_152612_a(player);
	}

	public EntityPlayerMP getEntityPlayerMPFromEntityPlayer(EntityPlayer player) {
		return getEntityPlayerMPFromPlayerName(player.getCommandSenderName());
	}
}
