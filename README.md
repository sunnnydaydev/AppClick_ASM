### Android 全埋点实现方案，ASM 技术！

>其实appClick全埋点的实现在前几节通过代理View.OnClickListener、通过AOP思想的AspectJ都已经简单的实现了，埋点的那一套逻辑都是几乎一致的，变化的是使用的技术。因此本节主要以总结学习新技术为主。

### 干预安卓打包的流程API -Transform 

###### 1、安卓打包流程简介

![](https://github.com/sunnnydaydev/AppClick_ASM/blob/master/android.png)

> 如上图安卓在打包时会将资源文件通过aapt工具编译为R文件，AIDl文件通过aidl工具编译为java 接口文件。然后把所有的java文件 通过javaC编译器便以为class文件。class文件通过dex工具打包为dex文件最终编译为apk。

###### 2、Transform简介 

![](https://github.com/sunnnydaydev/AppClick_ASM/blob/master/transform.png)

> Google从Android Gradle 1.5.0开始，提供了Transform API。通过Transform API，允许第三方以插件（Plugin）的形式，在Android应用程序打包成.dex文件之前的`编译过程中操作`.class文件。用户可自行定义Transform 实现遍历所有的class文件。拿到class文件后就可以操作class文件啦！实现自己的逻辑。
>
> 须知：
>
> 1、Transform 要以插件的形式引入，说明我们要自定义Gradle 插件。
>
> 2、其实每个Transform就是一个Task
>
> 3、Gradle 内置的 Transform（Task）有 mergeManifest、Proguard 等等。我们自定义的 Transform 与系统自带的一些Trasnform 形成了一个 Trasnform 链，我们定义的 Transform 会首先执行。
>
> 4、Transform是作用在.class编译后，打包成.dex前，Transform在这期间可以对`.class字节码文件`进行再处理。
>
> 5、概括来说，Transform 就是把输入的 .class 文件转变成目标字节码文件。

### ASM简介

> ASM 是一个 Java 字节码操控框架。它能被用来动态生成类或者增强既有类的功能。ASM 可以直接产生二进制 class 文件，也可以在类被加载入 Java 虚拟机之前动态改变类行为。Java class 被存储在严格格式定义的 .class 文件里，这些类文件拥有足够的元数据来解析类中的所有元素：类名称、方法、属性以及 Java 字节码（指令）。ASM 从类文件中读入信息后，能够改变类行为，分析类信息，甚至能够根据用户要求生成新类。

###### 1、为啥需要ASM

>通过Trasnform 我们可以遍历拿到所有的class字节码文件，但是Class文件是一组以8位字节为基础单位的二进制流，各项数据项目严格按照顺序紧凑地排列在Class文件之中，中间没有添加任何分隔符。这就导致我们直接操作class文件是十分不容易的。原始的修改工具UltraEdit 太麻烦。而ASM框架小、速度块、效率高正好可以使用。

### 总结

> 所以要想通过ASM实现全埋点，我们首先通过自定义Trasnform 拿到class字节码，然后通过ASM操作字节码即可。这就需要我们的知识积累了：
>
> 1、自定义Gradle插件
>
> 2、Trasnform 相关API 
>
> 3、ASM相关API知识
>
> 4、字节码知识入门

### Gradle Transform 

   待续！！！

### AMS 

  待续！！！



### ASM 实战全埋点

待续！！！



参考：

- 《安卓全埋点解决方案》微信读书版 第9章　$AppClick全埋点方案6：ASM
- https://www.ibm.com/developerworks/cn/java/j-lo-asm30/
- https://www.sensorsdata.cn/blog/20181206-9/
- https://www.jianshu.com/p/37a5e058830a
- https://www.jianshu.com/p/9039a3e46dbc







