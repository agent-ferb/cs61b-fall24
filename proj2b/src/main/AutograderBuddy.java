package main;

import browser.NgordnetQueryHandler;


public class AutograderBuddy {
    /**
     * Returns a HyponymHandler
     */
    private static final String WORDS_FILE = "data/ngrams/top_49887_words.csv";

    public static NgordnetQueryHandler getHyponymsHandler() {
        String synsetsFile = Main.SMALL_SYNSET_FILE;
        String hyponymsFile = Main.SMALL_HYPONYM_FILE;
        String wordsFile = WORDS_FILE; // Main.java uses this as the words file

        WordNet wn = new WordNet(synsetsFile, hyponymsFile, wordsFile);
        return new HyponymsHandler(wn);
    }
}
