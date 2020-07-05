// IServiceManager.aidl
package com.jeremy.jnotes;

// Declare any non-default types here with import statements

interface IServiceManager {
    IBinder getService(String serviceName);
}
