// IConnectionService.aidl
package com.jeremy.jnotes;

// Declare any non-default types here with import statements

interface IConnectionService {
    oneway void connect();
    void disconnect();
    boolean isConnected();
}
