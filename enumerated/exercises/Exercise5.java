package enumerated.exercises;

import java.util.Random;

import static net.mindview.util.Print.print;
import static net.mindview.util.Print.printnb;

public class Exercise5 {
    public static void main(String[] args) {
        VowelsAndConsonants.main(args);
    }
}

enum LetterType {
    VOWEL('a', 'e', 'i', 'o', 'u'),
    SOMETIMES_A_VOWEL('y', 'w'),
    CONSONANT;

    private char[] letters;

    public boolean hasLetter(char c) {
        for(int i = 0; i < letters.length; ++i) {
            if(letters[i] == c) return  true;
        }
        return false;
    }

    LetterType(char... letters) {
        this.letters = letters;
    }

    public static LetterType detectLetterType(char c) {
        for(LetterType letterType: values()) {
            if(letterType.hasLetter(c)) return letterType;
        }
        return CONSONANT;
    }
}

class VowelsAndConsonants {
    public static void main(String[] args) {
        Random rand = new Random(47);
        for(int i = 0; i < 100; i++) {
            int c = rand.nextInt(26) + 'a';
            printnb((char)c + ", " + c + ": ");
            print(LetterType.detectLetterType((char)c));
        }
    }
}
