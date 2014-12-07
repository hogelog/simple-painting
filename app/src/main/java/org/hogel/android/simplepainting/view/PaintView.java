package org.hogel.android.simplepainting.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import icepick.Icepick;
import icepick.Icicle;
import lombok.extern.java.Log;
import org.hogel.android.simplepainting.model.Line;
import org.roboguice.shaded.goole.common.base.Optional;

import java.util.ArrayList;

@Log
public class PaintView extends SurfaceView implements SurfaceHolder.Callback {
    private static final float DEFAULT_WIDTH = 8.0f;

    private static final int DEFAULT_BACKGROUND = Color.WHITE;

    @Icicle
    ArrayList<Line> lines;

    @Icicle
    Optional<Line> currentPath;

    private Paint linePaint;

    private SurfaceHolder holder;

    public PaintView(Context context) {
        super(context);
        setup();
    }

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return Icepick.saveInstanceState(this, super.onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(Icepick.restoreInstanceState(this, state));
    }

    private void setup() {
        lines = new ArrayList<Line>();
        currentPath = Optional.absent();

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(DEFAULT_WIDTH);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setStrokeJoin(Paint.Join.ROUND);

        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.holder = holder;
        updateCanvas();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();

        final Point point = new Point((int) event.getX(), (int) event.getY());

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!currentPath.isPresent()) {
                    Line line = new Line(Color.BLACK);
                    line.addPoint(point);
                    lines.add(line);
                    currentPath = Optional.of(line);
                    updateCanvas();
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (currentPath.isPresent()) {
                    Line line = currentPath.get();
                    line.addPoint(point);
                    updateCanvas();
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (currentPath.isPresent()) {
                    Line line = currentPath.get();
                    line.addPoint(point);
                    currentPath = Optional.absent();
                    updateCanvas();
                    return true;
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    private void updateCanvas() {
        final Canvas canvas = holder.lockCanvas();
        updateCanvas(canvas);
        holder.unlockCanvasAndPost(canvas);
    }

    private void updateCanvas(Canvas canvas) {
        canvas.drawColor(DEFAULT_BACKGROUND);

        for (Line line : lines) {
            linePaint.setColor(line.getColor());
            canvas.drawPath(line.toPath(), linePaint);
        }
    }
}
