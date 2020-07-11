package com.sunnyday.mytransform

import com.android.build.api.transform.Context
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager
import org.gradle.api.Project
import org.gradle.api.artifacts.transform.TransformOutputs

class MyTransform extends Transform {
    private static  final String NAME = MyTransform.class.simpleName
    private Project mProject

    public MyTransform(Project project) {
        this.mProject = project
    }

    @Override
    String getName() {
        return NAME
    }

    /**
     * 需要处理的数据类型,有两种值：
     * 1、TransformManager.CONTENT_CLASS
     * 2、TransformManager.CONTENT_RESOURCES
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
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    /**
     *核心回调方法，主要在这里进行遍历获得class字节码文件，然后就可根据自己的意愿进行任何操作了。
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
            TransformInput transformInput->

        }

    }

   static printExtraInfo(){
        println()
        println("######################################")
        println("#######                        #######")
        println("#######   Transform practise   #######")
        println("#######                        #######")
        println("######################################")

    }
}