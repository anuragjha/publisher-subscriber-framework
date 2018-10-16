package broker;

import java.util.LinkedList;

import subscriber.Subscriber;

public class AsyncOrderedBrokerHelper implements Runnable {

	
	public void run()	{
		
		//subscriber list
		System.out.println("run of helper");
		Object item;
		while((item = AsyncOrderedBroker.getInstance().getDispatcher().poll(1000)) != null) {
			LinkedList<Subscriber> subscribers = AsyncOrderedBroker.getInstance().getSubscriberList();
			for(Subscriber s : subscribers)	{
				
				s.onEvent(item);
			}
		}
		
		
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}