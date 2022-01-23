package com.ferzflun.androidjoystick;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.opengl.GLES20;

public class Joystick {	
	private int mJoystickPositionUniformLocation;
	private int mJoystickPointPositionUniformLocation;
	private int mScreenDimensionsUniformLocation;
	private int mUVOffsetUniformLocation;
	private int mRotationUniformLocation;
	
	private JoystickPoint mJoystickPoint;
	
	private float mScreenWidth;
	private float mScreenHeight;
	
	private float mButtonWidth;
	private float mButtonHeight;
	
	private Button mUp = new Button();
	private Button mLeft = new Button();
	private Button mRight = new Button();
	private Button mDown = new Button();
	
	private int mVertexBuffer;
	private int mShaderProgram;
	private int mAtlasTexture;
	
	private Vector2 mPosition = new Vector2();
	
	Joystick(float x,
			float y,
			float screenWidth,
			float screenHeight,
			float buttonWidth,
			float buttonHeight,
			int atlasTexture,
			float atlasTextureWidth,
			float atlasTextureHeight,
			int shaderProgram,
			JoystickPoint joystickPoint) {
		mJoystickPositionUniformLocation = GLES20.glGetUniformLocation(shaderProgram, "joystickPositionUniform");
		mJoystickPointPositionUniformLocation = GLES20.glGetUniformLocation(shaderProgram, "positionUniform");
		mScreenDimensionsUniformLocation = GLES20.glGetUniformLocation(shaderProgram, "screenDimensionsUniform");
		mUVOffsetUniformLocation = GLES20.glGetUniformLocation(shaderProgram, "uvOffsetUniform");
		mRotationUniformLocation = GLES20.glGetUniformLocation(shaderProgram, "rotationUniform");
		
		mJoystickPoint = joystickPoint;
		mShaderProgram = shaderProgram;
		mAtlasTexture = atlasTexture;
		
		mScreenWidth = screenWidth;
		mScreenHeight = screenHeight;
		
		mButtonWidth = buttonWidth;
		mButtonHeight = buttonHeight;

	    TextureRegion region = TextureRegion.create(0,
	    											0,
	    											atlasTextureWidth/2,
	    											atlasTextureHeight,
	    											atlasTextureWidth,
	    											atlasTextureHeight);
	    
	    float halfButtonWidth = mButtonWidth / 2.0f;
	    float halfButtonHeight = mButtonHeight / 2.0f;
	    
	    float vertices[] = {-halfButtonWidth, -halfButtonHeight, region.mBottomLeft.mX, region.mBottomLeft.mY,
	                        -halfButtonWidth, halfButtonHeight, region.mTopLeft.mX, region.mTopLeft.mY,
	                        halfButtonWidth, -halfButtonHeight, region.mBottomRight.mX, region.mBottomRight.mY,
	                        halfButtonWidth, halfButtonHeight, region.mTopRight.mX, region.mTopRight.mY};
	    
	    int vertexBuffers[] = new int[1];
	    GLES20.glGenBuffers(1, vertexBuffers, 0);
	    mVertexBuffer = vertexBuffers[0];
	    
	    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mVertexBuffer);
	    
	    FloatBuffer vertexBufferData = ByteBuffer.allocateDirect(vertices.length * 4)
	    .order(ByteOrder.nativeOrder()).asFloatBuffer();
	    
