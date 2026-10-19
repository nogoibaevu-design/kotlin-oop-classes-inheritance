/**
 * Kotlin OOP practice — Classes, Inheritance, Polymorphism
 * All 10 tasks in one file. Run main() to see demos.
 */

// ─────────────────────────────────────────────
// Task 1 — Sensor
// ─────────────────────────────────────────────

class Sensor(val id: String, var reading: Double) {
    init {
        require(id.isNotBlank()) { "id must not be blank" }
    }

    fun update(newReading: Double) {
        require(newReading.isFinite()) { "reading must be finite" }
        reading = newReading
    }

    override fun toString(): String = "Sensor(id=$id, reading=$reading)"
}

// ─────────────────────────────────────────────
// Task 2 — Encapsulated Counter
// ─────────────────────────────────────────────

class Counter(start: Int = 0) {
    private var value: Int = start

    fun value(): Int = value

    fun inc(step: Int = 1) {
        require(step > 0) { "step must be positive" }
        value += step
    }

    fun reset() {
        value = 0
    }
}

// ─────────────────────────────────────────────
// Task 3 + 6 + 7 — Vehicle hierarchy
// ─────────────────────────────────────────────

open class Vehicle(val brand: String) {
    init {
        require(brand.isNotBlank()) { "brand must not be blank" }
    }

    open fun info(): String = "Vehicle $brand"
    open fun maxSpeedKmh(): Int = 0
}

class Car(brand: String, val doors: Int) : Vehicle(brand) {
    init {
        require(doors >= 2) { "doors must be >= 2" }
    }

    override fun info(): String = "Car $brand, doors=$doors"
    override fun maxSpeedKmh(): Int = 180
}

class Bike(brand: String, val gears: Int) : Vehicle(brand) {
    init {
        require(gears >= 1) { "gears must be >= 1" }
    }

    override fun info(): String = "Bike $brand, gears=$gears"
    override fun maxSpeedKmh(): Int = 45
}

fun printFleet(fleet: List<Vehicle>) {
    for (v in fleet) {
        println(v.info())
    }
}

fun doorsOrZero(v: Vehicle): Int {
    return if (v is Car) v.doors else 0
}

// ─────────────────────────────────────────────
// Task 4 — Payroll (Employee family)
// ─────────────────────────────────────────────

open class Employee(val name: String, val baseSalary: Int) {
    init {
        require(name.isNotBlank()) { "name must not be blank" }
        require(baseSalary >= 0) { "baseSalary must be non-negative" }
    }

    open fun pay(): Int = baseSalary
    open fun describe(): String = "$name: ${pay()}"
}

class Accountant(
    name: String,
    baseSalary: Int,
    val bonusPercent: Int
) : Employee(name, baseSalary) {
    init {
        require(bonusPercent in 0..100) { "bonusPercent must be 0..100" }
    }

    override fun pay(): Int = super.pay() + super.pay() * bonusPercent / 100

    override fun describe(): String = "Accountant ${super.describe()}"
}

class Intern(
    name: String,
    baseSalary: Int,
    val mentor: String
) : Employee(name, baseSalary) {
    init {
        require(mentor.isNotBlank()) { "mentor must not be blank" }
    }

    override fun pay(): Int = super.pay() / 2
}

// ─────────────────────────────────────────────
// Task 5 — Polymorphic shipping
// ─────────────────────────────────────────────

open class Parcel(val id: String, val weightKg: Double) {
    init {
        require(id.isNotBlank()) { "id must not be blank" }
        require(weightKg > 0) { "weight must be positive" }
    }

    open fun shippingCost(): Double = weightKg * 2.0
}

class ExpressParcel(id: String, weightKg: Double) : Parcel(id, weightKg) {
    override fun shippingCost(): Double = super.shippingCost() + 15.0
}

class EconomyParcel(id: String, weightKg: Double) : Parcel(id, weightKg) {
    override fun shippingCost(): Double = super.shippingCost() * 0.8
}

fun totalShipping(parcels: List<Parcel>): Double =
    parcels.sumOf { it.shippingCost() }

// ─────────────────────────────────────────────
// Task 8 — Assessment hierarchy
// ─────────────────────────────────────────────

open class Assessment(val title: String, val maxPoints: Int) {
    init {
        require(title.isNotBlank()) { "title must not be blank" }
        require(maxPoints > 0) { "maxPoints must be positive" }
    }

    open fun scoreTowardGrade(raw: Int): Double {
        val clamped = raw.coerceIn(0, maxPoints)
        return clamped.toDouble() / maxPoints
    }
}

class Quiz(
    title: String,
    maxPoints: Int,
    val penaltyIfLate: Double
) : Assessment(title, maxPoints) {
    init {
        require(penaltyIfLate in 0.0..0.5) { "penaltyIfLate must be 0.0..0.5" }
    }

    override fun scoreTowardGrade(raw: Int): Double {
        val base = super.scoreTowardGrade(raw)
        return base * (1.0 - penaltyIfLate)
    }
}

class Project(
    title: String,
    maxPoints: Int,
    val bonusCap: Double
) : Assessment(title, maxPoints) {
    init {
        require(bonusCap >= 0.0) { "bonusCap must be non-negative" }
    }

    // If raw exceeds maxPoints we allow up to maxPoints * (1 + bonusCap),
    // then normalise. We still call super on the portion that is within maxPoints.
    override fun scoreTowardGrade(raw: Int): Double {
        if (raw <= maxPoints) {
            return super.scoreTowardGrade(raw)
        }
        val capped = raw.coerceAtMost((maxPoints * (1.0 + bonusCap)).toInt())
        return capped.toDouble() / maxPoints
    }
}

