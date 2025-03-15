package ru.ifmo.rain.bazhenov.walk;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class RecursiveWalk {

    public static void main(String[] args) {
        if (args == null || args.length != 2) {
            System.out.println("Error: Expected 2 files");
            return;
        }
        if (args[0] == null || args[1] == null) {
            System.out.println("Error: Expected not-null files");
            return;
        }
        Path input_path;
        Path output_path;
        try {
            input_path = Paths.get(args[0]);
            output_path = Paths.get(args[1]);
        } catch (InvalidPathException e) {
            System.out.println("Error : Incorrect path");
            return;
        }
        if (output_path.getParent() != null) {
            try {
                Files.createDirectories(output_path.getParent());
            } catch (IOException e) {
                System.out.println("Error : Can't create output file path");
                return;
            }
        }
        try (BufferedReader reader = Files.newBufferedReader(input_path, StandardCharsets.UTF_8)) {
            try (BufferedWriter writer = Files.newBufferedWriter(output_path, StandardCharsets.UTF_8)) {
                String file;
                while ((file = reader.readLine()) != null) {
                    try {
                        Path path = Paths.get(file);
                        try (Stream<Path> pathStream = Files.walk(path)) {
                            pathStream.filter(Predicate.not(Files::isDirectory)).forEach(
                                    file_path -> {
                                        try {
                                            writer.write(String.format("%08x %s%n", FNV(file_path), file_path.toString()));
                                        } catch (IOException e) {
                                            System.out.println("Output file error: " + e.getMessage());
                                        }
                                    });
                        } catch (IOException e) {
                            writer.write(String.format("%08x %s", 0, file + "\n"));
                        }
                    } catch (InvalidPathException e) {
                        writer.write(String.format("%08x %s", 0, file + "\n"));
                    }
                }
            } catch (IOException e) {
                System.out.println("Output file error: " + e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("Input file error: " + e.getMessage());
        }
    }

    private static int FNV(Path path) {
        int h = 0x811c9dc5;
        try (BufferedInputStream inputStream = new BufferedInputStream(Files.newInputStream(path))) {
            int input;
            byte[] temp = new byte[1024];
            while ((input = inputStream.read(temp)) != -1) {
                for (int i = 0; i < input; ++i) {
                    h = (h * 0x01000193) ^ (temp[i] & 0xff);
                }
            }
        } catch (IOException e) {
            h = 0;
        }
        return h;
    }
}