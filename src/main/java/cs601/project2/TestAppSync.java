/**
 * 
 */
package cs601.project2;

import java.util.LinkedList;
import java.util.logging.Level;

import broker.SynchronousBroker;
import item.Reviews;
import publisher.AmazonPublisher1;
import subscriber.Subscribers1;

/**
 * @author anuragjha
 * Sync Broker Test app - Initial class to start 
 */
public class TestAppSync {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		System.out.println("Project2 - Synchronous Broker Test App");
		//reading configuration file content into Project2Init object
		Project2Init init = Project2InitReader.project2InitjsonReader();
		
		//initializing logger 
		Project2Logger.initialize("Synchronous Broker Test App", init.getLoggerFile());
		
		long start = System.currentTimeMillis();
		
		//creating new Subscribers
		Subscribers1 s1 = new Subscribers1("new");
		Subscribers1 s2 = new Subscribers1("old");

		//initializing Synchronous Broker
		SynchronousBroker<Reviews> broker = SynchronousBroker.getInstance();

		//AsyncOrderedDispatchBroker1<Reviews> broker = AsyncOrderedDispatchBroker1.getInstance();

		//AsyncUnorderedDispatchBroker1<Reviews> broker = AsyncUnorderedDispatchBroker1.getInstance();
		
		//adding subscriber to borker
		broker.subscribe(s1);
		broker.subscribe(s2);

		//creating AmazonPublisher1 thread for each input file
		LinkedList<Thread> publisherThreadList = new LinkedList<>();
		for(String file : init.getInputFiles())	{
			Thread publisherThread = new Thread(new AmazonPublisher1(file, broker));
			publisherThreadList.add(publisherThread);
		}
		
		//starting publisher threads 
		for(Thread publisherThread: publisherThreadList) {
			publisherThread.start();
		}
		
		Project2Logger.write(Level.INFO, " Thread count : " + Thread.activeCount(), 0);
		//waiting for publisher threads to complete
		for(Thread publisherThread: publisherThreadList) {
			try {
				
				publisherThread.join();
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Publisher threads finished. ");
		Project2Logger.write(Level.INFO, "Publisher threads finished. ", 0);

		
		//closing subscriber resources
		s1.closeWriter();
		s2.closeWriter();
		
		long end = System.currentTimeMillis(); //retrieve current time when finishing calculations
		System.out.println("Time taken: " + (end-start)/1000 + " seconds");
		Project2Logger.write(Level.INFO, "Time taken: " + (end-start)/1000 + " seconds", 0);
		//System.out.println("No of records read: " + broker.getRecordCounter());
		Project2Logger.write(Level.INFO, "No of records read: " + broker.getRecordCounter(), 0);
		//System.out.println("Total records in subs1: " + s1.recordCount);
		Project2Logger.write(Level.INFO, "Total records in subs1: " + s1.recordCount, 0);
		//System.out.println("Total records in subs2: " + s2.recordCount);
		Project2Logger.write(Level.INFO, "Total records in subs2: " + s2.recordCount, 0);
		
		//closing logger
		Project2Logger.close();


	}


}