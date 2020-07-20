package com.sunnyday.appclick_asm.test;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

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
