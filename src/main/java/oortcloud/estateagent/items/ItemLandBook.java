package oortcloud.estateagent.items;

import java.util.ArrayList;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import oortcloud.estateagent.EstateAgent;
import oortcloud.estateagent.chunk.ChunkManager;
import oortcloud.estateagent.lib.References;
import oortcloud.estateagent.lib.Strings;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemLandBook extends Item {

	public ItemLandBook() {
		this.setCreativeTab(CreativeTabs.tabAllSearch);
		this.setUnlocalizedName(References.RESOURCESPREFIX
				+ Strings.itemLandBookName);
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

		player.openGui(EstateAgent.instance, Strings.GuiLandBookID, world, 0, 0, 0);
		
		return super.onItemRightClick(stack, world, player);
	}
	
}
