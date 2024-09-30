package com.example.bodybuildingprogram

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.view.View
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class PdfGenerator(private val context: Context) {
    // متدی برای تبدیل View به فایل PDF
    fun createPDFFromView(view: View, fileName: String) {
        // ایجاد یک PdfDocument
        val document = PdfDocument()

        // تعیین اطلاعات صفحه، سایز صفحه همان سایز View خواهد بود
        val pageInfo = PdfDocument.PageInfo.Builder(view.width, view.height, 1).create()
        val page = document.startPage(pageInfo)

        // رسم View بر روی Canvas
        val canvas = page.canvas
        view.draw(canvas)

        // پایان صفحه
        document.finishPage(page)

        // ذخیره کردن فایل در پوشه Downloads
        val directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path
        val file = File(directoryPath, "$fileName.pdf")

        try {
            document.writeTo(FileOutputStream(file))
            Toast.makeText(context, "PDF created successfully at: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
//            println("PDF created successfully at: ${file.absolutePath}")
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, "Error writing PDF: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
//            println("Error writing PDF: ${e.localizedMessage}")
        }

        // بستن سند PDF
        document.close()
    }
}