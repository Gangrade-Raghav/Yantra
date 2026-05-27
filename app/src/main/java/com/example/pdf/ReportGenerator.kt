package com.example.pdf

import android.content.Context
import android.os.Environment
import com.example.domain.model.Experiment
import com.example.domain.model.UserProfile
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Singleton
class ReportGenerator @Inject constructor() {

    suspend fun generateReport(
        context: Context,
        userProfile: UserProfile,
        experiment: Experiment,
        results: Map<String, Double>,
        pctError: Double?
    ): File = withContext(Dispatchers.IO) {
        
        val dateStr = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
        val fileName = "Yantra_${experiment.id}_$dateStr.pdf"
        
        // Save to cache directory to avoid permissions issues
        val documentsDir = File(context.cacheDir, "reports")
        if (!documentsDir.exists()) {
            documentsDir.mkdirs()
        }
        val file = File(documentsDir, fileName)

        val writer = PdfWriter(file)
        val pdf = PdfDocument(writer)
        val document = Document(pdf)

        // 1. Header block
        document.add(Paragraph("YANTRA — PRACTICAL LAB REPORT")
            .setBold()
            .setFontSize(14f)
            .setTextAlignment(TextAlignment.CENTER))

        val headerTable = Table(UnitValue.createPercentArray(floatArrayOf(50f, 50f))).useAllAvailableWidth()
        headerTable.addCell(Cell().add(Paragraph("Name: ${userProfile.name}\nClass: ${userProfile.className}\nSchool: ${userProfile.schoolName}")).setBorder(null))
        
        val dateDisplayStr = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH).format(Date())
        headerTable.addCell(Cell().add(Paragraph("Date: $dateDisplayStr\nRoll Number: ${userProfile.rollNumber.ifEmpty { "N/A" }}")).setTextAlignment(TextAlignment.RIGHT).setBorder(null))
        document.add(headerTable)
        
        document.add(Paragraph("---------------------------------------------------------------------------------------------------------").setTextAlignment(TextAlignment.CENTER))

        // 2. Aim
        document.add(Paragraph("Experiment").setBold().setFontSize(12f))
        document.add(Paragraph("Aim: ${experiment.aim}"))

        // 3. Apparatus
        document.add(Paragraph("\nApparatus and Materials").setBold())
        val materialsList = com.itextpdf.layout.element.List().setSymbolIndent(12f).setListSymbol("\u2022")
        experiment.materialsRequired.forEach { materialsList.add(it) }
        document.add(materialsList)

        // 4. Theory
        document.add(Paragraph("\nTheory").setBold())
        document.add(Paragraph(experiment.principle))

        // 5. Procedure
        document.add(Paragraph("\nProcedure").setBold())
        val procedureList = com.itextpdf.layout.element.List().setSymbolIndent(12f) // Numeric by default if we use numbers, we will just add text
        experiment.setupSteps.forEach { document.add(Paragraph("${it.stepNumber}. ${it.instruction}")) }
        experiment.liveInstructions.forEach { document.add(Paragraph("- ${it.instruction}")) }

        // 6. Observations (Results table for now)
        document.add(Paragraph("\nObservations & Calculations").setBold())
        val obsTable = Table(UnitValue.createPercentArray(floatArrayOf(70f, 30f))).useAllAvailableWidth()
        obsTable.addHeaderCell(Cell().add(Paragraph("Parameter").setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY)))
        obsTable.addHeaderCell(Cell().add(Paragraph("Value").setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY)))
        
        results.forEach { (key, value) ->
            obsTable.addCell(Cell().add(Paragraph(key)))
            obsTable.addCell(Cell().add(Paragraph(String.format("%.4f", value))))
        }
        document.add(obsTable)

        // Result
        document.add(Paragraph("\nResult").setBold())
        if (pctError != null) {
            document.add(Paragraph("The experiment resulted in a percentage error of ${String.format("%.2f%%", pctError)} compared to standard values."))
            document.add(Paragraph("Standard Value: ${experiment.standardValue} ${experiment.standardValueUnit}"))
            document.add(Paragraph("Real life insight: ${experiment.realLifeInsight}").setItalic())
        } else {
            document.add(Paragraph("Experiment completed successfully."))
        }

        // Footers
        document.add(Paragraph("\n\n\n\n"))
        val sigTable = Table(UnitValue.createPercentArray(floatArrayOf(33f, 33f, 34f))).useAllAvailableWidth()
        sigTable.addCell(Cell().add(Paragraph("___________________\nStudent Signature")).setBorder(null).setTextAlignment(TextAlignment.CENTER))
        sigTable.addCell(Cell().add(Paragraph("___________________\nTeacher Signature")).setBorder(null).setTextAlignment(TextAlignment.CENTER))
        sigTable.addCell(Cell().add(Paragraph("___________________\nDate")).setBorder(null).setTextAlignment(TextAlignment.CENTER))
        document.add(sigTable)
        
        document.add(Paragraph("\nGenerated by Yantra — yantra.app").setFontSize(8f).setFontColor(ColorConstants.GRAY).setTextAlignment(TextAlignment.CENTER))

        document.close()
        file
    }
}
