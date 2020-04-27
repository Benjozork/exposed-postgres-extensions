# exposed-postgres-extensions

`exposed-postgrrs-extensions` is a library that adds PostgreSQL-specific functionality to [JetBrains' Exposed](https://github.com/JetBrains/Exposed).

## Functionality

- Generated columns (`generated always as`) ([see: PostgreSQL Docs](https://www.postgresql.org/docs/current/ddl-generated-columns.html))
- Additional functions (currently `String` : `bit_length`, `char_length` and `octet_length`)

## Usage

Simply include the library with JitPack (Gradle Kotlin DSL) :

```kotlin
repositories {
    maven("https://jitpack.io")
}

/* ... */

dependencies {
    implementation("com.github.Benjozork:exposed-postgres-extensions:master-SNAPSHOT")
}

```

That's it ! No additional setup required.
