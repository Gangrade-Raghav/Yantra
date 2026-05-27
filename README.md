# Yantra

**The Phone is the Lab.**

250 million Indian students lack access to functional science labs. They are forced to fake their CBSE and State Board practical files because physical infrastructure does not exist outside the metro bubble. 

Yantra fixes this structural failure. It is an Android-first, offline-native application that converts standard smartphone sensors into high-precision scientific instruments, mapped directly to the high school science curriculum. It takes raw hardware telemetry, runs deterministic physics algorithms, and outputs real experimental data formatted into CBSE-compliant PDF lab reports.

No gamification. No social feeds. No cloud compute. Just pure first-principles engineering.

---

## ⚙️ Tech Stack

Yantra is built for extreme efficiency on low-end hardware (e.g., 2GB RAM, Snapdragon 450). It bypasses browser limitations by executing directly on the Android OS.

* **Language:** Kotlin
* **UI Framework:** Jetpack Compose (Material Design 3, Dark Theme)
* **Concurrency:** Kotlin Coroutines & StateFlow (for managing 60Hz+ sensor data without UI thread jank)
* **Hardware APIs:** `android.hardware.SensorManager` (Accelerometer, Gyroscope, Magnetometer), `CameraX`
* **Database:** Room (Local only)
* **Dependency Injection:** Hilt
* **PDF Generation:** `iText7` (com.itextpdf:itext7-core)

---

## 🏗️ System Architecture

Yantra is built across five decoupled layers:

1.  **The Curriculum Engine:** A structured Room database mapping every experiment to a specific class, board, and chapter.
2.  **The Sensor Interface:** The hardware hook. Handles sensor registration, data collection, and low-pass/high-pass filtering via a dedicated `HandlerThread`.
3.  **The Experiment Engine:** Wraps specific sensor configurations into actionable experiments with strict setups and target calculations.
4.  **The Analysis Engine:** Takes the raw telemetry rolling buffer, runs peak-detection and math filters, calculates percentage errors, and verifies against standard constants (e.g., $g = 9.8 m/s^2$).
5.  **The Report Engine:** Assembles the analyzed data into a fully formatted, A4 PDF lab report complete with observation tables and graphs, ready for CBSE submission.

---

## 🚀 The MVP Pipeline

Phase 1 focuses on the **Simple Harmonic Motion (Pendulum) Lab** to prove the complete vertical slice of the architecture:

* **Action:** Phone acts as the pendulum bob.
* **Telemetry:** Gyroscope Z-axis captures angular velocity.
* **Processing:** Custom peak-detection algorithm isolates half-swings ($T/2$) and calculates the exact Time Period ($T$).
* **Physics:** Computes gravitational acceleration using $g = \frac{4\pi^2 L}{T^2}$.

---

## 🛠️ Build & Installation

Yantra is strictly offline-first. It requires **zero** backend infrastructure to compile or run.

1.  Clone the repository:
    ```bash
    git clone [https://github.com/YOUR_USERNAME/yantra.git](https://github.com/YOUR_USERNAME/yantra.git)
    ```
2.  Open the project in **Android Studio** (Flamingo or newer recommended).
3.  Sync Gradle dependencies.
4.  Connect an Android device (Android 8.0 Oreo / API 26 minimum) via WebUSB or ADB. *Note: The Android Emulator cannot accurately simulate physical gravity/centripetal forces for these experiments.*
5.  Build and Run (`Shift + F10`).

---

## ⚖️ Legal & Liability

**CRITICAL WARNING:** Yantra instructs users to perform physical experiments with their devices (e.g., tying phones to strings, dropping them onto cushions). 

By contributing to or using Yantra, you acknowledge that all physical experiments carry inherent risk. **The developers of Yantra assume zero liability for smashed screens, broken hardware, or academic grading outcomes.** Read the full legal framework in `LEGAL.md` before deploying or testing this application.

---

*Built for the trenches. Engineered in Barwani.*
