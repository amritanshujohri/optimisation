package optimization;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;

// UStar algorithm horowitz sahni's knapsack soln
public class ToteSackHK extends ToteSack {
	final LinkedHashMap<Long, SumSet> S =  new LinkedHashMap<Long, SumSet>();
	
	ToteSackHK(int l, int b, int h, Product[] items) {
		super(l, b, h, items);
		// TODO Auto-generated constructor stub
	}

	boolean ustarCheckFit(SumSet x , long price, long weight) {
		boolean discardNew = false;
		if(x != null) {
			if(x.price > price) {
				discardNew = true;
			} else if(x.price == price && x.weight < weight) {
				discardNew = true;
			}
		} 
		return discardNew;
	}
	
	SumSet fgen(SumSet s, int i) {
		long volume = ITEMS[i].volume + s.volumeUsed;
		long price = ITEMS[i].price + s.price;
		
		long weight = ITEMS[i].weight + s.weight;
		SumSet x = S.get(volume);
		boolean discardNew = ustarCheckFit(x, price, weight);		
		if(!discardNew) {
			x = x != null ? x : new SumSet();
			x.price = price;
			x.volumeUsed = volume;
			x.productIDSum = ITEMS[i].productID + s.productIDSum;
			x.weight = weight;
			
		} else {
			x = null;
		}
		return x;
	}
	void ustar(int i) {
		LinkedHashMap<Long, SumSet> n = new LinkedHashMap<>();
		S.forEach((key, s) -> {
			SumSet x = fgen(s,i);
			if(x != null) {
				SumSet y = n.get(x.volumeUsed);
				if(!ustarCheckFit(y, x.price, x.weight)) {
					n.put(x.volumeUsed, x);
				}
			}
			});
		n.forEach((key, x) -> {
			// if the best case doesnt fit then nothing will
			if(this.ITEMS[i].priceDensity * (this.V - x.volumeUsed) + x.price > this.maxPrice) {
				SumSet z = S.put(key, x); assert(z == null);
				if(x.price > this.maxPrice) {
					this.maxPrice = x.price;
					this.productIDSum = x.productIDSum;
					this.bestWeight = x.weight;
				} else if(x.price == this.maxPrice && x.weight < this.bestWeight) {
					this.maxPrice = x.price;
					this.productIDSum = x.productIDSum;
					this.bestWeight = x.weight;
				}
			}
		});
		System.out.println(" + " + S.size());
	}
	void Solve() 
	{
		Product.ORDERED_BY = Product.Trait.PRICE_DENSITY;
		Arrays.sort(ITEMS, Collections.reverseOrder());
		SumSet zero = new SumSet(0, 0, 0, 0);
		S.put(0L, zero);
		for(int i = 0; i < ITEMS.length; i++) {
			ustar(i);
		}
		System.out.println(" " + this.maxPrice + " " + this.productIDSum);
	}
}
