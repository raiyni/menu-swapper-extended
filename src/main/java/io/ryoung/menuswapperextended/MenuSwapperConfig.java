package io.ryoung.menuswapperextended;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("menuswapperextended")
public interface MenuSwapperConfig extends Config
{
	@ConfigItem(
		keyName = "swapStory",
		name = "Story",
		description = "Swap Talk-to with Story for Juna at Tears of Guthix"
	)
	default boolean swapStory()
	{
		return true;
	}
}
