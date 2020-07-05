// IMessageService.aidl
package com.jeremy.jnotes;
import com.jeremy.jnotes.shortcourse.ipc.entity.Message;
import com.jeremy.jnotes.MessageReceiveListener;

// 如果我们在一个AIDL文件中，要使用另外一个AIDL文件作为参数，我们一定要在使用的这个AIDL文件中import 被使用的AIDL文件
// 如果AIDL文件中有使用一个实体类作为参数，都必须标注一个AIDL关键字，in或者out，如果是基本数据类型，则不需要申明
// Declare any non-default types here with import statements
// 消息服务
interface IMessageService {
    void sendMessage(out Message message);
    // 此处不需要设置in或者out，因为MessageReceiveListener并不是一个实体类，只是一个AIDL接口
    void registerMessageReceiveListener(MessageReceiveListener listener);
    void unRegisterMessageReceiveListener(MessageReceiveListener listener);
}
