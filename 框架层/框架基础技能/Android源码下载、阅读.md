## Android源码下载、阅读

### 一、源码下载

1. 打开https://android.googlesource.com/（注意：需要开墙才能访问），页面会列出Android源码目录。

   ![img_source](D:\Projects\LearningProjects\JSeries\JNotes\框架层\框架基础技能\img\img_source.jpg)

2. 点击对应的源码目录，比如：platform/frameworks/base，可以看到源码的很多分支。![img_source1](D:\Projects\LearningProjects\JSeries\JNotes\框架层\框架基础技能\img\img_source1.png)

3. 点击想要查看的分支，如果需要查看所有的分支，点击“More..."

   ![img_source2](D:\Projects\LearningProjects\JSeries\JNotes\框架层\框架基础技能\img\img_source2.png)

   ![img_source3](D:\Projects\LearningProjects\JSeries\JNotes\框架层\框架基础技能\img\img_source3.png)

4. 点击上图红色框中的“tgz"，即可下载platform/frameworks/base下的源码，我下载的是Android 10的，大小在767M。

### 二、源码阅读

​	通过SourceInsight导入Android源码进行阅读，注意，初学者不要将Android整个源码下载进行导入，不然在导入的时候卡很久，只需要导入自己关心的模块源代码即可。下面以platform/frameworks/base为例。

**步骤1：Project -- New Project**

![img_si_step1](D:\Projects\LearningProjects\JSeries\JNotes\框架层\框架基础技能\img\img_si_step1.png)

**步骤2：确定工程名和存储工程数据的路径**

![img_si_step2](D:\Projects\LearningProjects\JSeries\JNotes\框架层\框架基础技能\img\img_si_step2.png)

**步骤3：选择需要导入的源码路径，点击”OK“**

![img_si_step3](D:\Projects\LearningProjects\JSeries\JNotes\框架层\框架基础技能\img\img_si_step3.png)

**步骤4：点击“Add”，勾选下图中的两个选项，点击“OK"，即就会开始导入**

![img_si_step4](D:\Projects\LearningProjects\JSeries\JNotes\框架层\框架基础技能\img\img_si_step4.png)

**步骤5：正在导入源码**

![img_si_step5](D:\Projects\LearningProjects\JSeries\JNotes\框架层\框架基础技能\img\img_si_step5.png)

**步骤6：导入完毕，点击“Close”**

![img_si_step6](D:\Projects\LearningProjects\JSeries\JNotes\框架层\框架基础技能\img\img_si_step6.png)

**步骤7：在Project Files栏搜索你需要查看的文件名称，这里以SystemServer为例**

![img_si_step7](D:\Projects\LearningProjects\JSeries\JNotes\框架层\框架基础技能\img\img_si_step7.png)



​	以上就是源码下载和导入的所有流程。