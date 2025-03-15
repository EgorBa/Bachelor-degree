package model;

import dataReader.DataReader;
import drawingApi.DrawingApi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Graph {
    /**
     * Bridge to drawing api
     */
    private final DrawingApi drawingApi;

    /**
     * Bridge to data reader
     */
    private final DataReader dataReader;

    public Graph(DrawingApi drawingApi, DataReader dataReader) {
        this.drawingApi = drawingApi;
        this.dataReader = dataReader;
    }

    public void drawGraph(String fileName) {
        try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {
            int n = Integer.parseInt(in.readLine());
            int[][] matrix = new int[n][n];
            matrix = dataReader.readData(in, matrix);
            drawGraph(matrix);
        } catch (IOException e) {
            System.err.println("Some problems with file : " + fileName);
            e.printStackTrace();
        }
    }

    private void drawGraph(int[][] graph) {
        ArrayList<Point> listOfVertex = new ArrayList<>();
        Point startPoint = new Point(drawingApi.getDrawingAreaWidth() / 2,
                drawingApi.getDrawingAreaHeight() / 2 + drawingApi.getRadius());
        getPoints(startPoint, ((double) 360) / graph.length, listOfVertex);
        for (Point p : listOfVertex) {
            drawingApi.drawCircle(p);
        }
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph.length; j++) {
                if (graph[i][j] == 1) {
                    drawingApi.drawLine(listOfVertex.get(i), listOfVertex.get(j));
                }
            }
        }
        drawingApi.showGraph();
    }

    private void getPoints(Point point, double injection, ArrayList<Point> listOfVertex) {
        if (notContainsSame(point, listOfVertex)) {
            listOfVertex.add(point);
        } else return;
        double r = drawingApi.getRadius();
        double shift = drawingApi.getDrawingAreaWidth() / 2;
        double x = point.x - shift;
        double y = point.y - shift;
        double a = -2 * x;
        double b = -2 * y;
        double c = x * x + y * y + r * r * (2 * Math.cos(Math.PI * injection / 180) - 1);
        double x0 = -a * c / (a * a + b * b);
        double y0 = -b * c / (a * a + b * b);
        double d = r * r - c * c / (a * a + b * b);
        double mult = Math.sqrt(d / (a * a + b * b));
        double ax = x0 + b * mult + shift;
        double ay = y0 - a * mult + shift;
        double bx = x0 - b * mult + shift;
        double by = y0 + a * mult + shift;
        Point p1 = new Point(ax, ay);
        Point p2 = new Point(bx, by);
        if (notContainsSame(p1, listOfVertex)) {
            getPoints(p1, injection, listOfVertex);
        }
        if (notContainsSame(p2, listOfVertex)) {
            getPoints(p2, injection, listOfVertex);
        }
    }

    private boolean notContainsSame(Point p, ArrayList<Point> listOfVertex) {
        for (Point p1 : listOfVertex) {
            if (p.dist(p1) < 10) return false;
        }
        return true;
    }
}