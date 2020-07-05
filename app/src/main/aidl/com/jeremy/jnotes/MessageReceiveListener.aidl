// MessageReceiveListener.aidl
package com.jeremy.jnotes;
import com.jeremy.jnotes.shortcourse.ipc.entity.Message;

// Declare any non-default types here with import statements

interface MessageReceiveListener {
    void onReceiveMessage(in Message message);
}
