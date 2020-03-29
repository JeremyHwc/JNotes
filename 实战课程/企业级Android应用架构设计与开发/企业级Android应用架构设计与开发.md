# 企业级Android应用架构设计与开发

## 第4章 maven私服搭建

**maven是什么**

![image-maven](D:\Projects\LearningProjects\JSeries\JNotes\实战课程\企业级Android应用架构设计与开发\img\image-maven.png)

**maven私服**

![image-maven-private](D:\Projects\LearningProjects\JSeries\JNotes\实战课程\企业级Android应用架构设计与开发\img\image-maven-private.png)

**maven私服搭建流程**

（1）在https://www.sonatype.com/download-oss-sonatype地址下载nexus，下载完成以后，解压在磁盘下面；

（2）cmd进入解压以后的nexus的bin目录下，通过nexus /run或者nexus start 启动nexus服务；

（3）启动成功以后，浏览器输入localhost:8081，即可进入如下界面;

![image-nexus-home](D:\Projects\LearningProjects\JSeries\JNotes\实战课程\企业级Android应用架构设计与开发\img\image-nexus-home.png)

**maven私服接入**

（1）私服地址配置在根build.gradle文件

```
buildscript {
    repositories {
        jcenter()
        google()
        mavenCentral()
        // 为构建工具引入maven，下面的classpath就是从这些配置的repositories里查找
        maven {
            url 'http://localhost:8081/repository/imooc-releases/'
            credentials {
                username 'admin'
                password 'admin123'
            }
        }
        maven {
            url 'http://localhost:8081/repository/imooc-snapshots/'
            credentials {
                username 'admin'
                password 'admin123'
            }
        }
    }

    dependencies {
        classpath 'com.android.tools.build:gradle:3.4.1'
        //greendao插件依赖
        classpath 'org.greenrobot:greendao-gradle-plugin:3.2.2'
        //ARouter插件依赖
        classpath "com.alibaba:arouter-register:1.0.2"
    }
}

allprojects {
    repositories {
        jcenter()
        google()
        maven { url "https://jitpack.io" }
        //项目仓库下载地址
        maven {
            url 'http://localhost:8081/repository/imooc-releases/'
            credentials {
                username 'admin'
                password 'fighting,.,.hwc'
            }
        }
        maven {
            url 'http://localhost:8081/repository/imooc-snapshots/'
            credentials {
                username 'admin'
                password 'fighting,.,.hwc'
            }
        }
    }
}
```

（2）在需要使用maven私服的module的build.gradle中添加配置

```
apply plugin: 'maven'

def pomName = this.getName()
def pomVersionName = '1.0.0-SNAPSHOT'
def pomDescription = 'the audio library for all project'

//上传maven配置
uploadArchives {
    repositories {
        mavenDeployer {
            repository(url: NEXUS_REPOSITORY_URL) {
                authentication(userName: NEXUS_USERNAME, password: NEXUS_PASSWORD)
            }
            pom.project {
                name pomName
                version pomVersionName
                description pomDescription
                artifactId pomVersionName
                groupId POM_GROUPID
                packaging POM_PACKAGING
            }
        }
    }
}
```

