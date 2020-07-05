# Relugin源码解读

![image_classloader](D:\Projects\LearningProjects\JSeries\JNotes\实战课程\实战企业级项目 践行App重构之路\Replugin源码解读\img\image_classloader.png)

BootClassLoader:

PathClassLoader:

DexClassLoader:应用可以自己定义该ClassLoader，去加载未经过安装的jar包或apk中的dex文件

## RepluginClassLoader源码分析