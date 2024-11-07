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
            combinedHyponyms.add("[");
            combinedHyponyms.addAll(wordNet.getHyponyms(word));
            combinedHyponyms.add("]");
            return combinedHyponyms.toString();
        } else {
            // Multi-word case
            combinedHyponyms.addAll(wordNet.getCombinedHyponyms(words));
            Collections.sort(combinedHyponyms);
            List<String> ultiHyponyms = new ArrayList<>();
            ultiHyponyms.add("[");
            for (String w : combinedHyponyms) {
                ultiHyponyms.add(w);
                ultiHyponyms.add(", ");
            }
            ultiHyponyms.remove(", ");
            ultiHyponyms.add("]");
            return ultiHyponyms.toString();
        }

        if (k > 0) {
            List<String> topKHyponyms = new ArrayList<>();
            for (String word : combinedHyponyms) {
                List<String> topHyponyms = wordNet.retrieveTopKHyponyms(word, startYear, endYear, k);
                topKHyponyms.addAll(topHyponyms);
            }
            Collections.sort(topKHyponyms);
            return topKHyponyms.toString();
        }
    }
}
