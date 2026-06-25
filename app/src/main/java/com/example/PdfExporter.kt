package com.example

import android.content.Context
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.net.Uri
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PdfExporter {

    fun exportToPdf(
        context: Context,
        uri: Uri,
        grade: String,
        year: String,
        increment: Int,
        maxIncrements: Int,
        hraType: String,
        maritalStatus: String,
        basicPay: Int,
        hraValue: Int,
        conveyanceValue: Int,
        medicalValue: Int,
        entertainmentValue: Int,
        sPostValue: Int,
        carMonetizationValue: Int,
        gpFundValue: Int,
        grossPay: Int,
        netPay: Int
    ): Boolean {
        var outputStream: OutputStream? = null
        try {
            outputStream = context.contentResolver.openOutputStream(uri)
            if (outputStream == null) return false
            return exportToStream(
                outputStream = outputStream,
                grade = grade,
                year = year,
                increment = increment,
                maxIncrements = maxIncrements,
                hraType = hraType,
                maritalStatus = maritalStatus,
                basicPay = basicPay,
                hraValue = hraValue,
                conveyanceValue = conveyanceValue,
                medicalValue = medicalValue,
                entertainmentValue = entertainmentValue,
                sPostValue = sPostValue,
                carMonetizationValue = carMonetizationValue,
                gpFundValue = gpFundValue,
                grossPay = grossPay,
                netPay = netPay
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        } finally {
            try {
                outputStream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun exportToStream(
        outputStream: OutputStream,
        grade: String,
        year: String,
        increment: Int,
        maxIncrements: Int,
        hraType: String,
        maritalStatus: String,
        basicPay: Int,
        hraValue: Int,
        conveyanceValue: Int,
        medicalValue: Int,
        entertainmentValue: Int,
        sPostValue: Int,
        carMonetizationValue: Int,
        gpFundValue: Int,
        grossPay: Int,
        netPay: Int
    ): Boolean {
        var pdfDocument: PdfDocument? = null
        try {
            pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas

            // Colors
            val primaryColor = Color.rgb(0x38, 0x6B, 0x3F)      // Deep Emerald Green (#386B3F)
            val lightBgColor = Color.rgb(0xF6, 0xFB, 0xF4)      // Soft Green Background (#F6FBF4)
            val textDark = Color.rgb(0x19, 0x1C, 0x19)          // Dark neutral text (#191C19)
            val textGray = Color.rgb(0x55, 0x5A, 0x55)          // Secondary Gray text (#555A55)
            val textLightGray = Color.rgb(0x88, 0x8E, 0x88)     // Very light gray
            val dividerColor = Color.rgb(0xE0, 0xE9, 0xDE)     // Soft border/divider (#E0E9DE)
            val redColor = Color.rgb(0xD3, 0x2F, 0x2F)          // Error/Deduction Red

            // Paint configurations
            val paint = Paint()

            // 1. Header Banner (Solid Emerald Green rect)
            paint.style = Paint.Style.FILL
            paint.color = primaryColor
            canvas.drawRect(0f, 0f, 595f, 120f, paint)

            // Header Title (Left Aligned)
            paint.color = Color.WHITE
            paint.isAntiAlias = true
            paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            paint.textSize = 18f
            canvas.drawText("BPS PAY SCALE CALCULATOR", 40f, 50f, paint)

            // Header Subtitle
            paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            paint.textSize = 10f
            paint.color = Color.argb(220, 255, 255, 255) // Semi-transparent white
            canvas.drawText("Official Salary Calculation Breakdown Statement", 40f, 72f, paint)

            // Header Timestamp
            val dateFormat = SimpleDateFormat("EEEE, d MMMM yyyy, hh:mm a", Locale.getDefault())
            val formattedDate = dateFormat.format(Date())
            paint.textSize = 9f
            paint.color = Color.argb(180, 255, 255, 255)
            canvas.drawText("Generated on: $formattedDate", 40f, 92f, paint)

            // 2. Section 1: Parameters Card
            // Label
            paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            paint.textSize = 11f
            paint.color = primaryColor
            canvas.drawText("CALCULATION PARAMETERS", 40f, 155f, paint)

            // Draw Card background
            paint.style = Paint.Style.FILL
            paint.color = lightBgColor
            canvas.drawRoundRect(40f, 168f, 555f, 258f, 8f, 8f, paint)

            // Draw Card border
            paint.style = Paint.Style.STROKE
            paint.color = dividerColor
            paint.strokeWidth = 1f
            canvas.drawRoundRect(40f, 168f, 555f, 258f, 8f, 8f, paint)

            // Draw Parameter details
            paint.style = Paint.Style.FILL
            paint.color = textDark
            paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            paint.textSize = 10f

            val startY = 192f
            val spacingY = 18f
            val col1X = 55f
            val col2X = 300f

            // Column 1
            paint.color = textGray
            canvas.drawText("BPS Grade:", col1X, startY, paint)
            canvas.drawText("Pay Scale Year:", col1X, startY + spacingY, paint)
            canvas.drawText("Increment Stage:", col1X, startY + (2 * spacingY), paint)

            paint.color = textDark
            paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            canvas.drawText("BPS-$grade", col1X + 100f, startY, paint)
            canvas.drawText(year, col1X + 100f, startY + spacingY, paint)
            canvas.drawText("$increment / $maxIncrements", col1X + 100f, startY + (2 * spacingY), paint)

            // Column 2
            paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            paint.color = textGray
            canvas.drawText("Marital Status:", col2X, startY, paint)
            canvas.drawText("House Rent Category:", col2X, startY + spacingY, paint)

            paint.color = textDark
            paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            canvas.drawText(maritalStatus, col2X + 120f, startY, paint)
            canvas.drawText(hraType, col2X + 120f, startY + spacingY, paint)


            // 3. Section 2: Summary Blocks (Gross, Deductions, Net)
            // Title
            paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            paint.textSize = 11f
            paint.color = primaryColor
            canvas.drawText("FINANCIAL SUMMARY", 40f, 290f, paint)

            // Gross Pay Block
            paint.style = Paint.Style.FILL
            paint.color = lightBgColor
            canvas.drawRoundRect(40f, 302f, 195f, 362f, 8f, 8f, paint)
            paint.style = Paint.Style.STROKE
            paint.color = dividerColor
            canvas.drawRoundRect(40f, 302f, 195f, 362f, 8f, 8f, paint)

            paint.style = Paint.Style.FILL
            paint.color = textGray
            paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            paint.textSize = 9f
            canvas.drawText("GROSS PAY", 52f, 322f, paint)
            paint.color = textDark
            paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            paint.textSize = 13f
            canvas.drawText(String.format("Rs. %,d", grossPay), 52f, 344f, paint)

            // Deductions Block
            paint.style = Paint.Style.FILL
            paint.color = Color.rgb(0xFD, 0xF2, 0xF2) // Soft light red
            canvas.drawRoundRect(205f, 302f, 360f, 362f, 8f, 8f, paint)
            paint.style = Paint.Style.STROKE
            paint.color = Color.rgb(0xFC, 0xDE, 0xDE)
            canvas.drawRoundRect(205f, 302f, 360f, 362f, 8f, 8f, paint)

            paint.style = Paint.Style.FILL
            paint.color = redColor
            paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            paint.textSize = 9f
            canvas.drawText("DEDUCTIONS", 217f, 322f, paint)
            paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            paint.textSize = 13f
            canvas.drawText(String.format("Rs. %,d", gpFundValue), 217f, 344f, paint)

            // Net Monthly Pay Block (Prominent)
            paint.style = Paint.Style.FILL
            paint.color = primaryColor
            canvas.drawRoundRect(370f, 302f, 555f, 362f, 8f, 8f, paint)

            paint.color = Color.argb(180, 255, 255, 255)
            paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            paint.textSize = 9f
            canvas.drawText("NET MONTHLY PAY", 382f, 322f, paint)
            paint.color = Color.WHITE
            paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            paint.textSize = 15f
            canvas.drawText(String.format("Rs. %,d", netPay), 382f, 344f, paint)


            // 4. Section 3: Salary Breakdown Table
            paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            paint.textSize = 11f
            paint.color = primaryColor
            canvas.drawText("SALARY BREAKDOWN", 40f, 395f, paint)

            // Table Header Background
            paint.style = Paint.Style.FILL
            paint.color = primaryColor
            canvas.drawRect(40f, 408f, 555f, 430f, paint)

            // Table Headers
            paint.color = Color.WHITE
            paint.textSize = 9f
            paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            canvas.drawText("DESCRIPTION", 52f, 423f, paint)
            canvas.drawText("AMOUNT", 495f, 423f, paint) // aligned to right roughly

            // Construct list of items for table
            val items = ArrayList<Pair<String, Pair<Int, Boolean>>>()
            items.add("Basic Pay" to (basicPay to false))
            items.add("House Rent ($hraType)" to (hraValue to false))
            if (conveyanceValue > 0) {
                items.add("Conveyance Allowance" to (conveyanceValue to false))
            }
            items.add("Medical Allowance" to (medicalValue to false))
            if (entertainmentValue > 0) {
                items.add("Entertainment Allowance" to (entertainmentValue to false))
            }
            if (sPostValue > 0) {
                items.add("Senior Post Allowance" to (sPostValue to false))
            }
            if (carMonetizationValue > 0) {
                items.add("Car Monetization" to (carMonetizationValue to false))
            }
            items.add("GP Fund (Deduction)" to (gpFundValue to true))

            var currentY = 448f
            val itemHeight = 26f

            paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            paint.textSize = 10f

            for ((label, valPair) in items) {
                val (value, isDeduction) = valPair

                // Alternating row background for a modern spreadsheet look
                paint.style = Paint.Style.FILL
                if (isDeduction) {
                    paint.color = Color.rgb(0xFE, 0xF2, 0xF2) // light red for deduction row
                } else {
                    paint.color = if ((items.indexOfFirst { it.first == label } % 2) == 1) Color.rgb(0xFA, 0xFC, 0xFA) else Color.WHITE
                }
                canvas.drawRect(40f, currentY - 18f, 555f, currentY + 8f, paint)

                // Label
                paint.color = textDark
                paint.typeface = Typeface.create(Typeface.SANS_SERIF, if (label == "Basic Pay") Typeface.BOLD else Typeface.NORMAL)
                canvas.drawText(label, 52f, currentY, paint)

                // Value (Right Aligned)
                paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
                val valueStr = if (isDeduction) "- Rs. $value" else "Rs. $value"
                paint.color = if (isDeduction) redColor else primaryColor

                val bounds = Rect()
                paint.getTextBounds(valueStr, 0, valueStr.length, bounds)
                val valueWidth = bounds.width()
                canvas.drawText(valueStr, 555f - 15f - valueWidth, currentY, paint)

                // Border separator
                paint.style = Paint.Style.STROKE
                paint.color = dividerColor
                paint.strokeWidth = 0.5f
                canvas.drawLine(40f, currentY + 8f, 555f, currentY + 8f, paint)

                currentY += itemHeight
            }

            // 5. Official Verification & Notes
            currentY += 15f
            paint.style = Paint.Style.FILL
            paint.color = textGray
            paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.ITALIC)
            paint.textSize = 8.5f
            canvas.drawText("Note: House Rent Allowance (HRA) is subject to marital status.", 40f, currentY, paint)
            canvas.drawText("Conveyance allowance and medical allowance are configured per official BPS regulations.", 40f, currentY + 12f, paint)

            // 6. Footer Divider & Copyright
            paint.style = Paint.Style.STROKE
            paint.color = dividerColor
            paint.strokeWidth = 1f
            canvas.drawLine(40f, 770f, 555f, 770f, paint)

            paint.style = Paint.Style.FILL
            paint.color = textLightGray
            paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            paint.textSize = 8f
            val disclaimer1 = "This is a computer-generated calculation breakdown statement and is for informational purposes only."
            val disclaimer2 = "Powered by BPS Pay Scale Calculator App — © 2026"
            
            // Center align disclaimer
            val bounds1 = Rect()
            paint.getTextBounds(disclaimer1, 0, disclaimer1.length, bounds1)
            canvas.drawText(disclaimer1, (595f - bounds1.width()) / 2f, 790f, paint)

            val bounds2 = Rect()
            paint.getTextBounds(disclaimer2, 0, disclaimer2.length, bounds2)
            canvas.drawText(disclaimer2, (595f - bounds2.width()) / 2f, 804f, paint)

            // Finish page and save
            pdfDocument.finishPage(page)
            pdfDocument.writeTo(outputStream)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        } finally {
            try {
                pdfDocument?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
