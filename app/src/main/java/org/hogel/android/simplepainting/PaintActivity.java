package org.hogel.android.simplepainting;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.google.common.base.Optional;
import icepick.Icepick;
import icepick.Icicle;
import org.hogel.android.simplepainting.model.Line;
import org.hogel.android.simplepainting.model.Point;
import org.hogel.android.simplepainting.view.PaintView;

import java.util.ArrayList;

public class PaintActivity extends Activity {

    @InjectView(R.id.paint_view)
    PaintView paintView;

    @Icicle
    ArrayList<Line> lines;

    Optional<Line> currentPath = Optional.absent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
        if (savedInstanceState == null) {
            lines = new ArrayList<>();
        }

        setContentView(R.layout.activity_paint);
        ButterKnife.inject(this);
        setup();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setup() {
        paintView.setPaintListener(new PaintView.PaintListener() {
            @Override
            public void surfaceChanged() {
                paintView.updateCanvas(lines);
            }

            @Override
            public void touchDown(float x, float y) {
                final Point point = new Point(x, y);
                if (!currentPath.isPresent()) {
                    Line line = new Line(Color.BLACK);
                    line.addPoint(point);
                    lines.add(line);
                    currentPath = Optional.of(line);
                    paintView.updateCanvas(lines);
                }
            }

            @Override
            public void touchMove(float x, float y) {
                final Point point = new Point(x, y);

                if (currentPath.isPresent()) {
                    Line line = currentPath.get();
                    line.addPoint(point);
                    paintView.updateCanvas(lines);
                }
            }

            @Override
            public void touchHistoricalMove(float x, float y) {
                final Point point = new Point(x, y);

                if (currentPath.isPresent()) {
                    Line line = currentPath.get();
                    line.addPoint(point);
                }
            }

            @Override
            public void touchUp(float x, float y) {
                final Point point = new Point(x, y);

                if (currentPath.isPresent()) {
                    Line line = currentPath.get();
                    line.addPoint(point);
                    currentPath = Optional.absent();
                    paintView.updateCanvas(lines);
                }
            }
        });
    }
}