// ─────────────────────────────────────────────
// Task 9 — Notification channels
// ─────────────────────────────────────────────

open class Notification(private val payload: String) {
    protected fun body(): String = payload
    open fun render(): String = body()
}

class EmailNotification(payload: String, val to: String) : Notification(payload) {
    override fun render(): String = "Email to $to: ${body()}"
}

class SmsNotification(payload: String, val phone: String) : Notification(payload) {
    override fun render(): String = "SMS to $phone: ${body()}"
}

fun sendAll(items: List<Notification>) {
    for (n in items) {
        println(n.render())
    }
}

// ─────────────────────────────────────────────
// Task 10 — Zoo ticketing
// ─────────────────────────────────────────────

open class Ticket(val code: String, val basePrice: Int) {
    init {
        require(code.isNotBlank()) { "code must not be blank" }
        require(basePrice >= 0) { "basePrice must be non-negative" }
    }

    open fun price(): Int = basePrice
}

class AdultTicket(code: String, basePrice: Int) : Ticket(code, basePrice)

class ChildTicket(
    code: String,
    basePrice: Int,
    val guardianCode: String
) : Ticket(code, basePrice) {
    init {
        require(guardianCode.isNotBlank()) { "guardianCode must not be blank" }
    }

    override fun price(): Int = super.price() / 2
}

class VipTicket(
    code: String,
    basePrice: Int,
    val loungeAccess: Boolean
) : Ticket(code, basePrice) {
    override fun price(): Int = super.price() + if (loungeAccess) 50 else 20
}

fun checkout(tickets: List<Ticket>): Int =
    tickets.sumOf { it.price() }

fun childGuardianCodes(tickets: List<Ticket>): List<String> {
    val result = mutableListOf<String>()
    for (t in tickets) {
        if (t is ChildTicket) {
            result.add(t.guardianCode)
        }
    }
    return result
}

// ─────────────────────────────────────────────
// main — demos for every task
// ─────────────────────────────────────────────

fun main() {
    println("=== Task 1: Sensor ===")
    val s1 = Sensor("TEMP-01", 22.5)
    val s2 = Sensor("HUM-02", 48.0)
    s1.update(23.1)
    println(s1)
    println(s2)
    // Edge: Sensor("", 1.0) would throw

    println("\n=== Task 2: Counter ===")
    val c = Counter()
    c.inc(2)
    println("value after inc(2) = ${c.value()}")
    c.reset()
    println("after reset = ${c.value()}")

    println("\n=== Task 3: Vehicle hierarchy ===")
    val car = Car("Volvo", 4)
    val bike = Bike("Trek", 18)
    println("${car.info()}  max=${car.maxSpeedKmh()} km/h")
    println("${bike.info()}  max=${bike.maxSpeedKmh()} km/h")

    println("\n=== Task 4: Payroll ===")
    val staff: List<Employee> = listOf(
        Employee("Alice", 40000),
        Accountant("Bob", 50000, 10),
        Intern("Carol", 30000, "Alice")
    )
    var totalPay = 0
    for (e in staff) {
        println(e.describe())
        totalPay += e.pay()
    }
    println("sum of pay = $totalPay")

    println("\n=== Task 5: Shipping ===")
    val parcels: List<Parcel> = listOf(
        Parcel("P1", 2.0),
        ExpressParcel("E1", 1.5),
        EconomyParcel("C1", 3.0)
    )
    println("total shipping = %.2f".format(totalShipping(parcels)))

    println("\n=== Task 6: Upcast catalogue ===")
    val v: Vehicle = Car("Volvo", 4)
    println(v.info()) // Car override runs
    printFleet(listOf(car, bike))

    println("\n=== Task 7: Smart cast / safe downcast ===")
    println("doorsOrZero(car) = ${doorsOrZero(car)}")
    println("doorsOrZero(bike) = ${doorsOrZero(bike)}")
    val maybeCar = Bike("X", 3) as? Car
    println("as? Car on Bike → $maybeCar")
    // val crash = Bike("X", 3) as Car  // would throw — left commented

    println("\n=== Task 8: Assessment ===")
    val assessments: List<Assessment> = listOf(
        Quiz("Week 3 Quiz", 20, 0.1),
        Project("Final Project", 100, 0.2)
    )
    val raw = 18
    for (a in assessments) {
        println("${a.title}: scoreTowardGrade($raw) = ${a.scoreTowardGrade(raw)}")
    }

    println("\n=== Task 9: Notifications ===")
    val notes: List<Notification> = listOf(
        EmailNotification("Meeting at 10", "ada@uni.edu"),
        SmsNotification("Reminder", "+996700000001")
    )
    sendAll(notes)

    println("\n=== Task 10: Zoo tickets ===")
    val tickets: List<Ticket> = listOf(
        AdultTicket("A-01", 100),
        ChildTicket("C-01", 100, "G-1"),
        VipTicket("V-01", 100, true),
        ChildTicket("C-02", 80, "G-9")
    )
    println("total=${checkout(tickets)}")
    println("guardians=${childGuardianCodes(tickets).joinToString()}")
}
