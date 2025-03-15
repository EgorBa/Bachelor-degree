package ru.ifmo.rain.bazhenov.bank;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * @author Egor Bazhenov
 */
public class Client {

    /**
     * Execute program with given parameters.
     *
     * @param args name,
     *             surname,
     *             passport,
     *             accountName,
     *             amount.
     */
    public static void main(final String... args) throws RemoteException {
        if (args.length != 5) {
            System.out.println("Error : wrong number of arguments");
            return;
        }
        final Bank bank;
        try {
            bank = (Bank) Naming.lookup("//localhost/bank");
        } catch (final NotBoundException e) {
            System.out.println("Bank is not bound");
            return;
        } catch (final MalformedURLException e) {
            System.out.println("Bank URL is invalid");
            return;
        }
        String name = args[0];
        String surname = args[1];
        String passport = args[2];
        String subId = args[3];
        int sum;
        try {
            sum = Integer.parseInt(args[4]);
        } catch (NumberFormatException e) {
            System.out.println("Error : Illegal argument exception");
            return;
        }
        Person person = bank.getRemotePerson(passport);
        if (person == null) {
            System.out.println("Creating person");
            person = bank.createPerson(name, surname, passport);
        } else {
            System.out.println("Person already exists");
        }
        Account account = person.getAccount(subId);
        if (account == null) {
            System.out.println("Creating person's account");
            account = bank.createPersonAccount(subId, person);
        } else {
            System.out.println("Person's account already exists");
        }
        System.out.println("Account id: " + account.getId());
        System.out.println("Money: " + account.getAmount());
        System.out.println("Adding money");
        account.setAmount(account.getAmount() + sum);
        System.out.println("Money: " + account.getAmount());
    }
}
