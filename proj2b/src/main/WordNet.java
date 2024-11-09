package main;

import edu.princeton.cs.algs4.In;

import java.util.*;
import java.util.stream.Collectors;

public class WordNet {
    private Map<Integer, List<String>> synsetMap;
    private Map<String, Set<Integer>> nounToSynsetIDs;
    private Graph hyponymGraph;
    private TreeMap<String, TreeMap<Integer, Double>> wordFrequencies;

    public WordNet(String synsetFilename, String hyponymFilename, String frequencyFilename) {
        synsetMap = new HashMap<>();
        nounToSynsetIDs = new HashMap<>();
        hyponymGraph = new Graph();
        wordFrequencies = new TreeMap<>();

        loadSynsets(synsetFilename);
        loadHyponyms(hyponymFilename);
        loadFrequencies(frequencyFilename);
    }
    /** Loads synsets data from SynsetParser and populates synsetMap and nounToSynsetIDs. */
    private void loadSynsets(String filename) {
        In in = new In(filename);
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] fields = line.split(",", 3);
            int synsetID = Integer.parseInt(fields[0]);
            List<String> nouns = Arrays.asList(fields[1].split(" "));

            for (String noun : nouns) {
                nounToSynsetIDs.putIfAbsent(noun, new HashSet<>());
                nounToSynsetIDs.get(noun).add(synsetID);
            }

            synsetMap.put(synsetID, nouns);
            hyponymGraph.createNode(synsetID);
        }
    }

    /** Loads hyponym data from HyponymParser and populates the hyponymGraph. */
    private void loadHyponyms(String filename) {
        In in = new In(filename);
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] ids = line.split(",");

            int hypernymId = Integer.parseInt(ids[0]);
            hyponymGraph.createNode(hypernymId);

            for (int i = 1; i < ids.length; i++) {
                int hyponymId = Integer.parseInt(ids[i]);
                hyponymGraph.createNode(hyponymId);
                hyponymGraph.addEdge(hypernymId, hyponymId);
            }
        }
    }

    public void loadFrequencies(String filename) {
        In in = new In(filename);
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] parts = line.split("\t");
            String word = parts[0];
            int year = Integer.parseInt(parts[1]);
            double occurrences = Double.parseDouble(parts[2]);

            if (word == null) {
                continue;
            }

            wordFrequencies.putIfAbsent(word, new TreeMap<>()); // Map API
            wordFrequencies.get(word).put(year, occurrences);
        }
    }

    /** Retrieves all hyponyms of the given word using graph traversal. */
    public Set<String> getHyponyms(String word) {
        Set<String> result = new HashSet<>();
        if (!nounToSynsetIDs.containsKey(word)) {
            return result;
        }

        Set<Integer> synsetIDs = nounToSynsetIDs.get(word);
        Set<Integer> reachableIDs = new HashSet<>();

        for (int synsetID : synsetIDs) {
            reachableIDs.addAll(findReachable(synsetID));
        }
        for (int id : reachableIDs) {
            result.addAll(synsetMap.get(id));
        }
        return result;
    }

    public Set<String> getCombinedHyponyms(List<String> words) {
        if (words.isEmpty()) {
            return Collections.emptySet();
        }
        Set<String> combinedHyponyms = new HashSet<>(getHyponyms(words.get(0)));
        for (int i = 1; i < words.size(); i++) {
            combinedHyponyms.retainAll(getHyponyms(words.get(i)));
        }
        return combinedHyponyms;
    }

    /** Finds all reachable nodes (hyponyms) from a given synset ID. */
    private Set<Integer> findReachable(int synsetID) {
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();
        queue.add(synsetID);

        while (!queue.isEmpty()) {
            int current = queue.poll();
            if (!visited.contains(current)) {
                visited.add(current);
                Set<Integer> neighbors = hyponymGraph.getNeighbors(current);
                if (neighbors != null) {
                    for (int neighbor : neighbors) {
                        if (!visited.contains(neighbor)) {
                            queue.add(neighbor);
                        }
                    }
                }
            }
        }
        return visited;
    }

    private double calculatePopularity(String word, int startYear, int endYear) {
        double totalPopularity = 0.0;
        TreeMap<Integer, Double> yearlyCounts = wordFrequencies.get(word);
        if (yearlyCounts == null) {
            return 0;
        }
        for (int year = startYear; year <= endYear; year++) {
            totalPopularity += yearlyCounts.getOrDefault(year, 0.0);
        }
        return totalPopularity;
    }

    public List<String> retrieveTopKHyponyms(List<String> word, int startYear, int endYear, int k) {
        // Create a list to store hyponyms with their popularity scores
        List<String> result = new ArrayList<>();

        // Map to store each hyponym and its popularity score
        Map<String, Double> popularityMap = new HashMap<>();

        // Populate popularityMap with the popularity scores
        for (String hyponym : word) {
            if (hyponym != null) {  // Ensure the hyponym has a valid ID
                double popularity = calculatePopularity(hyponym, startYear, endYear);
                popularityMap.put(hyponym, popularity);
            }
        }

        // Sort hyponyms by popularity in descending order
        return popularityMap.entrySet().stream()
                .filter(e -> e.getValue() > 0) // Exclude hyponyms with zero popularity
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue())) // Sort by descending popularity
                .limit(k) // Take top k elements
                .map(Map.Entry::getKey) // Get only hyponym names
                .collect(Collectors.toList());
    }
}
