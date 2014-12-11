package org.hogel.android.simplepainting.model;

import android.graphics.Color;
import android.graphics.Path;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class Line implements Serializable {
    private int color;

    private List<Point> points = new ArrayList<Point>();

    public Line() {
        this(Color.BLACK);
    }

    public Line(int color) {
        this.color = color;
    }

    public void addPoint(Point point) {
        if (!points.isEmpty() && points.get(points.size() - 1).equals(point)) {
            return;
        }
        points.add(point);
        supplementPoints();
    }

    public Path toPath() {
        final Path path = new Path();
        if (points.isEmpty()) {
            return path;
        }

        final Point point1 = points.get(0);
        path.moveTo(point1.x, point1.y);
        for (int i = 1; i < points.size(); ++i) {
            final Point point = points.get(i);
            path.lineTo(point.x, point.y);
        }
        return path;
    }

    public float[] toArray() {
        final float[] array = new float[points.size() * 2];
        for (int i = 0; i < points.size(); ++i) {
            final Point point = points.get(i);
            array[i * 2] = point.x;
            array[i * 2 + 1] = point.y;
        }
        return array;
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
    }
}
