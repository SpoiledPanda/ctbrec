# Build Templates (JDK 21 + JavaFX 21)

These templates help you wire up the source repository when available. Replace placeholders as noted and confirm modules/launcher from your source code.

## Discover modules and launcher

- Inspect JavaFX modules used (adjust `-cp` to your project):

```cmd
jdeps -q -s -multi-release 21 -cp . ui\CamrecApplication.class
```

- Likely launcher class (from compiled snapshot): `ui.Launcher` (confirm in source).

---

## Maven (pom.xml) example

```xml
<!-- Requires JDK 21 -->
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.example</groupId>
  <artifactId>ctbrec</artifactId>
  <version>0.0.1-SNAPSHOT</version>

  <properties>
    <maven.compiler.release>21</maven.compiler.release>
    <javafx.version>21.0.4</javafx.version>
    <exec.mainClass>ui.Launcher</exec.mainClass> <!-- confirm in source -->
  </properties>

  <dependencies>
    <!-- Include only the JavaFX modules you actually use -->
    <dependency>
      <groupId>org.openjfx</groupId>
      <artifactId>javafx-controls</artifactId>
      <version>${javafx.version}</version>
    </dependency>
    <dependency>
      <groupId>org.openjfx</groupId>
      <artifactId>javafx-graphics</artifactId>
      <version>${javafx.version}</version>
      <classifier>win</classifier>
    </dependency>
    <dependency>
      <groupId>org.openjfx</groupId>
      <artifactId>javafx-media</artifactId>
      <version>${javafx.version}</version>
    </dependency>
    <!-- Add: javafx-web, javafx-fxml, etc., if used -->
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.13.0</version>
        <configuration>
          <release>${maven.compiler.release}</release>
        </configuration>
      </plugin>

      <plugin>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-maven-plugin</artifactId>
        <version>0.0.8</version>
        <configuration>
          <mainClass>${exec.mainClass}</mainClass>
          <launcher>ctbrec</launcher>
          <stripNativeCommandFiles>true</stripNativeCommandFiles>
          <jlinkZip>false</jlinkZip>
          <components>javafx-controls,javafx-graphics,javafx-media</components>
          <jvmArgs>
            <!-- Add as needed, e.g. to open packages -->
            <!-- <arg>--add-opens=javafx.graphics/com.sun.javafx.css=ALL-UNNAMED</arg> -->
          </jvmArgs>
        </configuration>
      </plugin>
    </plugins>
  </build>
</project>
```

Run:

```cmd
mvn -q clean javafx:run
```

---

## Gradle (build.gradle.kts) example

```kotlin
plugins {
  application
  id("org.openjfx.javafxplugin") version "0.1.0"
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(21))
  }
}

application {
  // Confirm from source; likely ui.Launcher
  mainClass.set("ui.Launcher")
}

javafx {
  version = "21.0.4"
  modules = listOf("javafx.controls", "javafx.graphics", "javafx.media")
}

// Optional runtime args
// tasks.run { jvmArgs = listOf("--add-opens=javafx.graphics/com.sun.javafx.css=ALL-UNNAMED") }
```

Run:

```cmd
gradlew run
```

