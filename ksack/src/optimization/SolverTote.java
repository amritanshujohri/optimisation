package optimization;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;


public class SolverTote {

	static Product [] items = new Product[20000];
	public static void main(String[] args) throws IOException {

		Reader is = new FileReader("products.csv");
		//System.setOut(new PrintStream(new FileOutputStream("output.txt")));

		try(BufferedReader bis = new BufferedReader(is)) {
			
		int i = 0;
		while(true) {
			String r = bis.readLine();
			if(r == null) {
				break;
			}
			// product ID, price, length, width, height, weight
			String[] values = r.split(",");
			if(values.length == 6) {
				items[i++] = new Product(values);
			}
		}
		int L = 45, B = 30, H = 35;
		long V = L*B*H;
		//  45 centimeters long, 30 wide and 35 high
		if(V*items.length * 4 < Utils.getMaxHeap()) {
			new ToteSackDP(45, 30, 35, items).Solve();
		}
		else {
			System.out.println("Using branch and bound this may take a while!");
			new ToteSackBB(L, B, H, items).Solve();
		}	
		
		}
		catch (Exception e) {
			System.out.println("Failed to open file" + e);
			e.printStackTrace();
		}
		
	}
}
