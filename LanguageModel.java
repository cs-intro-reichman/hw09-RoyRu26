import java.util.HashMap;
import java.util.Random;

public class LanguageModel {

    // The map of this model.
    // Maps windows to lists of charachter data objects.
    HashMap<String, List> CharDataMap;

    // The window length used in this model.
    int windowLength;

    // The random number generator used by this model.
    private Random randomGenerator;

    /**
     * Constructs a language model with the given window length and a given
     * seed value. Generating texts from this model multiple times with the
     * same seed value will produce the same random texts. Good for debugging.
     */
    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();
    }

    /**
     * Constructs a language model with the given window length.
     * Generating texts from this model multiple times will produce
     * different random texts. Good for production.
     */
    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();
    }

    /** Builds a language model from the text in the given file (the corpus). */
    public void train(String fileName) {
        String window = "";
        char c;
        In in = new In(fileName);
        for (int i = 0; i < windowLength; i++) {
            if (!in.isEmpty()) {
                window += in.readChar();
            }
        }
        while (!in.isEmpty()) {
            c = in.readChar();
            List probs = CharDataMap.get(window);
            if (probs == null) {
                probs = new List();
                CharDataMap.put(window, probs);
            }
            probs.update(c);
            window = window.substring(1) + c;
        }
        for (List probs : CharDataMap.values())
            calculateProbabilities(probs);
    }

    // Computes and sets the probabilities (p and cp fields) of all the
    // characters in the given list. */
    void calculateProbabilities(List probs) {
        int total = 0;
        double cpSum = 0.0;
        CharData[] chdArr = probs.toArray();
        for (CharData chd : chdArr) {
            total += chd.count;
        }
        for (CharData chd : chdArr) {
            chd.p = (double) chd.count / total;
            cpSum += chd.p;
            chd.cp = Math.round(cpSum * 1000) / 1000.0;
            ;
        }
    }

    // Returns a random character from the given probabilities list.
    char getRandomChar(List probs) {
        double r = randomGenerator.nextDouble();
        System.out.println("the random number is: " + r);
        CharData[] chdArr = probs.toArray();
        for (CharData chd : chdArr) {
            System.out.println(chd.chr + " " + chd.cp);
            if (chd.cp > r) {
                return chd.chr;
            }
        }
        return chdArr[chdArr.length - 1].chr;
    }

    /**
     * Generates a random text, based on the probabilities that were learned during
     * training.
     * 
     * @param initialText     - text to start with. If initialText's last substring
     *                        of size numberOfLetters
     *                        doesn't appear as a key in Map, we generate no text
     *                        and return only the initial text.
     * @param numberOfLetters - the size of text to generate
     * @return the generated text
     */
    public String generate(String initialText, int textLength) {
        // Your code goes here
        return "";
    }

    /** Returns a string representing the map of this language model. */
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (String key : CharDataMap.keySet()) {
            List keyProbs = CharDataMap.get(key);
            str.append(key + " : " + keyProbs + "\n");
        }
        return str.toString();
    }

    public static void main(String[] args) {
        // Your code goes here
        List list = new List();
        list.update(' ');
        list.update('e');
        list.update('e');
        list.update('t');
        list.update('t');
        list.update('i');
        list.update('m');
        list.update('m');
        list.update('o');
        list.update('c');

        LanguageModel lm = new LanguageModel(2, 20);
        lm.calculateProbabilities(list);
        //System.out.println(list);
        //System.out.println(lm.getRandomChar(list));
        //lm.train("originofspecies.txt");
        System.out.println(lm.getRandomChar(list));
    }
}
