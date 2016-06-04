package pl.edu.agh.kiro.latex_plagiat.commons.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TextUtils {
    public static Set<String> splitIntoSet(String text) {
        String[] splitResult = text.split("\\s");
        Set<String> keywords = new HashSet<String>(Arrays.asList(splitResult));
        return keywords;
    }
}
