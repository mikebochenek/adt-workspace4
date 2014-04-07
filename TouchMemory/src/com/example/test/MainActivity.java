package com.example.test;

import java.util.Arrays;
import java.util.Collections;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private static final String TAG = "MyPuzzleActivity";
	
	private PuzzleTile currentSelection;
	private int selectionCount;
	private PuzzleTile[] tiles = new PuzzleTile[8];
	private ImageView[] images = new ImageView[8];
	private final int[] imageIds = {R.id.imageView1, R.id.imageView2, R.id.imageView3, R.id.imageView4, R.id.imageView5, R.id.imageView6, R.id.imageView7, R.id.imageView8};
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	
    	generateTiles();
    	
    	for (int i = 0; i < imageIds.length; i++) {
        	ImageView imgView = (ImageView) findViewById(imageIds[i]);
        	images[i] = imgView;
        	
    		imgView.setOnTouchListener(new OnTouchListener() {
    			public boolean onTouch(View v, MotionEvent event) {
    				
    				if (v instanceof ImageView) {
    					
    					PuzzleTile tile = tiles[findIndex((ImageView)v)];
    					if (tile.isFaceup() && !tile.isMatched()) { // deselect a non-matched tile
    						((ImageView)v).setImageResource(R.drawable.question);
    						tile.setFaceup(false);
    						currentSelection = null;
    						selectionCount--;
    					} else if (selectionCount < 2) { 
            		    	((ImageView)v).setImageResource(tile.getImageId());
            		    	tile.setFaceup(true);
            		    	tile.setLastTouchTS(event.getDownTime());
            		    	
            		    	if (currentSelection != null && tile.getImageId() == currentSelection.getImageId()) {
            		    		tile.setMatched(true);
            		    		currentSelection.setMatched(true);
            		    		currentSelection = null;
        						selectionCount = 0;
        						//TODO animate a border or something
        						
        						if (getMatchedCount() == tiles.length) {
        							//TODO you've won the game
        					    	generateTiles(); // but this isn't enough because we'd need to redraw etc.
        						}
        						
            		    	} else {
                		    	currentSelection = tile;
                		    	selectionCount++;
                		    	
                		    	if (selectionCount == 2) {
                		    		Log.i(TAG, "we should probably auto-hide after a delay");

                		    		//http://stackoverflow.com/questions/15874117/how-to-set-delay-in-android
                		    		final Handler handler = new Handler();
                		    		handler.postDelayed(new Runnable() {
                		    		    @Override
                		    		    public void run() {
                        		    		Log.i(TAG, "oK now we hide");

                		    		        // Do something after 5s = 5000ms
                		    		        // buttons[inew][jnew].setBackgroundColor(Color.BLACK);
                		    		    }
                		    		}, 5000);
                		    		
                		    	}
            		    	}
    					} else {
    						Log.i(TAG, "you idiot, you already selected two");
    					}
    					
    				}
    				
    				Log.i(TAG, getGameStateString());
    				
    				return false;
    			}
    		});
    	}
    	

        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	private void generateTiles() {
    	for (int i = 0; i < imageIds.length; i++) {
    		tiles[i] = new PuzzleTile();
    	}
    	tiles[0].setImageId(R.drawable.luke);
    	tiles[1].setImageId(R.drawable.luke);
    	tiles[2].setImageId(R.drawable.babcia);
    	tiles[3].setImageId(R.drawable.babcia);
    	tiles[4].setImageId(R.drawable.dziadek);
    	tiles[5].setImageId(R.drawable.dziadek);
    	tiles[6].setImageId(R.drawable.daddy);
    	tiles[7].setImageId(R.drawable.daddy);
    	
    	Collections.shuffle(Arrays.asList(tiles));
    	
	}
    
    private int findIndex(ImageView v) {
    	for (int i = 0; i < images.length; i++) {
    		if (v.equals(images[i])) {
    			return i;
    		}
    	}
    	return -1;
    }
    
    private String getGameStateString() {
    	return "selectCnt=" + selectionCount + " matched=" + getMatchedCount();
    }
    
    private int getMatchedCount() {
    	int count = 0;
    	for (PuzzleTile tile : tiles) {
    		if (tile.isMatched()) {
    			count++;
    		}
    	}
    	return count;
    }
}
