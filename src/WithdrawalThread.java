/*
 	Name: Jackson Trudel
 	Course: CNT 4714 - Spring 2021
 	Assignment title: Project 2 - Synchronized, Cooperating Threads Under Locking
 	Date: February 12, 2021
 */

import java.util.Random;

public class WithdrawalThread implements Runnable {

	private BankAccount myBankAccount;
	private Random rand = new Random();
	private String name;
	
	/**
	 * Constructor for withdrawal thread
	 * @param ba - the shared location this thread interacts with (BankAccount object)
	 * @param threadNumber - used for unique identifier
	 */
	public WithdrawalThread(BankAccount ba, int threadNumber) {
		myBankAccount = ba;
		name = "Thread W" + threadNumber;
	}
	
	@Override
	public void run() {
		// infinite loop
		while (true) {
			// withdrawal random amount from $1-$50
			myBankAccount.withdrawal(rand.nextInt(50) + 1, name);

			// sleep for random amount of time between (5-15ms)
			int sleepTime = rand.nextInt(11) + 5;
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} // end while loop
	}
	
}
