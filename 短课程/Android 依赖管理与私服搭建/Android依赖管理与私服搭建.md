# Android依赖管理与私服搭建

## 第1章 Android依赖管理简介

### 1.1 什么是依赖

​	项目A需要使用项目B中已经完成的功能、类和方法

​	业务代码中需要使用已经封装好的库

​	库或框架中需要使用别的库或框架中已有的功能

### 1.2 为什么需要依赖管理

​	写程序的本质是一个抽象的过程，功能和业务需要分离

​	一个项目旺旺需要大量的库和功能，需要统一的管理

### 1.3 Android目前最好的依赖管理工具Gradle

​	Gradle是一个基于Apache Ant和Apach Maven概念的项目自动化建构工具

​	Gradle是以Groovy语言为基础，面向Java应用为主

​	Google默认并推荐使用Gradle

## 第2章 Android引入依赖的多种方式

### 2.1 Android依赖引入方式一

​	切换project视图 -- 新建libs目录 -- 拷贝jar包 -- add as library

### 2.2 Android依赖引入方式二

​	import module -- Project Structure -- 选中相应module -- module dependency