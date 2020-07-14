### Gradle Transform

> gradle 的Transform 要结合gradle插件使用，简单的说可以分为两步：
>
> 1、自定义gradle 插件
>
> 2、自定义Transform类，然后在自定义的gradle插件中注册下Transform

### Transform知识图谱

![](https://github.com/sunnnydaydev/AppClick_ASM/blob/master/GradleTransformContent.png)

### Transform

###### 1、类(主要api)

```java

public abstract class Transform {
    public Transform() {
    }
    
    public abstract String getName();

    public abstract Set<ContentType> getInputTypes();

    public Set<ContentType> getOutputTypes() {
        return this.getInputTypes();
    }

    public abstract Set<? super Scope> getScopes();

    public abstract boolean isIncremental();

    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        this.transform(transformInvocation.getContext(), transformInvocation.getInputs(), transformInvocation.getReferencedInputs(), transformInvocation.getOutputProvider(), transformInvocation.isIncremental());
    }
}

```

（1）getName

- Transform 对应的task name
- 他会现在app/build/intermediates/transforms目录下
- 这个Task名字会被系统管理。默认情况下在编译器右上角Gradle窗口的app->Tasks->others下生成特定的Task。Task的名字为：transformClassesWith+getName返回值+ForXxx

（2）getInputTypes，它是指定Transform要处理的数据类型。目前主要支持两种数据类型：

- TransformManager.CONTENT_CLASS：表示要处理编译后的字节码，可能是jar包也可能是目录。
- TransformManager.CONTENT_RESOURCES：表示处理标准的java资源。

（3）getScopes，Transform 要操作的内容范围，官方文档介绍有7种枚举类型：

- PROJECT：只处理当前项目。
- SUB_PROJECTS：只处理子项目。
- PROJECT_LOCAL_DEPS：只处理当前项目的本地依赖，例如jar、aar。
- SUB_PROJECTS_LOCAL_DEPS：只处理子项目的本地依赖，例如jar、aar。
- EXTERNAL_LIBRARIES：只处理外部的依赖库。
- PROVIDED_ONLY：只处理本地或远程以provided形式引入的依赖库。
- TESTED_CODE：测试代码。

ps：一般我们使用TransformManager.SCOPE_FULL_PROJECT这个常量值。这个常量值的范围包括三部分：当前项目、子项目以及外部的依赖库。

（4）isIncremental是否是增量构建。

> 这个方法的返回值就是增量编译的开关。当开启增量构建时则Transform的input有如下几种状态：
>
> - NOTCHANGED: 当前文件不需处理，甚至复制操作都不用；
> - ADDED、CHANGED: 正常处理，输出给下一个任务；
> - REMOVED: 移除outputProvider获取路径对应的文件。

### 简单实践

> 自定义插件的步骤就不在重复了，如果不熟悉可以参考[自定义Gradle Plugin](https://blog.csdn.net/qq_38350635/article/details/106986739)只是在自定义插件时要添加android gradle tools 依赖即可。这样就可使用gradle Transform api 了。

###### 1、自定义Transform 简单实践

```java

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
                        // todo 操作字节码，实现自己的逻辑（使用ASM）
                      
                        // 将input目录复制到outPut目录
                        FileUtils.copyDirectory(directoryInput.file, dest)
                }
                // 遍历jar包
                transformInput.jarInputs.each {
                    JarInput jarInput ->
                        // 重命名输出文件,因为同目录下copy file 会冲突
                        def jarName = jarInput.name
                        def md5Name = DigestUtils.md5Hex(jarInput.file.absolutePath)
                        if (jarName.endsWith(".jar")) {
                            jarName = jarName.substring(0, jarName.length() - 4)
                        }
                        //生成输出路径
                        def dest = outputProvider.getContentLocation(
                                jarName + md5Name,
                                JarInput.ContentType,
                                JarInput.Scope,
                                Format.JAR
                        )
                          // todo 操作字节码，实现自己的逻辑（使用ASM）
                        FileUtils.copyDirectory(jarInput.file, dest)
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
```

###### 2、注册Transform

> 自定义的插件中注册下即可

```java

class MyPlugin implements Plugin<Project>{

    @Override
    void apply(Project project) {
        // 注册自定义Transform
       AppExtension appExtension = project.extensions.findByType(AppExtension.class)
        appExtension.registerTransform(new MyTransform(project))
    }
}
```



###### 3、结果

> 插件引入我们的app 工程中，然后简单的运行下项目即可看见我们自定义的Transform 起作用了 log 如下：

```java
Executing tasks: [:app:assembleDebug] in project F:\ASworkpalce\AppClick_ASM
。。。略
> Task :app:compileDebugSources
> Task :app:mergeDebugAssets

> Task :app:transformClassesWithMyTransformForDebug

######################################
#######                        #######
#######   Transform practise   #######
#######                        #######
######################################

> Task :app:transformClassesWithDexBuilderForDebug
。。。略


```

#### 注意

> 即使我们什么都没有做，也需要把所有的输入文件拷贝到目标目录下，否则下一个Task就没有TransformInput了。如果我们空实现了transform方法，最后会导致打包的apk缺少.class文件。