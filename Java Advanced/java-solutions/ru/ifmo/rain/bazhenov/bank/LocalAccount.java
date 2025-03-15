package ru.ifmo.rain.bazhenov.bank;

import java.io.Serializable;

public class LocalAccount implements Account, Serializable {
    private final String id;
    private int amount;

    /**
     * Constructor by id.
     */
    public LocalAccount(final String id) {
        this.id = id;
        amount = 0;
    }

    /**
     * Constructor by id ans amount.
     */
    public LocalAccount(final String id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    /**
     * {@inheritDoc}
     */
    public String getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    public synchronized int getAmount() {
        System.out.println("Getting amount of money for account " + id);
        return amount;
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void setAmount(final int amount) {
        System.out.println("Setting amount of money for account " + id);
        this.amount = amount;
    }
}