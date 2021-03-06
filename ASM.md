### 简介

> ASM 是一个功能比较齐全的 Java 字节码操作与分析框架。它能被用来动态生成类或者增强既有类的功能。

### 功能

> 1、ASM 可以直接 产生二进制 class 文件。
>
> 2、ASM可以在类被加载入 Java 虚拟机之前动态改变类的行为。
>
> ps：Java class 被存储在严格格式定义 的 .class 文件里，这些类文件拥有足够的元数据来解析类中的所有元素，包括类名称、方法、属性以及 Java 字节码(指令)。 ASM 从类文件中读入这些信息后，能够改变类行为、分析类的信息，甚至能够根据具体的要求生成新的类。



### 核心类简介

|      类名       |                    介绍                    |
| :-----------: | :--------------------------------------: |
|  ClassReader  |          该类用来分析编译过的class字节码文件。           |
|  ClassWriter  | 该类用来重新构建编译后的类，比如说修改类名、属性以及方法，甚至可以生成新的类的字节码文件。 |
| ClassVisitor  | 主要负责 “拜访” 类成员信息。其中包括类上的注解，类的构造方法，类的字段，类的方法，静态代码块。 |
| AdviceAdapter | 实现了MethodVisitor接口，主要负责 “拜访” 方法的信息，用来进行具体的方法字节码操作。 |

### ClassVisitor

>1、这个类中的每个方法都对应于同名的类文件结构部分。如visit方法代表类头部分，这部分记录类名，包名，类的权限修饰，继承的父类等信息。visitMethod带不类的方法部分，这部分主要记录了方法相关的信息。
>
>2、这个类中的方法按照特定的顺序调用。（顺序规则参考下文）
>
>3、这个类是ASM api的核心，我们生成新类、转变已编译类、分析现有类都离不开他。

###### 1、ClassVisitor重要方法介绍

```java

/**
 * Create by SunnyDay on 20:45 2020/07/15
 */
public class AutoTrace extends ClassVisitor {
    /**
     * 构造，至少提供一个构造，传进ASM的版本即可。
     * 常量值可为：
     * Opcodes#ASM4
     * Opcodes#ASM5
     * Opcodes#ASM6
     * Opcodes#ASM7
     * */
    public AutoTrace(int api) {
        super(api);
    }
    /**
     * 扫描类时第一个拜访的方法
     * @param version 代表jdk的版本。比如52代表jdk1.8。
     * @param access 类的修饰符。修饰符在 ASM 中是以“ACC_”开头的常量。可以作用到类级别上 的修饰符有:ACC_PUBLIC(代表public)
     * @param name 类名。通常我们会使用完成的包名+类名表示类。例如com.example.MyClass。但是在字节码中是以路径的形式表示的。
     *             例如com/example/MyClass。值得注意的是，虽然是路径表示法但是不需要写明类的“.class”扩展名。
     * @param signature 表示泛型信息，如果类未定义任何泛型，则这里参数为空。
     * @param superName 表 示 所 继 承 的 父 类。由 于 Java 的 类 是 单 根 结 构，即 所 有 类 都 继 承 自 java.lang.Object。
     *                  因此可以简单的理解为任何类都会具有一个父类。虽然在编 写 Java 程序时我们没有去写 extends 关键字去明确
     *                  继承的父类，但是 JDK 在编 译时总会为我们加上“extends Object”。
     * @param interfaces 表示类实现的接口，在 Java 中，类是可以实现多个不同的接口，因此该参数是 一个数组。
     *
     * */
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
    }

    /**
     * 当扫描器扫描到类的方法时进行调用
     * @param access 表示方法的修饰符。与类的修饰符类似。
     * @param name 方法名
     * @param descriptor 方法签名。方法签名的格式：（参数类型1，参数类型2 ...）返回值类型。
     *                   例如： void test（int a,float b） ASM中方法签名表示为（IF）V
     * @param signature 泛型相关信息。
     * @param exceptions 表示将会抛出的异常，如果方法不会抛出异常，该参数为空。
     * */
    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }

    /**
     * 扫描注解时进行调用
     * */
    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        return super.visitAnnotation(descriptor, visible);
    }

    /**
     * 扫描字段时进行调用
     * @param access 字段修饰符
     * @param name 字段名
     * @param descriptor  字段类型
     * @param signature 泛型相关信息
     * @param value 字段值
     * */
    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        return super.visitField(access, name, descriptor, signature, value);
    }

    /**
     * 调用结束时调用这个方法
     * */
    @Override
    public void visitEnd() {
        super.visitEnd();
    }

}
```

