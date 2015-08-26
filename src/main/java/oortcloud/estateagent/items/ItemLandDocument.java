package oortcloud.estateagent.items;

import java.util.ArrayList;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import oortcloud.estateagent.EstateAgent;
import oortcloud.estateagent.chunk.ChunkCoordIntPairWithDimension;
import oortcloud.estateagent.chunk.ChunkManager;
import oortcloud.estateagent.lib.References;
import oortcloud.estateagent.lib.Strings;
import oortcloud.network.PacketGeneralClient;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemLandDocument extends Item {

	public ItemLandDocument() {
		this.setCreativeTab(CreativeTabs.tabAllSearch);
		this.setUnlocalizedName(References.RESOURCESPREFIX
				+ Strings.itemLandDocumentName);
		ModItems.register(this);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister iconRegister) {
		this.itemIcon = iconRegister.registerIcon(ModItems
				.getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world,
			EntityPlayer player) {

		if (!world.isRemote) {
			ArrayList chunks = ChunkManager.list.get(player
					.getCommandSenderName());
			ChunkCoordIntPairWithDimension chunk = new ChunkCoordIntPairWithDimension(world.provider.dimensionId, player.chunkCoordX,
					player.chunkCoordZ);
			
			if (!chunks.contains(chunk)) {
				chunks.add(chunk);
				ChunkManager.forceChunk(chunk);
				
				String name = player.getCommandSenderName();
				PacketGeneralClient msg = new PacketGeneralClient(0);
				msg.setIntArray(ChunkManager.toIntArray(name));
				EstateAgent.simpleChannel.sendTo(msg,
						MinecraftServer.getServer().getConfigurationManager()
								.func_152612_a(name));
				
				stack.stackSize--;
				if (stack.stackSize==0) player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
			}
		}
		return super.onItemRightClick(stack, world, player);
	}
}
