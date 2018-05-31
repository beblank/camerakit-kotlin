package id.mncplay.cameratest

import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.util.Log
import id.mncplay.cameratest.commons.RxBaseActivity
import rebus.permissionutils.PermissionEnum
import rebus.permissionutils.PermissionManager
import java.io.File

class MainActivity : RxBaseActivity() {

    val dir = "/Pictures/Camera/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null){
            checkDir()
            getPermission()
            changeFragment(CameraFragment(), "camera")
        }
    }

    fun changeFragment(f: Fragment, tag: String) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_content, f, tag)
                .addToBackStack(null)
                .commitAllowingStateLoss()
    }

    fun checkDir(){
        val imgDir = File(Environment.getExternalStorageDirectory(), dir)
        if (!imgDir.exists()) {
            imgDir.mkdirs()
            Log.d("dodol","Directory created")
        }
    }

    fun getPermission(){
        PermissionManager.Builder()
                .permission(
                        PermissionEnum.WRITE_EXTERNAL_STORAGE,
                        PermissionEnum.CAMERA)
                .askAgain(false)
                .ask(this)
    }
}