###### 1、jdk版本对应的int类型数值补充（参考visit方法的version参数）

| **JDK版本** | **int数值** |
| :-------: | :-------: |
|   jdk 8   |    52     |
|   jdk 7   |    51     |
|   jdk 6   |    50     |
|   jdk 5   |    49     |
|  jdk 1.4  |    48     |
|  jdk 1.3  |    47     |
|  jdk 1.2  |    46     |
|  jdk 1.1  |    45     |

###### 2、常见修饰符access补充（类、方法、字段等修饰符）

> 更多参考Opcodes接口源码中以ACC_开头常量。

|    **修饰符**     |  **含义**   |
| :------------: | :-------: |
|   ACC_PUBLIC   |  public   |
|  ACC_PRIVATE   |  private  |
| ACC_PROTECTED  | protected |
|   ACC_FINAL    |   final   |
|   ACC_SUPER    |  extends  |
| ACC_INTERFACE  |    接口     |
|  ACC_ABSTRACT  |    抽象类    |
| ACC_ANNOTATION |   注解类型    |
|    ACC_ENUM    |   枚举类型    |
|   ACC_STATIC   |  static   |

###### 3、字段描述符补充（参考visitField中descriptor）

>在已编译类中,字段类型都是用类型述符表示的。如下图。
>
>基元类型的描述符是单个字符：Z 表示 boolean，C 表示 char，B 表示 byte，S 表示 short， 
>
>I 表示 int，F 表示 float，J 表示 long，D 表示 double。一个类类型的描述符是这个类的
>
>内部名，前面加上字符 L ，后面跟有一个分号。例如， String 的类型描述符为
>
>Ljava/lang/String;。而一个数组类型的描述符是一个方括号后面跟有该数组元素类型的描
>
>述符。

