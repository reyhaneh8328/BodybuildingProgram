package com.example.bodybuildingprogram

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
//import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.renderscript.ScriptGroup
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.Toast
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bodybuildingprogram.databinding.ActivityPlanBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


import android.view.*
import android.widget.*
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.property.UnitValue
import java.io.ByteArrayOutputStream


class PdfGenerator(private val context: Context) {


    fun createMultiPagePdfWithoutGap(
        relativeLayout: RelativeLayout,
        recyclerView: RecyclerView,
        fileName: String,
        profileRl : RelativeLayout
    ) {
        relativeLayout.post {
            val viewWidth = recyclerView.width

            val scrollView = ScrollView(relativeLayout.context).apply {
                layoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    addRule(RelativeLayout.BELOW, profileRl.id)
                }
            }

            val linearLayout = LinearLayout(relativeLayout.context).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
            }

            scrollView.addView(linearLayout)

            val adapter = recyclerView.adapter
            val layoutManager = recyclerView.layoutManager

            if (adapter != null && layoutManager != null) {
                for (i in 0 until adapter.itemCount) {
                    val viewHolder = adapter.createViewHolder(recyclerView, adapter.getItemViewType(i))
                    adapter.onBindViewHolder(viewHolder, i)
                    val listItem = viewHolder.itemView
                    if (listItem.parent != null) {
                        (listItem.parent as ViewGroup).removeView(listItem)
                    }
                    listItem.measure(
                        View.MeasureSpec.makeMeasureSpec(viewWidth, View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                    )
                    listItem.layout(0, 0, listItem.measuredWidth, listItem.measuredHeight)
                    linearLayout.addView(listItem)
                }
            }

            relativeLayout.removeView(recyclerView)
            relativeLayout.addView(scrollView)

            scrollView.post {
                scrollView.measure(
                    View.MeasureSpec.makeMeasureSpec(viewWidth, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                )
                val totalHeight = profileRl.measuredHeight + scrollView.measuredHeight

                if (viewWidth <= 0 || totalHeight <= 0) {
                    Toast.makeText(relativeLayout.context, "Invalid dimensions for PDF", Toast.LENGTH_SHORT).show()
                    return@post
                }

                val directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path
                val file = File(directoryPath, "$fileName.pdf")

                try {
                    val pdfWriter = PdfWriter(FileOutputStream(file))
                    val pdfDocument = PdfDocument(pdfWriter)
                    val document = Document(pdfDocument)

                    // بخش اول: افزودن profileRl به عنوان صفحه اول
                    val profileBitmap = Bitmap.createBitmap(viewWidth, profileRl.measuredHeight, Bitmap.Config.ARGB_8888)
                    val profileCanvas = Canvas(profileBitmap)
                    profileRl.draw(profileCanvas)

                    val profileImage = Image(com.itextpdf.io.image.ImageDataFactory.create(profileBitmap.toByteArray()))
                    profileImage.setWidth(UnitValue.createPercentValue(100f))
                    document.add(profileImage)

                    // بخش دوم: افزودن عناصر اسکرول ویو به صفحات بعدی بدون فاصله
                    val pdfHeight = 1120
                    var currentHeight = 0

                    // دریافت محتوای ScrollView به صورت دقیق و حذف فاصله‌ها
                    for (i in 0 until linearLayout.childCount) {
                        val item = linearLayout.getChildAt(i)
                        item.measure(
                            View.MeasureSpec.makeMeasureSpec(viewWidth, View.MeasureSpec.EXACTLY),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                        )
                        item.layout(0, 0, item.measuredWidth, item.measuredHeight)

                        // محاسبه ارتفاع دقیق این عنصر
                        val itemHeight = item.measuredHeight
                        var itemCurrentHeight = 0

                        while (itemCurrentHeight < itemHeight) {
                            val pageHeight = minOf(pdfHeight, itemHeight - itemCurrentHeight)

                            val pageBitmap = Bitmap.createBitmap(viewWidth, pageHeight, Bitmap.Config.ARGB_8888)
                            val pageCanvas = Canvas(pageBitmap)
                            pageCanvas.translate(0f, -itemCurrentHeight.toFloat())
                            item.draw(pageCanvas)

                            val pageImage = Image(com.itextpdf.io.image.ImageDataFactory.create(pageBitmap.toByteArray()))
                            pageImage.setWidth(UnitValue.createPercentValue(100f))
                            document.add(pageImage)

                            itemCurrentHeight += pageHeight
                        }
                    }

                    document.close()
                    pdfDocument.close()
                    Toast.makeText(relativeLayout.context, "PDF created successfully at: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(relativeLayout.context, "Error writing PDF: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Extension to convert Bitmap to ByteArray
    fun Bitmap.toByteArray(): ByteArray {
        val stream = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }





//    fun createMultiPagePdfFromRelativeLayoutWithRecyclerView(
//        relativeLayout: RelativeLayout,
//        recyclerView: RecyclerView,
//        fileName: String, binding: ActivityPlanBinding
//    ) {
//        relativeLayout.post {
//            val viewWidth = recyclerView.width
//
//            val scrollView = ScrollView(relativeLayout.context).apply {
//                layoutParams = RelativeLayout.LayoutParams(
//                    RelativeLayout.LayoutParams.MATCH_PARENT,
//                    RelativeLayout.LayoutParams.WRAP_CONTENT
//                ).apply {
//                    addRule(RelativeLayout.BELOW, binding.profileRl.id)
//                }
//            }
//
//            val linearLayout = LinearLayout(relativeLayout.context).apply {
//                orientation = LinearLayout.VERTICAL
//                layoutParams = RelativeLayout.LayoutParams(
//                    RelativeLayout.LayoutParams.MATCH_PARENT,
//                    RelativeLayout.LayoutParams.WRAP_CONTENT
//                )
//            }
//
//            scrollView.addView(linearLayout)
//
//            val adapter = recyclerView.adapter
//            val layoutManager = recyclerView.layoutManager
//
//            if (adapter != null && layoutManager != null) {
//                for (i in 0 until adapter.itemCount) {
//                    val viewHolder = adapter.createViewHolder(recyclerView, adapter.getItemViewType(i))
//                    adapter.onBindViewHolder(viewHolder, i)
//                    val listItem = viewHolder.itemView
//                    if (listItem.parent != null) {
//                        (listItem.parent as ViewGroup).removeView(listItem)
//                    }
//                    listItem.measure(
//                        View.MeasureSpec.makeMeasureSpec(viewWidth, View.MeasureSpec.EXACTLY),
//                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
//                    )
//                    listItem.layout(0, 0, listItem.measuredWidth, listItem.measuredHeight)
//                    linearLayout.addView(listItem)
//                }
//            }
//
//            relativeLayout.removeView(recyclerView)
//            relativeLayout.addView(scrollView)
//
//            scrollView.post {
//                scrollView.measure(
//                    View.MeasureSpec.makeMeasureSpec(viewWidth, View.MeasureSpec.EXACTLY),
//                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
//                )
//                val totalHeight = binding.profileRl.measuredHeight + scrollView.measuredHeight
//
//                if (viewWidth <= 0 || totalHeight <= 0) {
//                    Toast.makeText(relativeLayout.context, "Invalid dimensions for PDF", Toast.LENGTH_SHORT).show()
//                    return@post
//                }
//
//                val pdfDocument = PdfDocument()
//                val pdfHeight = 1120
//                var pageNumber = 1
//                var currentHeight = 0
//
//                // رسم profileRl به عنوان صفحه اول
//                val profileBitmap = Bitmap.createBitmap(viewWidth, binding.profileRl.measuredHeight, Bitmap.Config.ARGB_8888)
//                val profileCanvas = Canvas(profileBitmap)
//                binding.profileRl.draw(profileCanvas)
//
//                val profilePageInfo = PdfDocument.PageInfo.Builder(viewWidth, binding.profileRl.measuredHeight, pageNumber).create()
//                val profilePage = pdfDocument.startPage(profilePageInfo)
//                profilePage.canvas.drawBitmap(profileBitmap, 0f, 0f, null)
//                pdfDocument.finishPage(profilePage)
//
//                // حرکت به صفحه‌های بعدی و رسم ScrollView
//                pageNumber++
//                currentHeight = 0 // بازنشانی currentHeight برای scrollView
//
//                while (currentHeight < scrollView.measuredHeight) {
//                    val pageBitmap = Bitmap.createBitmap(viewWidth, pdfHeight, Bitmap.Config.ARGB_8888)
//                    val pageCanvas = Canvas(pageBitmap)
//
//                    pageCanvas.translate(0f, -currentHeight.toFloat())
//                    scrollView.draw(pageCanvas)
//
//                    val pageInfo = PdfDocument.PageInfo.Builder(viewWidth, pdfHeight, pageNumber).create()
//                    val page = pdfDocument.startPage(pageInfo)
//                    page.canvas.drawBitmap(pageBitmap, 0f, 0f, null)
//                    pdfDocument.finishPage(page)
//
//                    pageNumber++
//                    currentHeight += pdfHeight
//                }
//
//                val directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path
//                val file = File(directoryPath, "$fileName.pdf")
//                try {
//                    pdfDocument.writeTo(FileOutputStream(file))
//                    pdfDocument.close()
//                    Toast.makeText(relativeLayout.context, "PDF created successfully at: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                    Toast.makeText(relativeLayout.context, "Error writing PDF: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }

//    fun createPDFFromView(view: View, fileName: String) {
//        // ایجاد یک PdfDocument
//        val document = PdfDocument()
//
//        // تعیین اطلاعات صفحه، سایز صفحه همان سایز View خواهد بود
//        val pageInfo = PageInfo.Builder(view.measuredWidth, view.measuredHeight, 1).create()
//        val page = document.startPage(pageInfo)
//
//        // رسم View بر روی Canvas
//        val canvas = page.canvas
//        view.draw(canvas)
//
//        // پایان صفحه
//        document.finishPage(page)
//
//        // ذخیره کردن فایل در پوشه Downloads
//        val directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path
//        val file = File(directoryPath, "$fileName.pdf")
//
//        try {
//            document.writeTo(FileOutputStream(file))
//            Toast.makeText(context, "PDF created successfully at: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
////            println("PDF created successfully at: ${file.absolutePath}")
//        } catch (e: IOException) {
//            e.printStackTrace()
//            Toast.makeText(context, "Error writing PDF: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
////            println("Error writing PDF: ${e.localizedMessage}")
//        }
//
//        // بستن سند PDF
//        document.close()
//    }
}