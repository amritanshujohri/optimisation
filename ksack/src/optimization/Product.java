package optimization;

public class Product implements Comparable<Product>{
	//product ID, price, length, width, height, weight
	final int productID;
	final int price;
	final int length;
	final int width;
	final int height;
	final int weight;
	final long volume;
	final double densityW;
	final double priceDensity;
	final double PW;
	static enum Trait {
		VOLUME,
		WEIGHT,
		PRICE,
		PRICE_DENSITY,
		WEIGHT_DENSITY;
	};
	public static Trait ORDERED_BY = Trait.VOLUME;
	Product(String []str)  {
		int i = 0;
		this.productID =  Integer.parseInt(str[i++]);
		this.price = Integer.parseInt(str[i++]);
		this.length = Integer.parseInt(str[i++]);
		this.width =  Integer.parseInt(str[i++]);
		this.height = Integer.parseInt(str[i++]);
		this.weight = Integer.parseInt(str[i++]);
		this.volume = length*width*height;
		this.densityW = this.weight / this.volume;
		this.priceDensity = this.price / this.volume;
		this.PW = this.price / this.weight;
	}
	@Override
	public int compareTo(Product arg0) {
		switch(ORDERED_BY) {
		case VOLUME:
			return Long.valueOf(this.volume).compareTo(arg0.volume);
		case PRICE_DENSITY:
			return Double.valueOf(this.priceDensity).compareTo(arg0.priceDensity);
		default:
			return 0;
		}		
	}


}
