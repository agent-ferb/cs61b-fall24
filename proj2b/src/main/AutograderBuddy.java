package main;

import browser.NgordnetQueryHandler;
import ngrams.NGramMap;


public class AutograderBuddy {
    /**
     * Returns a HyponymHandler
     */
    public static NgordnetQueryHandler getHyponymsHandler(String synsetsFile, String hyponymsFile, String wordsFile, String countsFile) {
        WordNet wn = new WordNet(synsetsFile, hyponymsFile, wordsFile);
        return new HyponymsHandler(wn);
    }
}
