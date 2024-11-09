package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HyponymsHandler extends NgordnetQueryHandler {

    private WordNet wordNet;

    public HyponymsHandler(WordNet wordNet) {
        this.wordNet = wordNet;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();
        int k = q.k();

        List<String> combinedHyponyms = new ArrayList<>();

        if (words.size() == 1) {
            // Single-word case
            String word = words.get(0);
            combinedHyponyms.addAll(wordNet.getHyponyms(word));

        } else {
            // Multi-word case
            combinedHyponyms.addAll(wordNet.getCombinedHyponyms(words));
        }

        if (k > 0) {
            List<String> topKHyponyms = new ArrayList<>();
            topKHyponyms.addAll(wordNet.retrieveTopKHyponyms(combinedHyponyms, startYear, endYear, k));
            Collections.sort(topKHyponyms);
            return topKHyponyms.toString();
        }
        Collections.sort(combinedHyponyms);
        return combinedHyponyms.toString();
    }
}
