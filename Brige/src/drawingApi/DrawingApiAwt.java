package drawingApi;

import model.Point;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.HashSet;

import javax.swing.*;

import static config.Config.*;

public class DrawingApiAwt extends JFrame implements DrawingApi {

    private final HashSet<Shape> setOfShapes;

    public DrawingApiAwt() {
        setSize(new Dimension((int) getDrawingAreaWidth(), (int) getDrawingAreaHeight()));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setOfShapes = new HashSet<>();
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
        Shape circle = new Ellipse2D.Double(p.x - VERTEX_SIZE / 2, p.y - VERTEX_SIZE / 2, VERTEX_SIZE, VERTEX_SIZE);
        setOfShapes.add(circle);
    }

    @Override
    public void drawLine(Point p1, Point p2) {
        Shape line = new Line2D.Double(p1.x, p1.y, p2.x, p2.y);
        setOfShapes.add(line);
    }

    @Override
    public void showGraph() {
        setVisible(true);
        setTitle("Graph");
        JPanel p = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                for (Shape shape : setOfShapes) {
                    g2.draw(shape);
                    g2.fill(shape);
                }
            }
        };
        this.getContentPane().add(p);
    }

}
