package optimization;

public class ToteSackDP extends ToteSack {

	nodeQQ[][] M;
	static final nodeQQ znode = nodeQQ.Build(0, 0, 0, 0, 0);
	ToteSackDP(int l, int b, int h, Product[] items) {
		super(l, b, h, items);
		M = new nodeQQ[(int)V][ITEMS.length + 1];		
		for(int i = 0; i < V; i++) {
			M[i][0] = znode;
		}
		for(int i = 0; i <= ITEMS.length; i++) {
			M[0][i] = znode;
		}
		Utils.showMetrics();
	}
	
	void Solve() {
		for(int i = (int)suffixMinVolume[0]; i < V ; i++) {
			for(int j = 1; j <= ITEMS.length; j++) {	
				Product E = ITEMS[j - 1];
				int volume = (int) E.volume;
				if(E.volume <= i) {
					nodeQQ m_prev = M[i-volume][j - 1];
					if(m_prev == null) {
						m_prev = znode;
					}
					long priceTillNow = m_prev.priceTillNow + E.price;
					long weight = m_prev.weight + E.weight;
					long productSum = m_prev.productIDSum + E.productID;
					if(( priceTillNow > M[i][j-1].priceTillNow ) ||
							(( priceTillNow  == M[i][j-1].priceTillNow) && 
									(m_prev.weight + E.weight <  M[i][j-1].weight))) {
						M[i][j] = nodeQQ.Build(j-1, priceTillNow, i - volume, productSum, weight); 
					}
					else {
						M[i][j] = M[i][j-1];
					}
				}
				else {
					M[i][j] = M[i][j-1];
				}
			}
		}
		// 41298 450164
		
		nodeQQ soln =  M[(int)V-1][ITEMS.length];
		System.out.println(" Price " + soln.priceTillNow + " ProductID " + soln.productIDSum);
		int checkSum = 0, checkPrice = 0;
		int count = 0;
		long weightSack = 0;
		while(soln != znode) {
			int i = soln.i;
			 checkSum += ITEMS[i].productID;
			 checkPrice += ITEMS[i].price;
			 weightSack += ITEMS[i].weight;
			 System.out.println(count + " " + i + " ID " + ITEMS[i].productID 
					 + " P " + ITEMS[i].price + " V " + ITEMS[i].volume  +  " PD " + ITEMS[i].priceDensity);
			 
			 soln = M[(int) soln.remainingVolume][soln.i];
			 count++;
		}
		System.out.println(" + " + checkPrice + " " + checkSum + " " + weightSack );
		Utils.showMetrics();
	}  
}

/*
Total items to look into 18532 Actual count 20000
8 Found 6151 4125
621 Found 2026 1980
Greedy solution has value  14141 for element count 9 I 18532
Email id to send 113915
 Total 3872391168 MAx 4294967296 Free 327314008
 Price 41298 ProductID 450164
0 16936 ID 36769 P 1382 V 1700 PD 0.0
1 16677 ID 36175 P 1494 V 1920 PD 0.0
2 16285 ID 35280 P 1675 V 1980 PD 0.0
3 15881 ID 34414 P 1976 V 2550 PD 0.0
4 15514 ID 33638 P 1366 V 1800 PD 0.0
5 12629 ID 27376 P 1521 V 1800 PD 0.0
6 8206 ID 17896 P 1768 V 2400 PD 0.0
7 8159 ID 17788 P 2509 V 2700 PD 0.0
8 6637 ID 14448 P 2191 V 2475 PD 0.0
9 5980 ID 12979 P 1695 V 2160 PD 0.0
10 5920 ID 12856 P 1194 V 1500 PD 0.0
11 5048 ID 10981 P 1432 V 1980 PD 0.0
12 4808 ID 10496 P 2080 V 2210 PD 0.0
13 4571 ID 9970 P 1949 V 2640 PD 0.0
14 3974 ID 8699 P 1625 V 1800 PD 0.0
15 2959 ID 6532 P 1830 V 2250 PD 0.0
16 2286 ID 5084 P 1651 V 1800 PD 0.0
17 2212 ID 4887 P 1604 V 1870 PD 0.0
18 621 ID 1370 P 1783 V 1980 PD 0.0
19 3 ID 39987 P 2229 V 2090 PD 1.0
20 2 ID 31288 P 1990 V 1500 PD 1.0
21 1 ID 26950 P 2215 V 2145 PD 1.0
22 0 ID 14301 P 2139 V 1950 PD 1.0
 + 41298 450164
 Total 3872391168 MAx 4294967296 Free 210996224
*/