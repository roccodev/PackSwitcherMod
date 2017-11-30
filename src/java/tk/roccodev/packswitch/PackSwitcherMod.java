package tk.roccodev.packswitch;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import tk.roccodev.packswitch.command.PackSwitchCommand;

@Mod(modid="packswitcher", name="Pack Switcher Mod", version="1.0")
public class PackSwitcherMod {

	public static KeyBinding openPacksKey;
	public static boolean rpoSupport; // Resource Pack Organizer
	
	
	
	@EventHandler
	public void onFMLInit(FMLInitializationEvent evt) {
		FMLCommonHandler.instance().bus().register(this);
		MinecraftForge.EVENT_BUS.register(this);
		
		openPacksKey = new KeyBinding("Open Pack Selector", Keyboard.KEY_U, "categories.general");
		ClientRegistry.registerKeyBinding(openPacksKey);
		ClientCommandHandler.instance.registerCommand(new PackSwitchCommand());
		
		try {
			 Class.forName("chylex.respack.ResourcePackOrganizer");
			 rpoSupport = true;
			} catch( ClassNotFoundException e ) {
			 rpoSupport = false;
			}
	}
	
	 @SubscribeEvent
	   public void onKey(KeyInputEvent evt) {
		 if(!Minecraft.getMinecraft().inGameHasFocus) return;
		 if(openPacksKey.isPressed()) {
			 Minecraft.getMinecraft().displayGuiScreen(new GuiScreenResourcePacks(Minecraft.getMinecraft().currentScreen));
		 }
	 }
	
}
