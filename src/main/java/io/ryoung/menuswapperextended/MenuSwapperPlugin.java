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

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import java.util.Set;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.NPC;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.Text;

@Slf4j
@PluginDescriptor(
	name = "Menu Swapper Extended"
)
public class MenuSwapperPlugin extends Plugin
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

	@Provides
	MenuSwapperConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(MenuSwapperConfig.class);
	}

	@Subscribe(priority = 10)
	public void onMenuEntryAdded(MenuEntryAdded event)
	{
		if (client.getGameState() != GameState.LOGGED_IN)
		{
			return;
		}

		final int eventId = event.getIdentifier();
		final String option = Text.removeTags(event.getOption()).toLowerCase();
		final String target = Text.removeTags(event.getTarget()).toLowerCase();
		final NPC hintArrowNpc = client.getHintArrowNpc();

		if (hintArrowNpc != null
			&& hintArrowNpc.getIndex() == eventId
			&& NPC_MENU_TYPES.contains(MenuAction.of(event.getType())))
		{
			return;
		}

		if (option.equals("talk-to"))
		{
			if (config.swapPlank() && target.equals("sawmill operator"))
			{
				swap("buy-plank", option, target, true);
			}

			if (config.claimDynamite() && target.contains("Thirus"))
			{
				swap("claim-dynamite", option, target, true);
			}

			if (config.swapMinigames())
			{
				swap("story", option, target, true);
				swap("start-minigame", option, target, true);
				swap("dream", option, target, true);
				swap("escort", option, target, true);
			}

			if (config.swapSendParcel())
			{
				swap("send-parcel", option, target, true);
			}

			if (config.swapZulrahCollect() && target.equals("priestess zul-gwenwynig"))
			{
				swap("collect", option, target, true);
			}
		}
		else if (option.equals("attack"))
		{
			if (config.swapStun() && target.contains("hoop snake"))
			{
				swap("stun", option, target, true);
			}
		}
		else if (config.swapSearch() && (option.equals("close") || option.equals("shut")))
		{
			swap("search", option, target, true);
		}
		else if (config.swapWildernessLever() && option.equals("ardougne") && target.equals("lever"))
		{
			swap("edgeville", option, target, true);
		}
		else if (target.equals("obelisk"))
		{
			switch (config.swapTeleportToDestination())
			{
				case SET_DESTINATION:
					swap("set destination", option, target, true);
					break;
				case TELEPORT_TO_DESTINATION:
					swap("teleport to destination", option, target, true);
					break;
			}
		}
		else if (config.swapDecant() && target.contains("bob barter"))
		{
			swap("decant", option, target, true);
		}
		else if (target.equals("zahur"))
		{
			swap(config.swapZahur().getOption().toLowerCase(), option, target, false);
		}
		else if (config.swapSlayer() && option.equals("standard") && target.contains("kings' ladder"))
		{
			swap("slayer", option, target, true);
		}
		else if (config.swapStore() && option.equals("trade-general-store"))
		{
			swap("trade-builders-store", option, target, true);
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
}

