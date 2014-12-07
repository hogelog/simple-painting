package org.hogel.android.simplepainting.model;

import android.graphics.Path;
import android.graphics.Point;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class Line implements Serializable {
    private int color;

    @NonNull
    private List<Point> points;

    public Line(int color) {
        this(color, new ArrayList<Point>());
    }

    public void addPoint(Point point) {
        if (!points.isEmpty() && points.get(points.size() - 1).equals(point)) {
            return;
        }
        points.add(point);
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
}
