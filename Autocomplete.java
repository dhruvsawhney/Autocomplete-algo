import java.util.ArrayList;

/**
 * Implements autocomplete on prefixes for a given dictionary of terms and weights.
 *
 * @author
 */
public class Autocomplete {
    String[] words;
    double[] value;
    AugTrie trie;

    /**
     * Initializes required data structures from parallel arrays.
     *
     * @param terms   Array of terms.
     * @param weights Array of weights.
     */
    public Autocomplete(String[] terms, double[] weights) {
        /* YOUR CODE HERE. */

        if (terms.length != weights.length) {
            throw new IllegalArgumentException("different length of input");
        }

        this.words = terms;
        this.value = weights;
        trie = new AugTrie(terms, weights);
        trie.buildTrie();
        trie.buildMax();
    }

    /**
     * Find the weight of a given term. If it is not in the dictionary, return 0.0
     *
     * @param term
     * @return
     */
    public double weightOf(String term) {

        double weight = trie.getNode(term).getVal();
        return weight;
    }

    /**
     * Return the top match for given prefix, or null if there is no matching term.
     *
     * @param prefix Input prefix to match against.
     * @return Best (highest weight) matching string in the dictionary.
     */
    public String topMatch(String prefix) {
        if (prefix == null) {
            throw new NullPointerException("Prefix is null");
        }
        ArrayList<String> s = trie.autoComplete(prefix, 5);

        try {
            return s.get(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Returns the top k matching terms (in descending order of weight) as an iterable.
     * If there are less than k matches, return all the matching terms.
     *
     * @param prefix
     * @param k
     * @return
     */
    public Iterable<String> topMatches(String prefix, int k) {
        if (k < 0) {
            throw new IllegalArgumentException("non-positive number given!");
        }
        if (prefix == null) {
            throw new NullPointerException("Prefix is null");
        }

        try {
            return trie.autoComplete(prefix, k);
        } catch (NullPointerException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Test client. Reads the data from the file, then repeatedly reads autocomplete
     * queries from standard input and prints out the top k matching terms.
     *
     * @param args takes the name of an input file and an integer k as
     *             command-line arguments
     */
    public static void main(String[] args) {
        // initialize autocomplete data structure
        In in = new In(args[0]);
        int N = in.readInt();
        String[] terms = new String[N];
        double[] weights = new double[N];
        for (int i = 0; i < N; i++) {
            weights[i] = in.readDouble();
            in.readChar();
            terms[i] = in.readLine();
        }
        Autocomplete autocomplete = new Autocomplete(terms, weights);
        int k = Integer.parseInt(args[1]);
        while (StdIn.hasNextLine()) {
            String prefix = StdIn.readLine();
            for (String term : autocomplete.topMatches(prefix, k)) {
                StdOut.printf("%14.1f  %s\n", autocomplete.weightOf(term), term);
            }
        }
    }
}
