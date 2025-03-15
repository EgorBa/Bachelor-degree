package ru.ifmo.rain.bazhenov.bank;

public class RemoteAccount implements Account {
    private final String id;
    private int amount;

    /**
     * Constructor.
     */
    public RemoteAccount(final String id) {
        this.id = id;
        amount = 0;
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
