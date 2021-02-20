/*
 	Name: Jackson Trudel
 	Course: CNT 4714 - Spring 2021
 	Assignment title: Project 2 - Synchronized, Cooperating Threads Under Locking
 	Date: February 12, 2021
 */

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.*;

public class BankAccount {
	
	private Lock accessLock = new ReentrantLock();
	
	private Condition canWithdrawal = accessLock.newCondition();
	
	private int funds;
	
	/** 
	 * Constructor to make a new Bank Account
	 * @param amount - dollar amount of funds in account
	 */
	public BankAccount(int amount) {
		funds = amount;
	}
	
	/**
	 * Deposits dollar amount into account.
	 * @param amount - amount to deposit
	 * @param name - the thread number used to identify the thread
	 */
	public void deposit(int amount, String name) {
		// acquire lock on the account
		accessLock.lock();
	    
		// add funds to account
		funds += amount;
		System.out.printf(" %s deposits $%3d\t\t\t\t\t\t  (+) Balance is $%d\n", name, amount, funds);
		// signal all blocked withdrawal threads
		canWithdrawal.signalAll();
		
		// release the lock
		accessLock.unlock();
	}
	
	/** 
	 * Withdrawal dollar amount from bank account
	 * @param amount - amount to withdrawal
	 * @param name - the thread number used to identify the thread
	 */
	public void withdrawal(int amount, String name) {
		// acquire lock on the account
		accessLock.lock();
		
		// if there is not enough money in the account
		if (amount > funds) {
			// print the blocked withdrawal
			System.out.printf("\t\t\t\t        %s withdraws $%3d\t  (******) WITHDRAWAL BLOCKED - INSUFFICIENT FUNDS!!!\n", name, amount, funds);
			
			
			// wait until the canWithdrawal condition is signaled by
			// deposit thread
			try {
				// 
				canWithdrawal.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			// we will not resume our last withdrawal
			accessLock.unlock();
			// return because we do not need to re-try this withdrawal
			return;
		}	
		// if there is enough money in the account
		else {
			// make the withdrawal
			funds -= amount;

			// output withdrawal information
			System.out.printf("\t\t\t\t        %s withdraws $%3d\t  (-) Balance is $%d\n", name, amount, funds);
			
			// release the lock
			accessLock.unlock();
		}
		
	}
	
	public static void main(String[] args) {
		
		// create a bank account object
		BankAccount ba = new BankAccount(0);
		
		
		// Create Executor to manage thread pool
		ExecutorService threadExecutor = Executors.newFixedThreadPool(14);
		
		// print header
		System.out.println("    Deposit Threads  \t\t\t  Withdrawal Threads\t\t       Balance");
		System.out.println("-----------------------  \t\t----------------------  \t  -----------------");
		
		// execute threads
		threadExecutor.execute(new DepositThread(ba, 1));
		threadExecutor.execute(new DepositThread(ba, 2));
		threadExecutor.execute(new WithdrawalThread(ba, 1));
		threadExecutor.execute(new WithdrawalThread(ba, 2));
		threadExecutor.execute(new WithdrawalThread(ba, 3));
		threadExecutor.execute(new DepositThread(ba, 3));
		threadExecutor.execute(new DepositThread(ba, 4));
		threadExecutor.execute(new WithdrawalThread(ba, 4));
		threadExecutor.execute(new WithdrawalThread(ba, 5));
		threadExecutor.execute(new WithdrawalThread(ba, 6));
		threadExecutor.execute(new DepositThread(ba, 5));
		threadExecutor.execute(new WithdrawalThread(ba, 7));
		threadExecutor.execute(new WithdrawalThread(ba, 8));
		threadExecutor.execute(new WithdrawalThread(ba, 9));
		
	}
}
