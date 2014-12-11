package org.hogel.android.simplepainting.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.google.common.base.Optional;
import lombok.extern.java.Log;
import org.hogel.android.simplepainting.model.Line;

import java.util.List;

@Log
public class PaintView extends SurfaceView implements SurfaceHolder.Callback {
    public interface PaintListener {
        void surfaceChanged();

        void touchDown(float x, float y);

        void touchMove(float x, float y);

        void touchUp(float x, float y);
    }

    private static final float DEFAULT_WIDTH = 8.0f;

    private static final int DEFAULT_BACKGROUND = Color.WHITE;

    private Paint linePaint;

    private SurfaceHolder holder;

    private Optional<PaintListener> paintListener;

    public PaintView(Context context) {
        super(context);
        setup();
    }

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    private void setup() {
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
        if (paintListener.isPresent()) {
            paintListener.get().surfaceChanged();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!paintListener.isPresent()) {
            return super.onTouchEvent(event);
        }
        final int action = event.getAction();
        final PaintListener listener = paintListener.get();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                listener.touchDown(event.getX(), event.getY());
                return true;
            case MotionEvent.ACTION_MOVE:
                listener.touchMove(event.getX(), event.getY());
                return true;
            case MotionEvent.ACTION_UP:
                listener.touchUp(event.getX(), event.getY());
                return true;
        }
        return super.onTouchEvent(event);
    }

    public void updateCanvas(List<Line> lines) {
        final Canvas canvas = holder.lockCanvas();
        updateCanvas(lines, canvas);
        holder.unlockCanvasAndPost(canvas);
    }

    private void updateCanvas(List<Line> lines, Canvas canvas) {
        canvas.drawColor(DEFAULT_BACKGROUND);

        for (Line line : lines) {
            linePaint.setColor(line.getColor());
            canvas.drawPath(line.toPath(), linePaint);
        }
    }

    public void setPaintListener(PaintListener paintListener) {
        this.paintListener = Optional.of(paintListener);
    }
}
