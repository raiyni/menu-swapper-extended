/*
 * Copyright (c) 2019, Ron Young <https://github.com/raiyni>
 * Copyright (c) 2021, Truth Forger <https://github.com/Blackberry0Pie>
 * Copyright (c) 2018, Adam <Adam@sigterm.info>
 * Copyright (c) 2018, Kamiel
 * Copyright (c) 2019, Rami <https://github.com/Rami-J>
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

import com.google.common.annotations.VisibleForTesting;
import static com.google.common.base.Predicates.alwaysTrue;
import static com.google.common.base.Predicates.equalTo;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Provides;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javax.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.ItemComposition;
import net.runelite.api.KeyCode;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.NPC;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.MenuOpened;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.PostItemComposition;
import net.runelite.api.events.WidgetMenuOptionClicked;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.ItemVariationMapping;
import net.runelite.client.input.KeyManager;
import net.runelite.client.menus.MenuManager;
import net.runelite.client.menus.WidgetMenuOption;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.menuentryswapper.MenuEntrySwapperConfig;
import net.runelite.client.util.Text;
import org.apache.commons.lang3.ArrayUtils;

@Slf4j
@PluginDescriptor(
	name = "Menu Swapper Extended"
)
public class MenuSwapperPlugin extends Plugin
{
	private static final String CONFIGURE = "Configure";
	private static final String SAVE = "Save";
	private static final String RESET = "Reset";
	private static final String MENU_TARGET = "Shift-click";

	private static final String SHIFTCLICK_CONFIG_GROUP = "shiftclick";
	private static final String ITEM_KEY_PREFIX = "item_";

	private static final WidgetMenuOption FIXED_INVENTORY_TAB_CONFIGURE = new WidgetMenuOption(CONFIGURE,
		MENU_TARGET, WidgetInfo.FIXED_VIEWPORT_INVENTORY_TAB);

	private static final WidgetMenuOption FIXED_INVENTORY_TAB_SAVE = new WidgetMenuOption(SAVE,
		MENU_TARGET, WidgetInfo.FIXED_VIEWPORT_INVENTORY_TAB);

	private static final WidgetMenuOption RESIZABLE_INVENTORY_TAB_CONFIGURE = new WidgetMenuOption(CONFIGURE,
		MENU_TARGET, WidgetInfo.RESIZABLE_VIEWPORT_INVENTORY_TAB);

	private static final WidgetMenuOption RESIZABLE_INVENTORY_TAB_SAVE = new WidgetMenuOption(SAVE,
		MENU_TARGET, WidgetInfo.RESIZABLE_VIEWPORT_INVENTORY_TAB);

	private static final WidgetMenuOption RESIZABLE_BOTTOM_LINE_INVENTORY_TAB_CONFIGURE = new WidgetMenuOption(CONFIGURE,
		MENU_TARGET, WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_INVENTORY_TAB);

	private static final WidgetMenuOption RESIZABLE_BOTTOM_LINE_INVENTORY_TAB_SAVE = new WidgetMenuOption(SAVE,
		MENU_TARGET, WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_INVENTORY_TAB);

	private static final Set<MenuAction> ITEM_MENU_TYPES = ImmutableSet.of(
		MenuAction.ITEM_FIRST_OPTION,
		MenuAction.ITEM_SECOND_OPTION,
		MenuAction.ITEM_THIRD_OPTION,
		MenuAction.ITEM_FOURTH_OPTION,
		MenuAction.ITEM_FIFTH_OPTION,
		MenuAction.EXAMINE_ITEM,
		MenuAction.ITEM_USE
	);

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
	private ClientThread clientThread;

	@Inject
	private MenuSwapperConfig config;

	@Inject
	private ConfigManager configManager;

	@Inject
	private MenuManager menuManager;

	@Inject
	private ItemManager itemManager;

	@Inject
	private KeyManager keyManager;

	@Getter
	private boolean configuringShiftClick = false;

	private final Multimap<String, Swap> swaps = LinkedHashMultimap.create();
	private final ArrayListMultimap<String, Integer> optionIndexes = ArrayListMultimap.create();

	@Provides
	MenuSwapperConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(MenuSwapperConfig.class);
	}

	@Override
	public void startUp()
	{
		if (configManager.getConfiguration(MenuEntrySwapperConfig.GROUP, "shiftClickCustomization").equals("true"))
		{
			enableCustomization();
		}

		setupSwaps();
	}

	@Override
	public void shutDown()
	{
		disableCustomization();
		swaps.clear();
	}

	@VisibleForTesting
	void setupSwaps()
	{
		swap("talk-to", "buy-plank", config::swapPlank);
		swap("talk-to", "claim", config::claimDynamite);
		swap("talk-to", "story", config::swapMinigames);
		swap("talk-to", "dream", config::swapMinigames);
		swap("talk-to", "escort", config::swapMinigames);
		swap("talk-to", "join", config::swapMinigames);
		swap("talk-to", "priestess zul-gwenwynig", "collect", config::swapZulrahCollect);
		swap("talk-to", "trade-builders-store", config::swapStore);
		swap("talk-to", "give-sword", config::swapGiveSword);
		swap("talk-to", "spellbook", config::swapTyssSpellbook);
		swap("talk-to", "zahur", "clean", () -> config.swapZahur() == ZahurMode.CLEAN);
		swap("talk-to", "zahur", "decant", () -> config.swapZahur() == ZahurMode.DECANT);
		swap("talk-to", "zahur", "make unfinished potion(s)", () -> config.swapZahur() == ZahurMode.MAKE_POTION);

		swap("wear", "tele to poh", () -> !shiftModifier() && config.swapConsCape());
		swap("wear", "gem mine", () -> !shiftModifier() && config.swapKaramjaGlovesLeftClick() == KaramjaGlovesMode.GEM_MINE);
		swap("wear", "duradel", () -> !shiftModifier() && config.swapKaramjaGlovesLeftClick() == KaramjaGlovesMode.DURADEL);
		swap("wear", "nardah", () -> !shiftModifier() && config.swapDesertAmuletLeftClick() == DesertAmuletMode.NARDAH);
		swap("wear", "kalphite cave", () -> !shiftModifier() && config.swapDesertAmuletLeftClick() == DesertAmuletMode.KALPHITE_CAVE);
		swap("wear", "ecto teleport", () -> !shiftModifier() && config.swapMorytaniaLegsLeftClick() == MorytaniaLegsMode.ECTO_TELEPORT);
		swap("wear", "burgh teleport", () -> !shiftModifier() && config.swapMorytaniaLegsLeftClick() == MorytaniaLegsMode.BURGH_TELEPORT);
		swap("wear", "monastery teleport", () -> !shiftModifier() && config.swapArdougneCloakLeftClick() == ArdougneCloakMode.MONASTERY_TELEPORT);
		swap("wear", "farm teleport", () -> !shiftModifier() && config.swapArdougneCloakLeftClick() == ArdougneCloakMode.FARM_TELEPORT);
		swap("wear", "ver sinhaza", () -> !shiftModifier() && config.swapDrakansMedallionLeftClick() == DrakansMedallionMode.VER_SINHAZA);
		swap("wear", "darkmeyer", () -> !shiftModifier() && config.swapDrakansMedallionLeftClick() == DrakansMedallionMode.DARKMEYER);
		swap("wield", "jalsavrah", () -> !shiftModifier() && config.swapPharaohSceptreLeftClick() == PharaohSceptreMode.JALSAVRAH);
		swap("wield", "jaleustrophos", () -> !shiftModifier() && config.swapPharaohSceptreLeftClick() == PharaohSceptreMode.JALEUSTROPHOS);
		swap("wield", "jaldraocht", () -> !shiftModifier() && config.swapPharaohSceptreLeftClick() == PharaohSceptreMode.JALDRAOCHT);

		swap("activate", "teleport to destination", () -> config.swapTeleportToDestination() == ObeliskMode.TELEPORT_TO_DESTINATION);
		swap("activate", "set destination", () -> config.swapTeleportToDestination() == ObeliskMode.SET_DESTINATION);
		swap("ardougne", "edgeville", config::swapWildernessLever);
		swapContains("attack", target -> target.startsWith("hoop snake"), "stun", config::swapStun);
		swap("cast", "standard", () -> !shiftModifier() && config.swapSpellbookSwapLeftClick() == SpellbookSwapMode.STANDARD);
		swap("cast", "ancient", () -> !shiftModifier() && config.swapSpellbookSwapLeftClick() == SpellbookSwapMode.ANCIENT);
		swap("cast", "arceuus", () -> !shiftModifier() && config.swapSpellbookSwapLeftClick() == SpellbookSwapMode.ARCEUUS);
		swap("open (normal)", "open (private)", config::swapGodWarsDoor);
		swap("close", "search", config::swapSearch);
		swap("shut", "search", config::swapSearch);
		swap("shoo-away", "pet", config::swapStrayDog);
		swap("standard", "slayer", config::dagganothKingsLadder);
		swap("talk-to", "trade", () -> !shiftModifier() && config.swapTraderCrewmemberLeftClick() == CharterShipsMode.TRADE);
		swap("talk-to", "charter", () -> !shiftModifier() && config.swapTraderCrewmemberLeftClick() == CharterShipsMode.CHARTER);
		swapContains("talk-to", alwaysTrue(), "charter-to", () -> !shiftModifier() && config.swapTraderCrewmemberLeftClick() == CharterShipsMode.LAST_DESTINATION);
		swap("lletya", "prifddinas", () -> !shiftModifier() && config.swapTeleCrystal());
	}

	private void swap(String option, String swappedOption, Supplier<Boolean> enabled)
	{
		swap(option, alwaysTrue(), swappedOption, enabled);
	}

	private void swap(String option, String target, String swappedOption, Supplier<Boolean> enabled)
	{
		swap(option, equalTo(target), swappedOption, enabled);
	}

	private void swap(String option, Predicate<String> targetPredicate, String swappedOption, Supplier<Boolean> enabled)
	{
		swaps.put(option, new Swap(alwaysTrue(), targetPredicate, swappedOption, enabled, true));
	}

	private void swapContains(String option, Predicate<String> targetPredicate, String swappedOption, Supplier<Boolean> enabled)
	{
		swaps.put(option, new Swap(alwaysTrue(), targetPredicate, swappedOption, enabled, false));
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (event.getGroup().equals(MenuEntrySwapperConfig.GROUP) && event.getKey().equals("shiftClickCustomization"))
		{
			if (configManager.getConfiguration(MenuEntrySwapperConfig.GROUP, "shiftClickCustomization").equals("true"))
			{
				enableCustomization();
			}
			else
			{
				disableCustomization();
			}
		}
		else if (event.getGroup().equals(SHIFTCLICK_CONFIG_GROUP) && event.getKey().startsWith(ITEM_KEY_PREFIX))
		{
			clientThread.invoke(this::resetItemCompositionCache);
		}
	}

	private void resetItemCompositionCache()
	{
		itemManager.invalidateItemCompositionCache();
		client.getItemCompositionCache().reset();
	}

	private Integer getSwapConfig(int itemId)
	{
		itemId = ItemVariationMapping.map(itemId);
		String config = configManager.getConfiguration(SHIFTCLICK_CONFIG_GROUP, ITEM_KEY_PREFIX + itemId);
		if (config == null || config.isEmpty())
		{
			return null;
		}

		return Integer.parseInt(config);
	}

	private void setSwapConfig(int itemId, int index)
	{
		itemId = ItemVariationMapping.map(itemId);
		configManager.setConfiguration(SHIFTCLICK_CONFIG_GROUP, ITEM_KEY_PREFIX + itemId, index);
	}

	private void unsetSwapConfig(int itemId)
	{
		itemId = ItemVariationMapping.map(itemId);
		configManager.unsetConfiguration(SHIFTCLICK_CONFIG_GROUP, ITEM_KEY_PREFIX + itemId);
	}

	private void enableCustomization()
	{
		refreshShiftClickCustomizationMenus();
		// set shift click action index on the item compositions
		clientThread.invoke(this::resetItemCompositionCache);
	}

	private void disableCustomization()
	{
		removeShiftClickCustomizationMenus();
		configuringShiftClick = false;
		// flush item compositions to reset the shift click action index
		clientThread.invoke(this::resetItemCompositionCache);
	}

	@Subscribe
	public void onWidgetMenuOptionClicked(WidgetMenuOptionClicked event)
	{
		if (event.getWidget() == WidgetInfo.FIXED_VIEWPORT_INVENTORY_TAB
			|| event.getWidget() == WidgetInfo.RESIZABLE_VIEWPORT_INVENTORY_TAB
			|| event.getWidget() == WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_INVENTORY_TAB)
		{
			configuringShiftClick = event.getMenuOption().equals(CONFIGURE) && Text.removeTags(event.getMenuTarget()).equals(MENU_TARGET);
			refreshShiftClickCustomizationMenus();
		}
	}

	@Subscribe
	public void onMenuOpened(MenuOpened event)
	{
		if (!configuringShiftClick)
		{
			return;
		}

		MenuEntry firstEntry = event.getFirstEntry();
		if (firstEntry == null)
		{
			return;
		}

		int widgetId = firstEntry.getParam1();
		if (widgetId != WidgetInfo.INVENTORY.getId())
		{
			return;
		}

		int itemId = firstEntry.getIdentifier();
		if (itemId == -1)
		{
			return;
		}

		ItemComposition itemComposition = itemManager.getItemComposition(itemId);
		MenuAction shiftClickAction = MenuAction.ITEM_USE;
		final int shiftClickActionIndex = itemComposition.getShiftClickActionIndex();

		if (shiftClickActionIndex >= 0)
		{
			shiftClickAction = MenuAction.of(MenuAction.ITEM_FIRST_OPTION.getId() + shiftClickActionIndex);
		}

		MenuEntry[] entries = event.getMenuEntries();

		for (MenuEntry entry : entries)
		{
			final MenuAction menuAction = MenuAction.of(entry.getType());

			if (ITEM_MENU_TYPES.contains(menuAction) && entry.getIdentifier() == itemId)
			{
				entry.setType(MenuAction.RUNELITE.getId());

				if (shiftClickAction == menuAction)
				{
					entry.setOption("* " + entry.getOption());
				}
			}
		}

		final MenuEntry resetShiftClickEntry = new MenuEntry();
		resetShiftClickEntry.setOption(RESET);
		resetShiftClickEntry.setTarget(MENU_TARGET);
		resetShiftClickEntry.setIdentifier(itemId);
		resetShiftClickEntry.setParam1(widgetId);
		resetShiftClickEntry.setType(MenuAction.RUNELITE.getId());
		client.setMenuEntries(ArrayUtils.addAll(entries, resetShiftClickEntry));
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (event.getMenuAction() != MenuAction.RUNELITE || event.getWidgetId() != WidgetInfo.INVENTORY.getId())
		{
			return;
		}

		int itemId = event.getId();

		if (itemId == -1)
		{
			return;
		}

		String option = event.getMenuOption();
		String target = event.getMenuTarget();
		ItemComposition itemComposition = itemManager.getItemComposition(itemId);

		if (option.equals(RESET) && target.equals(MENU_TARGET))
		{
			unsetSwapConfig(itemId);
			return;
		}

		if (!itemComposition.getName().equals(Text.removeTags(target)))
		{
			return;
		}

		if (option.equals("Use")) //because "Use" is not in inventoryActions
		{
			setSwapConfig(itemId, -1);
		}
		else
		{
			String[] inventoryActions = itemComposition.getInventoryActions();

			for (int index = 0; index < inventoryActions.length; index++)
			{
				if (option.equals(inventoryActions[index]))
				{
					setSwapConfig(itemId, index);
					break;
				}
			}
		}
	}

	private void swapMenuEntry(int index, MenuEntry menuEntry)
	{
		final int eventId = menuEntry.getIdentifier();
		final MenuAction menuAction = MenuAction.of(menuEntry.getType());
		final String option = Text.removeTags(menuEntry.getOption()).toLowerCase();
		final String target = Text.removeTags(menuEntry.getTarget()).toLowerCase();
		final NPC hintArrowNpc = client.getHintArrowNpc();

		if (hintArrowNpc != null
			&& hintArrowNpc.getIndex() == eventId
			&& NPC_MENU_TYPES.contains(menuAction))
		{
			return;
		}

		if (shiftModifier() && (menuAction == MenuAction.ITEM_FIRST_OPTION
			|| menuAction == MenuAction.ITEM_SECOND_OPTION
			|| menuAction == MenuAction.ITEM_THIRD_OPTION
			|| menuAction == MenuAction.ITEM_FOURTH_OPTION
			|| menuAction == MenuAction.ITEM_FIFTH_OPTION
			|| menuAction == MenuAction.ITEM_USE))
		{
			// Special case use shift click due to items not actually containing a "Use" option, making
			// the client unable to perform the swap itself.
			if (configManager.getConfiguration(MenuEntrySwapperConfig.GROUP, "shiftClickCustomization").equals("true") && !option.equals("use"))
			{
				Integer customOption = getSwapConfig(eventId);

				if (customOption != null && customOption == -1)
				{
					swap("use", target, index, true);
				}
			}

			// don't perform swaps on items when shift is held; instead prefer the client menu swap, which
			// we may have overwrote
			return;
		}

		Collection<Swap> swaps = this.swaps.get(option);
		for (Swap swap : swaps)
		{
			if (swap.getTargetPredicate().test(target) && swap.getEnabled().get())
			{
				if (swap(swap.getSwappedOption(), target, index, swap.isStrict()))
				{
					break;
				}
			}
		}
	}

	@Subscribe
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

	@Subscribe
	public void onPostItemComposition(PostItemComposition event)
	{
		if (configManager.getConfiguration(MenuEntrySwapperConfig.GROUP, "shiftClickCustomization").equals("false"))
		{
			// since shift-click is done by the client we have to check if our shift click customization is on
			// prior to altering the item shift click action index.
			return;
		}

		ItemComposition itemComposition = event.getItemComposition();
		Integer option = getSwapConfig(itemComposition.getId());

		if (option != null)
		{
			itemComposition.setShiftClickActionIndex(option);
		}
	}

	private boolean swap(String option, String target, int index, boolean strict)
	{
		MenuEntry[] menuEntries = client.getMenuEntries();

		// find option to swap with
		int optionIdx = findIndex(menuEntries, index, option, target, strict);

		if (optionIdx >= 0)
		{
			swap(optionIndexes, menuEntries, optionIdx, index);
			return true;
		}

		return false;
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
				if (idx < limit && entryTarget.equals(target))
				{
					return idx;
				}
			}
		}
		else
		{
			// Without strict matching we have to iterate all entries up to the current limit...
			for (int i = limit - 1; i >= 0; i--)
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

	private void swap(ArrayListMultimap<String, Integer> optionIndexes, MenuEntry[] entries, int index1, int index2)
	{
		MenuEntry entry1 = entries[index1],
			entry2 = entries[index2];

		entries[index1] = entry2;
		entries[index2] = entry1;

		client.setMenuEntries(entries);

		// Update optionIndexes
		String option1 = Text.removeTags(entry1.getOption()).toLowerCase(),
			option2 = Text.removeTags(entry2.getOption()).toLowerCase();

		List<Integer> list1 = optionIndexes.get(option1),
			list2 = optionIndexes.get(option2);

		// call remove(Object) instead of remove(int)
		list1.remove((Integer) index1);
		list2.remove((Integer) index2);

		sortedInsert(list1, index2);
		sortedInsert(list2, index1);
	}

	private static <T extends Comparable<? super T>> void sortedInsert(List<T> list, T value) // NOPMD: UnusedPrivateMethod: false positive
	{
		int idx = Collections.binarySearch(list, value);
		list.add(idx < 0 ? -idx - 1 : idx, value);
	}

	private void removeShiftClickCustomizationMenus()
	{
		menuManager.removeManagedCustomMenu(FIXED_INVENTORY_TAB_CONFIGURE);
		menuManager.removeManagedCustomMenu(FIXED_INVENTORY_TAB_SAVE);
		menuManager.removeManagedCustomMenu(RESIZABLE_BOTTOM_LINE_INVENTORY_TAB_CONFIGURE);
		menuManager.removeManagedCustomMenu(RESIZABLE_BOTTOM_LINE_INVENTORY_TAB_SAVE);
		menuManager.removeManagedCustomMenu(RESIZABLE_INVENTORY_TAB_CONFIGURE);
		menuManager.removeManagedCustomMenu(RESIZABLE_INVENTORY_TAB_SAVE);
	}

	private void refreshShiftClickCustomizationMenus()
	{
		removeShiftClickCustomizationMenus();
		if (configuringShiftClick)
		{
			menuManager.addManagedCustomMenu(FIXED_INVENTORY_TAB_SAVE);
			menuManager.addManagedCustomMenu(RESIZABLE_BOTTOM_LINE_INVENTORY_TAB_SAVE);
			menuManager.addManagedCustomMenu(RESIZABLE_INVENTORY_TAB_SAVE);
		}
		else
		{
			menuManager.addManagedCustomMenu(FIXED_INVENTORY_TAB_CONFIGURE);
			menuManager.addManagedCustomMenu(RESIZABLE_BOTTOM_LINE_INVENTORY_TAB_CONFIGURE);
			menuManager.addManagedCustomMenu(RESIZABLE_INVENTORY_TAB_CONFIGURE);
		}
	}

	private boolean shiftModifier()
	{
		return client.isKeyPressed(KeyCode.KC_SHIFT);
	}
}
