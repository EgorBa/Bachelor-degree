package ru.ifmo.rain.bazhenov.bank;

import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Set;
import java.util.concurrent.*;

import static junit.framework.TestCase.*;

public class BankTest {
    private static final int PORT = 8888;
    private static Bank bank;
    private final String passport = "8079";
    private final String name = "Egor";
    private final String surname = "Bazhenov";
    private final String accountName = "accountName";

    @Before
    public void before() throws MalformedURLException, RemoteException {
        bank = new RemoteBank(PORT);
        UnicastRemoteObject.exportObject(bank, PORT);
        Naming.rebind("//localhost/bank", bank);
        System.out.println("Bank created");
    }

    @Test
    public void createOneRemotePerson() throws RemoteException {
        bank.createPerson(name, surname, passport);
        Person remotePerson = bank.getRemotePerson(passport);
        assertEquals(remotePerson.getPassport(), passport);
        assertEquals(remotePerson.getName(), name);
        assertEquals(remotePerson.getSurname(), surname);
    }

    @Test
    public void createOneRemoteLocalPerson() throws RemoteException {
        bank.createPerson(name, surname, passport);
        Person localPerson = bank.getLocalPerson(passport);
        assertEquals(localPerson.getPassport(), passport);
        assertEquals(localPerson.getName(), name);
        assertEquals(localPerson.getSurname(), surname);
    }

    @Test
    public void createPerson() throws RemoteException {
        assertNull(bank.getRemotePerson(passport));
        bank.createPerson(name, surname, passport);
        assertNotNull(bank.getRemotePerson(passport));
    }

    @Test
    public void createManyPerson() throws RemoteException {
        int count = 1000;
        for (int i = 0; i < count; i++) {
            bank.createPerson(name + i, surname + i, passport + i);
            Person remotePerson = bank.getRemotePerson(passport + i);
            assertEquals(remotePerson.getPassport(), passport + i);
            assertEquals(remotePerson.getName(), name + i);
            assertEquals(remotePerson.getSurname(), surname + i);
            Person localPerson = bank.getLocalPerson(passport + i);
            assertEquals(localPerson.getPassport(), passport + i);
            assertEquals(localPerson.getName(), name + i);
            assertEquals(localPerson.getSurname(), surname + i);
        }
    }

    @Test
    public void createOneAccountRemotePerson() throws RemoteException {
        bank.createPerson(name, surname, passport);
        Person remotePerson = bank.getRemotePerson(passport);
        bank.createPersonAccount(accountName, remotePerson);
        Account account = remotePerson.getAccount(accountName);
        assertEquals(account.getId(), passport + ':' + accountName);
        assertEquals(account.getAmount(), 0);
    }

    @Test
    public void createOneAccountLocalPerson() throws RemoteException {
        bank.createPerson(name, surname, passport);
        Person localPerson = bank.getLocalPerson(passport);
        LocalAccount localAccount = new LocalAccount(passport + ':' + accountName);
        localPerson.setAccount(accountName, localAccount);
        Account account = localPerson.getAccount(accountName);
        assertEquals(account.getId(), passport + ':' + accountName);
        assertEquals(account.getAmount(), 0);
    }

    @Test
    public void createManyAccountRemotePerson() throws RemoteException {
        bank.createPerson(name, surname, passport);
        Person remotePerson = bank.getRemotePerson(passport);
        Person localPerson = bank.getLocalPerson(passport);
        for (int i = 0; i < 1000; i++) {
            bank.createPersonAccount(accountName + i, remotePerson);
            Account account = remotePerson.getAccount(accountName + i);
            assertEquals(account.getId(), passport + ':' + accountName + i);
            assertEquals(account.getAmount(), 0);
            account = localPerson.getAccount(accountName + i);
            assertNull(account);
        }
    }

