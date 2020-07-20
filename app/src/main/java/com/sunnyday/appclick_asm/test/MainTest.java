package com.sunnyday.appclick_asm.test;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.io.InputStream;

/**
 * Create by SunnyDay on 19:52 2020/07/18
 */
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
            classReader.accept(readClassInfo, ClassReader.SKIP_CODE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
