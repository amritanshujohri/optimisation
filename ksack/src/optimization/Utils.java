package optimization;

public class Utils {

	public static void showMetrics() {
		// Get current size of heap in bytes
		  long heapSize = Runtime.getRuntime().totalMemory(); 

		  // Get maximum size of heap in bytes. The heap cannot grow beyond this size.// Any attempt will result in an OutOfMemoryException.
		  long heapMaxSize = Runtime.getRuntime().maxMemory();

		   // Get amount of free memory within the heap in bytes. This size will increase // after garbage collection and decrease as new objects are created.
		  long heapFreeSize = Runtime.getRuntime().freeMemory(); 
		  
		  System.out.println(" Total " + heapSize + " MAx " + heapMaxSize +" Free " + heapFreeSize);
	  }
	public static long getMaxHeap() {
		return Runtime.getRuntime().maxMemory();
	}
}
