package oortcloud.estateagent.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import oortcloud.estateagent.EstateAgent;
import oortcloud.estateagent.chunk.ChunkCoordIntPairWithDimension;
import oortcloud.estateagent.chunk.ChunkManager;
import oortcloud.estateagent.gui.GuiLandBook;
import oortcloud.estateagent.lib.References;
import oortcloud.estateagent.lib.Strings;
import oortcloud.estateagent.properties.ExtendedPropertyLand;
import oortcloud.network.PacketGeneralClient;
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
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
		super.addInformation(stack, player, list, flag);
		list.add("Radius: " + getRadius(stack));
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (world.isRemote) {
			EstateAgent.proxy.openGUI(new GuiLandBook(getRadius(stack)));
		} else {
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
			ExtendedPropertyLand property = (ExtendedPropertyLand) player.getExtendedProperties(Strings.extendedPropertiesKey);
			if (property == null)
				return false;

			int radius = getRadius(stack);

			if (property.getForcableChunks() == Integer.MAX_VALUE) {
				boolean allLoaded = true;
				for (int X = player.chunkCoordX - radius; X <= player.chunkCoordX + radius; X++) {
					for (int Z = player.chunkCoordZ - radius; Z <= player.chunkCoordZ + radius; Z++) {
						ChunkCoordIntPairWithDimension chunk = new ChunkCoordIntPairWithDimension(world.provider.dimensionId, X, Z);
						if (!ChunkManager.getInstance().contains(name, chunk))
							allLoaded = false;
					}
				}

				if (allLoaded) {
					for (int X = player.chunkCoordX - radius; X <= player.chunkCoordX + radius; X++) {
						for (int Z = player.chunkCoordZ - radius; Z <= player.chunkCoordZ + radius; Z++) {
							ChunkCoordIntPairWithDimension chunk = new ChunkCoordIntPairWithDimension(world.provider.dimensionId, X, Z);
							ChunkManager.getInstance().removeLoadedChunkForPlayerSynced(name, chunk);
						}
					}
				} else {
					for (int X = player.chunkCoordX - radius; X <= player.chunkCoordX + radius; X++) {
						for (int Z = player.chunkCoordZ - radius; Z <= player.chunkCoordZ + radius; Z++) {
							ChunkCoordIntPairWithDimension chunk = new ChunkCoordIntPairWithDimension(world.provider.dimensionId, X, Z);
							if (!ChunkManager.getInstance().contains(name, chunk))
								ChunkManager.getInstance().addLoadedChunkForPlayerSynced(name, chunk);
						}
					}
				}
			} else {
				boolean allUnloaded = true;
				for (int X = player.chunkCoordX - radius; X <= player.chunkCoordX + radius; X++) {
					for (int Z = player.chunkCoordZ - radius; Z <= player.chunkCoordZ + radius; Z++) {
						ChunkCoordIntPairWithDimension chunk = new ChunkCoordIntPairWithDimension(world.provider.dimensionId, X, Z);
						if (ChunkManager.getInstance().contains(name, chunk))
							allUnloaded = false;
					}
				}
				
				if (allUnloaded && property.getForcableChunks()-ChunkManager.getInstance().sizeOfLoadedChunks(name)>=(2*radius+1)*(2*radius+1)) {
					for (int X = player.chunkCoordX - radius; X <= player.chunkCoordX + radius; X++) {
						for (int Z = player.chunkCoordZ - radius; Z <= player.chunkCoordZ + radius; Z++) {
							ChunkCoordIntPairWithDimension chunk = new ChunkCoordIntPairWithDimension(world.provider.dimensionId, X, Z);
							ChunkManager.getInstance().addLoadedChunkForPlayerSynced(name, chunk);
						}
					}
				} else {
					for (int X = player.chunkCoordX - radius; X <= player.chunkCoordX + radius; X++) {
						for (int Z = player.chunkCoordZ - radius; Z <= player.chunkCoordZ + radius; Z++) {
							ChunkCoordIntPairWithDimension chunk = new ChunkCoordIntPairWithDimension(world.provider.dimensionId, X, Z);
							if (ChunkManager.getInstance().contains(name, chunk))
								ChunkManager.getInstance().removeLoadedChunkForPlayerSynced(name, chunk);
						}
					}
				}
			}
		}
		return true;
	}

	public static int getRadius(ItemStack stack) {
		if (stack.stackTagCompound == null) {
			stack.setTagCompound(new NBTTagCompound());
		}
		if (!stack.stackTagCompound.hasKey("radius")) {
			stack.stackTagCompound.setInteger("radius", 0);
		}
		return stack.stackTagCompound.getInteger("radius");
	}

	public static void setRadius(ItemStack stack, int num) {
		if (stack.stackTagCompound == null) {
			stack.setTagCompound(new NBTTagCompound());
		}
		stack.stackTagCompound.setInteger("radius", num);
	}

}
