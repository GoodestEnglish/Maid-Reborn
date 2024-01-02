plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "rip.diamond"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("redis.clients:jedis:5.0.0")

    //lombok
    compileOnly("org.projectlombok:lombok:1.18.26")
    annotationProcessor("org.projectlombok:lombok:1.18.26")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"

}