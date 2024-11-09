package main;

import edu.princeton.cs.algs4.In;

import java.util.*;

public class WordNet {
    private Map<Integer, List<String>> synsetMap;
    private Map<String, Set<Integer>> nounToSynsetIDs;
    private Map<String, Integer> nounToIDs;
    private Graph hyponymGraph;
    private TreeMap<Integer, TreeMap<Integer, Double>> wordFrequencies;

    public WordNet(String synsetFilename, String hyponymFilename, String frequencyFilename) {
        synsetMap = new HashMap<>();
        nounToSynsetIDs = new HashMap<>();
        nounToIDs = new HashMap<>();
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
                nounToIDs.putIfAbsent(noun, synsetID);
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

            int wordID = nounToIDs.getOrDefault(word, -1);
            if (wordID == -1) {
                continue;
            }

            wordFrequencies.putIfAbsent(wordID, new TreeMap<>()); // Map API
            wordFrequencies.get(wordID).put(year, occurrences);
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

    private double calculatePopularity(int hyponymID, int startYear, int endYear) {
        double totalPopularity = 0.0;
        TreeMap<Integer, Double> yearlyCounts = wordFrequencies.get(hyponymID);
        if (yearlyCounts == null) {
            return 0;
        }
        for (int year = startYear; year <= endYear; year++) {
            totalPopularity += yearlyCounts.getOrDefault(year, 0.0);
        }
        return totalPopularity;
    }

    public List<String> retrieveTopKHyponyms(List<String> word, int startYear, int endYear, int k) {
        // Define a priority queue with a custom comparator for popularity
        PriorityQueue<String> topKHyponyms = new PriorityQueue<>(k, Comparator.comparingDouble(
                hyponym -> {
                    int hyponymID = nounToIDs.getOrDefault(hyponym, -1);
                    return (calculatePopularity(hyponymID, startYear, endYear));
                }
        ));
        // Iterate over each hyponym and keep the top k
        for (String hyponym : word) {
            int hyponymID = nounToIDs.getOrDefault(hyponym, -1);
            if (hyponymID != -1) {  // Ensure the hyponym has a valid ID
                topKHyponyms.offer(hyponym);
                if (topKHyponyms.size() > k) {
                    topKHyponyms.poll();
                }
            }
        }
        List<String> result = new ArrayList<>(topKHyponyms);
        Collections.sort(result);
        return result;
    }
}
