package ru.ifmo.rain.bazhenov.bank;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RemoteBank implements Bank {
    private final int port;
    private final ConcurrentMap<String, Account> accounts = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Person> persons = new ConcurrentHashMap<>();

    /**
     * Constructor.
     */
    public RemoteBank(final int port) {
        this.port = port;
    }

    /**
     * {@inheritDoc}
     */
    public Account createAccount(final String id) throws RemoteException {
        System.out.println("Creating account " + id);
        final Account account = new RemoteAccount(id);
        if (accounts.putIfAbsent(id, account) == null) {
            UnicastRemoteObject.exportObject(account, port);
            return account;
        } else {
            return getAccount(id);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Person createPerson(final String name, final String surname, final String passport) throws RemoteException {
        final Person person = new RemotePerson(name, surname, passport);
        System.out.println("Creating person " + name + " " + surname + "'s account");
        if (persons.putIfAbsent(passport, person) == null) {
            UnicastRemoteObject.exportObject(person, port);
            return person;
        } else {
            return getRemotePerson(passport);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Account createPersonAccount(final String subId, Person person) throws RemoteException {
        final String id = person.getPassport() + ':' + subId;
        Account account = createAccount(id);
        person.setAccount(subId, account);
        return account;
    }

    /**
     * {@inheritDoc}
     */
    public Account getAccount(final String id) {
        System.out.println("Retrieving account " + id);
        return accounts.get(id);
    }

    /**
     * {@inheritDoc}
     */
    public Person getRemotePerson(final String passport) {
        System.out.println("Retrieving person " + passport);
        return persons.get(passport);
    }

    /**
     * {@inheritDoc}
     */
    public Person getLocalPerson(final String passport) throws RemoteException {
        Person person = persons.get(passport);
        if (person == null) {
            return null;
        }
        Person localPerson = new LocalPerson(person.getName(), person.getSurname(), person.getPassport());
        person.getAccounts().keySet().forEach(subId -> {
            try {
                localPerson.setAccount(subId, new LocalAccount(localPerson.getPassport() + ':' + subId,
                        person.getAccounts().get(subId).getAmount()));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        return localPerson;
    }
}
