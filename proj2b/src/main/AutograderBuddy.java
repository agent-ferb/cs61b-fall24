package main;

import browser.NgordnetQueryHandler;
import ngrams.NGramMap;


public class AutograderBuddy {
    /**
     * Returns a HyponymHandler
     */
    public static NgordnetQueryHandler getHyponymsHandler(String wordsFile, String countsFile, String synsetsFile, String hyponymsFile) {
        WordNet wn = new WordNet(synsetsFile, hyponymsFile, wordsFile);
        return new HyponymsHandler(wn);
    }
}
