package graphic_system.model;

import math.Matrix;

public class Coordinate {

	private double x;
	private double y;
	private final double z;

	private double xScn;
	private double yScn;
	private double zScn;

	public Coordinate(double x, double y) {
		this.x = x;
		this.y = y;
		this.z = 1;

		this.xScn = x;
		this.yScn = y;
		this.zScn = 1;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public Coordinate getWindowViewportTransformation(Window window,
			Viewport viewport) {
		return getWindowViewportTransformation(window, viewport, x, y);
	}

	public Coordinate getWindowViewportTransformationSCN(Window window,
			Viewport viewport) {
		return getWindowViewportTransformation(window, viewport, xScn, yScn);
	}

	private static Coordinate getWindowViewportTransformation(Window window,
			Viewport viewport, double x, double y) {
		double viewportX = (x - window.getXMin())
				/ (window.getXMax() - window.getXMin())
				* (viewport.getXMax() - viewport.getXMin());
		double viewportY = (1 - ((y - window.getYMin()) / (window.getYMax() - window
				.getXMin()))) * (viewport.getYMax() - viewport.getYMin());
		return new Coordinate(viewportX, viewportY);
	}

	public void transform(double[][] transformationMatrix) {
		final double[][] coordinateMatrix = matrix(x, y, z);
		final double[][] transformedCoordinateMatrix = Matrix.multiply(
				coordinateMatrix, transformationMatrix);

		x = transformedCoordinateMatrix[0][0];
		y = transformedCoordinateMatrix[0][1];
	}

	public void transformSCN(double[][] transformationMatrix) {
		final double[][] coordinateMatrix = matrix(xScn, yScn, zScn);
		final double[][] transformedCoordinateMatrix = Matrix.multiply(
				coordinateMatrix, transformationMatrix);

		xScn = transformedCoordinateMatrix[0][0];
		xScn = transformedCoordinateMatrix[0][1];
	}

	private static double[][] matrix(double x, double y, double z) {
		double[][] coordinateMatrix = new double[1][3];
		coordinateMatrix[0][0] = x;
		coordinateMatrix[0][1] = y;
		coordinateMatrix[0][2] = z;
		return coordinateMatrix;
	}

	public double getXScn() {
		return xScn;
	}

	public double getYScn() {
		return yScn;
	}

	public double getZScn() {
		return zScn;
	}

	public Coordinate getTransformed(double[][] transformationMatrix) {
		final double[][] coordinateMatrix = matrix(x, y, z);
		final double[][] transformedCoordinateMatrix = Matrix.multiply(
				coordinateMatrix, transformationMatrix);

		double transformedX = transformedCoordinateMatrix[0][0];
		double transformedY = transformedCoordinateMatrix[0][1];

		return new Coordinate(transformedX, transformedY);
	}

	public RegionCode getRegionCode(Window window) {
		RegionCode code = new RegionCode();
		if (y > window.getYMax()) {
			code.top = 1;
			code.all += 8;
		} else if (y < window.getYMin()) {
			code.bottom = 1;
			code.all += 4;
		}
		if (x > window.getXMax()) {
			code.right = 1;
			code.all += 2;
		} else if (x < window.getXMin()) {
			code.left = 1;
			code.all += 1;
		}
		return code;
	}
}
