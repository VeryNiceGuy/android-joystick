package com.ferzflun.androidjoystick;

public class TextureRegion {
	
	public Vector2 mBottomLeft = new Vector2();
	public Vector2 mTopLeft = new Vector2();
	public Vector2 mBottomRight = new Vector2();
	public Vector2 mTopRight = new Vector2();
	
	public static TextureRegion create(float x,
            							float y,
            							float width,
            							float height,
            							float textureWidth,
            							float textureHeight){
		
		TextureRegion region = new TextureRegion();
		
		float u1 = x / textureWidth;
		float v1 = y / textureHeight;
		float u2 = u1 + width / textureWidth ;
		float v2 = v1 + height / textureHeight;
	    
	    region.mBottomLeft.mX = u1;
	    region.mBottomLeft.mY = v2;
	    
	    region.mTopLeft.mX = u1;
	    region.mTopLeft.mY = v1;
	    
	    region.mBottomRight.mX = u2;
	    region.mBottomRight.mY = v2;
	    
	    region.mTopRight.mX = u2;
	    region.mTopRight.mY = v1;
	    
	    return region;
	}
}
