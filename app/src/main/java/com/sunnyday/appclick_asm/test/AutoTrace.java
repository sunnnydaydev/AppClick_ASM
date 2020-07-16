package com.sunnyday.appclick_asm.test;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

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
        super(Opcodes.ACC_PUBLIC);
    }
    /**
     * 扫描类时第一个拜访的方法
     * @param version 代表jdk的版本。比如52代表jdk1.8。
     * @param access  类的修饰符。修饰符在 ASM 中是以“ACC_”开头的常量。可以作用到类级别上的修饰符有:ACC_PUBLIC(代表public)
     * @param name    类名。通常我们会使用完成的包名+类名表示类。例如com.example.MyClass。但是在字节码中是以路径的形式表示的。
     *                例如com/example/MyClass。值得注意的是，虽然是路径表示法但是不需要写明类的“.class”扩展名。
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
