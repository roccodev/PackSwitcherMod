package tk.roccodev.packswitch;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
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
