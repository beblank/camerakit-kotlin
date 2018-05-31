package id.mncplay.cameratest


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.mncplay.cameratest.commons.RxBaseFragment
import kotlinx.android.synthetic.main.fragment_camera.*


/**a
 * A simple [Fragment] subclass.
 *
 */
class CameraFragment : RxBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onResume() {
        super.onResume()
        camera.start()
    }

    override fun onPause() {
        camera.stop()
        super.onPause()
    }


}
