package com.example.test;

public class PuzzleTile {

	private boolean matched;
	private boolean faceup;
	private int imageId;
	
	public boolean isMatched() {
		return matched;
	}
	public void setMatched(boolean matched) {
		this.matched = matched;
	}
	public boolean isFaceup() {
		return faceup;
	}
	public void setFaceup(boolean faceup) {
		this.faceup = faceup;
	}
	public int getImageId() {
		return imageId;
	}
	public void setImageId(int imageId) {
		this.imageId = imageId;
	}
}
