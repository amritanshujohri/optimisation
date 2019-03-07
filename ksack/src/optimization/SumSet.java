package optimization;

public class SumSet implements Comparable<SumSet> {
	long productIDSum;
	long volumeUsed;
	long price;
	long weight;
	public SumSet(long productID, long volumeUsed, long price, long weight) {
		this.productIDSum = productID;
		this.volumeUsed = volumeUsed;
		this.price = price;
		this.weight = weight;
	}
	public SumSet() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public int compareTo(SumSet arg0) {
		// TODO Auto-generated method stub
		return Long.valueOf(this.volumeUsed).compareTo(arg0.volumeUsed);
	}
}
