package oortcloud.estateagent.items;

import java.util.ArrayList;
import java.util.List;

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
import oortcloud.estateagent.properties.ExtendedPropertyLand;
import oortcloud.network.PacketGeneralClient;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemLandDocument extends Item {

	public ItemLandDocument() {
		this.setMaxStackSize(1);
		this.setCreativeTab(CreativeTabs.tabAllSearch);
		this.setUnlocalizedName(References.RESOURCESPREFIX + Strings.itemLandDocumentName);
		this.setCreativeTab(EstateAgent.tab);
		ModItems.register(this);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
		super.addInformation(stack, player, list, flag);
		list.add("Required Level: " + stack.getItemDamage());
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
		subItems.add(new ItemStack(this,1,0));
		subItems.add(new ItemStack(this,1,1));
		subItems.add(new ItemStack(this,1,2));
		subItems.add(new ItemStack(this,1,3));
		subItems.add(new ItemStack(this,1,4));
		subItems.add(new ItemStack(this,1,5));
		subItems.add(new ItemStack(this,1,6));
		subItems.add(new ItemStack(this,1,7));
		subItems.add(new ItemStack(this,1,8));
		subItems.add(new ItemStack(this,1,9));
		subItems.add(new ItemStack(this,1,10));
		subItems.add(new ItemStack(this,1,11));
		subItems.add(new ItemStack(this,1,12));
		subItems.add(new ItemStack(this,1,13));
		subItems.add(new ItemStack(this,1,14));
		subItems.add(new ItemStack(this,1,15));
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister iconRegister) {
		this.itemIcon = iconRegister.registerIcon(ModItems.getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		ExtendedPropertyLand property = (ExtendedPropertyLand) player.getExtendedProperties(Strings.extendedPropertiesKey);
		if (property == null)
			return stack;
		
		if (property.getForcableChunks() == stack.getItemDamage()) {
			property.incForcableChunks();
			stack.stackSize--;
			return stack;
		} else {
			return stack;
		}
	}
}
