package yating_asr;

import java.util.Arrays;
import java.util.List;

public class Pipeline {
    public static final String General = "asr-stream-general";

    static List<String> getList() {
        String[] valueList = new String[] { General };
        return Arrays.asList(valueList);
    }

    public static boolean validate(String value) {
        List<String> list = getList();
        return list.contains(value);
    }
}
