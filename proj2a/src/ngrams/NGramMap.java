package ngrams;

import java.util.Collection;
import edu.princeton.cs.algs4.In;

import java.util.TreeMap;

import static ngrams.TimeSeries.MAX_YEAR;
import static ngrams.TimeSeries.MIN_YEAR;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 *
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {

    // TODO: Add any necessary static/instance variables.
    private TreeMap<String, TimeSeries> wordData;
    private TimeSeries totalCounts;

    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {
        // TODO: Fill in this constructor. See the "NGramMap Tips" section of the spec for help.
        wordData = new TreeMap<>();
        totalCounts = new TimeSeries();
        readWordsFile(wordsFilename);
        readCountsFile(countsFilename);
    }

        /**
         * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
         * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
         * words, changes made to the object returned by this function should not also affect the
         * NGramMap. This is also known as a "defensive copy". If the word is not in the data files,
         * returns an empty TimeSeries.
         */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        // TODO: Fill in this method.
        if (!wordData.containsKey(word)) {
            return new TimeSeries();
        }
        TimeSeries originalTS = wordData.get(word);
        return new TimeSeries(originalTS, startYear, endYear);
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy, not a link to this
     * NGramMap's TimeSeries. In other words, changes made to the object returned by this function
     * should not also affect the NGramMap. This is also known as a "defensive copy". If the word
     * is not in the data files, returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word) {
        // TODO: Fill in this method.
        if (!wordData.containsKey(word)) {
            return new TimeSeries();
        }
        TimeSeries originalTS = wordData.get(word);
        return new TimeSeries(originalTS, MIN_YEAR, MAX_YEAR);
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        // TODO: Fill in this method.
        return new TimeSeries(totalCounts, TimeSeries.MIN_YEAR, TimeSeries.MAX_YEAR);
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        // TODO: Fill in this method.
        if (!wordData.containsKey(word)) {
            return new TimeSeries();
        }
        TimeSeries wordOccurrences = wordData.get(word);
        TimeSeries relativeFrequency = new TimeSeries();

        for (int year = startYear; year <= endYear; year++) {
            Double occurrences = wordOccurrences.get(year);
            Double totalWords = totalCounts.get(year);

            if (totalWords != null && totalWords > 0) {
                double frequency = occurrences != null ? occurrences / totalWords : 0.0;
                relativeFrequency.put(year, frequency);
            }
        }
        return relativeFrequency;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to all
     * words recorded in that year. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        // TODO: Fill in this method.
        if (!wordData.containsKey(word)) {
            return new TimeSeries();
        }
        TimeSeries wordCountHistory = wordData.get(word);
        TimeSeries relativeFrequency = new TimeSeries();

        for (Integer year : wordCountHistory.keySet()) {
            if (totalCounts.containsKey(year)) {
                double wordCount = wordCountHistory.get(year);
                double totalCount = totalCounts.get(year);
                double frequency = wordCount / totalCount;
                relativeFrequency.put(year, frequency);
            }
        }
        return relativeFrequency;
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS between STARTYEAR and
     * ENDYEAR, inclusive of both ends. If a word does not exist in this time frame, ignore it
     * rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words,
                                          int startYear, int endYear) {
        // TODO: Fill in this method.
        TimeSeries summedHistory = new TimeSeries();
        for (String word : words) {
            TimeSeries wordWeightHistory = weightHistory(word, startYear, endYear);

            for (Integer year : wordWeightHistory.keySet()) {
                double currentSum = summedHistory.getOrDefault(year, 0.0);
                double newSum = currentSum + wordWeightHistory.get(year);
                summedHistory.put(year, newSum);
            }
        }
        return summedHistory;
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS. If a word does not
     * exist in this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        // TODO: Fill in this method.
        TimeSeries summedHistory = new TimeSeries();
        for (String word : words) {
            TimeSeries wordWeightHistory = weightHistory(word);

            for (Integer year : wordWeightHistory.keySet()) {
                double currentSum = summedHistory.getOrDefault(year, 0.0);
                double newSum = currentSum + wordWeightHistory.get(year);
                summedHistory.put(year, newSum);
            }
        }
        return summedHistory;
    }

    // TODO: Add any private helper methods.
    private void readWordsFile(String wordsFilename) { //FileReadDemo
        In in = new In(wordsFilename);
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] parts = line.split("\t");
            String word = parts[0];
            int year = Integer.parseInt(parts[1]); // Ed #1199a
            double occurrences = Double.parseDouble(parts[2]);

            wordData.putIfAbsent(word, new TimeSeries()); // Map API
            wordData.get(word).put(year, occurrences);
        }
    }
    private void readCountsFile(String countsFilename) {
        In in = new In(countsFilename);
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] parts = line.split(",");
            int year = Integer.parseInt(parts[0]);
            double totalWords = Double.parseDouble(parts[1]);

            totalCounts.put(year, totalWords);
        }
    }
}
