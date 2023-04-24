package com.kpi.lab4.task2.before;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import lombok.Synchronized;

public class Bank {

    public static final int NTEST = 10000;
    private final int[] accounts;
    private long nTransacts = 0;

    private final Lock locker = new ReentrantLock();

    public Bank(int n, int initialBalance) {
        accounts = new int[n];
        Arrays.fill(accounts, initialBalance);
        nTransacts = 0;
    }

//    public void transfer(int from, int to, int amount) {
//        accounts[from] -= amount;
//        accounts[to] += amount;
//        nTransacts++;
//        if (nTransacts % NTEST == 0) {
//            test();
//        }
//    }

    public synchronized void transferSync(int from, int to, int amount) {
        accounts[from] -= amount;
        accounts[to] += amount;
        nTransacts++;
        if (nTransacts % NTEST == 0) {
            test();
        }
    }

    @Synchronized
    public void transferSyncLombok(int from, int to, int amount) {
        accounts[from] -= amount;
        accounts[to] += amount;
        nTransacts++;
        if (nTransacts % NTEST == 0) {
            test();
        }
    }

    public void transferSyncBlock(int from, int to, int amount) {
        synchronized (this) {
            accounts[from] -= amount;
            accounts[to] += amount;
            nTransacts++;
            if (nTransacts % NTEST == 0) {
                test();
            }
        }
    }

    public void transferLock(int from, int to, int amount) {
        locker.lock();
        try {
            accounts[from] -= amount;
            accounts[to] += amount;
            nTransacts++;
            if (nTransacts % NTEST == 0) {
                test();
            }
        } finally {
            locker.unlock();
        }
    }

    public void test() {
        int sum = 0;
        for (int account : accounts) {
            sum += account;
        }
//        System.out.println("Transactions: " + nTransacts + " Sum: " + sum);
    }

    public int size() {
        return accounts.length;
    }
}
