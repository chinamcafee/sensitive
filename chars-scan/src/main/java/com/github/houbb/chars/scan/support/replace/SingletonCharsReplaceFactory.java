package com.github.houbb.chars.scan.support.replace;

import com.github.houbb.chars.scan.api.ICharsReplace;

/**
 * 替换策略工厂类
 */
public final class SingletonCharsReplaceFactory {

    private static final ICharsReplace NONE = new NoneCharsReplace();
    private static final ICharsReplace MASK_ALL = new MaskAllCharsReplace();
    private static final ICharsReplace MASK_HALF = new MaskHalfCharsReplace();
    private static final ICharsReplace PHONE = new PhoneCharsReplace();
    private static final ICharsReplace CHINESE_NAME = new ChineseNameCharsReplace();
    private static final ICharsReplace ID_NO = new IdNoCharsReplace();
    private static final ICharsReplace EMAIL = new EmailCharsReplace();
    private static final ICharsReplace BANK_CARD = new BankCardCharsReplace();
    private static final ICharsReplace BIRTHDAY = new BirthdayCharsReplace();
    private static final ICharsReplace GPS = new GPSCharsReplace();
    private static final ICharsReplace IPV4 = new IPV4CharsReplace();
    private static final ICharsReplace ADDRESS = new AddressCharsReplace();

    /**
     * @since 1.12.0
     */
    private static final ICharsReplace PASSPORT = new PassportCharsReplace();

    public static ICharsReplace getAddress() {
        return ADDRESS;
    }

    public static ICharsReplace getGps() {
        return GPS;
    }

    public static ICharsReplace getBirthday() {
        return BIRTHDAY;
    }

    public static ICharsReplace getEmail() {
        return EMAIL;
    }

    public static ICharsReplace getBankCard() {
        return BANK_CARD;
    }

    public static ICharsReplace getChineseName() {
        return CHINESE_NAME;
    }

    public static ICharsReplace getIdNo() {
        return ID_NO;
    }

    public static ICharsReplace getPhone() {
        return PHONE;
    }

    public static ICharsReplace getMaskAll() {
        return MASK_ALL;
    }

    public static ICharsReplace getMaskHalf() {
        return MASK_HALF;
    }

    public static ICharsReplace getNone() {
        return NONE;
    }

    public static ICharsReplace getIpv4() {
        return IPV4;
    }

    public static ICharsReplace getPassport() {
        return PASSPORT;
    }

}
