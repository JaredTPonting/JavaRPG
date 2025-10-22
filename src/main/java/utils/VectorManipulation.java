package utils;

import java.awt.geom.Point2D;

public class VectorManipulation {
    public Point2D.Double rotate(double x, double y, double angleDegrees) {
        double angleRad = Math.toRadians(angleDegrees);
        double newX = Math.cos(angleRad) * x - (Math.sin(angleRad) * y);
        double newY = Math.sin(angleRad) * x + (Math.cos(angleRad) * y);
        return new Point2D.Double(newX, newY);
    }
    public Point2D.Double normalise(double x, double y) {
        double magnitude = Math.sqrt(x*x + y*y);
        double newX = x / magnitude;
        double newY = y / magnitude;
        return new Point2D.Double(newX, newY);
    }
    public Point2D.Double rotateNormalise(double x, double y, double angleDegrees) {
        Point2D.Double angle = rotate(x, y, angleDegrees);
        return normalise(angle.getX(), angle.getY());
    }
}
