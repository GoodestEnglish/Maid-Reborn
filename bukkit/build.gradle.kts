plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "rip.diamond"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.dmulloy2.net/repository/public/")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("com.github.seeseemelk:MockBukkit-v1.20:3.78.0")
    testImplementation("com.github.fppt:jedis-mock:1.1.0")
    testImplementation("org.mockito:mockito-core:5.11.0")
    testImplementation("org.projectlombok:lombok:1.18.26")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.26")

    implementation(project(":shared"))
    implementation("org.mongodb:mongodb-driver-sync:4.11.1")
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    compileOnly("io.netty:netty-all:4.1.24.Final")
    compileOnly("com.comphenix.protocol:ProtocolLib:5.1.0")
    compileOnly("org.projectlombok:lombok:1.18.26")
    annotationProcessor("org.projectlombok:lombok:1.18.26")
}

tasks {
    test {
        useJUnitPlatform()
    }
    shadowJar {
        // Set the desired output file name
        archiveFileName.set("Maid-Reborn.jar")
    }
    javadoc {
        options.encoding = "UTF-8"
    }
    processResources {
        val props = mapOf("version" to version)
        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
