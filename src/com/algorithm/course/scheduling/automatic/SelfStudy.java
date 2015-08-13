package com.algorithm.course.scheduling.automatic;

public class SelfStudy {
	private int mode;

	private double LINE_A;
	private double LINE_B;

	private double COSIC_A;
	private double COSIC_C;

	private double COS_A;
	private double COS_a;
	private double COS_B;

	public SelfStudy(Point start, Point end, int mode) {
		this.mode = mode;
		switch (this.mode) {
		case 0: {
			this.LINE_A = (start.Y - end.Y) / (start.X - end.X);
			this.LINE_B = start.Y - LINE_A * start.X;
			break;
		}
		case 1: {
			this.COSIC_A = (start.Y - end.Y)
					/ (start.X * start.X - end.X * end.X);
			this.COSIC_C = start.Y - this.COSIC_A * start.X * start.X;
			break;
		}
		case 2: {
			this.COS_a = Math.PI / 2 / end.X;
			this.COS_A = start.Y - end.Y;
			this.COS_B = end.Y;
			break;
		}
		default: {
			this.LINE_A = start.Y + (start.Y - end.Y) / (start.X - end.X);
		}
		}
	}

	public double run(long x) {
		switch (this.mode) {
		case 0:
			return linner_study(x);
		case 1:
			return conic_study(x);
		case 2:
			return cos_study(x);
		default:
			return linner_study(x);
		}
	}

	private double linner_study(long x) {
		return LINE_A * x + LINE_B;
	}

	private double conic_study(long x) {

		return this.COSIC_A * x * x + this.COSIC_C;
	}

	private double cos_study(long x) {
		return this.COS_A * Math.cos(x * this.COS_a) + this.COS_B;
	}
}

class Point {
	public long X;
	public double Y;

	public Point(long X, double Y) {
		this.X = X;
		this.Y = Y;
	}
}
