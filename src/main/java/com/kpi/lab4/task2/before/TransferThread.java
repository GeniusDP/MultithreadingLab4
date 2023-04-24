package com.kpi.lab4.task2.before;

public class TransferThread extends Thread {

    private static final int REPS = 1000000;

    private final Bank bank;
    private final int fromAccount;
    private final int maxAmount;

    public TransferThread(Bank bank, int fromAccount, int maxAmount) {
        this.bank = bank;
        this.fromAccount = fromAccount;
        this.maxAmount = maxAmount;
    }

    @Override
    public void run() {
        for (int i = 0; i < REPS; i++) {
            int toAccount = (int) (bank.size() * Math.random());
            int amount = (int) (maxAmount * Math.random() / REPS);
            bank.transferSyncLombok(fromAccount, toAccount, amount);
        }
    }
}