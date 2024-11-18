package com.example.bodybuildingprogram

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Environment
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.itextpdf.kernel.geom.PageSize
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.canvas.PdfCanvas
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.property.UnitValue
import java.io.ByteArrayOutputStream


class PdfGenerator() {

    fun <T> createMultiPagePdfWithoutGap(
        relativeLayout: RelativeLayout,
        fileName: String,
        arrayList: ArrayList<T>
    ) {
        relativeLayout.post {
            val viewWidth = relativeLayout.width
            val viewHeight = relativeLayout.height

            if (viewWidth <= 0 || viewHeight <= 0) {
                Toast.makeText(relativeLayout.context, "Invalid dimensions for PDF", Toast.LENGTH_SHORT).show()
                return@post
            }

            val directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path
            val file = File(directoryPath, "$fileName.pdf")

            try {
                val pdfWriter = PdfWriter(FileOutputStream(file))
                val pdfDocument = PdfDocument(pdfWriter)
                val document = Document(pdfDocument)

                val pageWidth = pdfDocument.defaultPageSize.width.toDouble()
                val pageHeight = pdfDocument.defaultPageSize.height.toDouble()

                // رنگ پس‌زمینه
                val color = ContextCompat.getColor(relativeLayout.context, R.color.white02)
                val red = (color shr 16 and 0xFF) / 255f
                val green = (color shr 8 and 0xFF) / 255f
                val blue = (color and 0xFF) / 255f

                var currentHeight = 0

                // افزودن صفحه‌ی اول و تنظیم پس‌زمینه
                var page = pdfDocument.addNewPage(PageSize.A4)
                var pdfCanvas = PdfCanvas(page)
                pdfCanvas.setFillColorRgb(red, green, blue)
                pdfCanvas.rectangle(0.0, 0.0, pageWidth, pageHeight)
                pdfCanvas.fill()

                for (i in 0 until relativeLayout.childCount) {
                    val tempView = relativeLayout.getChildAt(i)

                    if (tempView is RecyclerView) {
                        val adapter = tempView.adapter
                        val layoutManager = tempView.layoutManager

                        if (adapter != null && layoutManager != null) {
                            for (j in 0 until adapter.itemCount) {
                                val viewHolder = adapter.createViewHolder(tempView, adapter.getItemViewType(j))
                                adapter.onBindViewHolder(viewHolder, j)
                                val listItem = viewHolder.itemView

                                if (currentHeight + listItem.height > pageHeight) {
                                    // افزودن صفحه‌ی جدید
                                    page = pdfDocument.addNewPage(PageSize.A4)
                                    pdfCanvas = PdfCanvas(page)
                                    pdfCanvas.setFillColorRgb(red, green, blue)
                                    pdfCanvas.rectangle(0.0, 0.0, pageWidth, pageHeight)
                                    pdfCanvas.fill()

                                    currentHeight = 0 // بازنشانی ارتفاع
                                }

                                listItem.measure(
                                    View.MeasureSpec.makeMeasureSpec(viewWidth, View.MeasureSpec.EXACTLY),
                                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                                )
                                listItem.layout(0, 0, listItem.measuredWidth, listItem.measuredHeight)

                                val listItemBitmap =
                                    Bitmap.createBitmap(viewWidth, listItem.height, Bitmap.Config.ARGB_8888)
                                val listItemCanvas = Canvas(listItemBitmap)
                                listItem.draw(listItemCanvas)

                                val listItemImage =
                                    Image(com.itextpdf.io.image.ImageDataFactory.create(listItemBitmap.toByteArray()))
                                listItemImage.setWidth(UnitValue.createPercentValue(100f))

                                document.add(listItemImage)
                                currentHeight += listItem.height
                            }
                        }
                    } else {
                        val profileBitmap = Bitmap.createBitmap(viewWidth, tempView.height, Bitmap.Config.ARGB_8888)
                        val profileCanvas = Canvas(profileBitmap)
                        tempView.draw(profileCanvas)

                        val profileImage = Image(com.itextpdf.io.image.ImageDataFactory.create(profileBitmap.toByteArray()))
                        profileImage.setWidth(UnitValue.createPercentValue(100f))

                        if (currentHeight + tempView.height > pageHeight) {
                            // افزودن صفحه‌ی جدید
                            page = pdfDocument.addNewPage(PageSize.A4)
                            pdfCanvas = PdfCanvas(page)
                            pdfCanvas.setFillColorRgb(red, green, blue)
                            pdfCanvas.rectangle(0.0, 0.0, pageWidth, pageHeight)
                            pdfCanvas.fill()

                            currentHeight = 0 // بازنشانی ارتفاع
                        }

                        document.add(profileImage)
                        currentHeight += tempView.height
                    }
                }

                // بستن اسناد
                document.close()
                pdfDocument.close()

                Toast.makeText(relativeLayout.context, "PDF ساخته شد", Toast.LENGTH_SHORT).show()
                Toast.makeText(relativeLayout.context, "PDF created successfully at: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
                arrayList.clear()
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(relativeLayout.context, "Error writing PDF: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Extension to convert Bitmap to ByteArray
    fun Bitmap.toByteArray(): ByteArray {
        val stream = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
}