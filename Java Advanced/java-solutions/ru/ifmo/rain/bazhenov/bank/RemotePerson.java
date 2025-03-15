package ru.ifmo.rain.bazhenov.bank;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RemotePerson implements Person {
    private final String name;
    private final String surname;
    private final String passport;
    private final ConcurrentMap<String, Account> accounts = new ConcurrentHashMap<>();

    /**
     * Constructor.
     */
    public RemotePerson(final String name,final String surname,final String passport){
        this.name = name;
        this.surname = surname;
        this.passport = passport;
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    public String getSurname() {
        return surname;
    }

    /**
     * {@inheritDoc}
     */
    public String getPassport() {
        return passport;
    }

    /**
     * {@inheritDoc}
     */
    public Account getAccount(String subId) {
        System.out.println("Retrieving person's account " + subId);
        return accounts.get(subId);
    }

    /**
     * {@inheritDoc}
     */
    public void setAccount(String subId, Account account) {
        accounts.putIfAbsent(subId, account);
    }

    /**
     * {@inheritDoc}
     */
    public ConcurrentMap<String, Account> getAccounts() {
        return accounts;
    }

}
