package com.demo.base.aspect.mask;

import com.demo.base.annotation.mask.Masker;
import lombok.experimental.UtilityClass;

/**
 * imitate from mybatis-flex
 */
@UtilityClass
@SuppressWarnings("unused")
public class Preset {
    public static final String MOBILE = "mobile";
    public static final String FIXED_PHONE = "fixed_phone";
    public static final String ID_CARD_NUMBER = "id_card_number";
    public static final String CHINESE_NAME = "chinese_name";
    public static final String ADDRESS = "address";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String CAR_LICENSE = "car_license";
    public static final String BANK_CARD_NUMBER = "bank_card_number";
    public static final Masker MOBILE_PROCESSOR = (data) -> data instanceof String && ((String) data).startsWith("1") && ((String) data).length() == 11 ? mask((String) data, 3, 4, 4) : data;
    public static final Masker FIXED_PHONE_PROCESSOR = (data) -> data instanceof String && ((String) data).length() > 5 ? mask((String) data, 3, 2, ((String) data).length() - 5) : data;
    public static final Masker ID_CARD_NUMBER_PROCESSOR = (data) -> data instanceof String && ((String) data).length() >= 15 ? mask((String) data, 3, 4, ((String) data).length() - 7) : data;
    public static final Masker CHINESE_NAME_PROCESSOR = (data) -> {
        if (data instanceof String name) {
            if (name.length() == 2) {
                return name.charAt(0) + "*";
            }

            if (name.length() == 3) {
                return name.charAt(0) + "*" + name.charAt(2);
            }

            if (name.length() == 4) {
                return "**" + name.substring(2, 4);
            }

            if (name.length() > 4) {
                return mask(name, 2, 1, name.length() - 3);
            }
        }

        return data;
    };
    public static final Masker ADDRESS_PROCESSOR = (data) -> {
        if (data instanceof String address) {
            if (address.length() > 6) {
                return mask(address, 6, 0, 3);
            }

            if (address.length() > 3) {
                return mask(address, 3, 0, 3);
            }
        }

        return data;
    };
    public static final Masker EMAIL_PROCESSOR = (data) -> {
        if (data instanceof String fullEmail && fullEmail.contains("@")) {
            int indexOf = fullEmail.lastIndexOf("@");
            String email = fullEmail.substring(0, indexOf);
            if (email.length() == 1) {
                return "*" + fullEmail.substring(indexOf);
            } else if (email.length() == 2) {
                return "**" + fullEmail.substring(indexOf);
            } else {
                return email.length() < 5 ? mask(email, 2, 0, email.length() - 2) + fullEmail.substring(indexOf) : mask(email, 3, 0, email.length() - 3) + fullEmail.substring(indexOf);
            }
        } else {
            return data;
        }
    };
    public static final Masker PASSWORD_PROCESSOR = (data) -> data instanceof String ? mask((String) data, 0, 0, ((String) data).length()) : data;
    public static final Masker CAR_LICENSE_PROCESSOR = (data) -> data instanceof String ? mask((String) data, 3, 1, ((String) data).length() - 4) : data;
    public static final Masker BANK_CARD_PROCESSOR = (data) -> data instanceof String && ((String) data).length() >= 8 ? mask((String) data, 4, 4, 4) : data;

    private static String createMask(int count) {
        return "*".repeat(Math.max(0, count));
    }

    private static String mask(String needToMaskString, int keepFirstCount, int keepLastCount, int maskCount) {
        return needToMaskString.substring(0, keepFirstCount) + createMask(maskCount) + needToMaskString.substring(needToMaskString.length() - keepLastCount);
    }
}
