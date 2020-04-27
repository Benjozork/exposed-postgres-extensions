# exposed-postgres-extensions

`exposed-postgres-extensions` is a library that adds PostgreSQL-specific functionality to [JetBrains' Exposed](https://github.com/JetBrains/Exposed).

## Functionality

- Generated columns (`generated always as`) ([see: PostgreSQL Docs](https://www.postgresql.org/docs/current/ddl-generated-columns.html))
- Very basic support for `JSONB`
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

## Example

Create a table with a generated column :

(note: `PgTable` is currently needed to use the `.generated {}` extension, because of limitation with Kotlin extension functions)

```kotlin
object : PgTable() {
    val id = integer("id")
    val name = text("name")
    val uppercaseName = bool("name_is_short")
            .generated { name.upperCase().charLength() less 10 }

    override val primaryKey = PrimaryKey(id)
}
```

This will result in the following DDL (formatted for legibility reasons) :

```sql
create table if not exists test (
    id int primary key,
    "name" text not null,
    name_is_short boolean
        generated always as (
            char_length(upper(test."name")) < 10
        ) stored not null
);
```
