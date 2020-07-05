## Android9.0动态运行时权限源码分析及封装改造

权限检测流程源码剖析

```
ActivityCompat.requestPermissions()
ActivityCompat.shouldShowRequestPermissionRationale()
ActivityCompat.checkSelfPermission(value)
```

在Android 6.0中如果我们申请了权限组当中的一个某一个子权限，则会授予我们整个权限组的权限。比如，我们申请了READ_EXTERNAL_STORAGE权限，如果用户授予了该权限，则权限组当中的WRITE_EXTERNAL_STORAGE也会被同时授予。在Android9.0中，如果我们只申请了权限组当中的某一个子权限，则系统只会授予该子权限，权限组当中的其他子权限还需要申请。

AOP运行时权限检测库封装

AbstractProcessor调试方式

权限检测中的安全设计

UID定义：

PID定义：

appId：

callerId：

|   名称   |                             定义                             |
| :------: | :----------------------------------------------------------: |
|   UID    | 本身是linux权限系统中泳衣区分用户身份的表示，由于Android是单一用户权限系统，uid在Android里面又可以理解为应用的标识Id，该Id自安装之日起就被分配，始终如一。 |
|   PID    | 进程的唯一标识id，进程重启，系统会重新分配pid，用以和远程服务交互。 |
|  appId   | 可以理解为应用Id，跟uid一样。这是本地应用在远程服务中的叫法  |
| callerId | callerId是服务请求者的身份Id，可以是本地应用的身份Id，也可以是远程服务的身份标识Id. |

**如何查看android.jar中的隐藏Api**

(1)GitHub已有人去除android.jar中的@hide注解；

(2)下载对应API版本android.jar;

(3)替换SDK/platforms/android-版本/android.jar。

(4)重新打开IDE就可以查看

![image-dangerous-permission](D:\Projects\LearningProjects\JSeries\JNotes\短课程\Android9.0动态运行时权限源码分析及封装改造\img\image-dangerous-permission.png)