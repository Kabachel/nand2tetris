plugins {
    kotlin("jvm") version "2.3.0"
    application
}

group = "me.kabachel.nand2tetris"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("me.kabachel.nand2tetris.MainKt")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(24)
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "me.kabachel.nand2tetris.MainKt"
    }
    archiveFileName.set("HackAssembler.jar")
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}