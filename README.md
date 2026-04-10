<p align="center">
  <img src="docs/logo.png" alt="PhontastIT Logo" width="500">
</p>

<h2 align="center">
  The robust, zero-dependency Italian phone number parser for Java.
</h2>

<p align="center">
  <a href="https://jitpack.io/#grational/phontastit">
    <img src="https://jitpack.io/v/grational/phontastit.svg" alt="JitPack Release">
  </a>
  <img src="https://img.shields.io/badge/Java-21+-orange" alt="Java 21+">
  <img src="https://img.shields.io/badge/License-Apache%202.0-blue.svg" alt="License">
</p>

---

**PhontastIT** is a lightweight, specialized library designed to solve one specific problem perfectly: **parsing and identifying Italian phone numbers**. 

Stop writing complex, fragile Regular Expressions. PhontastIT handles the nuance of Italian numbering plans (landlines, mobiles, toll-free, shared-cost, and premium services) with a simple, fluent API.

## ✨ Features

- 🇮🇹 **Specialized:** Tuned specifically for the Italian numbering plan (+39).
- 🔍 **Smart Parsing:** Extracts multiple numbers from messy text blocks.
- 🏷️ **Type Identification:** Distinguishes between Mobile, Landline, Toll-Free, Shared-Cost, and Premium numbers.
- 🚀 **Zero Dependencies:** Pure Java. No bloat.
- ⚡ **High Performance:** Stateless, static utility design.

## 📦 Installation

This library is distributed via **JitPack**.

### Gradle (Groovy)

Step 1. Add the JitPack repository to your build file:
```groovy
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}
```

Step 2. Add the dependency:
```groovy
dependencies {
    implementation 'com.github.grational:phontastit:master-SNAPSHOT'
}
```

### Maven

Step 1. Add the repository:
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Step 2. Add the dependency:
```xml
<dependency>
    <groupId>com.github.grational</groupId>
    <artifactId>phontastit</artifactId>
    <version>master-SNAPSHOT</version>
</dependency>
```

> **Note:** Replace `master-SNAPSHOT` with a specific [Release Tag](https://github.com/grational/phontastit/releases) for production stability.

## 🚀 Quick Start

### 1. Parsing Text
Extract all valid numbers from a string, regardless of formatting or surrounding text.

```java
import it.grational.phontastit.Parser;
import it.grational.phontastit.Phone;
import java.util.List;

String rawText = """
    Hi! You can reach our sales team at 348 1234567.
    For technical support, call the office: 02-9876543
    or our toll-free number 800 555 123.
""";

// ⚡ Magic happens here
List<Phone> numbers = Parser.parse(rawText);

System.out.println("Found " + numbers.size() + " numbers!"); 
// Output: Found 3 numbers!
```

### 2. Identifying Types
Once parsed, check what kind of number you're dealing with using the `Type` enum.

```java
import it.grational.phontastit.Type;

for (Phone p : numbers) {
    System.out.print(p.toString() + " is a ");
    
    switch (p.type()) {
        case MOBILE   -> System.out.println("📱 Mobile Number");
        case LANDLINE -> System.out.println("☎️ Landline");
        case TOLLFREE -> System.out.println("🆓 Toll-Free Number");
        case SHARED_COST -> System.out.println("🤝 Shared-Cost Service");
        case PREMIUM  -> System.out.println("💰 Premium Service");
        default       -> System.out.println("❓ Unknown");
    }
}
```

### 3. Formatting
The `Phone` object provides standard formatting options.

```java
Phone myPhone = new Phone("3351234567");

// Standard International Format (+39)
System.out.println(myPhone.toString());      
// Output: +393351234567

// Local Format (Clean, no prefix)
System.out.println(myPhone.toString(true));  
// Output: 3351234567
```

## 🛠️ Supported Types

| Enum Type | Description | Example |
| :--- | :--- | :--- |
| `Type.LANDLINE` | Traditional geographic numbers (02, 06, 011...) | `02 123456` |
| `Type.MOBILE` | Mobile networks (TIM, Vodafone, WindTre, Iliad...) | `348 1234567` |
| `Type.TOLLFREE` | Free-to-call numbers (Verde) | `800 123 456` |
| `Type.SHARED_COST` | Shared-cost services | `840 123 456`, `841 123` |
| `Type.PREMIUM` | Added-rate services, including the library's collapsed unique/personal ranges | `892 424` |
| `Type.FAX` | Fax numbers (requires manual identification) | - |

`Type.PREMIUM` intentionally collapses officially distinct "unique" and "personal" service families such as `199...` and `178...` into a single API category.

## 🤝 Contributing

Contributions are welcome! If you find a valid Italian number that isn't parsed correctly:

1.  Open an Issue with the number and expected behavior.
2.  Or, submit a Pull Request adding the number to `PhoneUSpec.groovy` and implementing the fix.

## 📄 License

This project is licensed under the Apache License 2.0 - see the LICENSE file for details.
