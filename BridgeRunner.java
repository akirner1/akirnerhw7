/**
 * Runs all threads
 */

public class BridgeRunner{

	public static void main(String[] args) {
		// TODO - check command line inputs
		if(args.length != 2){
			System.out.println("Usage: java BridgeRunner <bridge limit> <num cars>");
			return;
		}
		int limit;
		int numCars;
		try{
			limit = Integer.parseInt(args[0]);
			numCars = Integer.parseInt(args[1]);
			if(limit <= 0 || numCars <= 0){
				System.out.println("Error: bridge limit and/or num cars must be positive.");
				return;
			}
		}catch(NumberFormatException e){
			System.out.println("Error: bridge limit and/or num cars must be positive.");
			return;
		}

		// TODO - instantiate the bridge
		OneWayBridge bridge = new OneWayBridge(limit);
		// TODO - allocate space for threads
		Thread[] threads = new Thread[numCars];
		for(int i = 0; i < numCars; i++){
			threads[i] = new Thread(new Car(i, bridge));
			threads[i].start();
		}
		// TODO - start then join the threads
		for (int i = 0; i < numCars; i++) {
      		try {
        		threads[i].join();
      		} catch (InterruptedException e) {

      		}
    	}
		System.out.println("All cars have crossed!!");
	}


}