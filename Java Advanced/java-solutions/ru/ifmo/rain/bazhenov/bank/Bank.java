package ru.ifmo.rain.bazhenov.bank;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Bank extends Remote {

    /** Returns created account if account is absent else returns existing account. */
    Account createAccount(String id) throws RemoteException;

    /** Returns created person if person is absent else returns existing person. */
    Person createPerson(String name,String surname,String passport) throws RemoteException;

    /** Returns created person's account if person's account is absent else returns existing person's account. */
    Account createPersonAccount(String subId, Person person) throws RemoteException;

    /** Returns account by id or null. */
    Account getAccount(String id) throws RemoteException;

    /** Returns remote person by passport or null. */
    Person getRemotePerson(String passport) throws RemoteException;

    /** Returns local person by passport or null. */
    Person getLocalPerson(String passport) throws RemoteException;
}
