import com.android.build.gradle.internal.tasks.DexArchiveBuilderTask

buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.0.1")
        classpath(kotlin("gradle-plugin", version = "1.4.0"))
        classpath("com.github.ben-manes:gradle-versions-plugin:0.29.0")

        classpath("com.google.gms:google-services:4.3.3")
        classpath("com.google.firebase:perf-plugin:1.3.1")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.2.0")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
    tasks.withType<DexArchiveBuilderTask> {
        doFirst {
            mixedScopeClasses
                .asFileTree
                .matching { include("**/META-INF/versions/9/**/*.class") }
                .forEach { it.delete() }
        }
    }
}

tasks.create("clean", Delete::class) {
    delete(rootProject.buildDir)
}
