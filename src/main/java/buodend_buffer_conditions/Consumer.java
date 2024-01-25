package buodend_buffer_conditions;
/**
 * This is the consumer thread for the bounded buffer problem.
 *
 * Figure 7.15
 *
 * @author Gagne, Galvin, Silberschatz
 * Operating System Concepts with Java - Sixth Edition
 * Copyright John Wiley & Sons - 2003.
 */
import java.util.*;

public class Consumer implements Runnable
{
	public Consumer(Buffer b) {
		buffer = b;
	}

	public void run() 
	{
		Date message = null;

		while (true)
		{
			System.out.println("Consumer napping");
			SleepUtilities.nap();

			// consume an item from the buffer
			System.out.println("Consumer wants to consume.");

			try {
				message = (Date)buffer.remove();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("Consumer read the message: " + message);
		}
	}

	private  Buffer buffer;
}


