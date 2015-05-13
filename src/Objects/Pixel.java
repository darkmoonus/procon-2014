package Objects;

import Sources.ColorFormulas;

public class Pixel {
	private int red;
	private int green;
	private int blue;
	public Pixel() {
		setRed(0);
		setGreen(0);
		setBlue(0);
	}
	public int abs(int x, int y) {
		if(x > y) return x - y;
		return y - x;
	}
	public Pixel(int red, int green, int blue) {
		setRed(red);
		setGreen(green);
		setBlue(blue);
	}
	public double compareWith(Pixel pixel) {
		return (Math.sqrt((this.getRed() - pixel.getRed()) * (this.getRed() - pixel.getRed())
		+ (this.getBlue() - pixel.getBlue()) * (this.getBlue() - pixel.getBlue())
		+ (this.getGreen() - pixel.getGreen()) * (this.getGreen() - pixel.getGreen()))) ;
	}
	public double compareWith2(Pixel pixel) {
		return abs(pixel.getRed(), this.getRed()) + abs(pixel.getBlue(), this.getBlue()) + abs(pixel.getGreen(), this.getGreen());
	}
	public double compareWith3(Pixel pixel) {
		int diffRed   = Math.abs(pixel.getRed() - this.getRed());
		int diffGreen = Math.abs(pixel.getGreen() - this.getGreen());
		int diffBlue  = Math.abs(pixel.getBlue() - this.getBlue());
		float pctDiffRed   = (float)diffRed   / 255;
		float pctDiffGreen = (float)diffGreen / 255;
		float pctDiffBlue   = (float)diffBlue  / 255;
		return (pctDiffRed + pctDiffGreen + pctDiffBlue) / 3 * 100;
	}
	public double compareWithDeltaE(Pixel pixel) {
		return ColorFormulas.DoFullCompare(this.getRed(), this.getGreen(), this.getBlue(), pixel.getRed(), pixel.getGreen(), pixel.getBlue());
	}
	
	public int getRed() {
		return red;
	}
	public void setRed(int red) {
		this.red = red;
	}
	public int getGreen() {
		return green;
	}
	public void setGreen(int green) {
		this.green = green;
	}
	public int getBlue() {
		return blue;
	}
	public void setBlue(int blue) {
		this.blue = blue;
	}
	@Override
	public String toString() {
		return "["+ red + "," + green + "," + blue + "]";
	}
}
