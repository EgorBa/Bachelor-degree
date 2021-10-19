package drawingApi;

import model.Point;

public interface DrawingApi {

    double getDrawingAreaWidth();

    double getDrawingAreaHeight();

    double getRadius();

    void drawCircle(Point p);

    void drawLine(Point p1, Point p2);

    void showGraph();
}
