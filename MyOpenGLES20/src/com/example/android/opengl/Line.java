package com.example.android.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import android.opengl.GLES20;

public class Line {

	private FloatBuffer VertexBuffer;

	private final String vShaderCode = "uniform mat4 uMVPMatrix;"
			+ "attribute vec4 vPosition;" + "void main() {"
			+ "  gl_Position = uMVPMatrix * vPosition;" + "}";

	private final String fShaderCode = "precision mediump float;"
			+ "uniform vec4 vColor;" + "void main() {"
			+ "  gl_FragColor = vColor;" + "}";

	protected int GlProgram;
	protected int PositionHandle;
	protected int ColorHandle;
	protected int MVPMatrixHandle;

	// number of coordinates per vertex in this array
	static final int COORDS_PER_VERTEX = 3;
	private float LineCoords[] = initCoords();

	private final int VertexCount = LineCoords.length / COORDS_PER_VERTEX;
	private final int VertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per
															// vertex

	// Set color with red, green, blue and alpha (opacity) values
	float color[] = { 0.0f, 1.0f, 0.0f, 1.0f };

	public Line() {
		ByteBuffer bb = ByteBuffer.allocateDirect(LineCoords.length * 4);
		bb.order(ByteOrder.nativeOrder()); // use the device hardware's native byte order
		VertexBuffer = bb.asFloatBuffer(); // create a floating point buffer from the ByteBuffer
		
		VertexBuffer.put(LineCoords); // add the coordinates to the FloatBuffer
		VertexBuffer.position(0); // set the buffer to read the first coordinate

		int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vShaderCode);
		int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fShaderCode);

		GlProgram = GLES20.glCreateProgram(); // create empty OpenGL ES Program
		GLES20.glAttachShader(GlProgram, vertexShader); // add the vertex shader to program
		GLES20.glAttachShader(GlProgram, fragmentShader); // add the fragment shader to program
		GLES20.glLinkProgram(GlProgram); // creates OpenGL ES program  executables

	}

	public void draw(float[] mvpMatrix) {
		GLES20.glUseProgram(GlProgram); // Add program to OpenGL ES environment
		PositionHandle = GLES20.glGetAttribLocation(GlProgram, "vPosition"); // get handle to vertex shader's vPosition member
		GLES20.glEnableVertexAttribArray(PositionHandle); // Enable a handle to the triangle vertices
		GLES20.glVertexAttribPointer(PositionHandle, COORDS_PER_VERTEX,
				GLES20.GL_FLOAT, false, VertexStride, VertexBuffer); // Prepare the triangle coordinate data
		ColorHandle = GLES20.glGetUniformLocation(GlProgram, "vColor"); // get handle to fragment shader's vColor member
		GLES20.glUniform4fv(ColorHandle, 1, color, 0); // Set color for drawing the line

		// get handle to shape's transformation matrix
		MVPMatrixHandle = GLES20.glGetUniformLocation(GlProgram, "uMVPMatrix");
		MyGLRenderer.checkGlError("glGetUniformLocation");

		// Apply the projection and view transformation
		GLES20.glUniformMatrix4fv(MVPMatrixHandle, 1, false, mvpMatrix, 0);
		MyGLRenderer.checkGlError("glUniformMatrix4fv");

		
		GLES20.glDrawArrays(GLES20.GL_LINES, 0, VertexCount); // Draw 
		GLES20.glDisableVertexAttribArray(PositionHandle); // Disable vertex 
	}
	
	public static float[] initCoords() {
		ArrayList<Float> list = new ArrayList<Float>();
		
		for (int i = 0; i < 10; i++) {
			list.add(0.1f * (5-i)); // x
			list.add(0.5f); // y 
			list.add(0.0f); // z
			list.add(0.1f * (5-i)); // x
			list.add(-0.5f); // y
			list.add(0.0f); // z
		}

		// below for loop, is identical, we cut and paste, and switch x-y lines
		for (int i = 0; i < 10; i++) {
			list.add(0.5f); // x 
			list.add(0.1f * (5-i)); // y
			list.add(0.0f); // z
			list.add(-0.5f); // x
			list.add(0.1f * (5-i)); // y
			list.add(0.0f); // z
		}

		// com'n there should be an easier way to convert my list to a float array
		float x[] = new float[list.size()];
		int idx = 0;
		for (Float f : list) {
			x[idx++] = f;
		}
		return x;
	}
	

}