package drawingApi;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import model.Point;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static config.Config.*;

public class DrawingApiFx extends Application implements DrawingApi {

    private final ArrayList<String> list;

    public DrawingApiFx() {
        list = new ArrayList<>();
    }

    @Override
    public double getDrawingAreaWidth() {
        return SIZE;
    }

    @Override
    public double getDrawingAreaHeight() {
        return SIZE;
    }

    @Override
    public double getRadius() {
        return RADIUS;
    }

    @Override
    public void drawCircle(Point p) {
        list.add("c");
        list.add(String.valueOf(p.x - VERTEX_SIZE / 2));
        list.add(String.valueOf(p.y - VERTEX_SIZE / 2));
    }

    @Override
    public void drawLine(Point p1, Point p2) {
        list.add("l");
        list.add(String.valueOf(p1.x));
        list.add(String.valueOf(p1.y));
        list.add(String.valueOf(p2.x));
        list.add(String.valueOf(p2.y));
    }

    @Override
    public void showGraph() {
        String[] args = new String[list.size()];
        for (int i = 0; i < args.length; i++) {
            args[i] = list.get(i);
        }
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        List<String> params = getParameters().getUnnamed();
        HashSet<Shape> setOfShapes = createSetOfShapes(params);
        Group root = new Group();
        for (Shape s : setOfShapes) {
            root.getChildren().add(s);
        }
        Scene scene = new Scene(root, getDrawingAreaWidth(), getDrawingAreaHeight());
        stage.setTitle("Graph");
        stage.setResizable(true);
        stage.setScene(scene);
        stage.show();
    }

    private HashSet<Shape> createSetOfShapes(List<String> listOfParams) {
        HashSet<Shape> setOfShapes = new HashSet<>();
        ArrayList<String> arrayListOfParams = new ArrayList<>(listOfParams);
        for (int i = 0; i < arrayListOfParams.size(); i++) {
            String param = arrayListOfParams.get(i);
            switch (param) {
                case "c" -> {
                    Circle circle = new Circle();
                    circle.setCenterX(Double.parseDouble(arrayListOfParams.get(i + 1)) + VERTEX_SIZE / 2);
                    circle.setCenterY(Double.parseDouble(arrayListOfParams.get(i + 2)) + VERTEX_SIZE / 2);
                    circle.setRadius(VERTEX_SIZE / 2);
                    setOfShapes.add(circle);
                    i += 2;
                }
                case "l" -> {
                    Line line = new Line();
                    line.setStartX(Double.parseDouble(arrayListOfParams.get(i + 1)));
                    line.setStartY(Double.parseDouble(arrayListOfParams.get(i + 2)));
                    line.setEndX(Double.parseDouble(arrayListOfParams.get(i + 3)));
                    line.setEndY(Double.parseDouble(arrayListOfParams.get(i + 4)));
                    setOfShapes.add(line);
                    i += 4;
                }
                default -> System.err.println("Unknown param : " + param);
            }
        }
        return setOfShapes;
    }

}
