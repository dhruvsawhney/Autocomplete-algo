import org.junit.Test;
import ucb.junit.textui;

import java.util.ArrayList;

import static junit.framework.TestCase.assertTrue;

/**
 * The suite of all JUnit tests for the Autocomplete class.
 *
 * @author
 */
public class TestAutocomplete {


    @Test
    public void testConstructorValMax() throws Exception {

        String[] terms = {"hello", "he", "spite", "spit"};
        double[] weights = {34.2324, 4.86, 234.56, 9};
        Autocomplete auto = new Autocomplete(terms, weights);

        AugTrie t = auto.trie;
        t.insert("hellos", 2.0);
        // he
        assertTrue(t.getNode("he").getVal() == 4.86);
        assertTrue(t.getNode("he").getMax() == 34.2324);
        // hello
        assertTrue(t.getNode("hello").getVal() == 34.2324);
        assertTrue(t.getNode("hello").getMax() == 34.2324);
        // spit
        assertTrue(t.getNode("spit").getVal() == 9);
        assertTrue(t.getNode("spit").getMax() == 234.56);
        // spite
        assertTrue(t.getNode("spite").getVal() == 234.56);
        assertTrue(t.getNode("spite").getMax() == 234.56);


        assertTrue(t.getNode("hellos").getVal() == 2.0);
        assertTrue(t.getNode("hellos").getMax() == 2.0);

    }

    @Test
    public void testWeight() throws Exception {
        String[] terms = {"hello", "he", "spite", "spit", "auto", "relative", "rel"};
        double[] weights = {34.2324, 4.86, 234.56, 9, 1234.5, 34, 987};
        Autocomplete auto = new Autocomplete(terms, weights);

        assertTrue(auto.weightOf("hello") == 34.2324);
        assertTrue(auto.weightOf("he") == 4.86);
        assertTrue(auto.weightOf("spite") == 234.56);
        assertTrue(auto.weightOf("spit") == 9);
        assertTrue(auto.weightOf("auto") == 1234.5);
        assertTrue(auto.weightOf("relative") == 34);
        assertTrue(auto.weightOf("rel") == 987);

    }

    @Test
    public void testTopMatch() throws Exception {
        String[] terms = {"coming", "comes", "company", "come", "common"};
        double[] weights = {215147.0, 153299.0, 13319.0, 873007.0, 180061.0};
        Autocomplete auto = new Autocomplete(terms, weights);

        assertTrue(auto.topMatch("com").equals("come"));

        String[] terms2 = {"shore", "shot", "sum", "sock", "food", "find"};
        double[] weights2 = {32, 1533242423, 1234242355325.0, 81283219, 2, 180062390594513948503.0};
        Autocomplete auto2 = new Autocomplete(terms2, weights2);

        assertTrue(auto2.topMatch("s").equals("sum"));
        assertTrue(auto2.topMatch("sh").equals("shot"));
        assertTrue(auto2.topMatch("fo").equals("food"));
        assertTrue(auto2.topMatch("f").equals("find"));


    }

    @Test
    public void testTopMatches() throws Exception {

        String[] terms2 = {"shore", "shot", "sum", "sock", "food", "find"};
        double[] weights2 = {32, 1533242423, 1234242355325.0, 81283219, 2, 180062390594513948503.0};
        Autocomplete auto2 = new Autocomplete(terms2, weights2);
        ArrayList<String> top = new ArrayList<>();
        for (String t : auto2.topMatches("s", 10)) {
            top.add(t);
        }
        assertTrue(top.get(0).equals("sum"));
        assertTrue(top.get(1).equals("shot"));
        assertTrue(top.get(2).equals("sock"));

        ArrayList<String> top2 = new ArrayList<>();
        for (String t : auto2.topMatches("fo", 10)) {
            top2.add(t);
        }
        assertTrue(top2.get(0).equals("food"));

        ArrayList<String> top3 = new ArrayList<>();
        for (String t : auto2.topMatches("sh", 5)) {
            top3.add(t);
        }
        assertTrue(top3.get(1).equals("shore"));


    }

    @Test
    public void testDifferentLengthsException() throws Exception {

        String[] terms2 = {"shore", "shot", "sum", "sock", "food", "find"};
        double[] weights2 = {1533242423, 1234242355325.0, 81283219, 2, 180062390594513948503.0};

        boolean isException = false;
        try {
            Autocomplete auto2 = new Autocomplete(terms2, weights2);
        } catch (IllegalArgumentException e) {
            isException = true;
        }
        assertTrue(isException);

    }

    @Test
    public void testNegativeWeightsException() throws Exception {

        String[] terms2 = {"shore", "shot", "sum", "sock", "food", "find"};
        double[] weights2 = {1533242423, 1234242355325.0, 81283219, 2, -180062390594513948503.0, 5};

        boolean isException = false;
        try {
            Autocomplete auto2 = new Autocomplete(terms2, weights2);
        } catch (IllegalArgumentException e) {
            isException = true;
        }
        assertTrue(isException);

    }

    @Test
    public void testDuplicateInputsException() throws Exception {

        String[] terms2 = {"shore", "shot", "sum", "sock", "find", "find"};
        double[] weights2 = {1533242423, 1234242355325.0, 81283219, 2, 180062390594513948503.0, 5};

        boolean isException = false;
        try {
            Autocomplete auto2 = new Autocomplete(terms2, weights2);
        } catch (IllegalArgumentException e) {
            isException = true;
        }
        assertTrue(isException);

    }

    @Test
    public void testNegativeMatchesRequestedException() throws Exception {

        String[] terms2 = {"shore", "shot", "sum", "sock", "food", "find"};
        double[] weights2 = {1533242423, 1234242355325.0, 81283219, 2, 180062390594513948503.0, 5};
        Autocomplete auto2 = new Autocomplete(terms2, weights2);
        boolean isException = false;
        try {
            auto2.topMatches("f", -8);
        } catch (IllegalArgumentException e) {
            isException = true;
        }
        assertTrue(isException);

    }


    /**
     * Run the JUnit tests above.
     */
    public static void main(String[] ignored) {
        textui.runClasses(TestAutocomplete.class);
    }
}
