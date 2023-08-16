package yating_asr.asr;

import org.json.simple.JSONObject;

public class Word {
    String word;
    String punctuator;
    Integer start;
    Integer end;

    public Word(JSONObject jsonObject) {
        word = (String) jsonObject.get("word");
        punctuator = (String) jsonObject.get("punctuator");
        Double tempStart = ((Double) jsonObject.get("begin_time")) * 1000;
        start = tempStart.intValue();
        Double tempEnd = ((Double) jsonObject.get("end_time")) * 1000;
        end = tempEnd.intValue();
    }

    public String getWord() {
        return word;
    }
}
