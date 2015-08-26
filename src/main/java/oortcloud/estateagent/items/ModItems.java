package oortcloud.estateagent.items;

import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModItems {

	public static Item landdocument;
	public static Item landbook;
	
	public static void init() {
		landdocument = new ItemLandDocument();
		landbook = new ItemLandBook();
	}
	
	public static String getUnwrappedUnlocalizedName(String unlocalizedName)
	{
		return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
	}
	
	public static String getName(String unlocalizedName)
	{
		return unlocalizedName.substring(unlocalizedName.indexOf(":") + 1);
	}
	
	public static void register(Item item) {
		GameRegistry.registerItem(item, getName(item.getUnlocalizedName()));
	}
	
}
