package org.example.roomiehub.util;

import org.apache.commons.text.similarity.LevenshteinDistance;
import java.text.Normalizer;
import java.util.regex.Pattern;

public class TextUtils {
    private static final LevenshteinDistance levenshtein = new LevenshteinDistance();

    public static boolean fuzzyMatch(String source, String keyword, int threshold) {
        if (source == null || keyword == null) return false;

        String s1 = removeDiacritics(source.toLowerCase());
        String s2 = removeDiacritics(keyword.toLowerCase());

        return levenshtein.apply(s1, s2) <= threshold;
    }

    public static String removeDiacritics(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("").replaceAll("đ", "d");
    }
}

