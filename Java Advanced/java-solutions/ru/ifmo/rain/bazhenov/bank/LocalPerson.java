package ru.ifmo.rain.bazhenov.bank;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LocalPerson implements Person, Serializable {
    private final String passport;
    private final String name;
    private final String surname;
    private final ConcurrentMap<String, Account> accounts = new ConcurrentHashMap<>();

    /**
     * Constructor.
     */
    public LocalPerson(final String name, final String surname, final String passport) {
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
    public Account getAccount(String subId){
        return accounts.get(subId);
    }

    /**
     * {@inheritDoc}
     */
    public ConcurrentMap<String, Account> getAccounts() {
        return accounts;
    }

    /**
     * {@inheritDoc}
     */
    public void setAccount(String subId, Account account) {
        accounts.putIfAbsent(subId, account);
    }
}