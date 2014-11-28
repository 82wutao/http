package http.concurrents;

public class AsynchronizedCondition{
	int value=0;
	public void incrementAndNotify(){
		synchronized (this) {
			value++;
			this.notify();
		}
		
	}
	
	public void waitFor(int target){
		synchronized (this) {
			while(value!=target){
				try {
					this.wait() ;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
}
