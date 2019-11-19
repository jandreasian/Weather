package com.jandreasian.weatherapplication.overview

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer

import com.jandreasian.weatherapplication.R
import com.jandreasian.weatherapplication.databinding.OverviewFragmentBinding
import com.jandreasian.weatherapplication.network.GpsUtils
import kotlinx.android.synthetic.main.overview_fragment.*

const val LOCATION_REQUEST = 100
const val GPS_REQUEST = 101

class OverviewFragment : Fragment() {

    private val viewModel: OverviewViewModel by lazy {
        ViewModelProviders.of(this).get(OverviewViewModel::class.java)
    }

    private var isGPSEnabled = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = OverviewFragmentBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.setLifecycleOwner(this)

        // Giving the binding access to the OverviewViewModel
        binding.viewModel = viewModel

        GpsUtils(requireContext()).turnGPSOn(object : GpsUtils.OnGpsListener {

            override fun gpsStatus(isGPSEnable: Boolean) {
                this@OverviewFragment.isGPSEnabled = isGPSEnable
            }
        })

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        invokeLocationAction()
    }

    private fun isPermissionsGranted() =
        checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

    private fun shouldShowRequestPermissionRationale() =
        ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) && ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST -> {
                invokeLocationAction()
            }
        }
    }

    private fun invokeLocationAction() {
        when {
            !isGPSEnabled -> latLong.text = getString(R.string.enable_gps)

            isPermissionsGranted() -> startLocationUpdate()

            shouldShowRequestPermissionRationale() -> latLong.text = getString(R.string.permission_request)

            else -> ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_REQUEST
            )
        }
    }

    private fun startLocationUpdate() {
        viewModel.getLocationData().observe(this, Observer {
            latLong.text =  getString(R.string.latLong, it.longitude, it.latitude)
            Log.d("MainActivity", getString(R.string.latLong, it.longitude, it.latitude))
        })
    }
}
