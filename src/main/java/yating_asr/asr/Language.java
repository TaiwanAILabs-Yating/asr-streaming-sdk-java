package yating_asr.asr;

import java.util.Arrays;
import java.util.List;

public class Language {
    public static final String ZHEN = "zhen";
    public static final String ZHEN_OFFLINE = "zhen_offline";
    public static final String ZHTW = "zhtw";
    public static final String EN = "en";

    static List<String> getList() {
        String[] valueList = new String[] { ZHEN, ZHEN_OFFLINE, ZHTW, EN };
        return Arrays.asList(valueList);
    }

    public static boolean validate(String value) {
        List<String> list = getList();
        return list.contains(value);
    }
}
