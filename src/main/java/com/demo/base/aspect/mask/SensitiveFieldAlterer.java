package com.demo.base.aspect.mask;

import com.demo.base.annotation.mask.Masker;
import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class SensitiveFieldAlterer {
    private static final Map<String, Masker> processorMap = new HashMap<>();

    static {
        registerMaskProcessor("mobile", Preset.MOBILE_PROCESSOR);
        registerMaskProcessor("fixed_phone", Preset.FIXED_PHONE_PROCESSOR);
        registerMaskProcessor("id_card_number", Preset.ID_CARD_NUMBER_PROCESSOR);
        registerMaskProcessor("chinese_name", Preset.CHINESE_NAME_PROCESSOR);
        registerMaskProcessor("address", Preset.ADDRESS_PROCESSOR);
        registerMaskProcessor("email", Preset.EMAIL_PROCESSOR);
        registerMaskProcessor("password", Preset.PASSWORD_PROCESSOR);
        registerMaskProcessor("car_license", Preset.CAR_LICENSE_PROCESSOR);
        registerMaskProcessor("bank_card_number", Preset.BANK_CARD_PROCESSOR);
    }

    public static void registerMaskProcessor(String type, Masker processor) {
        processorMap.put(type, processor);
    }

    public static Object mask(String type, Object data) {
        Masker maskProcessor = processorMap.get(type);
        if (maskProcessor == null)
            throw new IllegalStateException("Can not get mask processor for by type: " + type);
        else
            return maskProcessor.mask(data);
    }

    @SuppressWarnings("unused")
    public static Map<String, Masker> getProcessorMap() {
        return Collections.unmodifiableMap(processorMap);
    }

}