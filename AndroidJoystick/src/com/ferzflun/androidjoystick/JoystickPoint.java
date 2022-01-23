package com.ferzflun.androidjoystick;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;

public class JoystickPoint {
	private float mHorizontalAcceleration = 0;;
	private float mVerticalAcceleration = 0;
	
    int mVertexBuffer = 0;
    int mShaderProgram;

    int mScreenDimensionsUniformLocation;
    int mPositionUniformLocation;
    int mScaleUniformLocation;

    Vector2 mPosition = new Vector2();
    float mRadius = 0;

    float mScreenWidth;
    float mScreenHeight;
	
    public JoystickPoint(float screenWidth,
            			float screenHeight,
            			int shaderProgram){
    	
    	setRadius(100);
    	
    	mShaderProgram = shaderProgram;
    	mScreenWidth = screenWidth;
    	mScreenHeight = screenHeight;
    	
    	mPosition.mY += 300.0f;  
    	
        mPositionUniformLocation = GLES20.glGetUniformLocation(mShaderProgram, "positionUniform");
        mScaleUniformLocation = GLES20.glGetUniformLocation(mShaderProgram, "scaleUniform");
        mScreenDimensionsUniformLocation = GLES20.glGetUniformLocation(mShaderProgram, "screenDimensionsUniform");
        
        float circlePoints[] = new float[722];
        
        circlePoints[0] = 0;
        circlePoints[1] = 0;
        
        for(int i = 2, d = 0; i < 720; i+=2, ++d) {
        	circlePoints[i] = (float)Math.cos(Math.toRadians((float)(d)));
        	circlePoints[i + 1] = (float)Math.sin(Math.toRadians((float)(d)));
        }
        
        circlePoints[720] = circlePoints[2];
        circlePoints[721] = circlePoints[3];
        
        int vertexBuffers[] = new int[1];
        
        GLES20.glGenBuffers(1, vertexBuffers, 0);
        mVertexBuffer = vertexBuffers[0];
        
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mVertexBuffer);
        
	    FloatBuffer vertexBufferData = ByteBuffer.allocateDirect(circlePoints.length * 4)
	    .order(ByteOrder.nativeOrder()).asFloatBuffer();
	    
	    vertexBufferData.put(circlePoints).position(0);
        
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,
        					vertexBufferData.capacity() * 4,
        					vertexBufferData,
        					GLES20.GL_STATIC_DRAW);
    }
	public void setHorizontalAcceleration(float acceleration) {
		mHorizontalAcceleration = acceleration;
	}
	public void setVerticalAcceleration(float acceleration) {
		mVerticalAcceleration = acceleration;
	}
	public void setRadius(float radius){
		mRadius = radius;
	}
	public float getHorizontalAcceleration() {
		return mHorizontalAcceleration;
	}
	public float getVerticalAcceleration() {
		return mVerticalAcceleration;
	}
	public float getRadius(){
		return mRadius;
	}
	public void setScreenWidth(float width){
		mScreenWidth = width;
	}
	public void setScreenHeight(float height){
		mScreenHeight = height;
	}
	public float getScreenWidth(){
		return mScreenWidth;
	}
	public float getScreenHeight(){
		return mScreenHeight;
	}
	
	void draw() {
		GLES20.glFrontFace(GLES20.GL_CCW);
		GLES20.glUseProgram(mShaderProgram);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,
							mVertexBuffer);
		
	    GLES20.glEnableVertexAttribArray(0);
	    GLES20.glVertexAttribPointer(0,
	                          		2,
	                          		GLES20.GL_FLOAT,
	                          		false,
	                          		8,
	                          		0);
	    
	    mPosition.mX += mHorizontalAcceleration;
	    mPosition.mY += mVerticalAcceleration;
	    
	    if((mPosition.mX + mRadius) > mScreenWidth / 2.0f)
	    	mPosition.mX = mScreenWidth / 2.0f - mRadius;
	    
	    if((mPosition.mX - mRadius) < -(mScreenWidth / 2.0f))
	    	mPosition.mX = -(mScreenWidth / 2.0f) + mRadius;
	    
	    if((mPosition.mY + mRadius) > mScreenHeight / 2.0f)
	    	mPosition.mY = mScreenHeight / 2.0f - mRadius;
	    
	    if((mPosition.mY - mRadius) < -(mScreenHeight / 2.0f))
	    	mPosition.mY = -(mScreenHeight / 2.0f) + mRadius;
	    
	    GLES20.glUniform2f(mScreenDimensionsUniformLocation,
	                	mScreenWidth,
	                	mScreenHeight);
	    
	    GLES20.glUniform2f(mPositionUniformLocation,
	                	mPosition.mX,
	                	mPosition.mY);
	    
	    GLES20.glUniform2f(mScaleUniformLocation,
	                	mRadius,
	                	mRadius);
	    
	    GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 361);
	    GLES20.glDisableVertexAttribArray(0);
	}
}
