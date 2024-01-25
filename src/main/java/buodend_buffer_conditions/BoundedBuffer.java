package buodend_buffer_conditions;
import java.util.concurrent.locks.*;

/**
 * BoundedBuffer.java
 *
 * This program implements the bounded buffer using Java monitor with condition
 * variables.
 *
 * 
 * @author Patrizia Scandurra (revised by Luca Gherardi)
 * 
 */

public class BoundedBuffer implements Buffer
{
	private static final int   BUFFER_SIZE = 5;

	private int count; // numero di oggetti nel buffer
	private int in;   // index della prossima posizione libera per l'inserimento
	private int out;  // index della prossima posizione piena per il prelevamento
	private Object[] buffer;

	// Lock e conditions
	final Lock lock = new ReentrantLock();
	final Condition notFull  = lock.newCondition(); 
	final Condition notEmpty = lock.newCondition(); 


	public BoundedBuffer()
	{
		// Il buffer e' inizialmente vuoto
		count = 0;
		in = 0;
		out = 0;

		buffer = new Object[BUFFER_SIZE];
	}

	// metodo chiamato dai produttori
	public  void insert(Object item) throws InterruptedException {

		// acquisizione del lock
		lock.lock();
		System.out.println("Producer acuired lock");  
		try {

			// se il buffer e' pieno rilascio il lock e mi metto in attesa
			// della condizione notFull
			while (count == BUFFER_SIZE){
				notFull.await();
			}

			// aggiungo un oggetto al buffer
			++count;
			buffer[in] = item;
			in = (in + 1) % BUFFER_SIZE;

			if (count == BUFFER_SIZE)
				System.out.println("\tProducer Entered " + item + " in the buffer. Buffer FULL");
			else
				System.out.println("\tProducer Entered " + item + " in the buffer. Buffer Size = " +  count);

			// risveglio uno dei thread in attesa della condizione notEmpty
			notEmpty.signal();
		} finally {
			// il blocco finally viene sempre eseguito all'uscita dal blocco try
			// in questo punto si rilascia il lock
			System.out.println("Producer releasing lock");  
			lock.unlock();
		}

	}

	// metodo chiamato dai consumatori 
	public Object remove() throws InterruptedException {

		Object item;

		// acquisizione del lock
		lock.lock();
		System.out.println("Consumer acuired lock");  
		try {
			
			// se il buffer e' vuoto rilascio il lock e mi metto in attesa
			// della condizione notEmpty
			while (count == 0){
				notEmpty.await();
			}

			// rimuovo un ogggetto dal buffer
			--count;
			item = buffer[out];
			out = (out + 1) % BUFFER_SIZE;

			if (count == 0)
				System.out.println("\tConsumer Consumed " + item + " Buffer EMPTY");
			else
				System.out.println("\tConsumer Consumed " + item + " Buffer Size = " + count);

			// risveglio uno dei thread in attesa della condizione notFull
			notFull.signal();
			return item;
		} 
		finally {
			// rilascio del lock
			System.out.println("Consumer releasing lock");  
			lock.unlock();
		}

	}


}
