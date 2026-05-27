# Yantra

**The Phone is the Lab.**

250 million Indian students lack access to functional science labs. They are forced to fake their CBSE and State Board practical files because physical infrastructure does not exist outside the metro bubble. 

Yantra fixes this structural failure. It is an Android-first, offline-native application that converts standard smartphone sensors into high-precision scientific instruments, mapped directly to the high school science curriculum. It takes raw hardware telemetry, runs deterministic physics algorithms, and outputs real experimental data formatted into CBSE-compliant PDF lab reports.

No gamification. No social feeds. No cloud compute. Just pure first-principles engineering.

---

## 🚀 Status: MVP Complete

The Phase 1 MVP is fully operational. Yantra currently supports **12 complete physics experiments** mapping to the Class 11 curriculum, covering mechanics, thermodynamics, waves, and electromagnetism:

1. **Simple Pendulum ($g$ Calculation):** Gyroscope angular velocity tracking & peak detection.
2. **Free Fall ($g$ Calculation):** Accelerometer zero-G state detection & impact logging.
3. **Kinetic Friction:** Accelerometer deceleration tracking across surfaces.
4. **Static Friction (Inclined Plane):** Real-time angle calculation via parallel acceleration.
5. **Spring-Mass SHM:** Vertical oscillation tracking and spring constant ($k$) calculation.
6. **Newton's Law of Cooling:** Temperature logging and exponential decay curve fitting.
7. **Speed of Sound:** Microphone peak-to-echo time differential calculation.
8. **Resonance Frequency:** Audio capture and Fast Fourier Transform (FFT) analysis.
9. **Heart Rate Monitor:** Optical photoplethysmography (PPG) via Light Sensor/Camera.
10. **Magnetic Field Mapping:** 3D spacial mapping using the native Magnetometer.
11. **Reaction Time:** High-precision UI event logging (visual & auditory).
12. **Projectile Motion:** Trajectory kinematics analysis.

---

## ⚙️ Tech Stack

Yantra is built for extreme efficiency on low-end hardware (e.g., 2GB RAM, Snapdragon 450). It bypasses browser security limitations by executing directly on the Android OS to guarantee 60Hz+ sensor polling without UI thread jank.

* **Language:** Kotlin
* **UI Framework:** Jetpack Compose (Material Design 3, Dark Theme)
* **Architecture:** MVVM + Clean Architecture
* **Concurrency:** Kotlin Coroutines & `StateFlow`
* **Hardware APIs:** `android.hardware.SensorManager`
* **Database:** Room (Local-only persistence)
* **Dependency Injection:** Dagger Hilt
* **PDF Generation:** `iText7` (`com.itextpdf:itext7-core`)

---

## 🏗️ System Architecture

Yantra is built across five decoupled layers:

1.  **The Curriculum Engine:** A static data engine mapping every experiment to a specific class, board, and chapter.
2.  **The Sensor Interface:** The hardware hook. Handles sensor registration, lifecycles, and low-pass/high-pass filtering via dedicated coroutine dispatchers.
3.  **The Experiment Engine:** Wraps specific sensor configurations into actionable experiments with strict setup flows (`Briefing` -> `Device Check` -> `Setup` -> `Live`).
4.  **The Analysis Engine:** Takes the raw telemetry rolling buffer, runs peak-detection and math filters, calculates percentage errors, and verifies against standard constants.
5.  **The Report Engine:** Assembles the analyzed data into a fully formatted, A4 PDF lab report complete with observation tables and graphs, ready for CBSE submission.

---

## 🛠️ Build & Installation

Yantra is strictly offline-first. It requires **zero** backend infrastructure to compile or run.

1.  Clone the repository:
    ```bash
    git clone [https://github.com/YOUR_USERNAME/yantra.git](https://github.com/YOUR_USERNAME/yantra.git)
    ```
2.  Open the project in **Android Studio** (Flamingo or newer).
3.  Sync Gradle dependencies.
4.  Connect an Android device (Android 8.0 Oreo / API 26 minimum) via WebUSB or ADB. 
    *Note: The Android Emulator cannot accurately simulate physical gravity, centripetal forces, or magnetic fields. A physical device is mandatory for testing.*
5.  Build and Run (`Shift + F10`).

---

## ⚖️ Legal & Liability

**CRITICAL WARNING:** Yantra instructs users to perform physical experiments with their devices (e.g., tying phones to strings, dropping them). 

By contributing to or using Yantra, you acknowledge that all physical experiments carry inherent risk. **The developers of Yantra assume zero liability for smashed screens, broken hardware, or academic grading outcomes.** Read the full legal framework in the `LEGAL.md` file before deploying or testing this application.

---

*Built for the trenches. Engineered in Barwani.*
