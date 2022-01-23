package com.ferzflun.androidjoystick;

public class Button {
	private float mWidth = 0;
	private float mHeight = 0;
	private boolean mTapped = false;
	private Vector2 mPosition = new Vector2();
	private Quaternion mRotation = new Quaternion();
	
	public Button(){}
	
	public void setWidth(float width){
		mWidth = width;
	}
	public void setHeight(float height){
		mHeight = height;
	}
	public float getWidth(){
		return mWidth;
	}
	public float getHeight(){
		return mHeight;
	}
	public Vector2 getPosition() {
		return mPosition;
	}
	public Quaternion getRotation() {
		return mRotation;
	}
	public void setTapped(boolean tapped) {
		mTapped = tapped;
	}
	public boolean isTapped() {
		return mTapped;
	}
	void tap(float x, float y) {
	    if(x >= mPosition.mX + -(mWidth / 2.0f) &&
	    x <= mPosition.mX + (mWidth / 2.0f) &&
	    y >= mPosition.mY + -(mHeight / 2.0f) &&
	    y <= mPosition.mY + (mHeight / 2.0f))
	    	mTapped = true;
	}
}
