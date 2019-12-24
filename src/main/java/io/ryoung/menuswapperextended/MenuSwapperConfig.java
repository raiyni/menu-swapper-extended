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
}
