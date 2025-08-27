import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.transformers.PropertiesFileTransformer

repositories {
    maven("https://repo.codemc.io/repository/nms")
    maven("https://repo.codemc.io/repository/maven-public")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {

    // -- spigot api -- (base)
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")

    // -- dream-platform --
    implementation("cc.dreamcode.platform:bukkit:1.13.6")
    //implementation("cc.dreamcode.platform:bukkit-hook:1.13.6")
    implementation("cc.dreamcode.platform:bukkit-config:1.13.6")
    implementation("cc.dreamcode.platform:dream-command:1.13.6")
    implementation("cc.dreamcode.platform:persistence:1.13.6")

    // -- dream-utilties --
    implementation("cc.dreamcode:utilities-adventure:1.5.7")

    // -- dream-notice --
    implementation("cc.dreamcode.notice:bukkit:1.7.1")
    implementation("cc.dreamcode.notice:bukkit-serializer:1.7.1")

    // -- dream-command --
    implementation("cc.dreamcode.command:bukkit:2.2.3")

    // -- dream-menu --
    implementation("cc.dreamcode.menu:bukkit:1.4.3")
    implementation("cc.dreamcode.menu:bukkit-serializer:1.4.3")

    // -- tasker (easy sync/async scheduler) --
    implementation("eu.okaeri:okaeri-tasker-bukkit:3.0.2-beta.8")
}

tasks.withType<ShadowJar> {

    archiveFileName.set("Dream-Tpa-${project.version}.jar")
    mergeServiceFiles()

    relocate("com.cryptomorin", "cc.dreamcode.tpa.libs.com.cryptomorin")
    relocate("eu.okaeri", "cc.dreamcode.tpa.libs.eu.okaeri")
    relocate("net.kyori", "cc.dreamcode.tpa.libs.net.kyori")

    relocate("cc.dreamcode.platform", "cc.dreamcode.tpa.libs.cc.dreamcode.platform")
    relocate("cc.dreamcode.utilities", "cc.dreamcode.tpa.libs.cc.dreamcode.utilities")
    relocate("cc.dreamcode.menu", "cc.dreamcode.tpa.libs.cc.dreamcode.menu")
    relocate("cc.dreamcode.command", "cc.dreamcode.tpa.libs.cc.dreamcode.command")
    relocate("cc.dreamcode.notice", "cc.dreamcode.tpa.libs.cc.dreamcode.notice")

    relocate("org.bson", "cc.dreamcode.tpa.libs.org.bson")
    relocate("com.mongodb", "cc.dreamcode.tpa.libs.com.mongodb")
    relocate("com.zaxxer", "cc.dreamcode.tpa.libs.com.zaxxer")
    relocate("org.slf4j", "cc.dreamcode.tpa.libs.org.slf4j")
    relocate("org.json", "cc.dreamcode.tpa.libs.org.json")
    relocate("com.google.gson", "cc.dreamcode.tpa.libs.com.google.gson")


    transform(PropertiesFileTransformer::class.java) {
        paths.set(listOf("META-INF/native-image/org.mongodb/bson/native-image.properties"))
        mergeStrategy.set(PropertiesFileTransformer.MergeStrategy.Append)
    }
}