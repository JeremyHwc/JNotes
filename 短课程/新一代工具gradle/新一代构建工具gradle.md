**构建工具的作用**
（1）依赖管理
（2）测试、打包、发布

**主流构建工具**

![image-20200328142614789](D:\Projects\LearningProjects\JSeries\JNotes\短课程\新一代工具gradle\img\image-20200328142614789.png)

**Gradle是什么**

​	一个开源的项目自动化构建工具，简历在Apache Ant和Apache Maven概念的基础上，并引入了基于Groovy的特定领域语言（DSL），而不再使用XML形式管理构建脚本。

**Gradle安装**

（1）确保已经安装JDK，java - version

（2）从Gradle官网下载Gradle，https://gradle.org

（3）配置环境变量，GRADLE_HOME

（4）添加到Path，%GRADLE_HOME%\bin

（5）验证是否安装成功，gradle -v

**Groovy是什么**

​	Groovy是用于Java虚拟机的一种敏捷的动态语言，它是一种成熟的面向对象编程语言，既可以用于面向对象编程，又可以用作纯粹的脚本语言。使用该种语言不必编写过多的代码，同时又具有闭包和动态语言中的其他特性

​	. Groovy完全建荣Java的语法，分号是可选的，类、方法默认是public的。

​	. 编译器给属性自动添加getter/setter方法。

​	. 属性可以直接用点号获取

​	. 最后一个表达式的值会被作为返回值

​	. ==等同于equals()，不会有NullointerExceptions

```groovy
class ProjectVersion{
    private int major
    private int minor

    int getMajor() {
        major
    }

    void setMajor(int major) {
        this.major = major
    }

    int getMinor() {
        minor
    }

    void setMinor(int minor) {
        this.minor = minor
    }

    ProjectVersion(int major, int minor) {
        this.major = major
        this.minor = minor
    }
}

ProjectVersion projectVersion = new ProjectVersion(2,1)

println projectVersion.getMajor()
println projectVersion.minor

```

**高效的Groovy特性**

```groovy
// Groovy 高效特性
// 1. 可选的类型定义
def version = 1

// 2. assert
assert version == 1

// 3. 括号是可选的
println version

// 4. 字符串
def s1 = 'imooc'
def s2 = "gradle version is ${version}"
def s3 = '''my
name
is
imooc'''
println s1
println s2
println s3

//4. 集合api
//list
def buildTools = ['ant', 'maven']
buildTools << 'gradle' //追加一个元素
assert buildTools.getClass() == ArrayList
assert buildTools.size() == 3
println buildTools

//5. map
def buildYears = ['ant': 2000, 'maven': 2004]
buildYears.gradle = 2009
println buildYears.ant
println buildYears['gradle']
println buildYears
println buildYears.getClass()

//6. 闭包
def c1 = { v ->
    println v
}

def c2 = {
    println 'hello'
}

def method1(Closure closure) {
    closure('param')
}

def method2(Closure closure) {
    closure()
}

method1(c1)
method2(c2)
```

**Groovy基础知识**

```groovy
// 构建脚本中默认都是有个Project实例的
apply plugin: 'java'

version = '1'

repositories{
    mavenCentral()
}

dependencies{
    compile 'commons-codec:commons-codec:1.6'
}
```

**构建脚本概要**

1. **构建块**

   Gradle构建中的两个基本概念是项目（project）和任务（task），每个构建至少包含一个项目，项目中包含一个或多个任务。在多项目构建中，一个项目可以依赖于其他项目；类似的，任务可以形成一个依赖关系图来确保他们的执行顺序。

   ![image-project-task](D:\Projects\LearningProjects\JSeries\JNotes\短课程\新一代工具gradle\img\image-project-task.png)

2. **项目（project）**

   一个项目代表一个正在构建的组件（比如一个jar文件），当构建启动后，Gradle会基于build.gradle实例化一个org.gradle.api.Project类，并且能够通过project变量使其隐式可用

   . group、name、version

   . apply、dependencies、repositories、task

   . 属性的其他配置方式：ext、gradle.properties

3. **任务（task）**

   . dependsOn

   . doFirst、doLast、<<

   ```groovy
   def createDir = {
       path ->
           File dir = new File(path)
           if (!dir.exists()) {
               dir.mkdirs()
           }
   }
   
   task makeJavaDir(group: 'imooc') {
       def paths = ['src/main/java', 'src/main/resources', 'src/test/java', 'src/test/resources']
       doFirst {
           paths.forEach(createDir)
       }
   }
   
   task makeWebDir(group: 'imooc') {
       dependsOn makeJavaDir
       def paths = ['src/main/webapp', 'src/test/webapp']
       doLast {
           paths.forEach(createDir)
       }
   }
   ```

**构建生命周期**

**初始化：**会初始化所有将会参与到构建中的项目；

**配置：**根据配置代码（除了动作代码，其余都是配置代码）生成task的依赖顺序以及执行顺序

**执行：**执行动作代码

![image-configure-perform](D:\Projects\LearningProjects\JSeries\JNotes\短课程\新一代工具gradle\img\image-configure-perform.png)

