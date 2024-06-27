package com.example.movieapplication.worker

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.io.FileOutputStream

class BlurWorker(context: Context, workerParams: WorkerParameters): Worker(context, workerParams){
    override fun doWork(): Result {
        val imagePath = inputData.getString("image_path") ?: return Result.failure()
        return try {
            val bitmap = BitmapFactory.decodeFile(imagePath)
            val blurredBitmap = blurBitmap(bitmap, applicationContext)
            saveBitmapToFile(blurredBitmap, imagePath)
            Result.success()
        } catch (e: Exception) {
            Log.e("BlurWorker", "Error blurring image", e)
            Result.failure()
        }
    }
    private fun blurBitmap(bitmap: Bitmap, context: Context): Bitmap {
        val outputBitmap = Bitmap.createBitmap(bitmap)
        val renderScript = RenderScript.create(context)
        val tmpIn = Allocation.createFromBitmap(renderScript, bitmap)
        val tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap)

        val theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
        theIntrinsic.setRadius(10f)
        theIntrinsic.setInput(tmpIn)
        theIntrinsic.forEach(tmpOut)
        tmpOut.copyTo(outputBitmap)

        renderScript.destroy()
        return outputBitmap
    }
    private fun saveBitmapToFile(bitmap: Bitmap, path: String) {
        val fos = FileOutputStream(path)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
        fos.close()
    }

}