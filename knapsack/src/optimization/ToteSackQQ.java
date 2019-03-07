package optimization;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class ToteSackQQ extends ToteSack {

	// more experimental bits for branch and bound problems
	ToteSackQQ(int l, int b, int h, Product[] items) {
		super(l, b, h, items);
	}
	
	void ksackQ(int i, long currentPrice, long remainingVolume, long productIDSum, long weight, Set<Integer> soln) {
		Stack<nodeQQ> stack = new Stack<>(); 
		nodeQQ c =	nodeQQ.Build(i, currentPrice, remainingVolume, productIDSum, weight);
		soln.add(i);
		stack.push(c);
		
		while((c = stack.pop()) != null) {
			if(c.isTerminal) {
				// comment this for final test
				assert(c.i == ITEMS.length || c.remainingVolume == 0);
				if(c.priceTillNow >= maxPrice) {
					//System.arraycopy(bitmap, 0, this.L, 0, this.L.length);
					synchronized(this)  {
						if(c.priceTillNow > maxPrice || (c.priceTillNow == maxPrice && c.weight < bestWeight)) {
							maxPrice = c.priceTillNow;
							bestWeight = c.weight;
							productIDSum = c.productIDSum;
							countUpdated++;
							long time = (System.nanoTime() - timeTakenStart)/ 1000000;
							System.out.print("( "+  time + " ) Ksack " + "[ countU" +  countUpdated + "]" + " [ Pruned "+ countPruned +"]" 
							+ " nodes visited " + nodesVisited + " maxPrice "+ this.maxPrice + " Sum " + this.productIDSum +" \n{");
							soln.forEach(x -> {System.out.print(" " + x + " ");});
							System.out.print("}\n");
						}
					}		
				}
				continue;
			}
			
			
			if(maxPrice > c.priceTillNow + suffixPrice[i]) {
				// the remaining elements will not add up to be an optimal solution
				countPruned++;
				continue;
			}
			
			if(c.remainingVolume < suffixMinVolume[i]) {
				countPruned++;
				c.isTerminal = true;
				stack.push(c);
				continue;
			}
			if(!c.isRightPushed) {				
				if(c.remainingVolume >= ITEMS[i].volume ) {
					c.isRightPushed = true;
					stack.push(c);					
					Product I = ITEMS[c.i];
					soln.add(I.productID);
					nodeQQ next =nodeQQ.Build(i+1, c.priceTillNow + I.price, remainingVolume - I.volume, 
							c.productIDSum + I.productID, c.weight + I.weight);
					stack.push(next);
					nodesVisited++;						
				}
			} else {
				soln.remove(ITEMS[c.i].productID);
				nodeQQ next =nodeQQ.Build(i+1, c.priceTillNow, remainingVolume, 
						c.productIDSum, c.weight);
				stack.push(next);
			}			
		}		
	}
	
	void Solve() {
		final Set<Integer> s1 = new HashSet<>(), s2 = new HashSet<>();
		
		Thread t1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				s1.add(ITEMS[0].productID);
				ksackQ(1, ITEMS[0].price, V - ITEMS[0].volume, ITEMS[0].productID, ITEMS[0].weight, s1);
				
			}
		});
		
		Thread t2 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ksackQ(1, 0, 0, 0, 0, s2);
			}
		});
		timeTakenStart = System.nanoTime();
		try {
			t1.start();
			t2.start();
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
