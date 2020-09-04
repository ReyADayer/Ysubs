plugins {
    java
    kotlin("jvm").version(Dependencies.Kotlin.version)
    kotlin("kapt").version(Dependencies.Kotlin.version)
}

group = "neo.atlantis"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
    maven(Dependencies.Spigot.repository)
    maven(Dependencies.SonaType.repository)
}

dependencies {
    compile(Dependencies.Spigot.api)
    compileOnly(Dependencies.Spigot.annotations)
    kapt(Dependencies.Spigot.annotations)
    compile(Dependencies.Kotlin.stdlib)
    compile(Dependencies.Kotlin.reflect)
    compile(Dependencies.Kotlin.Coroutines.core)
    compile(Dependencies.Rx.java)
    compile(Dependencies.Koin.core)
    compile(Dependencies.Koin.coreExt)
    compile(Dependencies.Json.core)
    compile(Dependencies.Retrofit.core)
    compile(Dependencies.OkHttp.core)
    compile(Dependencies.Moshi.core)
    compile(Dependencies.Moshi.kotlin)
    compile(Dependencies.Retrofit.moshi)
    testCompile(Dependencies.JUnit.core)
    testCompile(Dependencies.Koin.test)
    testCompile(Dependencies.MockK.core)
}

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(Dependencies.Kotlin.classpath)
        classpath(Dependencies.Koin.classpath)
    }
}

tasks {
    withType<Jar> {
        from(configurations.getByName("compile").map { if (it.isDirectory) it else zipTree(it) })
    }

    withType<Test>().configureEach {
        useJUnitPlatform()
    }
}