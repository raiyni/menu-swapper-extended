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

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("menuswapperextended")
public interface MenuSwapperConfig extends Config
{
	@ConfigSection(
		name = "Item Swaps",
		description = "All options that swap item menu entries",
		position = 0
	)
	String itemSection = "items";

	@ConfigSection(
		name = "NPC Swaps",
		description = "All options that swap NPC menu entries",
		position = 1
	)
	String npcSection = "npcs";

	@ConfigSection(
		name = "Object Swaps",
		description = "All options that swap object menu entries",
		position = 2
	)
	String objectSection = "objects";

	@ConfigSection(
		name = "UI Swaps",
		description = "All options that swap entries in the UI (except Items)",
		position = 3
	)
	String uiSection = "ui";

	@ConfigItem(
		keyName = "swapMinigames",
		name = "Minigames",
		description = "Swap Talk-to with actions that start Minigames",
		section = npcSection
	)
	default boolean swapMinigames()
	{
		return false;
	}

	@ConfigItem(
		keyName = "swapSearch",
		name = "Search",
		description = "Swap Close, Shut with Search on chests, cupboards, etc.",
		section = objectSection
	)
	default boolean swapSearch()
	{
		return false;
	}

	@ConfigItem(
		keyName = "swapBuyPlank",
		name = "Buy-Plank",
		description = "Swap Talk-to with Buy-Plank on Sawmill Operator",
		section = npcSection
	)
	default boolean swapPlank()
	{
		return false;
	}

	@ConfigItem(
		keyName = "swapStun",
		name = "Stun Hoop Snakes",
		description = "Swap Attack with Stun",
		section = npcSection
	)
	default boolean swapStun()
	{
		return false;
	}

	@ConfigItem(
		keyName = "claimDynamite",
		name = "Claim Dynamite",
		description = "Swap Talk-to with Claim Dynamite on Thirus",
		section = npcSection
	)
	default boolean claimDynamite()
	{
		return false;
	}

	@ConfigItem(
		keyName = "swapWildernessLever",
		name = "Wilderness Lever to Edgeville",
		description = "Swap Edgeville Lever as default for wilderness lever",
		section = objectSection
	)
	default boolean swapWildernessLever()
	{
		return false;
	}

	@ConfigItem(
		keyName = "swapTeleportToDestination",
		name = "Obelisk",
		description = "Swap Activate with Teleport to Destination or Set Destination",
		section = objectSection
	)
	default ObeliskMode swapTeleportToDestination()
	{
		return ObeliskMode.ACTIVATE;
	}

	@ConfigItem(
		keyName = "swapZulrahCollect",
		name = "Collect (Zulrah)",
		description = "Swap Collect with Talk-to for Priestess Zul-Gwenwynig at Zul-Andra.",
		section = npcSection
	)
	default boolean swapZulrahCollect()
	{
		return false;
	}

	@ConfigItem(
		keyName = "collectRantz",
		name = "Collect (Rantz)",
		description = "Swap Collect with Talk-to for Rantz.",
		section = npcSection
	)
	default boolean collectRantz()
	{
		return false;
	}

	@ConfigItem(
		keyName = "zahurOption",
		name = "Zahur",
		description = "Zahur default option",
		section = npcSection
	)
	default ZahurMode swapZahur()
	{
		return ZahurMode.TALK;
	}

	@ConfigItem(
		keyName = "dagganothKingsLadder",
		name = "Dagganoth King Ladder",
		description = "Change the default option to slayer in the Dagannoth Kings lair.",
		section = objectSection
	)
	default boolean dagganothKingsLadder()
	{
		return false;
	}

	@ConfigItem(
		keyName = "swapStore",
		name = "Builders Store",
		description = "Swap Talk-to with Trade-Builders-Store for the NPC Razmire Keelgan",
		section = npcSection
	)
	default boolean swapStore()
	{
		return false;
	}

	@ConfigItem(
		keyName = "swapTeleCrystal",
		name = "Teleport Crystal",
		description = "Swaps the Crystal Seed Teleport to Prifddinas",
		section = itemSection
	)
	default boolean swapTeleCrystal()
	{
		return false;
	}

	@ConfigItem(
		keyName = "swapPharaohSceptreLeftClick",
		name = "Pharaoh's Sceptre",
		description = "Change the left-click option on Pharaoh Sceptre",
		section = itemSection
	)
	default PharaohSceptreMode swapPharaohSceptreLeftClick()
	{
		return PharaohSceptreMode.JALSAVRAH;
	}

	@ConfigItem(
		keyName = "swapTraderCrewmemberLeftClick",
		name = "Charter Ships",
		description = "Change the left-click option on Trader Crewmembers on Charter ships",
		section = npcSection
	)
	default CharterShipsMode swapTraderCrewmemberLeftClick()
	{
		return CharterShipsMode.TALK;
	}

	@ConfigItem(
		keyName = "swapDrakansMedallionLeftClick",
		name = "Drakan's Medallion",
		description = "Change the left-click option on Drakan's Medallion",
		section = itemSection
	)
	default DrakansMedallionMode swapDrakansMedallionLeftClick()
	{
		return DrakansMedallionMode.WEAR;
	}

