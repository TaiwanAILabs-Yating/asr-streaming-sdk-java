package yating_asr.asr;

import java.util.ArrayList;
import java.util.List;

public class AsrResult {
    List<Sentence> sentenceList = new ArrayList<Sentence>();

    public void addSentence(Sentence sentence) {
        sentenceList.add(sentence);
        System.out.println(
                "[" + sentence.getStart() + ":" + sentence.getEnd() + "] " +
                        sentence.getSentence());
    }

    public List<Sentence> getSentences() {
        return sentenceList;
    }
}
