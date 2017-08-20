import java.util.*;

/**
 * Prefix-Trie. Supports linear time find() and insert().
 * Should support determining whether a word is a full word in the
 * Trie or a prefix.
 *
 * @author
 */
public class AugTrie {

    private TrieNode root;
    String[] words;
    double[] values;
    Set<String> set;

    public AugTrie() {
    }

    public AugTrie(String[] w, double[] val) {
        words = w;
        values = val;
        set = new TreeSet<>();
    }

    public String[] getWords() {
        return words;
    }

    public double[] getValues() {
        return values;
    }

    /**
     * @param s
     * @return
     */
    public TrieNode getNode(String s) {
        TrieNode curr = root;

        for (int i = 0; i < s.length(); i++) {
            curr = curr.getChar(s.charAt(i));
        }
        return curr;
    }


    /**
     * Builds the trie based on the array
     */
    public void buildTrie() {
        String[] wd = getWords();
        double[] val = getValues();

        for (int i = 0; i < wd.length; i++) { // for every word and value: put it in the trie
            insert(wd[i], val[i]);
        }
    }

    /**
     * @param s
     * @param val
     */
    public void insert(String s, double val) {

        if (val < 0) {
            throw new IllegalArgumentException("There are negative weights");
        }

        if (s == null || s.length() == 0) {
            throw new IllegalArgumentException("Null and empty string are not allowed in the trie");
        }

        if (set.contains(s)) {
            throw new IllegalArgumentException("There are duplicate input terms");
        }

        if (root == null) {

            root = new TrieNode();
        }

        TrieNode curr = root;

        char[] arr = s.toCharArray();

        for (int i = 0; i < arr.length; i++) {

            if (curr.getChar(arr[i]) != null) {

                curr = curr.getChar(arr[i]);

            } else {
                String prevData = curr.getPrefix();
                curr = curr.putChar(arr[i]);
                curr.setPrefix(prevData + arr[i]);
            }
        }

        curr.setWord();
        set.add(s);
        curr.setVal(val);
        curr.setMax(val);

    }

    /**
     * Builds the the Max values of all the nodes
     */
    public void buildMax() {

        maxHelper(root);

    }

    private Double maxHelper(TrieNode node) {
        if (node.getMap().size() == 0) {
            return node.getMax();
        } else {

            for (Character c : node.getMap().keySet()) {
                TrieNode n = node.getChar(c);

                Double newMax = maxHelper(n);

                if (newMax > node.getMax()) {
                    node.setMax(newMax);
                }
            }
            return node.getMax();
        }
    }

    /**
     * Autocomplete function
     *
     * @param s
     * @param k
     * @return
     */
    public ArrayList<String> autoComplete(String s, int k) {

        PriorityQueue<TrieNode> pqVal = new PriorityQueue<>(new CompVal());

        TrieNode curr = getNode(s); // find the specific node

        PriorityQueue<TrieNode> pq = new PriorityQueue<>(Collections.reverseOrder());

        pq.add(curr);

        autoHelper(pqVal, pq, k);

        ArrayList<String> result = new ArrayList<>();

        while (!pqVal.isEmpty()) {
            result.add(0, pqVal.poll().getPrefix());
        }
        return result;

    }

    /**
     * Recursive helper method for autocomplete
     *
     * @param bestAnswer
     * @param pq
     * @param k
     */
    public void autoHelper(PriorityQueue<TrieNode> bestAnswer, PriorityQueue<TrieNode> pq, int k) {

        if (pq.peek() == null) {
            return;
        }
        TrieNode node = pq.poll();

        if (bestAnswer.size() < k) {
            if (node.getisWord()) {
                bestAnswer.add(node);
            }

        } else {
            if (node.getMax() <= bestAnswer.peek().getVal()) {
                return;
            }

            if (node.getisWord()) {
                if (node.getVal() > bestAnswer.peek().getVal()) {
                    bestAnswer.poll();
                    bestAnswer.add(node);
                }
            }
        }

        for (Character c : node.getMap().keySet()) {
            TrieNode n = node.getChar(c);
            pq.add(n);
        }
        autoHelper(bestAnswer, pq, k);
    }


    public class TrieNode implements Comparable<TrieNode> {
        private Map<Character, TrieNode> map;
        private boolean isWord;
        private String prefix;
        private Double val;
        private Double max;


        private TrieNode() {
            map = new TreeMap<>();
            isWord = false;
            prefix = "";
            val = 0.0;
            max = 0.0;
        }

        /*
        Getters and Setters
         */

        public Map<Character, TrieNode> getMap() {

            return map;
        }


        public boolean getisWord() {

            return isWord;
        }


        public void setWord() {

            isWord = true;
        }

        /**
         * @param c: the potential key in the Map
         * @return the TrieNode associated with the character
         */
        public TrieNode getChar(Character c) {
            return map.get(c);
        }

        /**
         * @param c: the character to insert in the map
         * @return the new TrieNode in the tree
         */
        public TrieNode putChar(Character c) {
            map.put(c, new TrieNode()); // insert the key in current map and new TrieNode object
            return map.get(c);
        }

        public String getPrefix() {

            return prefix;
        }

        public void setPrefix(String s) {
            prefix = s;
        }

        public void setVal(double v) {
            val = v;
        }

        public void setMax(double m) {
            max = m;
        }

        public Double getMax() {
            return max;
        }

        public Double getVal() {
            return val;
        }

        @Override
        public int compareTo(TrieNode o) {
            if (getMax() < o.getMax()) {
                return -1;
            } else if (getMax() > o.getMax()) {
                return 1;
            } else {
                return 0;
            }

        }

    }

    class CompVal implements Comparator<TrieNode> {

        /**
         * Nodes to be placed in ascending order according to value
         *
         * @param o1
         * @param o2
         * @return
         */
        @Override
        public int compare(TrieNode o1, TrieNode o2) {
            if (o1.getVal() < o2.getVal()) {
                return -1;
            } else if (o1.getVal() > o2.getVal()) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}