	@ConfigItem(
		keyName = "swapSpellbookSwapLeftClick",
		name = "Spellbook Swap",
		description = "Change the left-click option on Spellbook Swap (Lunar Spell)",
		section = uiSection
	)
	default SpellbookSwapMode swapSpellbookSwapLeftClick()
	{
		return SpellbookSwapMode.CAST;
	}

	@ConfigItem(
		keyName = "swapGiveSword",
		name = "Give Sword",
		description = "Swaps Talk-To to with Give-Sword for Tindel Marchant at Port Khazard",
		section = npcSection
	)
	default boolean swapGiveSword()
	{
		return false;
	}

	@ConfigItem(
		keyName = "swapTyssSpellbook",
		name = "Tyss Spellbook",
		description = "Swaps Talk-To to with Spellbook for Tyss at the dark altar",
		section = npcSection
	)
	default boolean swapTyssSpellbook()
	{
		return false;
	}

	@ConfigItem(
		keyName = "swapGodWarsDoor",
		name = "God Wars Private Instance",
		description = "Swaps the normal and private GWD rooms for Trailblazer League",
		section = objectSection
	)
	default boolean swapGodWarsDoor()
	{
		return false;
	}

	@ConfigItem(
		keyName = "swapStrayDog",
		name = "Stray dog",
		description = "Swaps Shoo-away with Pet for Stray dogs",
		section = npcSection
	)
	default boolean swapStrayDog()
	{
		return false;
	}

	@ConfigItem(
		keyName = "swapFremennikSeaBootsLeftClick",
		name = "Fremennik Sea Boots",
		description = "Change the left-click option on Fremennik Sea Boots",
		section = itemSection
	)
	default FremennikSeaBootsMode swapFremennikSeaBootsLeftClick()
	{
		return FremennikSeaBootsMode.WEAR;
	}

	@ConfigItem(
		keyName = "castBloom",
		name = "Cast Bloom",
		description = "Change the left click option to Cast Bloom for sickles and flails.",
		section = itemSection
	)
	default boolean castBloom()
	{
		return false;
	}

	@ConfigItem(
		keyName = "snowSnowglobe",
		name = "Snow Snow Globe",
		description = "Change the left click option to Snow for Snow Globes.",
		section = itemSection
	)
	default boolean snowSnowglobe()
	{
		return false;
	}

	@ConfigItem(
			keyName = "tobCrystal",
			name = "Verzik's Crystal Shard",
			description = "Change the left click option to Use for Verzik's Crystal Shard.",
			section = itemSection
	)
	default boolean tobCrystal()
	{
		return false;
	}

	@ConfigItem(
		keyName = "quickexitSepulchre",
		name = "Quick-Exit Sepulchre",
		description = "Change the left click option to quick-exit for Magical Obelisk in Sepulchre.",
		section = objectSection
	)
	default boolean quickexitSepulchre()
	{
		return false;
	}

	@ConfigItem(
		keyName = "swapMythicalCapeLeftClick",
		name = "Mythical Cape",
		description = "Change the left-click option on Mythical capes",
		section = itemSection
	)
	default MythicalCapeMode swapMythicalCapeLeftClick()
	{
		return MythicalCapeMode.WEAR;
	}

	@ConfigItem(
		keyName = "privateKBD",
		name = "Private KBD",
		description = "Change the left click option to private for KKB.",
		section = objectSection
	)
	default boolean privateKBD()
	{
		return false;
	}

	@ConfigItem(
		keyName = "kharedstsMemoirs",
		name = "Kharesdt's Memoirs Reminisce",
		description = "Change the left click option to Reminisce for Kharedst's Memoirs.",
		section = itemSection
	)
	default boolean kharedstsMemoirs()
	{
		return false;
	}

	@ConfigItem(
		keyName = "templeTrekkking",
		name = "Temple Trekking Continue-trek",
		description = "Change the left click option to Continue-Trek for Temple Trekking.",
		section = objectSection
	)
	default boolean templeTrekkking()
	{
		return false;
	}

	@ConfigItem(
		keyName = "sandBert",
		name = "Bert Sand",
		description = "Change the left click option to Sand for NPC bert",
		section = npcSection
	)
	default boolean sandBert()
	{
		return false;
	}

	@ConfigItem(
		keyName = "kittenGertrude",
		name = "Gertrude Kitten",
		description = "Change the left click option to Kitten for NPC Gertrude",
		section = npcSection
	)
	default boolean kittenGertrude()
	{
		return false;
	}

	@ConfigItem(
		keyName = "swapNMZBarrelLeftClick",
		name = "NMZ Barrel",
		description = "Change the left-click option on the NMZ barrels",
		section = objectSection
	)
	default NMZBarrelMode swapNMZBarrelLeftClick()
	{
		return NMZBarrelMode.CHECK;
	}

	@ConfigItem(
		keyName = "kazgar",
		name = "Kazgar",
		description = "Change the left click option for NPC Kazgar",
		section = npcSection
	)
	default KazgarMode swapKazgar() {
		return KazgarMode.TALK;
	}

	@ConfigItem(
		keyName = "mistag",
		name = "Mistag",
		description = "Change the left click option for NPC Mistag",
		section = npcSection
	)
	default MistagMode swapMistag() {
		return MistagMode.TALK;
	}
}

