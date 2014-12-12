package org.hogel.android.simplepainting.model;

import android.graphics.Color;
import android.graphics.Path;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class Line implements Serializable {
    private final int color;

    private final List<Point> points = new ArrayList<Point>();

    private final Path path = new Path();

    public Line() {
        this(Color.BLACK);
    }

    public Line(int color) {
        this.color = color;
    }

    public void addPoint(Point point) {
        if (points.isEmpty()) {
            path.moveTo(point.getX(), point.getY());
            points.add(point);
        } else {
            points.add(point);
            supplementPoints();
        }
    }

    private void supplementPoints() {
        final int size = points.size();
        if (size < 3) {
            return;
        }
        final Point point1 = points.get(size - 3);
        final Point point2 = points.get(size - 2);
        final Point point3 = points.get(size - 1);

        point2.x = (point1.x + point2.x + point3.x) / 3;
        point2.y = (point1.y + point2.y + point3.y) / 3;
        path.lineTo(point2.x, point2.y);
    }
}
