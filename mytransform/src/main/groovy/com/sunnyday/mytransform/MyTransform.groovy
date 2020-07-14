package com.sunnyday.mytransform

import com.android.build.api.transform.Context
import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.gradle.api.Project
/**
 * 简单的目录拷贝实践
 *
 * 注意：
 * 即使我们什么都没有做，
 * 也需要把所有的输入文件拷贝到目标目录下，
 * 否则下一个Task就没有TransformInput了。
 * 在这个实例中，如果我们空实现了transform方法，
 * 最后会导致打包的apk缺少.class文件。
 * */

class MyTransform extends Transform {
    private static final String NAME = MyTransform.class.simpleName
    private Project mProject

    public MyTransform(Project project) {
        this.mProject = project
    }

    @Override
    String getName() {
        return NAME // Transform 对应的task name
    }

    /**
     * 需要处理的数据类型,有两种值：
     * 1、TransformManager.CONTENT_CLASS：java class文件
     * 2、TransformManager.CONTENT_RESOURCES：资源文件
     *
     * */
    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    /**
     * Transform 要操作的内容范围，官方文档介绍有7种枚举类型。
     * */
    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        //SCOPE_FULL_PROJECT = ImmutableSet.of(Scope.PROJECT, Scope.SUB_PROJECTS, Scope.EXTERNAL_LIBRARIES);
        //这个常量值的范围包括三部分：当前项目、子项目以及外部的依赖库。
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    /**
     * 核心回调方法，主要在这里进行遍历获得class字节码文件，然后就可根据自己的意愿进行任何操作了。
     *
     * */
    @Override
    void transform(Context context,
                   Collection<TransformInput> inputs,
                   Collection<TransformInput> referencedInputs,
                   TransformOutputProvider outputProvider,
                   boolean isIncremental) throws IOException, TransformException, InterruptedException {

        printExtraInfo()
        // transform 的input 有两种类型：目录与jar包。这二者要分开遍历。
        inputs.each {
            TransformInput transformInput ->
                // 1、遍历目录
                //遍历目录，获取outPut目录
                transformInput.directoryInputs.each {
                    DirectoryInput directoryInput ->
                        // 生成输出路径
                        def dest = outputProvider.getContentLocation(
                                directoryInput.name,
                                directoryInput.contentTypes,
                                directoryInput.scopes,
                                Format.DIRECTORY)
                        // 将input目录复制到outPut目录
                        FileUtils.copyDirectory(directoryInput.file, dest)
                }
                // 遍历jar包
                transformInput.jarInputs.each {
                    JarInput jarInput ->
                        // 重命名输出文件,因为同目录下copy file 会冲突
                        def jarName = jarInput.name
                         ////DigestUtils  导包注意org.apache.commons，非gradle包下的
                        def md5Name = DigestUtils.md5Hex(jarInput.file.absolutePath)
                        if (jarName.endsWith(".jar")) {
                            jarName = jarName.substring(0, jarName.length() - 4)
                        }
                        //生成输出路径
                        def dest = outputProvider.getContentLocation(
                                jarName + md5Name,
                                jarInput.contentTypes,
                                jarInput.scopes,
                                Format.JAR
                        )
                        FileUtils.copyFile(jarInput.file, dest)//FileUtils  导包注意org.apache.commons，非gradle包下的
                }
        }


    }

    static printExtraInfo() {
        println()
        println("######################################")
        println("#######                        #######")
        println("#######   Transform practise   #######")
        println("#######                        #######")
        println("######################################")

    }
}