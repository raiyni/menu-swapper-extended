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

@ConfigGroup("menuswapperextended")
public interface MenuSwapperConfig extends Config
{
	@ConfigItem(
		keyName = "swapMinigames",
		name = "Minigames",
		description = "Swap Talk-to with actions that start Minigames"
	)
	default boolean swapMinigames()
	{
		return false;
	}

	@ConfigItem(
		keyName = "swapSearch",
		name = "Search",
		description = "Swap Close, Shut with Search on chests, cupboards, etc."
	)
	default boolean swapSearch()
	{
		return false;
	}

	@ConfigItem(
		keyName = "swapBuyPlank",
		name = "Buy-Plank",
		description = "Swap Talk-to with Buy-Plank on Sawmill Operator"
	)
	default boolean swapPlank()
	{
		return false;
	}

	@ConfigItem(
		keyName = "swapStun",
		name = "Stun Hoop Snakes",
		description = "Swap Attack with Stun"
	)
	default boolean swapStun()
	{
		return false;
	}

	@ConfigItem(
		keyName = "claimDynamite",
		name = "Claim Dynamite",
		description = "Swap Talk-to with Claim Dynamite on Thirus"
	)
	default boolean claimDynamite()
	{
		return false;
	}

	@ConfigItem(
		keyName = "swapWildernessLever",
		name = "Wilderness Lever to Edgeville",
		description = "Swap Edgeville Lever as default for wilderness lever"
	)
	default boolean swapWildernessLever()
	{
		return false;
	}

	@ConfigItem(
		keyName = "swapSendParcel",
		name = "Send Parcel",
		description = "Swap Talk-To to with Send-Parcel for Rionasta at the Tai Bwo Wannai village."
	)
	default boolean swapSendParcel()
	{
		return false;
	}

	@ConfigItem(
		keyName = "swapTeleportToDestination",
		name = "Obelisk",
		description = "Swap Activate with Teleport to Destination or Set Destination"
	)
	default ObeliskMode swapTeleportToDestination()
	{
		return ObeliskMode.ACTIVATE;
	}

	@ConfigItem(
		keyName = "swapZulrahCollect",
		name = "Collect (Zulrah)",
		description = "Swap Collect with Talk-to for Priestess Zul-Gwenwynig at Zul-Andra."
	)
	default boolean swapZulrahCollect()
	{
		return false;
	}

	@ConfigItem(
		keyName = "decant",
		name = "Decant",
		description = "Decant for e.g. Bob Barter"
	)
	default boolean swapDecant()
	{
		return false;
	}

	@ConfigItem(
		keyName = "zahurOption",
		name = "Zahur",
		description = "Zahur default option"
	)
	default ZahurMode swapZahur()
	{
		return ZahurMode.MAKE_POTION;
	}

	@ConfigItem(
		keyName = "dagganothKingsLadder",
		name = "Dagganoth King Ladder",
		description = "Change the default option to slayer in the Dagannoth Kings lair."
	)
	default boolean dagganothKingsLadder()
	{
		return false;
	}

	@ConfigItem(
		keyName = "swapStore",
		name = "Builders Store",
		description = "Swap Trade-General-Store with Trade-Builders-Store for the NPC Razmire Keelgan"
	)
	default boolean swapStore()
	{
		return false;
	}

	@ConfigItem(
		keyName = "swapKaramjaGlovesLeftClick",
		name = "Karamja Gloves",
		description = "Change the left-click option on Karamja Gloves"
	)
	default KaramjaGlovesMode swapKaramjaGlovesLeftClick()
	{
		return KaramjaGlovesMode.WEAR;
	}

	@ConfigItem(
		keyName = "swapConsCape",
		name = "Construction Cape",
		description = "Change the left-click option on Construction Cape"
	)
	default boolean swapConsCape()
	{
		return false;
	}

	@ConfigItem(
		keyName = "swapTeleCrystal",
		name = "Teleport Crystal",
		description = "Swaps the Crystal Seed Teleport to Prifddinas"
	)
	default boolean swapTeleCrystal()
	{
		return false;
	}

	@ConfigItem(
		keyName = "swapPharaohSceptreLeftClick",
		name = "Pharaoh's Sceptre",
		description = "Change the left-click option on Pharaoh Sceptre"
	)
	default PharaohSceptreMode swapPharaohSceptreLeftClick()
	{
		return PharaohSceptreMode.JALSAVRAH;
	}

	@ConfigItem(
		keyName = "swapDesertAmuletLeftClick",
		name = "Desert Amulet",
		description = "Change the left-click option on Desert Amulet"
	)
	default DesertAmuletMode swapDesertAmuletLeftClick()
	{
		return DesertAmuletMode.WEAR;
	}

	@ConfigItem(
		keyName = "swapMorytaniaLegsLeftClick",
		name = "Morytania Legs",
		description = "Change the left-click option on Morytania Legs"
	)
	default MorytaniaLegsMode swapMorytaniaLegsLeftClick()
	{
		return MorytaniaLegsMode.WEAR;
	}

	@ConfigItem(
		keyName = "swapArdougneCloakLeftClick",
		name = "Ardougne Cloak",
		description = "Change the left-click option on Ardougne Cloak"
	)
	default ArdougneCloakMode swapArdougneCloakLeftClick()
	{
		return ArdougneCloakMode.WEAR;
	}

	@ConfigItem(
		keyName = "swapTraderCrewmemberLeftClick",
		name = "Charter Ships",
		description = "Change the left-click option on Trader Crewmembers on Charter ships"
	)
	default CharterShipsMode swapTraderCrewmemberLeftClick()
	{
		return CharterShipsMode.LAST_DESTINATION;
	}
}
