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
		return true;
	}

	@ConfigItem(
		keyName = "swapSearch",
		name = "Search",
		description = "Swap Close, Shut with Search on chests, cupboards, etc."
	)
	default boolean swapSearch()
	{
		return true;
	}

	@ConfigItem(
		keyName = "swapBuyPlank",
		name = "Buy-Plank",
		description = "Swap Talk-to with Buy-Plank on Sawmill Operator"
	)
	default boolean swapPlank()
	{
		return true;
	}

	@ConfigItem(
		keyName = "swapStun",
		name = "Stun Hoop Snakes",
		description = "Swap Attack with Stun"
	)
	default boolean swapStun()
	{
		return true;
	}

	@ConfigItem(
		keyName = "claimDynamite",
		name = "Claim Dynamite",
		description = "Swap Talk-to with Claim Dynamite on Thirus"
	)
	default boolean claimDynamite()
	{
		return true;
	}

	@ConfigItem(
		keyName = "swapWildernessLever",
		name = "Wilderness Lever to Edgeville",
		description = "Swap Edgeville Lever as default for wilderness lever"
	)
	default boolean swapWildernessLever()
	{
		return true;
	}

	@ConfigItem(
		position = 19,
		keyName = "swapSendParcel",
		name = "Send Parcel",
		description = "Swap Talk-To to with Send-Parcel for Rionasta at the Tai Bwo Wannai village."
	)
	default boolean swapSendParcel()
	{
		return false;
	}

	@ConfigItem(
		position = 19,
		keyName = "swapTeleportToDestination",
		name = "Obelisk",
		description = "Swap Activate with Teleport to Destination or Set Destination"
	)
	default ObeliskMode swapTeleportToDestination()
	{
		return ObeliskMode.ACTIVATE;
	}

	@ConfigItem(
		position = 19,
		keyName = "swapZulrahCollect",
		name = "Collect (Zulrah)",
		description = "Swap Collect with Talk-to for Priestess Zul-Gwenwynig at Zul-Andra."
	)
	default boolean swapZulrahCollect()
	{
		return true;
	}

	@ConfigItem(
		position = 20,
		keyName = "decant",
		name = "Decant",
		description = "Decant for e.g. Bob Barter"
	)
	default boolean swapDecant()
	{
		return true;
	}

	@ConfigItem(
		position = 21,
		keyName = "zahurOption",
		name = "Zahur",
		description = "Zahur default option"
	)
	default ZahurMode swapZahur()
	{
		return ZahurMode.MAKE_POTION;
	}

	@ConfigItem(
		position = 19,
		keyName = "dagganothKingsLadder",
		name = "Slayer",
		description = "Change the default option to slayer in the dagganothkings lair."
	)
	default boolean swapSlayer()
	{
		return true;
	}

	@ConfigItem(
		keyName = "swapStore",
		name = "Builders Store",
		description = "Swap Trade-General-Store with Trade-Builders-Store for the NPC Razmire Keelgan"
	)
	default boolean swapStore()
	{
		return true;
	}
}
