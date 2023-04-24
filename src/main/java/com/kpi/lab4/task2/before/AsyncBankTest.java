package com.kpi.lab4.task2.before;

import java.util.ArrayList;
import java.util.List;

public class AsyncBankTest {

    public static final int NACCOUNTS = 10;
    public static final int INITIAL_BALANCE = 10000;

    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        Bank b = new Bank(NACCOUNTS, INITIAL_BALANCE);
        List<TransferThread> transferThreadList = new ArrayList<>();
        for (int i = 0; i < NACCOUNTS; i++) {
            TransferThread t = new TransferThread(b, i, INITIAL_BALANCE);
            t.setPriority(Thread.NORM_PRIORITY + i % 2);
            t.start();
            transferThreadList.add(t);
        }
        for (var thread : transferThreadList) {
            thread.join();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Time: " + (endTime - startTime));
    }
}