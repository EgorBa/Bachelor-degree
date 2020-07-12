package ru.ifmo.rain.bazhenov.bank;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentMap;

public interface Person extends Remote {

    /** Returns person name */
    String getName() throws RemoteException;

    /** Returns person surname */
    String getSurname() throws RemoteException;

    /** Returns person passport */
    String getPassport() throws RemoteException;

    /** Set person account by subId */
    void setAccount(String subId, Account account) throws RemoteException;

    /** Returns person account by subId or null */
    Account getAccount(String subId) throws RemoteException;

    /** Returns all person accounts */
    ConcurrentMap<String, Account> getAccounts() throws RemoteException;
}