package yating_asr.constants;

import java.util.Arrays;
import java.util.List;

public class Pipeline {
    public static final String General = "asr-stream-general";
    public static final String File = "asr-file-std";

    static List<String> getList() {
        String[] valueList = new String[] { General };
        return Arrays.asList(valueList);
    }

    public static boolean validate(String value) {
        List<String> list = getList();
        return list.contains(value);
    }
}
