package ru.ifmo.rain.bazhenov.i18n;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.*;

import java.io.*;

public class TestStatistics {
    private static String[] args = new String[4];

    @Before
    public void before() {
        args[2] = "in.in";
        args[3] = "out.html";
    }

    @Test
    public void statisticsFromRUToEN() throws IOException {
        PrintWriter out = new PrintWriter(args[2]);
        String text = "Текст с датой 23.09.2018. \n" + "Текст с числом 7. \n" + "Текст с валютой 10₽ \n";
        out.println(text);
        out.close();
        args[0] = "ru";
        args[1] = "en";
        TextStatistics.main(args);
        StringBuilder ans = new StringBuilder();
        BufferedReader in = new BufferedReader(new FileReader(args[3]));
        while (in.ready()) {
            ans.append(in.readLine());
        }
        assertTrue(ans.toString().contains("23.09.2018"));
        assertTrue(ans.toString().contains("7"));
        assertTrue(ans.toString().contains("10₽"));
        in.close();
    }

    @Test
    public void statisticsFromRUToRU() throws IOException {
        PrintWriter out = new PrintWriter(args[2]);
        String text = "Текст с датой 23.09.2018. \n" + "Текст с числом 7. \n" + "Text with currency $10 \n";
        out.println(text);
        out.close();
        args[0] = "ru";
        args[1] = "ru";
        TextStatistics.main(args);
        StringBuilder ans = new StringBuilder();
        BufferedReader in = new BufferedReader(new FileReader(args[3]));
        while (in.ready()) {
            ans.append(in.readLine());
        }
        assertTrue(ans.toString().contains("23.09.2018"));
        assertTrue(ans.toString().contains("Число дат: 1"));
        assertTrue(ans.toString().contains("7"));
        assertTrue(ans.toString().contains("$10"));
        in.close();
    }

    @Test
    public void statisticsFromENToRU() throws IOException {
        PrintWriter out = new PrintWriter(args[2]);
        String text = "Text with date 5/25/2020. \n" + "Text with number 7. \n" + "Text with currency $10 \n";
        out.println(text);
        out.close();
        args[0] = "en";
        args[1] = "ru";
        TextStatistics.main(args);
        StringBuilder ans = new StringBuilder();
        BufferedReader in = new BufferedReader(new FileReader(args[3]));
        while (in.ready()) {
            ans.append(in.readLine());
        }
        assertTrue(ans.toString().contains("5/25/2020"));
        assertTrue(ans.toString().contains("7"));
        assertTrue(ans.toString().contains("$10"));
        in.close();
    }

    @Test
    public void statisticsFromENToEN() throws IOException {
        PrintWriter out = new PrintWriter(args[2]);
        String text = "Text with date 23.09.2018. \n" + "Text with number 7. \n";
        out.println(text);
        out.close();
        args[0] = "en";
        args[1] = "en";
        TextStatistics.main(args);
        StringBuilder ans = new StringBuilder();
        BufferedReader in = new BufferedReader(new FileReader(args[3]));
        while (in.ready()) {
            ans.append(in.readLine());
        }
        assertTrue(ans.toString().contains("23.09.2018"));
        assertTrue(ans.toString().contains("7"));
        in.close();
    }

    @Test
    public void statisticsFromFRToEN() throws IOException {
        PrintWriter out = new PrintWriter(args[2]);
        String text = "Texte avec date 5/25/2020. \n" + "Texte avec le numéro 7. \n";
        out.println(text);
        out.close();
        args[0] = "fr";
        args[1] = "en";
        TextStatistics.main(args);
        StringBuilder ans = new StringBuilder();
        BufferedReader in = new BufferedReader(new FileReader(args[3]));
        while (in.ready()) {
            ans.append(in.readLine());
        }
        assertTrue(ans.toString().contains("5/25/2020"));
        assertTrue(ans.toString().contains("7"));
        in.close();
    }

    @Test
    public void statisticsFromFRToRU() throws IOException {
        PrintWriter out = new PrintWriter(args[2]);
        String text = "Texte avec date 5/25/2020. \n" + "Texte avec le numéro 7. \n";
        out.println(text);
        out.close();
        args[0] = "fr";
        args[1] = "ru";
        TextStatistics.main(args);
        StringBuilder ans = new StringBuilder();
        BufferedReader in = new BufferedReader(new FileReader(args[3]));
        while (in.ready()) {
            ans.append(in.readLine());
        }
        assertTrue(ans.toString().contains("5/25/2020"));
        assertTrue(ans.toString().contains("7"));
        in.close();
    }

    @Test
    public void statisticsFromPLToEN() throws IOException {
        PrintWriter out = new PrintWriter(args[2]);
        String text = "Tekst z datą 23.09.2018. \n" + "tekst z numerem 7. \n";
        out.println(text);
        out.close();
        args[0] = "pl";
        args[1] = "en";
        TextStatistics.main(args);
        StringBuilder ans = new StringBuilder();
        BufferedReader in = new BufferedReader(new FileReader(args[3]));
        while (in.ready()) {
            ans.append(in.readLine());
        }
        assertTrue(ans.toString().contains("23.09.2018"));
        assertTrue(ans.toString().contains("7"));
        in.close();
    }

    @Test
    public void statisticsFromPLToRU() throws IOException {
        PrintWriter out = new PrintWriter(args[2]);
        String text = "Tekst z datą  23.09.2018. \n" + "tekst z numerem 7. \n";
        out.println(text);
        out.close();
        args[0] = "pl";
        args[1] = "ru";
        TextStatistics.main(args);
        StringBuilder ans = new StringBuilder();
        BufferedReader in = new BufferedReader(new FileReader(args[3]));
        while (in.ready()) {
            ans.append(in.readLine());
        }
        assertTrue(ans.toString().contains("23.09.2018"));
        assertTrue(ans.toString().contains("7"));
        in.close();
    }
}
