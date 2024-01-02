plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "rip.diamond"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")

    implementation(project(":shared"))
    implementation("org.mongodb:mongodb-driver-sync:4.9.0")

    //lombok
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
