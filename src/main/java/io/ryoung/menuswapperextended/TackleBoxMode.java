package io.ryoung.menuswapperextended;

import java.util.function.Predicate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TackleBoxMode implements SwapMode
{
 	VIEW("View"),
	EMPTY("Empty");

	private final String option;

	@Override
	public String toString()
	{
		return option;
	}

	@Override
	public Predicate<String> checkTarget()
	{
		return target -> target.startsWith("tackle box");
	}

}
