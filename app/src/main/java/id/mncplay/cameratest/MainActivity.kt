package id.mncplay.cameratest

import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.util.Log
import id.mncplay.cameratest.commons.RxBaseActivity
import id.mncplay.cameratest.commons.RxBus
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
            manageSubscription()
            changeFragment(CameraFragment(), "camera")
        }
    }

    fun manageSubscription(){
        subscriptions.add(RxBus.get().toObservable().subscribe{
            event -> manageBus(event)
        })
    }

    fun manageBus(event:Any){
        when(event){
            is Data -> processData(event)
        }
    }

    fun processData(event:Data){
        Log.d("dodol", "$event")
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
