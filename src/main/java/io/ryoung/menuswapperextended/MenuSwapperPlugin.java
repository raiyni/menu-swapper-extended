/*
 * Copyright (c) 2019, Ron Young <https://github.com/raiyni>
 * All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *     list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package io.ryoung.menuswapperextended;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.NPC;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.FocusChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyListener;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.Text;

@Slf4j
@PluginDescriptor(
	name = "Menu Swapper Extended"
)
public class MenuSwapperPlugin extends Plugin implements KeyListener
{
	private static final Set<MenuAction> NPC_MENU_TYPES = ImmutableSet.of(
		MenuAction.NPC_FIRST_OPTION,
		MenuAction.NPC_SECOND_OPTION,
		MenuAction.NPC_THIRD_OPTION,
		MenuAction.NPC_FOURTH_OPTION,
		MenuAction.NPC_FIFTH_OPTION,
		MenuAction.EXAMINE_NPC);

	@Inject
	private Client client;

	@Inject
	private MenuSwapperConfig config;

	@Inject
	private KeyManager keyManager;

	private boolean shiftHeld = false;

	@Provides
	MenuSwapperConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(MenuSwapperConfig.class);
	}

	@Override
	public void startUp()
	{
		shiftHeld = false;
		keyManager.registerKeyListener(this);
	}

	@Override
	public void shutDown()
	{
		shiftHeld = false;
		keyManager.unregisterKeyListener(this);
	}

	private final ArrayListMultimap<String, Integer> optionIndexes = ArrayListMultimap.create();

	@Subscribe(priority = 10)
	public void onClientTick(ClientTick clientTick)
	{
		// The menu is not rebuilt when it is open, so don't swap or else it will
		// repeatedly swap entries
		if (client.getGameState() != GameState.LOGGED_IN || client.isMenuOpen())
		{
			return;
		}

		MenuEntry[] menuEntries = client.getMenuEntries();

		// Build option map for quick lookup in findIndex
		int idx = 0;
		optionIndexes.clear();
		for (MenuEntry entry : menuEntries)
		{
			String option = Text.removeTags(entry.getOption()).toLowerCase();
			optionIndexes.put(option, idx++);
		}

		// Perform swaps
		idx = 0;
		for (MenuEntry entry : menuEntries)
		{
			swapMenuEntry(idx++, entry);
		}
	}

	private void swapMenuEntry(int index, MenuEntry menuEntry)
	{
		final int eventId = menuEntry.getIdentifier();
		final String option = Text.removeTags(menuEntry.getOption()).toLowerCase();
		final String target = Text.removeTags(menuEntry.getTarget()).toLowerCase();
		final NPC hintArrowNpc = client.getHintArrowNpc();

		if (hintArrowNpc != null
			&& hintArrowNpc.getIndex() == eventId
			&& NPC_MENU_TYPES.contains(MenuAction.of(menuEntry.getType())))
		{
			return;
		}

		if (option.equals("talk-to"))
		{
			if (config.swapPlank() && target.equals("sawmill operator"))
			{
				swap("buy-plank", option, target, index);
			}

			if (config.claimDynamite() && target.contains("Thirus"))
			{
				swap("claim", option, target, index);
			}

			if (config.swapMinigames())
			{
				swap("story", option, target, index);
				swap("start-minigame", option, target, index);
				swap("dream", option, target, index);
				swap("escort", option, target, index);
			}

			if (config.swapSendParcel())
			{
				swap("send-parcel", option, target, index);
			}

			if (config.swapZulrahCollect() && target.equals("priestess zul-gwenwynig"))
			{
				swap("collect", option, target, index);
			}

			if (config.swapStore())
			{
				swap("trade-builders-store", option, target, index);
			}
		}
		else if (!shiftHeld && option.equals("wear"))
		{
			if (config.swapConsCape() && (target.startsWith("construct. cape")))
			{
				swap("tele to poh", option, target, index);
			}
			else if (target.startsWith("karamja gloves"))
			{
				swap(config.swapKaramjaGlovesLeftClick().getOption().toLowerCase(), option, target, index);
			}
			else if (target.startsWith("desert amulet"))
			{
				swap(config.swapDesertAmuletLeftClick().getOption().toLowerCase(), option, target, index);
			}
			else if (target.startsWith("morytania legs"))
			{
				swap(config.swapMorytaniaLegsLeftClick().getOption().toLowerCase(), option, target, index);
			}
			else if (target.startsWith("ardougne cloak"))
			{
				swap(config.swapArdougneCloakLeftClick().getOption().toLowerCase(), option, target, index);
			}
		}
		else if (option.equals("attack"))
		{
			if (config.swapStun() && target.contains("hoop snake"))
			{
				swap("stun", option, target, index);
			}
		}
		else if (config.swapSearch() && (option.equals("close") || option.equals("shut")))
		{
			swap("search", option, target, index);
		}
		else if (config.swapWildernessLever() && option.equals("ardougne") && target.equals("lever"))
		{
			swap("edgeville", option, target, index);
		}
		else if (target.equals("obelisk"))
		{
			switch (config.swapTeleportToDestination())
			{
				case SET_DESTINATION:
					swap("set destination", option, target, index);
					break;
				case TELEPORT_TO_DESTINATION:
					swap("teleport to destination", option, target, index);
					break;
			}
		}
		else if (config.swapDecant() && target.contains("bob barter"))
		{
			swap("decant", option, target, index);
		}
		else if (target.equals("zahur"))
		{
			swap(config.swapZahur().getOption().toLowerCase(), option, target, index);
		}
		else if (config.swapSlayer() && option.equals("standard") && target.contains("kings' ladder"))
		{
			swap("slayer", option, target, index);
		}
		else if (!shiftHeld && config.swapTeleCrystal() && option.equals("lletya"))
		{
			swap("prifddinas", option, target, index);
		}
		else if (!shiftHeld && target.startsWith("pharaoh's sceptre") && option.equals("jalsavrah"))
		{
			swap(config.swapPharaohSceptreLeftClick().getOption().toLowerCase(), option, target, index);
		}
		else if (!shiftHeld && target.startsWith("trader crewmember"))
		{
			CharterShipsMode configOption = config.swapTraderCrewmemberLeftClick();
			if (configOption == CharterShipsMode.LAST_DESTINATION)
			{
				swap("charter-to", option, target, index, false);
			}
			else
			{
				// NOTE: Selecting Talk-To conflicts with Runelites own Menu entry swappers options "Trade" and "Travel"
				swap(configOption.getOption().toLowerCase(), option, target, index);
			}
		}
	}

	private int searchIndex(MenuEntry[] entries, String option, String target, boolean strict)
	{
		for (int i = entries.length - 1; i >= 0; i--)
		{
			MenuEntry entry = entries[i];
			String entryOption = Text.removeTags(entry.getOption()).toLowerCase();
			String entryTarget = Text.removeTags(entry.getTarget()).toLowerCase();

			if (strict)
			{
				if (entryOption.equals(option) && entryTarget.equals(target))
				{
					return i;
				}
			}
			else
			{
				if (entryOption.contains(option.toLowerCase()) && entryTarget.equals(target))
				{
					return i;
				}
			}
		}

		return -1;
	}

	private void swap(String optionA, String optionB, String target, int index)
	{
		swap(optionA, optionB, target, index, true);
	}

	private void swap(String optionA, String optionB, String target, int index, boolean strict)
	{
		MenuEntry[] menuEntries = client.getMenuEntries();

		int thisIndex = findIndex(menuEntries, index, optionB, target, strict);
		int optionIdx = findIndex(menuEntries, thisIndex, optionA, target, strict);

		if (thisIndex >= 0 && optionIdx >= 0)
		{
			swap(optionIndexes, menuEntries, optionIdx, thisIndex);
		}
	}

	private void swap(ArrayListMultimap<String, Integer> optionIndexes, MenuEntry[] entries, int index1, int index2)
	{
		MenuEntry entry = entries[index1];
		entries[index1] = entries[index2];
		entries[index2] = entry;

		client.setMenuEntries(entries);

		// Rebuild option indexes
		optionIndexes.clear();
		int idx = 0;
		for (MenuEntry menuEntry : entries)
		{
			String option = Text.removeTags(menuEntry.getOption()).toLowerCase();
			optionIndexes.put(option, idx++);
		}
	}

	private int findIndex(MenuEntry[] entries, int limit, String option, String target, boolean strict)
	{
		if (strict)
		{
			List<Integer> indexes = optionIndexes.get(option);

			// We want the last index which matches the target, as that is what is top-most
			// on the menu
			for (int i = indexes.size() - 1; i >= 0; --i)
			{
				int idx = indexes.get(i);
				MenuEntry entry = entries[idx];
				String entryTarget = Text.removeTags(entry.getTarget()).toLowerCase();

				// Limit to the last index which is prior to the current entry
				if (idx <= limit && entryTarget.equals(target))
				{
					return idx;
				}
			}
		}
		else
		{
			// Without strict matching we have to iterate all entries up to the current limit...
			for (int i = limit; i >= 0; i--)
			{
				MenuEntry entry = entries[i];
				String entryOption = Text.removeTags(entry.getOption()).toLowerCase();
				String entryTarget = Text.removeTags(entry.getTarget()).toLowerCase();

				if (entryOption.contains(option.toLowerCase()) && entryTarget.equals(target))
				{
					return i;
				}
			}

		}

		return -1;
	}

	private void swap(String optionA, String optionB, String target, boolean strict)
	{
		MenuEntry[] entries = client.getMenuEntries();

		int idxA = searchIndex(entries, optionA, target, strict);
		int idxB = searchIndex(entries, optionB, target, strict);

		if (idxA >= 0 && idxB >= 0)
		{
			MenuEntry entry = entries[idxA];
			entries[idxA] = entries[idxB];
			entries[idxB] = entry;

			client.setMenuEntries(entries);
		}
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_SHIFT)
		{
			shiftHeld = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_SHIFT)
		{
			shiftHeld = false;
		}
	}

	@Subscribe
	public void onFocusChanged(FocusChanged event)
	{
		if (!event.isFocused())
		{
			shiftHeld = false;
		}
	}
}

