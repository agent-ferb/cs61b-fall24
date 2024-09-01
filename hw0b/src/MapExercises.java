import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MapExercises {
    /** Returns a map from every lower case letter to the number corresponding to that letter, where 'a' is
     * 1, 'b' is 2, 'c' is 3, ..., 'z' is 26.
     */
    public static Map<Character, Integer> letterToNum() {
        // TODO: Fill in this function.
        Map<Character, Integer> ltn= new TreeMap<>();
        int i=1;

        for(char ch='a'; ch<='z'; ch++) {
            ltn.put(ch, i);
            i++;
        }
        return ltn;
    }



    /** Returns a map from the integers in the list to their squares. For example, if the input list
     *  is [1, 3, 6, 7], the returned map goes from 1 to 1, 3 to 9, 6 to 36, and 7 to 49.
     */
    public static Map<Integer, Integer> squares(List<Integer> nums) {
        // TODO: Fill in this function.
        Map<Integer, Integer> squa= new TreeMap<>();
        for(int i=0; i<nums.size(); i++) {
            int j= nums.get(i);
            squa.put(j, j*j);
        }
        return squa;
    }

    /** Returns a map of the counts of all words that appear in a list of words. */
    public static Map<String, Integer> countWords(List<String> words) {
        // TODO: Fill in this function.
        Map<String, Integer> cw = new TreeMap<>();

        for (int i = 0; i < words.size(); i++) {
            String s = words.get(i);
            if (!cw.containsKey(s)) {
                int count = 0;  // Reset count for each new word
                for (int j = 0; j < words.size(); j++) {
                    if (s.equals(words.get(j))) {
                        count++;
                    }
                }
                cw.put(s, count);  // Update the map after counting
            }
        }
        return cw;
    }
}