![image-gradle-hook](D:\Projects\LearningProjects\JSeries\JNotes\短课程\新一代工具gradle\img\image-gradle-hook.png)

**依赖管理**

 1. **概述**

    几乎所有的基于JVM的软件项目都需要依赖外部类库来重用现有的功能。自动化的依赖管理可以明确依赖的版本，可以解决因传递性依赖带来的版本冲突。

2. **工件坐标**

   . group、name、version

3. **常用仓库**

   . mavenLocal/mavenCentral/jcenter，mavenLocal是本机中通过maven使用过的仓库，都会在本机存有一份。

   . 自定义maven仓库，也就是常说的maven私服

   . 文件仓库，即本地机器上的文件路径也可以作为仓库（不建议使用）

4. **依赖的传递性**

   . B依赖A，如果C依赖B，那么C依赖A

5. **自动化依赖管理**

![image-dependency-manager](D:\Projects\LearningProjects\JSeries\JNotes\短课程\新一代工具gradle\img\image-dependency-manager.png)

6. **依赖阶段配置**

   （1）compile、runtime

   （2）testCompile、testRuntime

   ![image-dependency-period](D:\Projects\LearningProjects\JSeries\JNotes\短课程\新一代工具gradle\img\image-dependency-period.png)

   运行时阶段，都是扩展与编译阶段，也就是说在编译阶段依赖的jar包，在运行时都会依赖；

   如果在运行时依赖，在编译阶段不一定依赖；

   源代码依赖的，测试代码都会依赖；

   测试代码依赖的，源代码不一定会依赖；

7. **解决版本冲突**

   ![image-dependency-conflict](D:\Projects\LearningProjects\JSeries\JNotes\短课程\新一代工具gradle\img\image-dependency-conflict.png)

   （1）查看依赖报告

   ​	执行dependencies任务

   （2）排除传递性依赖

   ```
   dependencies {
       implementation ('org.hibernate:hibernate-core:3.6.3.Final'){
           exclude group: 'org.slf4j',module:'slf4j-api'
       }
   }
   ```

   （3）强制一个版本

   ```
   // 遇到版本冲突时，gradle默认采用最新版本的
   // 以下是修改默认策略，只要遇到版本冲突时，直接让其构建失败
   configurations.all {
       resolutionStrategy{
           failOnVersionConflict() // 版本冲突时直接构建失败
           force 'org.slf4j:slf4j-api:1.7.22'// 强制性指定一个版本，解决冲突
       }
   }
   ```

**多项目构建**

1. 项目模块化

   在企业项目中，包层次和类关系比较复杂，把代码拆分成模块通常是最佳实践，这需要你清晰的划分功能的便捷，比如把业务逻辑和数据持久化拆分开来。项目符合高内聚低耦合时，模块就变得很容易，这是一条非常好的软件开发实践。

   ```
   // 根project配置所有project的配置
   allprojects {
       apply plugin: 'java'
       sourceCompatibility = 1.8
   }
   // 子project公有的配置可以抽取的根project进行统一配置
   subprojects {
       dependencies {
           testCompile group: 'junit', name: 'junit', version: '4.12'
       }
   }
   ```

2. Gradle测试

   （1）自动化测试

   ​	一些开源的测试框架比如JUnit,TestNG能够帮助你编写可服用的结构化的测试，为了运行这些测试，你要先编译他们，就想编译源代码一样。测试代码的作用仅仅用于测试的情况，不应该被发布到生产环境中，需要把源代码和测试代码分开来。

   ![image-gradle-test](D:\Projects\LearningProjects\JSeries\JNotes\短课程\新一代工具gradle\img\image-gradle-test.png)

   如果测试成功，构建会继续进行；如果测试失败，构建就直接结束了。只要我们将测试代码放在相应的目录，构建工具会自动发现这些测试用例，并且在构建之前自动执行。

   ![image-gradle-test-flow](D:\Projects\LearningProjects\JSeries\JNotes\短课程\新一代工具gradle\img\image-gradle-test-flow.png)

   ![image-gradle-test-found](D:\Projects\LearningProjects\JSeries\JNotes\短课程\新一代工具gradle\img\image-gradle-test-found.png)

   ```
   import org.junit.Assert;
   import org.junit.Test;
   
   public class GradleTestExample {
       private ToolItem toolItem = new ToolItem("imooc");
       @Test
       public void testSave(){
   //        Assert.assertNotNull(toolItem);
           Assert.assertNull(toolItem);
       }
   }
   ```

3. 发布

   ![image-plugin-publish](D:\Projects\LearningProjects\JSeries\JNotes\短课程\新一代工具gradle\img\image-plugin-publish.png)

   ```
   // 发布
   apply plugin: 'maven-publish'
   
   publishing{
       publications{
           myPublish(MavenPublication){
               from components.java
           }
       }
       repositories {
           maven{
               name "myRepo"
               // maven仓库地址
               url ""
           }
       }
   }
   ```

**课程总结**

![image-gradle-conclusion](D:\Projects\LearningProjects\JSeries\JNotes\短课程\新一代工具gradle\img\image-gradle-conclusion.png)