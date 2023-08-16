package yating_asr.asr;

import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Sentence {
    String sentence;
    Double confidence;
    Integer start;
    Integer end;
    List<Word> wordList = new ArrayList<Word>();

    public Sentence(JSONObject jsonObject) {
        sentence = (String) jsonObject.get("asr_sentence");
        confidence = (Double) jsonObject.get("asr_confidence");
        Double tempStart = ((Double) jsonObject.get("asr_begin_time")) * 1000;
        start = tempStart.intValue();
        Double tempEnd = ((Double) jsonObject.get("asr_end_time")) * 1000;
        end = tempEnd.intValue();

        // add words
        JSONArray words = (JSONArray) jsonObject.get("asr_word_time_stamp");
        Iterator<?> iterator = words.iterator();
        while (iterator.hasNext()) {
            wordList.add(new Word((JSONObject) iterator.next()));
        }
    }

    public Integer getStart() {
        return start;
    }

    public Integer getEnd() {
        return end;
    }

    public String getSentence() {
        return sentence;
    }

    public List<Word> getWordList() {
        return wordList;
    }

    public String getSentenceByWords() {
        String sentence = "";

        Iterator<Word> iterator = wordList.iterator();
        while (iterator.hasNext()) {
            sentence += iterator.next().getWord();
        }

        return sentence;
    }
}
