package optimization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public abstract class ToteSack  {
	//  45 centimeters long, 30 wide and 35 high
	final int LENGTH;
	final int BREADTH;
	final int HEIGHT;
	final int D1, D2, D3;
	final long V;
	final Product[] ITEMS;
	
	
	long[] suffixPrice;
	long[] suffixMinVolume;
	volatile long maxPrice = Long.MIN_VALUE;
	long bestWeight = Long.MAX_VALUE;
	long productIDSum = 0;

	int countUpdated = 0;
	int countPruned = 0;
	long nodesVisited = 0;
	long timeTakenStart = 0;
	
	ToteSack(int l, int b, int h, Product[] items) {
		this.LENGTH = l;
		this.BREADTH = b;
		this.HEIGHT = h;
		D1 = Math.min(Math.min(l,b), h);
		D3 = Math.max(Math.max(l,b), h);
		D2 = l + b + h - D1 - D3;
		this.V = l*b*h;
		ArrayList<Product> lItems = new ArrayList<>();
		for(int i = 0; i < items.length; i++) {
			Product x = items[i];
			if(x.price == 0) {
				continue;
			}
			long v = x.height*x.length*x.width;
			if(v > this.V) {
				continue;
			}
			if(!willItFit(x.height, x.width, x.length)) {
				continue;
			}
			lItems.add(x);	
		}
		// Total items to look into 16235 Actual count 20000

		System.out.println("Total items to look into " + lItems.size() + " Actual count " + items.length);
		this.ITEMS = new Product[lItems.size()];
		
		lItems.toArray(this.ITEMS);
		// set up a bare minimum optimum value, for BB
		GetGreedyMaxValue();
		Product.ORDERED_BY = Product.Trait.PRICE_DENSITY;
		Arrays.sort(ITEMS, Collections.reverseOrder());
		suffixPrice  = new long[ITEMS.length];
		suffixMinVolume = new long[ITEMS.length];
		long sPrice = 0;
		
		for(int i = this.ITEMS.length - 1; i >= 0; i--) {
			sPrice += this.ITEMS[i].price;
			this.suffixPrice[i] = sPrice;
		}
		
		long minValue = Long.MAX_VALUE;
		for(int i = ITEMS.length - 1; i >= 0; i--) {
			if(minValue > ITEMS[i].volume) {
				minValue = ITEMS[i].volume;
			}			
			suffixMinVolume[i] = minValue;
		}		
	}
	private boolean willItFit(int l, int b, int h) {

		int c1 = Math.min(Math.min(l,b), h);
		int c3 = Math.max(Math.max(l,b), h);
		int c2 = l + b + h - D1 - D3;
		
		return (c1 <= D1 && c2 <= D2 && c3 <= D3) || true;
	
	}
	private void GetGreedyMaxValue() {
		// TODO Auto-generated method stub
		Product.ORDERED_BY = Product.Trait.PRICE_DENSITY;
		Arrays.sort(this.ITEMS, Collections.reverseOrder());
		long remainingVolume = this.V;
		int i;
		int k = 0;
		this.maxPrice = 0;
		for(i = 0; i < this.ITEMS.length - 1; i++) {
			if(remainingVolume < this.ITEMS[i].volume)
				break;
			k++;
			remainingVolume -= this.ITEMS[i].volume;
			this.maxPrice += this.ITEMS[i].price;
			this.productIDSum += this.ITEMS[i].productID;
			this.bestWeight += this.ITEMS[i].weight;
		}
		
		// try to fill the remaining space in with whatever we can!
		while(i < this.ITEMS.length && remainingVolume > 0) {
			
			if(remainingVolume > this.ITEMS[i].volume) {
				k++;
				System.out.println( i + " Found " + remainingVolume + " " + this.ITEMS[i].volume);
				remainingVolume -= this.ITEMS[i].volume;
				this.maxPrice += this.ITEMS[i].price;
				this.productIDSum += this.ITEMS[i].productID;
				this.bestWeight += this.ITEMS[i].weight;
			}
			i++;
		}
		
		System.out.println("Greedy solution has value  " + this.maxPrice + 
					" for element count " +  k + " I " + i);
		System.out.println("Email id to send " + this.productIDSum);
	}
	
	abstract void Solve();
	
}
