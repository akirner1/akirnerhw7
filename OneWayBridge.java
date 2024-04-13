public class OneWayBridge extends Bridge{

    private int MAX_CARS;
    private Object enterQueue;
    private Object leaveQueue;

    public OneWayBridge(int limit){
        super();
        this.MAX_CARS = limit;
        this.enterQueue = new Object();
        this.leaveQueue = new Object();
    }

    //logs car, places it on bridge, and increases time
    private synchronized void placeCarOn(Car car) throws InterruptedException{
        car.setEntryTime(this.currentTime);
        this.currentTime++;
        this.bridge.add(car);
        System.out.println("Bridge: (dir=" + this.direction + ") " + this.bridge.toString());
    }

    public void arrive(Car car) throws InterruptedException{
        synchronized(enterQueue){
            while(true){
                if(this.bridge.size() == 0){
                    this.direction = car.getDirection();
                    //this car will always be let in
                    this.placeCarOn(car);
                    return;
                }else if(this.bridge.size() < this.MAX_CARS){
                    if(this.direction == car.getDirection()){
                        //car is good to go!
                        this.placeCarOn(car);
                        return;
                    }else{
                        //there is space for a new car, but this one is the wrong direction, so try the next car
                        this.enterQueue.notifyAll();
                        this.enterQueue.wait();
                    }
                }else{
                    //bridge is full, so wait for a leaving car to notify the next car
                    this.enterQueue.wait();
                }
            }
        }
    }

    public void exit(Car car) throws InterruptedException{
        synchronized(enterQueue){
            while(this.bridge.get(0).getID() != car.getID()){
                this.enterQueue.wait();
            }
            //car is first, it can leave
            this.bridge.remove(0);
            System.out.println("Bridge: (dir=" + this.direction + ") " + this.bridge.toString());
            this.enterQueue.notifyAll();//if any cars were blocked from leaving, let them leave
        }
    }
}