    @Test
    public void createManyAccountLocalPerson() throws RemoteException {
        bank.createPerson(name, surname, passport);
        Person localPerson = bank.getLocalPerson(passport);
        Person remotePerson = bank.getRemotePerson(passport);
        for (int i = 0; i < 1000; i++) {
            LocalAccount localAccount = new LocalAccount(passport + ':' + accountName + i);
            localPerson.setAccount(accountName + i, localAccount);
            Account account = localPerson.getAccount(accountName + i);
            assertEquals(account.getId(), passport + ':' + accountName + i);
            assertEquals(account.getAmount(), 0);
            account = remotePerson.getAccount(accountName + i);
            assertNull(account);
        }
    }

    @Test
    public void setAccountAmountRemotePersonAfterLocalPerson() throws RemoteException {
        int amount = 500;
        bank.createPerson(name, surname, passport);
        Person remotePerson = bank.getRemotePerson(passport);
        Account account = bank.createPersonAccount(accountName, remotePerson);
        Person localPerson = bank.getLocalPerson(passport);
        account.setAmount(amount);
        assertEquals(remotePerson.getAccount(accountName).getAmount(), amount);
        assertEquals(localPerson.getAccount(accountName).getAmount(), 0);
    }

    @Test
    public void setAccountAmountRemotePersonBeforeLocalPerson() throws RemoteException {
        int amount = 500;
        bank.createPerson(name, surname, passport);
        Person remotePerson = bank.getRemotePerson(passport);
        Account account = bank.createPersonAccount(accountName, remotePerson);
        account.setAmount(amount);
        Person localPerson = bank.getLocalPerson(passport);
        assertEquals(remotePerson.getAccount(accountName).getAmount(), amount);
        assertEquals(localPerson.getAccount(accountName).getAmount(), amount);
    }

    @Test
    public void setAccountAmountLocalPerson() throws RemoteException {
        int amount = 500;
        bank.createPerson(name, surname, passport);
        Person remotePerson = bank.getRemotePerson(passport);
        bank.createPersonAccount(accountName, remotePerson);
        Person localPerson = bank.getLocalPerson(passport);
        Account account = localPerson.getAccount(accountName);
        account.setAmount(amount);
        assertEquals(remotePerson.getAccount(accountName).getAmount(), 0);
        assertEquals(localPerson.getAccount(accountName).getAmount(), amount);
    }

    @Test
    public void createManyAccountRemotePersonsAndSetAmountMultiThreading() throws RemoteException {
        final int countOfPersons = 100;
        final int countOfAccount = 10;
        final int amount = 50;
        ExecutorService streams = Executors.newFixedThreadPool(countOfAccount);
        Phaser phaser = new Phaser(1);
        Set<Account>[] set = new Set[countOfAccount];
        for (int i = 0; i < countOfAccount; i++) {
            set[i] = ConcurrentHashMap.newKeySet();
        }
        for (int i = 0; i < countOfPersons; i++) {
            Person remotePerson = bank.createPerson(name + i, surname + i, passport + i);
            for (int j = 0; j < countOfAccount; j++) {
                set[j].add(bank.createPersonAccount(accountName + j, remotePerson));
            }
        }
        for (int i = 0; i < countOfAccount; i++) {
            phaser.register();
            final int j = i;
            streams.submit(() -> {
                set[j].forEach(account -> {
                            while (true) {
                                try {
                                    account.setAmount(account.getAmount() + amount);
                                    break;
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                );
                phaser.arrive();
            });
        }
        phaser.arriveAndAwaitAdvance();
        for (int i = 0; i < countOfPersons; i++) {
            Person remotePerson = bank.getRemotePerson(passport + i);
            for (int j = 0; j < countOfAccount; j++) {
                assertEquals(remotePerson.getAccount(accountName + j).getAmount(), amount);
            }
        }
    }

    @Test
    public void testClient() throws RemoteException {
        String amount = "100";
        Client.main(name, surname, passport, accountName, amount);
        Person remotePerson = bank.getRemotePerson(passport);
        Account account = remotePerson.getAccount(accountName);
        assertEquals(account.getId(), passport + ':' + accountName);
        assertEquals(account.getAmount(), Integer.parseInt(amount));
        Client.main(name, surname, passport, accountName, amount);
        assertEquals(account.getAmount(), 2 * Integer.parseInt(amount));
    }

}
