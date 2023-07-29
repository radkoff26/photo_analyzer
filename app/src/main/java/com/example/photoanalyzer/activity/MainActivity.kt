package com.example.photoanalyzer.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.fragment.NavHostFragment
import com.example.core_navigation.NavigationController
import com.example.core_navigation.NavigationRoot
import com.example.photoanalyzer.R
import com.example.photoanalyzer.navigation.NavigationControllerImpl

class MainActivity : AppCompatActivity(), NavigationRoot {

    override lateinit var navigationController: NavigationController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkPermissionAndManageActivityOnResult()
    }

    private fun checkPermissionAndManageActivityOnResult() {
        if (!isReadPermissionGranted()) {
            if (
                ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                showRationale()
            } else {
                requestPermission()
            }
        } else {
            initContentViewAndController()
        }
    }

    private fun isReadPermissionGranted(): Boolean =
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            REQUEST_READ_PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_READ_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initContentViewAndController()
            } else {
                showToast()
                finish()
            }
        }
    }

    private fun initContentViewAndController() {
        setContentView(R.layout.activity_main)
        val navController = findViewById<FragmentContainerView>(R.id.nav_host_fragment)
            .getFragment<NavHostFragment>()
            .navController
        navigationController = NavigationControllerImpl(navController)
    }

    private fun showRationale() {
        AlertDialog.Builder(this)
            .setTitle(R.string.permission)
            .setMessage(R.string.grant_permission_message)
            .setPositiveButton(R.string.grant) { _, _ ->
                requestPermission()
            }
            .setNegativeButton(R.string.deny) { _, _ ->
                showToast()
                finish()
            }
            .create().show()
    }

    private fun showToast() {
        Toast.makeText(this, R.string.needs_permission_message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUEST_READ_PERMISSION_CODE = 1001
    }
}
