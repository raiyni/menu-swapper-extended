package io.ryoung.menuswapperextended;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.MenuAction;
import net.runelite.client.config.Keybind;
import net.runelite.client.plugins.menuentryswapper.ShiftDepositMode;
import net.runelite.client.plugins.menuentryswapper.ShiftWithdrawMode;

import java.util.function.Function;

@Getter
@RequiredArgsConstructor
enum BankSwapMode {
    OFF(ShiftDepositMode.OFF, ShiftWithdrawMode.OFF, config -> Keybind.NOT_SET),
    SWAP_1(ShiftDepositMode.DEPOSIT_1, ShiftWithdrawMode.WITHDRAW_1, config -> config.getBankSwap1Hotkey()),
    SWAP_5(ShiftDepositMode.DEPOSIT_5, ShiftWithdrawMode.WITHDRAW_5, config -> config.getBankSwap5Hotkey()),
    SWAP_10(ShiftDepositMode.DEPOSIT_10, ShiftWithdrawMode.WITHDRAW_10, config -> config.getBankSwap10Hotkey()),
    SWAP_X(ShiftDepositMode.DEPOSIT_X, ShiftWithdrawMode.WITHDRAW_X, config -> config.getBankSwapXHotkey()),
    SWAP_SET_X(7, 6, 5, MenuAction.CC_OP_LOW_PRIORITY, 6, 5, config -> config.getBankSwapSetXHotkey()),
    SWAP_ALL(ShiftDepositMode.DEPOSIT_ALL, ShiftWithdrawMode.WITHDRAW_ALL, config -> config.getBankSwapAllHotkey()),
    SWAP_ALL_BUT_1(-1, -1, -1, ShiftWithdrawMode.WITHDRAW_ALL_BUT_1.getMenuAction(), ShiftWithdrawMode.WITHDRAW_ALL_BUT_1.getIdentifier(), ShiftWithdrawMode.WITHDRAW_ALL_BUT_1.getIdentifierChambersStorageUnit(), config -> config.getBankSwapAllBut1Hotkey()),
    SWAP_EXTRA_OP(ShiftDepositMode.EXTRA_OP.getIdentifier(), ShiftDepositMode.EXTRA_OP.getIdentifierDepositBox(), ShiftDepositMode.EXTRA_OP.getIdentifierChambersStorageUnit(), null, -1, -1, config -> config.getBankSwapExtraOpHotkey()),
    ;

    private final int depositIdentifier;
    private final int depositIdentifierDepositBox;
    private final int depositIdentifierChambersStorageUnit;

    private final MenuAction withdrawMenuAction;
    private final int withdrawIdentifier;
    private final int withdrawIdentifierChambersStorageUnit;

    private final Function<MenuSwapperConfig, Keybind> keybindFunction;

    BankSwapMode(ShiftDepositMode depositMode, ShiftWithdrawMode withdrawMode, Function<MenuSwapperConfig, Keybind> keybindFunction) {
        depositIdentifier = depositMode.getIdentifier();
        depositIdentifierDepositBox = depositMode.getIdentifierDepositBox();
        depositIdentifierChambersStorageUnit = depositMode.getIdentifierChambersStorageUnit();
        withdrawMenuAction = withdrawMode.getMenuAction();
        withdrawIdentifier = withdrawMode.getIdentifier();
        withdrawIdentifierChambersStorageUnit = withdrawMode.getIdentifierChambersStorageUnit();
        this.keybindFunction = keybindFunction;
    }

    public Keybind getKeybind(MenuSwapperConfig config) {
        return keybindFunction.apply(config);
    }
}
