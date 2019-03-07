package optimization;

import java.util.concurrent.atomic.AtomicLong;
// experimental bits trying to push the envelope on an old assignment I had worked on
public class ToteSackBB extends ToteSack {

	final AtomicLong pruneMinVolumeCount = new AtomicLong(0);
	ToteSackBB(int l, int b, int h, Product[] items) {
		super(l, b, h, items);
		// TODO Auto-generated constructor stub
	}
	void Solve() {
		Thread t1 = new Thread( new Runnable() {
			@Override
			public void run() {
				ksack(1, V, 0, 0, 0);
			}
		});
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				// trial assume ksack has first four elements
				long remainingVolume = 0, currentPrice = 0, productIDSum = 0, weight = 0;
				int i = 0;
				for(i = 0; i < 1; i++) {
					remainingVolume += ITEMS[i].volume;
					currentPrice += ITEMS[i].price;
					productIDSum += ITEMS[i].productID;
					weight += ITEMS[i].weight;
				}
				ksack(i, V - remainingVolume , currentPrice, productIDSum, weight);
			}
		});
		timeTakenStart = System.nanoTime();
		t1.start();
		t2.start();
		try {
			//t1.join();
			t2.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Ksack " + this.maxPrice + " " + this.productIDSum);
	}
	
	void ksack(int i, long remainingVolume, long priceTillNow,  long email, long weight) {
		if(i == ITEMS.length || remainingVolume == 0 || remainingVolume < suffixMinVolume[i]) {
			if(remainingVolume < suffixMinVolume[i]) {
				if(pruneMinVolumeCount.get() < 10 )
				System.out.println("+++ "  + i + " " + 	suffixMinVolume[i] + " " 
				+ remainingVolume + " " + ITEMS[i].volume );
				pruneMinVolumeCount.incrementAndGet();
			}
			if(priceTillNow >= maxPrice) {
				//System.arraycopy(bitmap, 0, this.L, 0, this.L.length);
				synchronized(this)  {
					if(priceTillNow > maxPrice || ( priceTillNow == maxPrice && weight < bestWeight )) {
						maxPrice = priceTillNow;
						productIDSum = email;
						bestWeight = weight;
						countUpdated++;
						long timeSince = ((System.nanoTime() - timeTakenStart) / 1000000); 
						System.out.println("(" + timeSince + ")Ksack " + "[ countU" +  countUpdated + "]" + 
						" [ Pruned maxP"+ countPruned + " minV" + pruneMinVolumeCount.get() + "]" 
						+ " nodes visited " + nodesVisited + " maxPrice "+ maxPrice + " " + productIDSum);
			
					}
				}
			}
			return;
		}
		
		if(maxPrice > priceTillNow + suffixPrice[i]) {
			// the remaining elements will not add up to be an optimal solution
			countPruned++;
			return;
		}
		
		if(remainingVolume >= ITEMS[i].volume ) {
			nodesVisited++;
			//bitmap[i] = true;
			ksack(i + 1, remainingVolume - ITEMS[i].volume,	priceTillNow + ITEMS[i].price, 
					email + ITEMS[i].productID, weight + ITEMS[i].weight);
			//bitmap[i] = false;
			
		} 
		ksack(i + 1, remainingVolume, priceTillNow,  email, weight);		
	}
}
