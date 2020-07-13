package com.sunnyday.mytransform

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class MyPlugin implements Plugin<Project>{


    @Override
    void apply(Project project) {
        // 注册自定义Transform
       AppExtension appExtension = project.extensions.findByType(AppExtension.class)
        appExtension.registerTransform(new MyTransform(project))
    }
}