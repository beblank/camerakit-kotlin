package id.mncplay.cameratest

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.support.v7.widget.LinearLayoutCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import com.wonderkiln.camerakit.CameraView
import kotlinx.android.synthetic.main.camera_controls.view.*
import kotlinx.android.synthetic.main.fragment_camera.view.*
import com.wonderkiln.camerakit.CameraKit
import android.view.animation.OvershootInterpolator
import android.support.annotation.DrawableRes
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.wonderkiln.camerakit.CameraKitImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DateFormat
import java.util.*


open class CameraControls @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    private var cameraViewId = -1
    private var coverViewId = -1
    private var cameraView:CameraView? = null
    val dir = "/Pictures/Camera/"

    init {
        LayoutInflater.from(context)
                .inflate(R.layout.camera_controls, this, true)
        orientation = HORIZONTAL

        if (attrs != null) {
            val a = context.theme.obtainStyledAttributes(
                    attrs,
                    R.styleable.CameraControls,
                    0, 0)
            try {
                cameraViewId = a.getResourceId(R.styleable.CameraControls_camera, -1)
                coverViewId = a.getResourceId(R.styleable.CameraControls_cover, -1)
            } finally {
                a.recycle()
            }
        }

        captureButton.setOnClickListener { onTouchCapture() }
        flashButton.setOnClickListener { onTouchFlash(it) }
        facingButton.setOnClickListener { onTouchFacing(it) }
    }

    private fun onTouchFacing(view: View) {
        if (cameraView!!.isFacingFront()) {
            cameraView?.setFacing(CameraKit.Constants.FACING_BACK)
            changeViewImageResource(view as ImageView, R.drawable.ic_facing_front)
        } else {
            cameraView?.setFacing(CameraKit.Constants.FACING_FRONT)
            changeViewImageResource(view as ImageView, R.drawable.ic_facing_back)
        }
    }

    private fun onTouchFlash(view:View) {
        if (cameraView?.getFlash() === CameraKit.Constants.FLASH_OFF) {
            cameraView?.setFlash(CameraKit.Constants.FLASH_ON)
            changeViewImageResource(view as ImageView, R.drawable.ic_flash_on)
        } else {
            cameraView?.setFlash(CameraKit.Constants.FLASH_OFF)
            changeViewImageResource(view as ImageView, R.drawable.ic_flash_off)
        }
    }

    private fun onTouchCapture() {
        cameraView?.captureImage {
            imageCapture(it)
        }
    }

    private fun imageCapture(image: CameraKitImage) {
        val jpeg = image.bitmap
        val photo = File(Environment.getExternalStorageDirectory(), dir + getImgName())
        try {
            val fOut = FileOutputStream(photo)
            jpeg.compress(Bitmap.CompressFormat.JPEG, 25, fOut)
            fOut.flush()
            fOut.close()
        } catch (e:IOException){
            Log.e("dodol", "Exception in photo capture to image $e")
        }
        Log.d("dodol", "${getDateTime()} - $jpeg")
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (cameraViewId !== -1) {
            val view = rootView.camera
            if (view is CameraView) {
                cameraView = view
                cameraView?.bindCameraKitListener(this)
                setFacingImageBasedOnCamera()
            }
        }

    }

    private fun setFacingImageBasedOnCamera() {
        if (cameraView!!.isFacingFront()) {
            facingButton.setImageResource(R.drawable.ic_facing_back);
        } else {
            facingButton.setImageResource(R.drawable.ic_facing_front);
        }
    }

    fun getImgName():String{
        return "Cam"+randNumber() + ".jpg"
    }
    fun randNumber():Int{
        return Random().nextInt(10000) + 65
    }

    fun getDateTime(): String {
        val currentTime = DateFormat.getDateTimeInstance().format(Date())
        currentTime.toString()
        if (currentTime == "") {
            Log.d("dodol","Failed to get Date & Time. Please retake a Picture!")
        }
        return currentTime
    }

    fun changeViewImageResource(imageView: ImageView, @DrawableRes resId: Int) {
        imageView.setRotation(0.toFloat())
        imageView.animate()
                .rotationBy(360.toFloat())
                .setDuration(400)
                .setInterpolator(OvershootInterpolator())
                .start()

        imageView.postDelayed({ imageView.setImageResource(resId) }, 120)
    }
}