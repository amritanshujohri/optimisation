package optimization;

public class nodeQQ {
	final int i;
	final long priceTillNow;
	final long remainingVolume;
	final long productIDSum;
	final long weight;
	boolean isRightPushed;
	boolean isTerminal;
	static int N = 20000;
	public nodeQQ(int i, long currentPrice, long remainingVolume, long productIDSum, long weight) {
		this.i = i;
		assert(i <= N);
		this.priceTillNow = currentPrice;
		this.remainingVolume = remainingVolume;
		this.productIDSum = productIDSum;
		this.weight = weight;
		this.isTerminal = (remainingVolume == 0 || i >= N)? true : false;
	}
	static void setTerminal(int n) {
		N = n;
	}
	static nodeQQ Build(int i, long currentPrice, long remainingVolume, long productIDSum, long weight) {
		return new nodeQQ(i, currentPrice, remainingVolume, productIDSum, weight);
	}
}
