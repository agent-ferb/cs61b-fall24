package main;

import browser.NgordnetQueryHandler;
import ngrams.NGramMap;


public class AutograderBuddy {
    /**
     * Returns a HyponymHandler
     */
    private static final String WORDS_FILE = "data/ngrams/top_49887_words.csv";

    public static NgordnetQueryHandler getHyponymsHandler(String[] args) {
        String synsetsFile = Main.SMALL_SYNSET_FILE;
        String hyponymsFile = Main.SMALL_HYPONYM_FILE;
        String wordsFile = WORDS_FILE;
        String countsFile = Main.TOTAL_COUNTS_FILE;

        NGramMap ngm = new NGramMap(wordsFile, countsFile);
        WordNet wn = new WordNet(synsetsFile, hyponymsFile, wordsFile);
        return new HyponymsHandler(wn);
    }
}
