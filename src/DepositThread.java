/*
 	Name: Jackson Trudel
 	Course: CNT 4714 - Spring 2021
 	Assignment title: Project 2 - Synchronized, Cooperating Threads Under Locking
 	Date: February 12, 2021
 */

import java.util.Random;

public class DepositThread implements Runnable {

	private BankAccount myBankAccount;
	private Random rand = new Random();
	private String name;
	
	/**
	 * Constructor for deposit thread
	 * @param ba - the shared location this thread interacts with (BankAccount object)
	 * @param threadNumber - used for unique identifier
	 */
	public DepositThread(BankAccount ba, int threadnumber) {
		myBankAccount = ba;
		name = "Thread D" + threadnumber;
	}
	
	@Override
	public void run() {
		// infinite loop
		while (true) {
			// deposit random amount $1-$250 
			myBankAccount.deposit(rand.nextInt(250) + 1, name);
			
			// sleep for random amount of time between (50-100ms)
			int sleepTime = rand.nextInt(51) + 50;
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} // end while loop
	}

}
