package com.example.data.local.static

import com.example.domain.model.*

object ExperimentData {
    val allExperiments = listOf(
        Experiment(
            id = "physics_pendulum_g",
            name = "Simple Pendulum",
            subject = Subject.PHYSICS,
            className = 11,
            chapterNumber = 14,
            chapterName = "Oscillations",
            difficulty = Difficulty.EASY,
            estimatedMinutes = 15,
            sensorsRequired = listOf(SensorType.GYROSCOPE),
            isInCBSEExamList = true,
            aim = "To measure the acceleration due to gravity (g) using a simple pendulum.",
            principle = "The time period of a simple pendulum is T = 2π√(L/g). By measuring the period for a known length, we can calculate g.",
            whatPhoneMeasures = "The phone acts as the pendulum bob. The gyroscope detects the angular velocity peaks to count oscillations precisely.",
            materialsRequired = listOf("String (approx 1m)", "Ruler/Measuring tape", "Tape/Rubber band to attach phone"),
            setupSteps = listOf(
                SetupStep(1, "Attach the phone securely to one end of the string."),
                SetupStep(2, "Measure the exact length from the pivot point to the center of the phone screen", true, "Length", "cm", "string_length_cm")
            ),
            liveInstructions = listOf(
                LiveInstruction(1, "Pull the phone slightly to one side (less than 15 degrees) and release.", LiveActionType.TAP_TO_START)
            ),
            calculations = listOf(
                CalculationStep(1, "Time Period", "T = time / oscillations", "T = \\frac{t}{n}", "time_period", "s"),
                CalculationStep(2, "Gravity", "g = 4π² * L / T²", "g = \\frac{4\\pi^2 L}{T^2}", "gravity", "m/s²")
            ),
            standardValue = 9.8,
            standardValueUnit = "m/s²",
            standardValueLabel = "Standard Gravity",
            realLifeInsight = "This is why all grandfather clocks use the same pendulum length — 99.4 cm gives exactly a 2 second period.",
            reportTemplate = ReportTemplate("Pendulum Lab Report")
        ),
        Experiment(
            id = "physics_freefall_g",
            name = "Free Fall Gravity",
            subject = Subject.PHYSICS,
            className = 11,
            chapterNumber = 3,
            chapterName = "Motion in a Straight Line",
            difficulty = Difficulty.MEDIUM,
            estimatedMinutes = 10,
            sensorsRequired = listOf(SensorType.ACCELEROMETER),
            isInCBSEExamList = false,
            aim = "To measure the acceleration due to gravity (g) by dropping the device onto a soft surface.",
            principle = "In free fall, the accelerometer reads zero because it's in a state of weightlessness. By measuring the time it's in free fall and the distance dropped, we can calculate g: h = 0.5 * g * t^2.",
            whatPhoneMeasures = "The accelerometer detects the start of free fall (magnitude drops to ~0) and the impact (magnitude spikes), calculating the time elapsed.",
            materialsRequired = listOf("Soft cushion or mattress (CRITICAL!)", "Ruler/Measuring tape to measure height"),
            setupSteps = listOf(
                SetupStep(1, "Place a very soft cushion or pillow on the floor."),
                SetupStep(2, "Measure a specific drop height in centimeters", true, "Drop Height", "cm", "drop_height_cm")
            ),
            liveInstructions = listOf(
                LiveInstruction(1, "Hold the phone flat and still above the cushion at the specified height. Press start and then drop it.", LiveActionType.TAP_TO_START)
            ),
            calculations = listOf(
                CalculationStep(1, "Time in Air", "t = time between freefall and impact", "t", "time_in_air", "s"),
                CalculationStep(2, "Gravity", "g = 2h / t²", "g = \\frac{2h}{t^2}", "gravity", "m/s²")
            ),
            standardValue = 9.8,
            standardValueUnit = "m/s²",
            standardValueLabel = "Standard Gravity",
            realLifeInsight = "Objects in freefall experience zero-G, which is how zero gravity is simulated on airplanes like the 'Vomit Comet'.",
            reportTemplate = ReportTemplate("Free Fall Lab Report")
        ),
        Experiment(
            id = "physics_kinetic_friction",
            name = "Kinetic Friction",
            subject = Subject.PHYSICS,
            className = 11,
            chapterNumber = 5,
            chapterName = "Laws of Motion",
            difficulty = Difficulty.MEDIUM,
            estimatedMinutes = 15,
            sensorsRequired = listOf(SensorType.ACCELEROMETER),
            isInCBSEExamList = true,
            aim = "To find the coefficient of kinetic friction between the phone and a surface.",
            principle = "By giving the phone a quick push and letting it slide to a stop, we measure its deceleration. According to Newton's Second Law, μk = a / g.",
            whatPhoneMeasures = "The linear accelerometer measures deceleration after the initial push until the phone stops sliding.",
            materialsRequired = listOf("A flat, safe sliding surface (table, floor)"),
            setupSteps = listOf(
                SetupStep(1, "Place the phone flat on the surface.")
            ),
            liveInstructions = listOf(
                LiveInstruction(1, "Press start, then give the phone a quick but gentle push to slide it across the table.", LiveActionType.TAP_TO_START)
            ),
            calculations = listOf(
                CalculationStep(1, "Deceleration", "a = average deceleration", "a", "deceleration", "m/s²"),
                CalculationStep(2, "Kinetic Friction", "μk = a / g", "\\mu_k = \\frac{a}{g}", "kinetic_friction", "")
            ),
            standardValue = 0.3,
            standardValueUnit = "",
            standardValueLabel = "Typical Phone on Wood",
            realLifeInsight = "Anti-lock braking systems (ABS) in cars are designed to prevent the tires from entering kinetic friction because static friction provides more grip.",
            reportTemplate = ReportTemplate("Kinetic Friction Lab Report")
        ),
        Experiment(
            id = "physics_static_friction",
            name = "Static Friction on Inclined Plane",
            subject = Subject.PHYSICS,
            className = 11,
            chapterNumber = 5,
            chapterName = "Laws of Motion",
            difficulty = Difficulty.MEDIUM,
            estimatedMinutes = 15,
            sensorsRequired = listOf(SensorType.ACCELEROMETER, SensorType.ROTATION_VECTOR),
            isInCBSEExamList = true,
            aim = "To find the coefficient of static friction by raising an inclined plane.",
            principle = "When an object just begins to slide down an inclined plane, the coefficient of static friction is equal to the tangent of the angle of inclination: μs = tan(θ).",
            whatPhoneMeasures = "The phone calculates its tilt angle using gravity sensors and detects the exact moment sliding begins using linear acceleration.",
            materialsRequired = listOf("A flat stiff board or book to act as the inclined plane"),
            setupSteps = listOf(
                SetupStep(1, "Place the phone flat on the board horizontally.")
            ),
            liveInstructions = listOf(
                LiveInstruction(1, "Press start, then very slowly lift one end of the board to increase the angle until the phone just starts to slide.", LiveActionType.TAP_TO_START)
            ),
            calculations = listOf(
                CalculationStep(1, "Angle of Repose", "θ = tilt angle at sip", "\\theta", "angle_repose", "°"),
                CalculationStep(2, "Static Friction", "μs = tan(θ)", "\\mu_s = \\tan(\\theta)", "static_friction", "")
            ),
            standardValue = 0.4,
            standardValueUnit = "",
            standardValueLabel = "Typical Phone on Book",
            realLifeInsight = "Engineers use angle of repose calculations when designing everything from grain silos to avalanche barriers.",
            reportTemplate = ReportTemplate("Static Friction Lab Report")
        ),
        Experiment(
            id = "physics_shm_spring",
            name = "Spring-Mass Oscillation",
            subject = Subject.PHYSICS,
            className = 11,
            chapterNumber = 14,
            chapterName = "Oscillations",
            difficulty = Difficulty.HARD,
            estimatedMinutes = 20,
            sensorsRequired = listOf(SensorType.ACCELEROMETER),
            isInCBSEExamList = true,
            aim = "To find the spring constant (k) of a spring by oscillating a known mass.",
            principle = "The period of a mass-spring system in Simple Harmonic Motion is T = 2π√(m/k). By measuring the period and knowing the mass, we calculate k.",
            whatPhoneMeasures = "The accelerometer detects the periodic up-and-down motion to accurately measure the time period of oscillation.",
            materialsRequired = listOf("A stretchable spring", "A secure way to attach the phone to the spring (e.g. pouch)"),
            setupSteps = listOf(
                SetupStep(1, "Suspend the spring vertically and attach the phone to the bottom."),
                SetupStep(2, "Enter the exact mass of the phone in grams", true, "Phone Mass", "g", "phone_mass_g")
            ),
            liveInstructions = listOf(
                LiveInstruction(1, "Pull the phone down slightly and release to start vertical oscillations.", LiveActionType.TAP_TO_START)
            ),
            calculations = listOf(
                CalculationStep(1, "Time Period", "T = time / oscillations", "T = \\frac{t}{n}", "time_period", "s"),
                CalculationStep(2, "Spring Constant", "k = 4π²m / T²", "k = \\frac{4\\pi^2 m}{T^2}", "spring_constant", "N/m")
            ),
            standardValue = 15.0,
            standardValueUnit = "N/m",
            standardValueLabel = "Typical Slinky",
            realLifeInsight = "Car suspension systems rely on these exact principles to tune the ride quality over bumps.",
            reportTemplate = ReportTemplate("Spring Oscillation Lab Report")
        ),
        Experiment(
            id = "physics_centripetal",
            name = "Centripetal Acceleration",
            subject = Subject.PHYSICS,
            className = 11,
            chapterNumber = 4,
            chapterName = "Motion in a Plane",
            difficulty = Difficulty.HARD,
            estimatedMinutes = 20,
            sensorsRequired = listOf(SensorType.ACCELEROMETER, SensorType.GYROSCOPE),
            isInCBSEExamList = true,
            aim = "To verify the relationship between centripetal acceleration, angular velocity, and radius: a = ω²r.",
            principle = "An object moving in a circle experiences acceleration toward the center. By spinning the phone on a turntable, we measure a and ω simultaneously.",
            whatPhoneMeasures = "The gyroscope measures angular velocity (ω) and the accelerometer measures centripetal acceleration (a).",
            materialsRequired = listOf("A turntable or swivel chair", "Tape to secure phone"),
            setupSteps = listOf(
                SetupStep(1, "Place phone flat on the turntable."),
                SetupStep(2, "Measure distance from center of rotation to phone's center", true, "Radius", "cm", "radius_cm")
            ),
            liveInstructions = listOf(
                LiveInstruction(1, "Start spinning the turntable at a steady pace.", LiveActionType.TAP_TO_START)
            ),
            calculations = listOf(
                CalculationStep(1, "Calculated Acceleration", "a = ω²r", "a_{calc} = \\omega^2 r", "calc_accel", "m/s²")
            ),
            standardValue = 1.0,
            standardValueUnit = "ratio",
            standardValueLabel = "Expected ratio (Measured/Calculated)",
            realLifeInsight = "This force is what keeps you in your seat on a looping roller coaster.",
            reportTemplate = ReportTemplate("Centripetal Acceleration Report")
        ),
        Experiment(
            id = "sound_speed",
            name = "Speed of Sound (Echo)",
            subject = Subject.PHYSICS,
            className = 11,
            chapterNumber = 15,
            chapterName = "Waves",
            difficulty = Difficulty.HARD,
            estimatedMinutes = 15,
            sensorsRequired = listOf(SensorType.MICROPHONE),
            isInCBSEExamList = true,
            aim = "To measure the speed of sound using the echo from a wall.",
            principle = "By making a loud sound and timing how long it takes for the echo to return, v = 2d / t.",
            whatPhoneMeasures = "The microphone records audio at a high sample rate and detects the initial peak (clap) and the secondary peak (echo) to find the exact time difference.",
            materialsRequired = listOf("A large, flat outdoor wall (at least 20m away)"),
            setupSteps = listOf(
                SetupStep(1, "Stand far away from a large, flat wall."),
                SetupStep(2, "Measure the exact distance to the wall in meters", true, "Distance to Wall", "m", "distance_m")
            ),
            liveInstructions = listOf(
                LiveInstruction(1, "Press Start, then clap your hands loudly once.", LiveActionType.TAP_TO_START)
            ),
            calculations = listOf(
                CalculationStep(1, "Time Delay", "t = time difference between peaks", "t", "time_delay", "s"),
                CalculationStep(2, "Speed of Sound", "v = 2d / t", "v = \\frac{2d}{t}", "speed_sound", "m/s")
            ),
            standardValue = 343.0,
            standardValueUnit = "m/s",
            standardValueLabel = "Speed of sound in air at 20°C",
            realLifeInsight = "Sonar and medical ultrasound imaging use this exact echo-timing principle to map the ocean floor or look inside the body.",
            reportTemplate = ReportTemplate("Speed of Sound Lab Report")
        ),
        Experiment(
            id = "sound_doppler",
            name = "Doppler Effect",
            subject = Subject.PHYSICS,
            className = 11,
            chapterNumber = 15,
            chapterName = "Waves",
            difficulty = Difficulty.MEDIUM,
            estimatedMinutes = 15,
            sensorsRequired = listOf(SensorType.MICROPHONE),
            isInCBSEExamList = false,
            aim = "To observe the Doppler shift in sound frequency from a moving source.",
            principle = "When a sound source moves towards an observer, the frequency appears higher. When moving away, it appears lower.",
            whatPhoneMeasures = "The microphone analyzes the sound spectrum using Fast Fourier Transform (FFT) to detect shifts in the peak frequency as the source passes.",
            materialsRequired = listOf("A second phone or speaker to play a constant tone"),
            setupSteps = listOf(
                SetupStep(1, "Set a second phone to play a constant tone (e.g., 4000 Hz) using a tone generator app."),
                SetupStep(2, "Enter the emitted frequency", true, "Frequency", "Hz", "source_hz")
            ),
            liveInstructions = listOf(
                LiveInstruction(1, "Have a friend swing the phone playing the tone towards and away from your recording phone.", LiveActionType.TAP_TO_START)
            ),
            calculations = listOf(
                CalculationStep(1, "Max Frequency", "f_max = highest frequency detected", "f_{max}", "max_freq", "Hz"),
                CalculationStep(2, "Min Frequency", "f_min = lowest frequency detected", "f_{min}", "min_freq", "Hz")
            ),
            standardValue = 4000.0,
            standardValueUnit = "Hz",
            standardValueLabel = "Expected Average",
            realLifeInsight = "Police radar guns and weather radar use the Doppler effect with electromagnetic waves to measure speed.",
            reportTemplate = ReportTemplate("Doppler Effect Report")
        ),
        Experiment(
            id = "math_pi_darts",
            name = "Estimating Pi (Monte Carlo)",
            subject = Subject.MATH,
            className = 10,
            chapterNumber = 14,
            chapterName = "Probability",
            difficulty = Difficulty.EASY,
            estimatedMinutes = 5,
            sensorsRequired = listOf(), // No sensors, UI based simulation/game
            isInCBSEExamList = false,
            aim = "To estimate the value of Pi using random sampling (Monte Carlo method).",
            principle = "If random points drop inside a square holding a circle, the ratio of points inside the circle to total points approaches π/4.",
            whatPhoneMeasures = "The app records the location of your screen taps. You'll randomly tap the screen as fast as possible.",
            materialsRequired = listOf("Just your phone and your fingers!"),
            setupSteps = listOf(
                SetupStep(1, "The screen will show a circle inscribed in a square.")
            ),
            liveInstructions = listOf(
                LiveInstruction(1, "Tap randomly all over the square area as wildly as you can for 10 seconds.", LiveActionType.TAP_TO_START)
            ),
            calculations = listOf(
                CalculationStep(1, "Ratio", "p = Circle Hits / Total Hits", "p", "ratio", ""),
                CalculationStep(2, "Estimated Pi", "π ≈ 4 * p", "\\pi \\approx 4p", "est_pi", "")
            ),
            standardValue = 3.14159,
            standardValueUnit = "",
            standardValueLabel = "Actual Pi",
            realLifeInsight = "Monte Carlo simulations are heavily used in modern finance, physics, and AI to estimate complex probabilities.",
            reportTemplate = ReportTemplate("Monte Carlo Pi Report")
        ),
        Experiment(
            id = "bio_reaction_time",
            name = "Human Reaction Time",
            subject = Subject.BIOLOGY,
            className = 11,
            chapterNumber = 21,
            chapterName = "Neural Control and Coordination",
            difficulty = Difficulty.EASY,
            estimatedMinutes = 5,
            sensorsRequired = listOf(), // UI based
            isInCBSEExamList = true,
            aim = "To measure visual human reaction time.",
            principle = "Reaction time is the interval between a stimulus presentation and the muscular response. It involves sensory pathways, processing in the brain, and motor pathways.",
            whatPhoneMeasures = "Measures the time difference between the screen changing color and the user tapping the screen with millisecond precision.",
            materialsRequired = listOf("Just your phone"),
            setupSteps = listOf(
                SetupStep(1, "Hold the phone comfortably, ready to tap the screen.")
            ),
            liveInstructions = listOf(
                LiveInstruction(1, "Wait for the screen to turn GREEN, then tap as fast as you can. We will do 5 trials.", LiveActionType.TAP_TO_START)
            ),
            calculations = listOf(
                CalculationStep(1, "Average Reaction Time", "Sum of times / 5", "t_{avg}", "avg_time", "ms")
            ),
            standardValue = 250.0,
            standardValueUnit = "ms",
            standardValueLabel = "Average human visual reaction",
            realLifeInsight = "Fighter pilots and Formula 1 drivers train extensively to reduce their reaction times closer to 150ms.",
            reportTemplate = ReportTemplate("Reaction Time Report")
        ),
        Experiment(
            id = "physics_magnetic_field",
            name = "Magnetic Field Mapping",
            subject = Subject.PHYSICS,
            className = 12,
            chapterNumber = 4,
            chapterName = "Moving Charges and Magnetism",
            difficulty = Difficulty.EASY,
            estimatedMinutes = 10,
            sensorsRequired = listOf(SensorType.MAGNETOMETER),
            isInCBSEExamList = false,
            aim = "To map the magnetic field strength around a small bar magnet.",
            principle = "The magnetic field strength decreases proportionally to the cube of the distance from a dipole magnet: B ∝ 1/r³.",
            whatPhoneMeasures = "The phone's 3-axis magnetometer directly measures the ambient magnetic field in microteslas (μT).",
            materialsRequired = listOf("A small magnet", "A ruler"),
            setupSteps = listOf(
                SetupStep(1, "Place the phone on a table away from metal objects."),
                SetupStep(2, "Record ambient field (baseline).")
            ),
            liveInstructions = listOf(
                LiveInstruction(1, "Slowly slide the magnet towards the phone along the ruler and watch the field strength increase.", LiveActionType.TAP_TO_START)
            ),
            calculations = listOf(
                CalculationStep(1, "Max Field", "Max B - Baseline B", "B_{max}", "max_field", "μT")
            ),
            standardValue = 50.0,
            standardValueUnit = "μT",
            standardValueLabel = "Average Earth Magnetic Field",
            realLifeInsight = "Smartphones use this sensor combined with gravity to act as a compass for navigation.",
            reportTemplate = ReportTemplate("Magnetic Field Report")
        ),
        Experiment(
            id = "physics_incline_accel",
            name = "Acceleration on an Incline",
            subject = Subject.PHYSICS,
            className = 11,
            chapterNumber = 3,
            chapterName = "Motion in a Straight Line",
            difficulty = Difficulty.MEDIUM,
            estimatedMinutes = 15,
            sensorsRequired = listOf(SensorType.ACCELEROMETER, SensorType.ROTATION_VECTOR),
            isInCBSEExamList = false,
            aim = "To determine the acceleration of an object rolling or sliding down a frictionless incline.",
            principle = "Without friction, the acceleration of an object down an incline is a = g * sin(θ).",
            whatPhoneMeasures = "Measures the angle of the incline and the linear acceleration of the phone as it slides down.",
            materialsRequired = listOf("Smooth ramp", "Phone case to protect phone"),
            setupSteps = listOf(
                SetupStep(1, "Place the phone at the top of the ramp.")
            ),
            liveInstructions = listOf(
                LiveInstruction(1, "Let the phone slide down the ramp. Catch it before it hits the floor!", LiveActionType.TAP_TO_START)
            ),
            calculations = listOf(
                CalculationStep(1, "Expected Accel", "g * sin(θ)", "a = g \\sin(\\theta)", "expected_a", "m/s²"),
                CalculationStep(2, "Measured Accel", "Average from graph", "a_{meas}", "measured_a", "m/s²")
            ),
            standardValue = 0.0,
            standardValueUnit = "% difference",
            standardValueLabel = "Expected vs Measured",
            realLifeInsight = "Galileo used inclined planes to slow down falling objects so he could measure acceleration using water clocks.",
            reportTemplate = ReportTemplate("Inclined Plane Report")
        )
    )
}
