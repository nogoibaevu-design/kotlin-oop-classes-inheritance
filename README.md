# Kotlin OOP — Classes & Inheritance Practice

Solutions for the university practice sheet on classes, primary constructors, visibility, inheritance (`open` / `override` / `super`), polymorphism, upcast / downcast / smart cast.

## How to run

```bash
kotlinc Main.kt -include-runtime -d Main.jar
java -jar Main.jar
```

Or open `Main.kt` in IntelliJ / Android Studio and run the `main` function.

## Tasks covered

| Task | Topic |
|------|-------|
| 1 | Sensor — primary constructor, `require`, `toString` |
| 2 | Counter — private state, default args |
| 3 | Vehicle hierarchy (Car / Bike) |
| 4 | Employee / Accountant / Intern + polymorphic list |
| 5 | Parcel shipping costs |
| 6 | Upcast + `printFleet` |
| 7 | Smart cast + safe downcast |
| 8 | Assessment / Quiz / Project scoring |
| 9 | Notification channels (protected visibility) |
| 10 | Ticket hierarchy + checkout + guardian codes |

All edge cases from the sheet are handled with `require`.
