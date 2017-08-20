import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.TreeSet;

/**
 * AlphabetSort takes input from stdin and prints to stdout.
 * The first line of input is the alphabet permutation.
 * The the remaining lines are the words to be sorted.
 * <p>
 * The output should be the sorted words, each on its own line,
 * printed to std out.
 */
public class AlphabetSort {


    public static Set<Character> dictionary(String s) {
        Set<Character> valid = new TreeSet<>();

        for (int i = 0; i < s.length(); i++) {

            if (valid.contains(s.charAt(i))) {
                throw new IllegalArgumentException("A letter appears multiple times");
            }
            valid.add(s.charAt(i));
        }
        return valid;
    }

    /**
     * Reads input from standard input and prints out the input words in
     * alphabetical order.
     *
     * @param args ignored
     */


    public static void main(String[] args) {

        Set<Character> letters = new TreeSet<>();

        Trie obj = new Trie();

        BufferedReader in = null;

        try {
            in = new BufferedReader(new InputStreamReader(System.in));

            String line;

            boolean alphabet = true;
            boolean firstWord = true;

            line = in.readLine();

            if (line == null || line.length() == 0) {
                throw new IllegalArgumentException("No words or alphabet are given.");
            }
            letters = dictionary(line);

            obj = new Trie(line);

            line = in.readLine();

            if (line == null || line.length() == 0) {
                throw new IllegalArgumentException("No words are given.");
            }


            boolean notInAlphabet = false;
            for (int i = 0; i < line.length(); i++) {
                if (!letters.contains(line.charAt(i))) {
                    notInAlphabet = true;
                }
            }

            if (!notInAlphabet) {
                obj.insert(line);
            }
            line = in.readLine();
            while (line != null) {

                notInAlphabet = false;
                for (int i = 0; i < line.length(); i++) {
                    if (!letters.contains(line.charAt(i))) {
                        notInAlphabet = true;
                    }
                }

                if (!notInAlphabet) {
                    obj.insert(line);
                }

                line = in.readLine();
            }
            obj.printWords();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
