package com.kpi.lab4.task2.after;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;

public class AsyncBankTest {

    public static final int NACCOUNTS = 10;
    public static final int INITIAL_BALANCE = 10000;

    public static void main(String[] args) {
        Bank b = new Bank(NACCOUNTS, INITIAL_BALANCE);
        long startTime = System.currentTimeMillis();
        List<TransferTask> tasks = new ArrayList<>();
        for (int i = 0; i < NACCOUNTS; i++) {
            TransferTask task = new TransferTask(b, i, INITIAL_BALANCE);
            tasks.add(task);
        }
        ForkJoinTask.invokeAll(tasks);
        long endTime = System.currentTimeMillis();
        System.out.println("Time: " + (endTime - startTime));
    }
}