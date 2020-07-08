### Android 全埋点实现方案，ASM 技术！

>其实appClick全埋点的实现在前几节通过代理View.OnClickListener、通过AOP思想的AspectJ都已经简单的实现了，埋点的那一套逻辑都是几乎一致的，变化的是使用的技术。因此本节主要以总结学习新技术为主。

### ASM简介

> ASM 是一个 Java 字节码操控框架。它能被用来动态生成类或者增强既有类的功能。ASM 可以直接产生二进制 class 文件，也可以在类被加载入 Java 虚拟机之前动态改变类行为。Java class 被存储在严格格式定义的 .class 文件里，这些类文件拥有足够的元数据来解析类中的所有元素：类名称、方法、属性以及 Java 字节码（指令）。ASM 从类文件中读入信息后，能够改变类行为，分析类信息，甚至能够根据用户要求生成新类。

### 干预安卓打包的流程API -Transform 

###### 1、安卓打包流程简介

![](https://github.com/sunnnydaydev/AppClick_ASM/blob/master/android.png)

> 如上图安卓在打包时会将资源文件通过aapt工具编译为R文件，AIDl文件通过aidl工具编译为java 接口文件。然后把所有的java文件 通过javaC编译器便以为class文件。class文件通过dex工具打包为dex文件最终编译为apk。

###### 2、埋点原理

> 通过上图可知，我们只要在图中生成.dex文件之前拦截，就可以拿到当前应用程序中所有的.class文件，然后借助一些库，就可以遍历这些.class文件中的所有方法，再根据一定的条件找到需要的目标方法，最后进行修改并保存，就可以插入埋点代码了。

###### 3、Android Gradle提供的类Transform 



![](https://github.com/sunnnydaydev/AppClick_ASM/blob/master/transform.png)

> Google从Android Gradle 1.5.0开始，提供了Transform API。通过Transform API，允许第三方以插件（Plugin）的形式，在Android应用程序打包成.dex文件之前的编译过程中操作.class文件。我们只要实现一套Transform，去遍历所有.class文件的所有方法，然后进行修改（在特定listener的回调方法中插入埋点代码），最后再对原文件进行替换，即可达到插入代码的目的。
>
> 须知：
>
> 1、Transform 要以插件的形式引入，说明我们要自定义Gradle 插件。
>
> 2、Gradle 内置的 Transform（Task）有 mergeManifest、Proguard 等等，和我们自定义的 Transform 形成了一个 Trasnform 链，我们定义的 Transform 会首先执行。



### Transform 

   待续！！！

### AMS 

  待续！！！

### ASM 实战全埋点

待续！！！



参考：

- 《安卓全埋点解决方案》微信读书版 第9章　$AppClick全埋点方案6：ASM
- https://www.ibm.com/developerworks/cn/java/j-lo-asm30/







