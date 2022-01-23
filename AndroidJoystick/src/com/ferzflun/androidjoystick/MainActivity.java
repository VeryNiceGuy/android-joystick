package com.ferzflun.androidjoystick;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class MainActivity extends Activity {

	private GLSurfaceView mGLSurfaceView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        mGLSurfaceView = new FerzGLSurfaceView(this);
        setContentView(mGLSurfaceView);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        mGLSurfaceView.onResume();
    }
     
    @Override
    protected void onPause() {
        super.onPause();
        mGLSurfaceView.onPause();
    }
}
