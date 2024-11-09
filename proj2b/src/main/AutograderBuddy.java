package main;

import browser.NgordnetQueryHandler;
import ngrams.NGramMap;


public class AutograderBuddy {
    /**
     * Returns a HyponymHandler
     */
    public static NgordnetQueryHandler getHyponymsHandler(String synsetsFile, String countsFile, String hyponymsFile, String wordsFile) {
        WordNet wn = new WordNet(synsetsFile, hyponymsFile, wordsFile);
        return new HyponymsHandler(wn);
    }
}
