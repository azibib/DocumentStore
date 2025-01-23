import org.junit.jupiter.*;
import org.junit.jupiter.api.Assertions;

import random.Stack;

public class StackTest {
    private Stack<String> words = new Stack<>();
    private String word1 = "hello";
    private String word2 = "hell";
    private String word3 = "hel";


    void stackpush(){
        words.push(word1);
        words.push(word2);
        words.push(word3);

        Assertions.assertEquals(words.pop(), false);

    }
}
