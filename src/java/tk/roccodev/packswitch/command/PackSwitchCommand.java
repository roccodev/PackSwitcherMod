package tk.roccodev.packswitch.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import chylex.respack.ResourcePackOrganizer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.ResourcePackRepository.Entry;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import tk.roccodev.packswitch.PackSwitcherMod;

public class PackSwitchCommand extends CommandBase {

	@Override
	public String getCommandName() {
		// TODO Auto-generated method stub
		return "packswitch";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "/packswitch [filter] [add/replace]";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		final Minecraft mc = Minecraft.getMinecraft();
		if (args.length != 0) {

			mc.getResourcePackRepository().updateRepositoryEntriesAll();
			String mode = args[0];
			int modeNum = isInteger(mode);

			List<String> argsL = new ArrayList<String>(Arrays.asList(args));
			argsL.remove(0);
			StringBuilder filter = new StringBuilder();
			for (String s : argsL) {
				filter.append(s).append(" ");
			}

			List<Entry> packs = mc.getResourcePackRepository().getRepositoryEntriesAll();

			Optional<Entry> pO = packs.stream().filter(pack -> EnumChatFormatting.getTextWithoutFormattingCodes(pack.getResourcePackName().toUpperCase()).contains(filter.toString().trim().toUpperCase())).findFirst();
			
			Entry pack = pO.get();
			if(pack != null) {
				List<Entry> enabled = new ArrayList<Entry>();
				enabled.addAll(mc.getResourcePackRepository().getRepositoryEntries());
				if (enabled.contains(pack) && !mode.toUpperCase().equals("REPLACE")
						&& !mode.toUpperCase().equals("REMOVE") && modeNum == -1) {
					mc.thePlayer.addChatComponentMessage(
							new ChatComponentText("§aPack already loaded. Nothing to do."));
					return;
				} else if (!enabled.contains(pack) && mode.toUpperCase().equals("REMOVE")) {
					mc.thePlayer
							.addChatComponentMessage(new ChatComponentText("§aPack not loaded. Nothing to do."));
					return;
				}
				if (mode.toUpperCase().equals("REPLACE"))
					enabled.clear();
				if (mode.toUpperCase().equals("REMOVE"))
					enabled.remove(pack);
				else {
					if (modeNum != -1) {
						try {
							int lastIndex = -1;
							Entry lastPack = null;
							if (enabled.contains(pack)) {
								lastIndex = enabled.indexOf(pack);
								if (enabled.size() - 1 >= modeNum)
									lastPack = enabled.get(modeNum);
							}

							enabled.set(modeNum, pack);
							if (lastIndex != -1 && lastPack != null) {
								enabled.set(lastIndex, lastPack);
							}

						} catch (Exception e) {
							mc.thePlayer.addChatComponentMessage(new ChatComponentText(
									"§cError! Your index is too high or too low! Remember they start from 0. #1 pack shown is at index "
											+ (enabled.size() - 1) + "."));
							return;
						}
					} else {
						enabled.add(pack);
					}
				}

				mc.getResourcePackRepository().setRepositories(enabled);
				mc.getResourcePackRepository().updateRepositoryEntriesAll();
				if (mode.toUpperCase().equals("REPLACE"))
					mc.gameSettings.resourcePacks.clear();
				if (mode.toUpperCase().equals("REMOVE"))
					mc.gameSettings.resourcePacks.remove(pack.getResourcePackName());
				else if (modeNum != -1) {
					int lastIndex = -1;
					String lastPack = null;
					if (mc.gameSettings.resourcePacks.contains(pack.getResourcePackName())) {
						lastIndex = mc.gameSettings.resourcePacks.indexOf(pack.getResourcePackName());
						if (mc.gameSettings.resourcePacks.size() - 1 >= modeNum)
							lastPack = mc.gameSettings.resourcePacks.get(modeNum);
					}

					mc.gameSettings.resourcePacks.set(modeNum, pack.getResourcePackName());
					if (lastIndex != -1 && lastPack != null) {
						mc.gameSettings.resourcePacks.set(lastIndex, lastPack);
					}

				}
					
				else
					mc.gameSettings.resourcePacks.add(pack.getResourcePackName());
				mc.gameSettings.saveOptions();
				mc.refreshResources();
				mc.thePlayer.addChatComponentMessage(
						new ChatComponentText("§a" + (mode.toUpperCase().equals("REMOVE") ? "Unloaded" : "Loaded")
								+ " pack '" + pack.getResourcePackName() + "§a'."));
				System.out.println(mc.gameSettings.resourcePacks.size());
				if (PackSwitcherMod.rpoSupport) {
					ResourcePackOrganizer.getConfig().options.updateEnabledPacks();
					ResourcePackOrganizer.getConfig().reload();
				}
				return;
			}
		}
		else {

			List<Entry> enabled = new ArrayList<Entry>();
			enabled.addAll(mc.getResourcePackRepository().getRepositoryEntries());

			mc.thePlayer.addChatComponentMessage(new ChatComponentText("§aLoaded packs (" + enabled.size() + "):"));
			Collections.reverse(enabled);
			int size = enabled.size();
			for (int i = 0; i < size; i++) {

				mc.thePlayer.addChatComponentMessage(
						new ChatComponentText("§a#" + (size - 1 - i) + ": " + enabled.get(i).getResourcePackName()));

			}
		}
	
			
			
		
		

	}

	@Override
	public int getRequiredPermissionLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		// TODO Auto-generated method stub
		return true;
	}

	private int isInteger(String input) {
		try {
			return Integer.parseInt(input);
		} catch (NumberFormatException e) {
			return -1;
		}
	}

}
