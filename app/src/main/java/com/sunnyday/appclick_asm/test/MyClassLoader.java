package com.sunnyday.appclick_asm.test;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

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
            return defineClass(name, bytes, 0, bytes.length);
        }
        return super.findClass(name);
    }


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
}
