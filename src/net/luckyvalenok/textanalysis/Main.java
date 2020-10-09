package net.luckyvalenok.textanalysis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Main {
    
    public static void main(String[] args) throws IOException {
        String[][] parsedText = readParsedText();
        System.out.println("Текст, разбитый на предложения, которые разбиты на слова: \n" + Arrays.deepToString(parsedText) + "\n");
        Arrays.stream(parsedText)
            .max(Comparator.comparingInt(o -> o.length))
            .ifPresent(strings -> {
                for (int i = 2; i < strings.length; i++)
                    System.out.println(i + "-граммы: \n" + searchNgram(parsedText, i) + "\n");
            });
    }
    
    private static Map<String, String> searchNgram(String[][] parsedText, int count) {
        Map<String, Map<String, Integer>> counter = new HashMap<>();
        for (String[] strings : parsedText) {
            for (int i = 0; i < strings.length - count + 1; i++) {
                StringBuilder ngramBuilder = new StringBuilder();
                for (int c = 0; c < count - 1; c++) {
                    ngramBuilder.append(strings[i + c]).append(" ");
                }
                String ngram = ngramBuilder.substring(0, ngramBuilder.length() - 1);
                Map<String, Integer> integerMap = counter.getOrDefault(ngram, new HashMap<>());
                integerMap.put(strings[i + count - 1], integerMap.getOrDefault(strings[i + count - 1], 0) + 1);
                counter.put(ngram, integerMap);
            }
        }
        Map<String, String> ngrams = new HashMap<>();
        counter.forEach((key, value) -> value.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .ifPresent(entry1 -> ngrams.put(key, entry1.getKey())));
        return ngrams;
    }
    
    private static String[][] readParsedText() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Введите текст.");
        String text = reader.readLine().trim();
        String[][] parsedText;
        while ((parsedText = parseText(text)).length == 0) {
            System.out.println("В вашем тексте нет предложений. Введите текст снова.");
            text = reader.readLine();
        }
        return parsedText;
    }
    
    private static String[][] parseText(String text) {
        String[] sentences = Arrays.stream(text.toLowerCase().split("[.!?;:()]"))
            .filter(s -> s.length() > 0)
            .toArray(String[]::new);
        String[][] parsedText = new String[sentences.length][];
        int i = 0;
        for (String sentence : sentences) {
            parsedText[i] = Arrays.stream(sentence.trim().split("[^a-zA-Zа-яА-ЯёЁ']"))
                .filter(s -> s.length() > 0)
                .toArray(String[]::new);
            i++;
        }
        return parsedText;
    }
}