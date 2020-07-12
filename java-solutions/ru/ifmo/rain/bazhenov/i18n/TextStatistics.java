package ru.ifmo.rain.bazhenov.i18n;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.*;
import java.util.*;
import java.util.stream.Collectors;

public class TextStatistics {

    private static final Map<Integer, String> MAP_OF_SUFFIX = Map.of(
            0, "lines",
            1, "sentences",
            2, "words",
            3, "dates",
            4, "currency",
            5, "numbers"
    );

    public static void main(String[] args) {
        Locale localeFrom = new Locale(args[0]);
        Locale localeTo = new Locale(args[1]);
        File file = new File(args[2]);
        ArrayList<String>[] arrayOfCategory = new ArrayList[6];
        init(arrayOfCategory);
        StringBuilder text = new StringBuilder();
        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader(file));
            while (in.ready()) {
                String str = in.readLine();
                arrayOfCategory[0].add(str);
                text.append(str).append(" ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 1; i < 3; i++) {
            arrayOfCategory[i] = new ArrayList<>(getSentences(localeFrom, text.toString(), i));
        }
        Format[] parsers = creatingParsers(localeFrom);
        for (String word : arrayOfCategory[2]) {
            for (int i = 0; i < parsers.length; i++) {
                try {
                    String str = parsers[i].format(parsers[i].parseObject(word));
                    arrayOfCategory[i < 4 ? 3 : i].add(str);
                    break;
                } catch (IllegalArgumentException | ParseException ignored) {
                }
            }
        }
        ResourceBundle rb = ResourceBundle.getBundle("text", localeTo);
        StringBuilder ans = new StringBuilder();
        ans.append("<html><body>");
        ans.append("<h1>").append(rb.getObject("analise")).append(file.getName()).append("</h1>").append('\n');
        ans.append("<h2>").append(rb.getObject("total")).append("</h2>").append('\n');
        for (int i = 0; i < 6; i++) {
            getStat(arrayOfCategory[i], ans, i, rb, null);
        }
        for (int i = 0; i < 6; i++) {
            ArrayList<String> category = arrayOfCategory[i];
            TreeSet<String> treeSet = new TreeSet<>(Collator.getInstance(localeFrom));
            treeSet.addAll(category);
            getTitle(ans, i, rb);
            getStat(category, ans, i, rb, treeSet);
            getMinOrMax(treeSet, "min.", ans, i, rb);
            getMinOrMax(treeSet, "max.", ans, i, rb);
            getMinOrMaxOrAverageLength(category, "minlength.", ans, i, rb);
            getMinOrMaxOrAverageLength(category, "maxlength.", ans, i, rb);
            getMinOrMaxOrAverageLength(category, "average.", ans, i, rb);
            ans.append("<br>").append('\n');
        }
        ans.append("</body></html>");
        try {
            PrintWriter out = new PrintWriter(args[3]);
            out.println(ans);
            out.close();
//            Files.deleteIfExists(Paths.get(args[3]));
//            Files.createFile(Paths.get(args[3]));
//            Files.write(Paths.get(args[3]), ans.toString().getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void getStat(ArrayList<String> category, StringBuilder ans, int i, ResourceBundle rb, TreeSet<String> treeSet) {
        ans.append(rb.getObject("count." + MAP_OF_SUFFIX.get(i))).append(category.size());
        if (treeSet != null) {
            ans.append(" (").append(rb.getObject("unique")).append(treeSet.size()).append(")");
        }
        ans.append("<br>").append('\n');
    }

    private static void getTitle(StringBuilder ans, int i, ResourceBundle rb) {
        ans.append("<h2>").append(rb.getObject("stat." + MAP_OF_SUFFIX.get(i))).append("</h2>").append('\n');
    }

    private static void getMinOrMaxOrAverageLength(ArrayList<String> category, String prefix, StringBuilder ans, int i, ResourceBundle rb) {
        TreeSet<String> treeSet = new TreeSet<>(Comparator.comparing(String::length));
        treeSet.addAll(category);
        ans.append(rb.getObject(prefix + MAP_OF_SUFFIX.get(i)));
        if (treeSet.isEmpty()) {
            ans.append(0).append("<br>").append('\n');
        } else {
            if (prefix.equals("average.")) {
                int sum = 0;
                for (String str : category) {
                    sum += str.length();
                }
                ans.append(sum / category.size()).append("<br>").append('\n');
                return;
            }
            if (prefix.equals("minlength.")) {
                ans.append(treeSet.first().length()).append(" (").append(treeSet.first());
            }
            if (prefix.equals("maxlength.")) {
                ans.append(treeSet.last().length()).append(" (").append(treeSet.last());
            }
            ans.append(")").append("<br>").append('\n');
        }
    }

    private static void getMinOrMax(TreeSet<String> treeSet, String prefix, StringBuilder ans, int i, ResourceBundle rb) {
        ans.append(rb.getObject(prefix + MAP_OF_SUFFIX.get(i)));
        if (treeSet.isEmpty()) {
            ans.append("-").append("<br>").append('\n');
        } else {
            if (prefix.equals("min.")) {
                ans.append(treeSet.first());
            } else {
                ans.append(treeSet.last());
            }
            ans.append("<br>").append('\n');
        }
    }

    private static List<String> getSentences(Locale locale, String text, int mode) {
        BreakIterator iterator = null;
        switch (mode) {
            case 1: {
                iterator = BreakIterator.getSentenceInstance(locale);
                break;
            }
            case 2: {
                iterator = BreakIterator.getWordInstance(locale);
                break;
            }
        }
        List<String> ans = new ArrayList<>();
        Objects.requireNonNull(iterator).setText(text);
        int start = iterator.first();
        for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
            ans.add(text.substring(start, end));
        }
        if (mode == 2) {
            return ans.stream().filter(word -> Character.isLetterOrDigit(word.charAt(0))).collect(Collectors.toList());
        }
        return ans.stream().map(String::trim).collect(Collectors.toList());
    }

    private static Format[] creatingParsers(Locale locale) {
        return new Format[]{DateFormat.getDateInstance(DateFormat.FULL, locale),
                DateFormat.getDateInstance(DateFormat.LONG, locale),
                DateFormat.getDateInstance(DateFormat.MEDIUM, locale),
                DateFormat.getDateInstance(DateFormat.SHORT, locale),
                NumberFormat.getCurrencyInstance(locale),
                NumberFormat.getNumberInstance(locale)
        };
    }

    private static void init(ArrayList[] arrayLists) {
        for (int i = 0; i < arrayLists.length; i++) {
            arrayLists[i] = new ArrayList<>();
        }
    }

}
