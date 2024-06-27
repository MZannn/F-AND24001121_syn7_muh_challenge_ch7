package com.example.movieapplication.presentation.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.domain.model.User
import com.example.movieapplication.worker.BlurWorker
import com.example.movieapplication.databinding.FragmentProfileBinding

import com.example.movieapplication.presentation.viewModel.AuthViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val authViewModel: AuthViewModel by inject()
    private var userPhoto: String? = null
    private val REQUEST_CODE = 100

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)

        var userId: Int? = null

        lifecycleScope.launch {
            authViewModel.userFlow.collect { user ->
                user?.let {
                    userId = user.id
                    binding.etUsername.setText(user.username)
                    binding.etAddress.setText(user.address)
                    binding.etBirthdate.setText(user.birthdate)
                    binding.etFullname.setText(user.fullname)
                    if (user.photo != "") {
                        user.photo?.let {
                            binding.imageProfile.setImageBitmap(Base64.decode(it, Base64.DEFAULT).let { bytes ->
                                BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                            })
                            binding.imageProfile.background = null
                        }
                    }

                }
            }
        }

        binding.btnUpdate.setOnClickListener {
            val id = userId ?: return@setOnClickListener
            val updatedUser = User(
                id = id,
                username = binding.etUsername.text.toString(),
                fullname = binding.etFullname.text.toString(),
                birthdate = binding.etBirthdate.text.toString(),
                address = binding.etAddress.text.toString(),
                photo = userPhoto ?: null,
                email = "",
                password = ""
            )
            lifecycleScope.launch {
                authViewModel.updateUser(
                    updatedUser.username,
                    updatedUser.fullname ?: "",
                    updatedUser.birthdate ?: "",
                    updatedUser.address ?: "",
                    updatedUser.photo?:null,
                    updatedUser.id ?: 0
                )
            }
        }

        binding.btnLogout.setOnClickListener {
            lifecycleScope.launch {
                authViewModel.clearUser()
            }
            val action = ProfileFragmentDirections.actionProfileFragmentToLoginFragment()
            findNavController().navigate(action)
        }

        binding.imageProfile.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_CODE
                )
            } else {
                openCamera()
            }
        }

        return binding.root
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val photo = data?.extras?.get("data") as Bitmap
            binding.imageProfile.setImageBitmap(photo)
            binding.imageProfile.background = null
            saveImageToLocal(photo)
            userPhoto = bitmapToBase64(photo)
        }
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return stream.toByteArray()
    }

    private fun startBlurWorker(imagePath: String) {
        val inputData = Data.Builder()
            .putString("image_path", imagePath)
            .build()

        val blurRequest = OneTimeWorkRequest.Builder(BlurWorker::class.java)
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(requireContext()).enqueue(blurRequest)
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArray = bitmapToByteArray(bitmap)
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun saveImageToLocal(bitmap: Bitmap) {
        val filename = "profile_${System.currentTimeMillis()}.jpg"
        val file = File(requireContext().getExternalFilesDir(null), filename)
        val fos = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
        fos.close()
    }
}
