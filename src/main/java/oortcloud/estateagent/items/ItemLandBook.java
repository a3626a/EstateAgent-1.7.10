package oortcloud.estateagent.items;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import oortcloud.estateagent.EstateAgent;
import oortcloud.estateagent.chunk.ChunkCoordIntPairWithDimension;
import oortcloud.estateagent.chunk.ChunkManager;
import oortcloud.estateagent.gui.GuiLandBook;
import oortcloud.estateagent.lib.References;
import oortcloud.estateagent.lib.Strings;
import oortcloud.estateagent.properties.ExtendedPropertyLand;
import oortcloud.network.PacketGeneralClient;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemLandBook extends Item {

	public ItemLandBook() {
		this.setCreativeTab(CreativeTabs.tabAllSearch);
		this.setUnlocalizedName(References.RESOURCESPREFIX + Strings.itemLandBookName);
		this.setCreativeTab(EstateAgent.tab);
		ModItems.register(this);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister iconRegister) {
		this.itemIcon = iconRegister.registerIcon(ModItems.getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (world.isRemote) {
			EstateAgent.proxy.openGUI(new GuiLandBook());
		} else {
			/*
			 * String name = player.getCommandSenderName(); PacketGeneralClient
			 * msg = new PacketGeneralClient(0);
			 * msg.setIntArray(ChunkManager.getInstance().toIntArray(name));
			 * msg.setString(name); EstateAgent.simpleChannel.sendTo(msg,
			 * MinecraftServer
			 * .getServer().getConfigurationManager().func_152612_a(name));
			 */
			ExtendedPropertyLand property = (ExtendedPropertyLand) player.getExtendedProperties(Strings.extendedPropertiesKey);
			if (property != null) {
				String name = player.getCommandSenderName();
				PacketGeneralClient msg = new PacketGeneralClient(3);
				msg.setInt(property.getForcableChunks());
				EstateAgent.simpleChannel.sendTo(msg, MinecraftServer.getServer().getConfigurationManager().func_152612_a(name));
			}
		}
		return super.onItemRightClick(stack, world, player);
	}

	@Override
	public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
		EntityPlayer player = (EntityPlayer) entityLiving;
		String name = player.getCommandSenderName();
		World world = player.worldObj;

		if (!world.isRemote) {
			ChunkCoordIntPairWithDimension chunk = new ChunkCoordIntPairWithDimension(world.provider.dimensionId, player.chunkCoordX, player.chunkCoordZ);

			ExtendedPropertyLand property = (ExtendedPropertyLand) player.getExtendedProperties(Strings.extendedPropertiesKey);

			if (property == null)
				return false;

			if (!ChunkManager.getInstance().contains(name, chunk)) {
				if (property.getForcableChunks() > ChunkManager.getInstance().sideOfLoadedChunks(name)) {
					ChunkManager.getInstance().addLoadedChunkForPlayerSynced(name, chunk);
				}
			} else {
				ChunkManager.getInstance().removeLoadedChunkForPlayerSynced(name, chunk);
			}
		}
		return true;
	}

}
