package com.example.domain.model

enum class Board {
    CBSE, MP_BOARD, UP_BOARD, MAHARASHTRA_BOARD, RAJASTHAN_BOARD
}

enum class Subject {
    PHYSICS, CHEMISTRY, BIOLOGY, SCIENCE_COMBINED, MATH
}

data class UserProfile(
    val name: String,
    val className: Int,
    val board: Board,
    val schoolName: String,
    val subjects: List<Subject>,
    val rollNumber: String = ""
)

enum class Difficulty { EASY, MEDIUM, HARD }

enum class SensorType {
    ACCELEROMETER, GYROSCOPE, MAGNETOMETER, 
    MICROPHONE, CAMERA, GPS, LIGHT, BAROMETER, PROXIMITY, ROTATION_VECTOR
}

data class SetupStep(
    val stepNumber: Int,
    val instruction: String,
    val requiresInput: Boolean = false,
    val inputLabel: String = "",
    val inputUnit: String = "",
    val inputKey: String = ""
)

enum class LiveActionType {
    TAP_TO_START, TAP_TO_STOP, TAP_TO_CAPTURE, 
    AUTO_CAPTURE, HOLD_STEADY, PERFORM_ACTION
}

data class LiveInstruction(
    val stepNumber: Int,
    val instruction: String,
    val actionType: LiveActionType,
    val autoDetect: Boolean = false,
    val autoDetectThreshold: Double? = null
)

data class CalculationStep(
    val stepNumber: Int,
    val label: String,
    val formula: String,
    val formulaLatex: String,
    val resultKey: String,
    val unit: String
)

data class ReportTemplate(
    val title: String
    // We'll expand this if necessary, but PDF generation handles logic based on Experiment
)

data class Experiment(
    val id: String,
    val name: String,
    val subject: Subject,
    val className: Int,
    val chapterNumber: Int,
    val chapterName: String,
    val difficulty: Difficulty,
    val estimatedMinutes: Int,
    val sensorsRequired: List<SensorType>,
    val isInCBSEExamList: Boolean,
    val aim: String,
    val principle: String,
    val whatPhoneMeasures: String,
    val materialsRequired: List<String>,
    val setupSteps: List<SetupStep>,
    val liveInstructions: List<LiveInstruction>,
    val calculations: List<CalculationStep>,
    val standardValue: Double?,
    val standardValueUnit: String?,
    val standardValueLabel: String?,
    val realLifeInsight: String,
    val reportTemplate: ReportTemplate
)

data class SensorReading(
    val timestamp: Long,
    val values: FloatArray,
    val sensorType: SensorType,
    val accuracy: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as SensorReading
        if (timestamp != other.timestamp) return false
        if (!values.contentEquals(other.values)) return false
        if (sensorType != other.sensorType) return false
        if (accuracy != other.accuracy) return false
        return true
    }

    override fun hashCode(): Int {
        var result = timestamp.hashCode()
        result = 31 * result + values.contentHashCode()
        result = 31 * result + sensorType.hashCode()
        result = 31 * result + accuracy
        return result
    }
}

data class LiveExperimentState(
    val currentInstruction: LiveInstruction,
    val instructionIndex: Int,
    val trialNumber: Int,
    val totalTrials: Int,
    val capturedReadings: List<SensorReading>,
    val isCollecting: Boolean,
    val isPaused: Boolean,
    val errorMessage: String?,
    val isComplete: Boolean
)
