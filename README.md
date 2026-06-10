<div align="center">

<img src="https://readme-typing-svg.herokuapp.com?font=Fira+Code&weight=700&size=40&pause=1000&color=00FF99&center=true&vCenter=true&width=600&lines=Yantra;The+Pocket+Laboratory;Zero+Abstraction;Deployable+Telemetry" alt="Yantra Animated Header" />

<br/>
<br/>

**A Comprehensive Sensor-Based Physics Laboratory for Android**

[![License: AGPL v3](https://img.shields.io/badge/License-AGPL_v3-blue.svg?style=for-the-badge)](https://www.gnu.org/licenses/agpl-3.0)
[![Offline First](https://img.shields.io/badge/Offline_First-100%25-00FF99.svg?style=for-the-badge)]()
[![Platform: Android](https://img.shields.io/badge/Android_8.0+-Native-3DDC84.svg?style=for-the-badge&logo=android&logoColor=white)]()

<br/>

</div>

---

### 🔬 Project Overview

Many students studying advanced secondary physics lack access to equipped physical laboratories, forcing them to rely entirely on theoretical abstractions. 

**Yantra** bridges this gap. It is an offline-first, F-Droid-compliant Android application designed to transform a standard smartphone into a comprehensive hardware telemetry node. By directly accessing the device's native sensors, Yantra enables students to conduct rigorous empirical physics experiments, analyze data, and generate professional laboratory reports entirely on-device.

---

### ⚙️ Core Architecture

Yantra is engineered for precision, utilizing local computation to ensure zero reliance on cloud databases or external network requests. 

🟢 **Hardware Polling:** Direct interface with the Android `SensorManager` API and `CameraX` (Accelerometer, Gyroscope, Magnetometer, Microphone, and Optics) utilizing asynchronous Kotlin Coroutines and Flows.  
🟢 **Mathematical Filtering:** Raw sensor output is processed through custom algorithm architectures—including Low-pass filters, High-pass filters, Peak Detection, and Fast Fourier Transforms (FFT)—to isolate specific variables and mitigate hardware noise.  
🟢 **Automated PDF Engine:** Utilizes `iText7 Core` to automatically compile formatted observation tables, calculation steps, error analyses, and graphs into A4-ready laboratory reports saved locally via the Android MediaStore API.

---

### 🧪 Experiment Library (MVP)

Yantra ships with 12 foundational experiments mapped directly to standard physics and scientific syllabi:

**Mechanics & Gravity**
* **Simple Pendulum:** Measures $g$ utilizing Gyroscope Z-axis polling and peak detection for period calculation.
* **Free Fall:** Measures $g$ via Accelerometer magnitude monitoring (detecting $<1.5 m/s^2$ free fall states and $>15 m/s^2$ impact spikes).
* **Spring-Mass Oscillation (SHM):** Calculates the spring constant ($k$) via Accelerometer sinusoidal peak tracking.

**Friction & Kinematics**
* **Kinetic Friction:** Analyzes deceleration curves over sliding surfaces.
* **Static Friction:** Calculates critical slip angles ($\theta_c$) via continuous incline angle measurements.
* **Projectile Motion:** Tracks parabolic trajectories utilizing CameraX and optical flow/frame differencing.

**Waves, Sound & Thermodynamics**
* **Speed of Sound:** Utilizes Microphone high-threshold pulse detection to time acoustic echo differentials.
* **Resonance Frequency:** Extracts dominant frequency peaks via real-time FFT audio analysis.
* **Newton's Law of Cooling:** Fits manual interval temperature logging to an exponential decay regression model.

**Electromagnetism & Biometrics**
* **Magnetic Field Mapping:** Maps field strength and polarities via Magnetometer spatial tracking in a 5x5 coordinate grid.
* **Heart Rate Monitor (PPG):** Calculates BPM via CameraX red-channel frame extraction and 0.5-3Hz bandpass filtering.
* **Reaction Time Benchmark:** Measures human visual and auditory reaction times via high-precision UI event timing.

---

### 💻 Technology Stack

Engineered using modern Android development paradigms with a strict clean architecture approach.

| Domain | Implementation |
| :--- | :--- |
| **Frontend UI** | 🎨 Jetpack Compose (Strict scientific aesthetic, no XML) |
| **Architecture** | 🏗️ MVVM + Clean Architecture |
| **Dependency Injection** | 💉 Dagger-Hilt |
| **Concurrency** | 🧵 Kotlin Coroutines & `Dispatchers.Default` for computation |
| **Local Persistence** | 💾 Room Database & DataStore Preferences |
| **Hardware Access** | 📷 CameraX & Native `SensorManager` API |
| **Data Visualization** | 📈 MPAndroidChart / Compose Charts |

---

### 🚀 Development Roadmap: The Multimodal Supervisor

<details>
<summary><b>Click to expand Phase II details...</b></summary>
<br>
While the current release functions as a standalone data logger and mathematical filter, Phase II will introduce an active, agentic supervisor for complex DIY hardware setups.

**Proposed Execution:**
1. **Optical Interface:** Students utilize the CameraX viewfinder to scan physical breadboards, optical benches, or circuitry.
2. **Cloud Processing (Opt-In):** The application captures high-resolution frame buffers and securely transmits them to an enterprise multimodal LLM API.
3. **Pedagogical Feedback:** The AI acts as a digital lab assistant, visually verifying hardware routing, detecting structural anomalies (e.g., miswired components), and providing guided, step-by-step setup corrections.
</details>

---

### 🛠️ Build & Deployment

Yantra targets SDK 34 and requires a minimum of Android 8.0 (API 26). It is optimized to execute flawlessly on entry-level devices (e.g., 2GB RAM, Snapdragon 450 equivalents) by strictly offloading heavy computations (FFT, PDF generation) to background threads.

```bash
# Clone the repository
git clone [https://github.com/yourusername/yantra.git](https://github.com/yourusername/yantra.git)

# Navigate to the directory
cd yantra

# Build the debug APK
./gradlew assembleDebug