	    vertexBufferData.put(vertices).position(0);
	    GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,
	    					vertexBufferData.capacity() * 4,
	    					vertexBufferData,
	    					GLES20.GL_STATIC_DRAW);
	    
	    mUp.setWidth(mButtonWidth);
	    mUp.setHeight(mButtonHeight);
	    
	    mLeft.setWidth(mButtonWidth);
	    mLeft.setHeight(mButtonHeight);
	    
	    mRight.setWidth(mButtonWidth);
	    mRight.setHeight(mButtonHeight);
	    
	    mDown.setWidth(mButtonWidth);
	    mDown.setHeight(mButtonHeight);
	    
	    Vector2 upPosition = mUp.getPosition();
	    Vector2 leftPosition = mLeft.getPosition();
	    Vector2 rightPosition = mRight.getPosition();
	    Vector2 downPosition = mDown.getPosition();
	    
	    upPosition.mX = 0.0f;
	    upPosition.mY = mButtonHeight;
	    
	    leftPosition.mX = -mButtonWidth;
	    leftPosition.mY = 0.0f;
	    
	    rightPosition.mX = mButtonWidth;
	    rightPosition.mY = 0.0f;
	    
	    downPosition.mX = 0.0f;
	    downPosition.mY = -mButtonHeight;
	    
	    Quaternion upRotation = mUp.getRotation();
	    Quaternion leftRotation = mLeft.getRotation();
	    Quaternion rightRotation = mRight.getRotation();
	    Quaternion downRotation = mDown.getRotation();
	    
	    upRotation.mX = 0.0f;
	    upRotation.mY = 0.0f;
	    upRotation.mZ = (float)Math.sin(Math.toRadians(0.0f));
	    upRotation.mW = (float)Math.cos(Math.toRadians(0.0f));
	    
	    leftRotation.mX = 0.0f;
	    leftRotation.mY = 0.0f;
	    leftRotation.mZ = (float)Math.sin(0.5f * Math.toRadians(90.0f));
	    leftRotation.mW = (float)Math.cos(0.5f * Math.toRadians(90.0f));
	    
	    rightRotation.mX = 0.0f;
	    rightRotation.mY = 0.0f;
	    rightRotation.mZ = (float)Math.sin(0.5f * -Math.toRadians(90.0f));
	    rightRotation.mW = (float)Math.cos(0.5f * -Math.toRadians(90.0f));
	    
	    downRotation.mX = 0.0f;
	    downRotation.mY = 0.0f;
	    downRotation.mZ = (float)Math.sin(0.5f * Math.toRadians(180.0f));
	    downRotation.mW = (float)Math.cos(0.5f * Math.toRadians(180.0f));
	    
	    mPosition.mY = -180;
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
	
	//complete
	public void isButtonTapped(float x, float y){
	    float px = x - (mScreenWidth / 2.0f);
	    float py = (y - (mScreenHeight / 2.0f)) * -1;
	    
	    float rx = px - mPosition.mX;
	    float ry = py - mPosition.mY;
	    
	    mUp.tap(rx, ry);
	    mLeft.tap(rx, ry);
	    
	    mRight.tap(rx, ry);
	    mDown.tap(rx, ry);
	}
	
	//complete
	public void tappingEnded() {
	    if(mUp.isTapped())
	        mUp.setTapped(false);
	    
	    else if(mLeft.isTapped())
	        mLeft.setTapped(false);
	    
	    else if(mRight.isTapped())
	        mRight.setTapped(false);
	    
	    else if(mDown.isTapped())
	        mDown.setTapped(false);
	}
	
	//complete
	public void tapInterval(){
	    final float acceleration = 1.0f;
	    
	    if(mUp.isTapped())
	        mJoystickPoint.setVerticalAcceleration(mJoystickPoint.getVerticalAcceleration() + acceleration);
	    
	    else if(mDown.isTapped())
	        mJoystickPoint.setVerticalAcceleration(mJoystickPoint.getVerticalAcceleration() - acceleration);
	    
	    else if(mLeft.isTapped())
	        mJoystickPoint.setHorizontalAcceleration(mJoystickPoint.getHorizontalAcceleration() - acceleration);
	    
	    else if(mRight.isTapped())
	        mJoystickPoint.setHorizontalAcceleration(mJoystickPoint.getHorizontalAcceleration() + acceleration);
	}
	
	//complete
	public void draw(){
		GLES20.glFrontFace(GLES20.GL_CW);
	    GLES20.glUseProgram(mShaderProgram);
	    
	    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mAtlasTexture);
	    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mVertexBuffer);
	    
	    GLES20.glEnableVertexAttribArray(0);
	    GLES20.glVertexAttribPointer(0, 2, GLES20.GL_FLOAT, false, 16, 0);
	    
	    GLES20.glEnableVertexAttribArray(1);
	    GLES20.glVertexAttribPointer(1, 2, GLES20.GL_FLOAT, false, 16, 8);
	    
	    GLES20.glUniform2f(mScreenDimensionsUniformLocation,
	                		mScreenWidth,
	                		mScreenHeight);
	    
	    GLES20.glUniform2f(mJoystickPositionUniformLocation, mPosition.mX, mPosition.mY);
	    
	    Vector2 upPosition = mUp.getPosition();
	    GLES20.glUniform2f(mJoystickPointPositionUniformLocation, upPosition.mX, upPosition.mY);
	    drawButton(mUp);
	    
	    Vector2 leftPosition = mLeft.getPosition();
	    GLES20.glUniform2f(mJoystickPointPositionUniformLocation, leftPosition.mX, leftPosition.mY);
	    drawButton(mLeft);
	    
	    Vector2 rightPosition = mRight.getPosition();
	    GLES20.glUniform2f(mJoystickPointPositionUniformLocation, rightPosition.mX, rightPosition.mY);
	    drawButton(mRight);
	    
	    Vector2 downPosition = mDown.getPosition();
	    GLES20.glUniform2f(mJoystickPointPositionUniformLocation, downPosition.mX, downPosition.mY);
	    drawButton(mDown);
	    
	    GLES20.glDisableVertexAttribArray(0);
	    GLES20.glDisableVertexAttribArray(1);
	}
	
	//complete
	private void drawButton(Button button){
	    if(button.isTapped())
	    	GLES20.glUniform2f(mUVOffsetUniformLocation, 0.5f, 0.0f);
	    else
	    	GLES20.glUniform2f(mUVOffsetUniformLocation, 0.0f, 0.0f);
	    
	    Quaternion buttonRotation = button.getRotation();
	    
	    FloatBuffer quaternionBuffer = ByteBuffer.allocateDirect(16)
	    .order(ByteOrder.nativeOrder()).asFloatBuffer();
	    
	    quaternionBuffer.put(new float[]{buttonRotation.mX,
										buttonRotation.mY,
										buttonRotation.mZ,
										buttonRotation.mW}).position(0);
	    
	    GLES20.glUniform4fv(mRotationUniformLocation, 1, quaternionBuffer);
	    GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
	}
}
