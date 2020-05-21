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
		keyName = "swapWildernessLever",
		name = "Wilderness Lever to Edgeville",
		description = "Swap Edgeville Lever as default for wilderness lever"
	)
	default boolean swapWildernessLever()
	{
		return true;
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
		return true;
	}
	
	@ConfigItem(
			keyName = "swapCraftCape",
			name = "Crafting Cape",
			description = "Change the left-click option on the Crafting Cape"
	)
	default boolean swapCraftCape()
	{
		return true;
	}
	
	@ConfigItem(
			keyName = "swapWestBanner",
			name = "Western Banner",
			description = "Change the left-click option on the Western Banner"
	)
	default boolean swapWestBanner()
	{
		return true;
	}
	@ConfigItem(
			keyName = "swapDesertAmmy",
			name = "Desert Amulet",
			description = "Change the left-click option on the Desert Amulet"
	)
	default boolean swapDesertAmmy()
	{
		return true;
	}
	@ConfigItem(
			keyName = "swapQPC",
			name = "Quest Point Cape",
			description = "Change the left-click option on the QPC"
	)
	default boolean swapWestBanner()
	{
		return true;
	}
}