| **java类型** |      **类型描述符**       |
| :--------: | :------------------: |
|  boolean   |          Z           |
|    char    |          C           |
|    byte    |          B           |
|   short    |          S           |
|    int     |          I           |
|   float    |          F           |
|    long    |          J           |
|   double   |          D           |
|   Object   |  Ljava/lang/Object;  |
|   int[]    |          [I          |
| Object[][] | [[Ljava/lang/Object; |

###### 4、方法描述符补充（参考visitMethod中的descriptor）

> 方法描述符是一个类型描述符列表，它用一个字符串描述一个方法的参数类型和返回类型。方法描述符以左括号开头，然后是每个形参的类型描述符，然后是一个右括号，接下来是返回类型的类型描述符，如果该方法返回 void，则是 V（方法描述符中不包含方法的名字或参数名）。栗子如下表。

|    **java源文件中的方法声明**     |        **方法描述符**        |
| :----------------------: | :---------------------: |
|   void m(int i ,int f)   |          (IF)V          |
|     int m(Object o)      |  (Ljava/lang/Object;)I  |
| int[] m(int i, String s) | (ILjava/lang/String;)[I |
|    Object m(int[] i)     | ([I)Ljava/lang/Object;  |

###### 5、ClassVisitor类内的方法调用顺序

> 点击这个类的源码，看类的介绍时你会发现这个类中的方法是有调用顺序的。我们必须按照规定来调用。
>
> 如下类的源码注释摘抄整理，调用顺序：visit->visitSource->visitModule->visitNestHost->visitOuterClass
>
> ->......visitEnd。但是下面[] ,{},(),* ,| 代表舍意思？

```java
 
 {visit}

 [ {visitSource} ] [ { visitModule} ] [ { visitNestHost} ][ {
 visitOuterClass} ] 

( { visitAnnotation} | { visitTypeAnnotation} | {
 visitAttribute} )* 
( { visitNestMember} | { visitInnerClass} | { visitField} |
  { visitMethod} )* 
  
{ visitEnd}.
 
 
```

>  1、{visit} ，只用{}括起来的方法是必须被调用的。如 visit，visitEnd。
>
>  2、[{visitSource}],用[{}]括起来的方法最多一次调用。（也就是可以调用一次，或者不调用）
>
>  3、({visitAnnotation}) , 用（{}）* 括起来的方法代表可被调用任意次，|表示 方法间的调用顺序也随意。
>
>  ps：其实符号倒无所谓，我们只需记得这些方法被调用的先后顺序，以及哪些在调用期间可被多次调用，无顺序调用即可。

### ClassReader

> ClassReader 类分析以字节数组形式给出的已编译类，并针对在其 accept 方法参数中传送的 ClassVisitor 实例，调用ClassVisitor 实例的 visitXxx 方法。这个类可以看作一个事件产生器。

###### 1、栗子：读取指定class文件的信息

（1）准备一个类

```java
package com.sunnyday.appclick_asm.test;

/**
 * Create by SunnyDay on 19:48 2020/07/18
 */
public class Person {
    public String age;
    public String name;

    public void run(){
        System.out.println("person can run!");
    }
}

```

（2）定义ClassVisitor的子类，用于输出信息。

```java

/**
 * Create by SunnyDay on 19:48 2020/07/18
 */
public class ReadClassInfo extends ClassVisitor {
    public ReadClassInfo(int api) {
        super(api);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        System.out.println("--------visit--------");
        System.out.println("jdk version:"+version);
        System.out.println("权限修饰符access:"+access);
        System.out.println("泛型信息signature:"+signature);
        System.out.println("类名name:"+name);
        System.out.println("父类名superName:"+superName);

    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        System.out.println("--------visitField--------");
        System.out.println("权限修饰符access:"+access);
        System.out.println("泛型信息signature:"+signature);
        System.out.println("方法名name:"+name);
        System.out.println("descriptor:"+descriptor);
        System.out.println("value:"+value);
        return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        System.out.println("--------visitMethod--------");
        System.out.println("权限修饰符access:"+access);
        System.out.println("泛型信息signature:"+signature);
        System.out.println("方法名name:"+name);
        System.out.println("descriptor:"+descriptor);
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }

    @Override
    public void visitEnd() {
        System.out.println("--------visitEnd--------");
        super.visitEnd();
    }
}

```

（3）简单调用及其log

```java
public class MainTest {
    public static void main(String[] args) {
      readClassInfo();
    }
    /**
     * 分析类：读取类的信息。
     */
    private static void readClassInfo() {
        try {
            ReadClassInfo readClassInfo = new ReadClassInfo(Opcodes.ASM7);
            // 分析person类的字节码
            ClassReader classReader = new ClassReader("com.sunnyday.appclick_asm.test.Person");
            classReader.accept(readClassInfo, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

--------visit--------
jdk version:51
权限修饰符access:33
泛型信息signature:null
类名name:com/sunnyday/appclick_asm/test/Person
父类名superName:java/lang/Object
--------visitField--------
权限修饰符access:1
泛型信息signature:null
方法名name:age
descriptor:Ljava/lang/String;
value:null
--------visitField--------
权限修饰符access:1
泛型信息signature:null
方法名name:name
descriptor:Ljava/lang/String;
value:null
--------visitMethod--------
权限修饰符access:1
泛型信息signature:null
方法名name:<init>
descriptor:()V
--------visitMethod--------
权限修饰符access:1
泛型信息signature:null
方法名name:run
descriptor:()V
--------visitEnd--------

Process finished with exit code 0

```

###### 2、api简介

（1）首先是Opcodes.ASM7参数，这个为ASM的实现版本。由于自定义ClassVisitor要求要调用父类的构造传这个参数，所以我们使用时传递下即可。Opcodes下有好多常量值。详情可以看这个类的源码。

（2）ClassReader类构造读取字节码文件

```java
//1、最简单使用方式，指明类的全路径名即可。底层使用ClassLoader来加载class文件
 public ClassReader(final String className) throws IOException {
    this(
        readStream(
            ClassLoader.getSystemResourceAsStream(className.replace('.', '/') + ".class"), true));
  }
//2、 传入输入流（字节码文件的）其实和1源码一致。如下实例代码：
public ClassReader(final InputStream inputStream) throws IOException {
    this(readStream(inputStream, false));
  }
// 实例代码
  String className = "com.sunnyday.appclick_asm.test.Person.class";
      InputStream inputStream =  ClassLoader.getSystemResourceAsStream(className.replace('.', '/') + ".class");

//3、其他构造略。。。。
    
```



（3）ClassReader的accept

```java
 /**
   * @function 访问class文件信息
   * @param classVisitor 用户自定义的classVisitor
   * @param parsingOptions 解析类时的筛选条件，下面常量值之一  {@link
   *     #ClassReader.SKIP_CODE}, {@link #ClassReader.SKIP_DEBUG}, {@link #ClassReader.SKIP_FRAMES} or {@link #ClassReader.EXPAND_FRAMES}.
   */
  public void accept(final ClassVisitor classVisitor, final int parsingOptions) {
    accept(classVisitor, new Attribute[0], parsingOptions);
  }
```

### ClassWriter

> 继承了ClassVisitor，最简单的使用就是生成class，通过调用一系列特定顺序的方法即可。

###### 1、栗子：生成类

```java
 /**
     * 生成如下类：
     * package com.sunnyday.appclick_asm.test
     * public interface MyInterface{
     * int NUM = 1；
     * int sum（int a）；
     * }
     */
    private  void generateClass() {
        ClassWriter classWriter = new ClassWriter(0);
        classWriter.visit(
                Opcodes.V1_8,
                Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT + Opcodes.ACC_INTERFACE,//这里注意，修饰符使用+连接。且ASM中接口要用ACC_ABSTRACT+ACC_INTERFACE表示
                "package/com/sunnyday/appclick_asm/test/MyInterface",
                null,
                "java/lang/Object",// 这里需要注意接口的直接父类就是Object。不管接口继承了另一个接口。这点和普通类有区别。
                null);
        classWriter.visitField(
                Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC,
                "NUM",
                "I",
                null,
                1).visitEnd();

        classWriter.visitMethod(
                Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT,
                "sum",
                "(I)I",
                null,
                null).visitEnd();
        classWriter.visitEnd();// 结束
        byte[] bytes = classWriter.toByteArray();//吧生成的类直接转换为字节数组
        // todo 使用类
    }
```

###### 2、使用类：ClassLoader加载

```java
/**
 * Create by SunnyDay on 20:56 2020/07/27
 */
public class MyClassLoader extends ClassLoader {
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (name.endsWith("_Stub")) {
            ClassWriter classWriter = new ClassWriter(0);
            classWriter.visit(
                    Opcodes.V1_8,
                    Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT + Opcodes.ACC_INTERFACE,//这里注意，修饰符使用+连接。且ASM中接口要用ACC_ABSTRACT+ACC_INTERFACE表示
                    "package/com/sunnyday/appclick_asm/test/MyInterface",
                    null,
                    "java/lang/Object",// 这里需要注意接口的直接父类就是Object。不管接口继承了另一个接口。这点和普通类有区别。
                    null);
            classWriter.visitField(
                    Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC,
                    "NUM",
                    "I",
                    null,
                    1).visitEnd();

            classWriter.visitMethod(
                    Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT,
                    "sum",
                    "(I)I",
                    null,
                    null).visitEnd();
            classWriter.visitEnd();// 结束
            byte[] bytes = classWriter.toByteArray();//吧生成的类直接转换为字节数组
            return defineClass(name, bytes, 0, bytes.length);//classLoader 加载
        }
        return super.findClass(name);
    }
}
```



###  修改类

> 修改字节码文件、可进行增删加类成员、类方法等操作
>
> 待参考->ASM4使用指南

