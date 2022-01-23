package com.ferzflun.androidjoystick;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;

public class FerzGLRenderer implements GLSurfaceView.Renderer {

	private final static String vertexShaderForJoystick =
	"attribute vec2 positionAttrib; \n"+
	"attribute vec2 uv0; \n"+
	"varying vec2 uv; \n"+

	"uniform vec2 joystickPositionUniform; \n"+
	"uniform vec2 positionUniform; \n"+
	"uniform vec2 screenDimensionsUniform; \n"+
	"uniform vec4 rotationUniform; \n"+

	"void main() \n"+
	"{"+
		"vec2 buttonAbsolutePosition = joystickPositionUniform + positionUniform;"+
    
		"vec3 vector = vec3(positionAttrib.x + buttonAbsolutePosition.x, \n"+
							"positionAttrib.y + buttonAbsolutePosition.y, \n"+
							"0.0); \n"+
    
		"vector.xy -= buttonAbsolutePosition; \n"+
    
		"vec3 quatVector1 = cross(rotationUniform.xyz, vector); \n"+
		"vec3 quatVector2 = cross(rotationUniform.xyz, quatVector1); \n"+
    
		"quatVector1 *= 2.0 * rotationUniform.w; \n"+
		"quatVector2 *= 2.0; \n"+
    
		"vec3 rotatedVector = vector + quatVector1 + quatVector2; \n"+
		"rotatedVector.xy -= -buttonAbsolutePosition; \n"+
    
		"gl_Position = vec4(rotatedVector.x * (2.0 / screenDimensionsUniform.x), \n"+
							"rotatedVector.y * (2.0 / screenDimensionsUniform.y), \n"+
							"0.0, \n"+
							"1.0); \n"+
		"uv = uv0; \n"+
	"}";
	
	private final static String fragmentShaderForJoystick =
	"precision mediump float; \n"+
	"varying vec2 uv; \n"+
	"uniform sampler2D baseMap; \n"+
	"uniform vec2 uvOffsetUniform; \n"+

	"void main() \n"+
	"{ \n"+
		"vec2 uvWithOffset = uv + uvOffsetUniform; \n"+
		"gl_FragColor = texture2D(baseMap, uvWithOffset); \n"+
	"}";
	
	private final static String vertexShaderForJoystickPoint = 
	"attribute vec2 positionAttrib; \n"+
	"uniform vec2 positionUniform; \n"+
	"uniform vec2 scaleUniform; \n"+
	"uniform vec2 screenDimensionsUniform; \n"+

	"void main() \n"+
	"{ \n"+
		"gl_Position = vec4((positionAttrib.x * scaleUniform.x + positionUniform.x) * (2.0 / screenDimensionsUniform[0]), \n"+
                       	"(positionAttrib.y * scaleUniform.y + positionUniform.y) * (2.0 / screenDimensionsUniform[1]), \n"+
                       	"0.0, \n"+
                       	"1.0); \n"+
	"}";
	
	private final static String fragmentShaderForJoystickPoint = 
	"precision mediump float;  \n"+

	"void main() \n"+
	"{ \n"+
		"gl_FragColor = vec4(0.0, 0.0, 0.5, 0.7); \n"+
	"}";
	
	private Joystick mJoystick;
	private JoystickPoint mJoystickPoint;
	private Bitmap mJoystickBitmap;
	
	public FerzGLRenderer(Bitmap joystickBitmap){
		mJoystickBitmap = joystickBitmap;
	}
	
	public Joystick getJoystick()
	{
		return mJoystick;
	}
	
	@Override
	public void onDrawFrame(GL10 arg0) {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		
		mJoystickPoint.draw();
		mJoystick.draw();
	}

	@Override
	public void onSurfaceChanged(GL10 arg0, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
		
		mJoystick.setScreenWidth(width);
		mJoystick.setScreenHeight(height);
		
		mJoystickPoint.setScreenWidth(width);
		mJoystickPoint.setScreenHeight(height);
	}

	@Override
	public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
		GLES20.glEnable( GLES20.GL_CULL_FACE );
		GLES20.glEnable(GLES20.GL_BLEND);
		
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		GLES20.glBlendFunc (GLES20.GL_ONE, GLES20.GL_ONE);
		
		int programHandleForJoystick = GLES20.glCreateProgram();
		int programHandleForJoystickPoint = GLES20.glCreateProgram();
		
		int vertexShaderHandleForJoystick = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
		GLES20.glShaderSource(vertexShaderHandleForJoystick, vertexShaderForJoystick);
		GLES20.glCompileShader(vertexShaderHandleForJoystick);
		
		int fragmentShaderHandleForJoystick = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
		GLES20.glShaderSource(fragmentShaderHandleForJoystick, fragmentShaderForJoystick);
		GLES20.glCompileShader(fragmentShaderHandleForJoystick);
		
		int vertexShaderHandleForJoystickPoint = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
		GLES20.glShaderSource(vertexShaderHandleForJoystickPoint, vertexShaderForJoystickPoint);
		GLES20.glCompileShader(vertexShaderHandleForJoystickPoint);
		
		int fragmentShaderHandleForJoystickPoint = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
		GLES20.glShaderSource(fragmentShaderHandleForJoystickPoint, fragmentShaderForJoystickPoint);
		GLES20.glCompileShader(fragmentShaderHandleForJoystickPoint);
		
		GLES20.glAttachShader(programHandleForJoystick, vertexShaderHandleForJoystick);
	    GLES20.glAttachShader(programHandleForJoystick, fragmentShaderHandleForJoystick);
	    
		GLES20.glAttachShader(programHandleForJoystickPoint, vertexShaderHandleForJoystickPoint);
	    GLES20.glAttachShader(programHandleForJoystickPoint, fragmentShaderHandleForJoystickPoint);
		
	    GLES20.glBindAttribLocation(programHandleForJoystick, 0, "positionAttrib");
	    GLES20.glBindAttribLocation(programHandleForJoystick, 1, "uv0");
	    GLES20.glBindAttribLocation(programHandleForJoystickPoint, 0, "positionAttrib");
	    
	    GLES20.glLinkProgram(programHandleForJoystick);
	    GLES20.glLinkProgram(programHandleForJoystickPoint);
    
		int[] textures = new int[1];
		GLES20.glGenTextures(1, textures, 0);
		GLES20.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
 
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,
        					0,
        					mJoystickBitmap,
        					0);
        	
	    mJoystickPoint = new JoystickPoint(0,
	    								0,
	    								programHandleForJoystickPoint);
	    
	    mJoystick = new Joystick(0,
	    						0,
	    						0,
	    						0,
	    						180,
	    						180,
	    						textures[0],
	    						mJoystickBitmap.getWidth(),
	    						mJoystickBitmap.getHeight(),
	    						programHandleForJoystick,
	    						mJoystickPoint);
	    
	    mJoystickBitmap.recycle();
	}

}
