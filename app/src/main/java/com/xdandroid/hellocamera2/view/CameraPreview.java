package com.xdandroid.hellocamera2.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

@SuppressLint("ViewConstructor")
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    /**
     * 需要让Activity/Fragment处理Throwable时，提供的回调监听类.
     */
    public interface ThrowableListener {
        public void onThrowable(Throwable throwable);
    }

    private SurfaceHolder mHolder;
    private Camera mCamera;
    private ThrowableListener throwableListener;

    public CameraPreview(Context context, Camera camera, ThrowableListener l) {
        super(context);
        mCamera = camera;
        mHolder = getHolder();
        mHolder.addCallback(this);
        throwableListener = l;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
            if (throwableListener != null) throwableListener.onThrowable(e);
        }
    }

    /**
     * 由Activity/Fragment处理Camera的释放，此处为空实现.
     * @param holder SurfaceHolder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if (mHolder.getSurface() == null) return;
        try {
            mCamera.stopPreview();
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
            if (throwableListener != null) throwableListener.onThrowable(e);
        }
    }
}