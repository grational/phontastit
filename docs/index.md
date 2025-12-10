# PhontastIT

![PhontastIT Logo](logo.svg)

A simple, lightweight Java library to parse and identify Italian phone numbers.

Read the [Groovydoc API documentation](groovydoc/index.html).

## Features

* **Parse** texts to find Italian phone numbers.
* **Identify** the type of number (Landline, Mobile, Toll-free, Premium, etc.).
* **Zero dependencies** (standard Java library).

## Usage

### Parsing

The `Parser` class provides a static utility to extract numbers from any text.

```java
import it.grational.phontastit.Parser;
import it.grational.phontastit.Phone;
import it.grational.phontastit.Type;
import java.util.List;

String text = "Call me at 348 1234567 or at the office 02 9876543";

// Extract numbers
List<Phone> numbers = Parser.parse(text);

for (Phone p : numbers) {
    System.out.println("Number: " + p.toString());
    
    if (p.type() == Type.MOBILE) {
        System.out.println("Type: Mobile");
    } else if (p.type() == Type.LANDLINE) {
        System.out.println("Type: Landline");
    }
}
```

### Phone Object

The `Phone` object represents a parsed Italian phone number.

```java
Phone myPhone = new Phone("3351234567");

// Get standardized format (+39 prefix)
System.out.println(myPhone.toString()); // Output: +393351234567

// Get local format (no prefix)
System.out.println(myPhone.toString(true)); // Output: 3351234567

// Check type
if (myPhone.type() == Type.TOLLFREE) {
    // ...
}
```

### Supported Types

The `Type` enum supports:

* `LANDLINE` (e.g., 02..., 06...)
* `MOBILE` (e.g., 348..., 335...)
* `TOLLFREE` (800..., 803...)
* `PREMIUM` (892..., 199...)
* `FAX` (Requires manual flag)