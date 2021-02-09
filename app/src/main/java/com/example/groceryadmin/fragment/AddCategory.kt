package com.example.groceryadmin.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.groceryadmin.ModelClasses.Category
import com.example.groceryadmin.R
import com.example.groceryadmin.viewmodel.CategoryViewModel
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_add_category.*
import kotlinx.android.synthetic.main.fragment_add_category.view.*
import java.util.*


class AddCategory : Fragment() {
    private lateinit var uploadButton: Button
    private lateinit var imageButton: Button
    lateinit var filePath: Uri
    private val REQUESTCODE: Int = 111
    private lateinit var storageRef: StorageReference
    private lateinit var progressBar: ProgressDialog
    private lateinit var categoryTitle: EditText
    private lateinit var categoryDesc: EditText
    private lateinit var rootView: View
    private lateinit  var viewModel: CategoryViewModel
    private var isSelecetIcon=false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.fragment_add_category, container, false)

        initUi()
        viewModel= activity?.let {
            ViewModelProvider(it).get(CategoryViewModel::class.java)
        }!!

       rootView.showImageView.setOnClickListener {
            selectImage()
        }
      rootView.uploadCategory.setOnClickListener {
          uploadCategory()
      }

        return rootView
    }

//    private fun addCategory(category: Category) {
//        val db = FirebaseFirestore.getInstance()
//        db.collection("Category")
//            .add(category).addOnSuccessListener {
//                Toast.makeText(activity, "Its Done.....", Toast.LENGTH_SHORT).show()
//                db.collection("Category").document(it.id).update("categoryId", it.id)
//                progressBar.dismiss()
//            }
//            .addOnFailureListener {
//
//            }
//    }

    private fun selectImage() {
        var i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(i, "Choose Picture"), REQUESTCODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUESTCODE && resultCode == Activity.RESULT_OK && data != null) {
            filePath = data.data!!
            isSelecetIcon=true
            var bitmap: Bitmap =
                MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, filePath)
            showImageView.setImageBitmap(bitmap)
        }
    }

   private fun uploadCategory(){
       if(isSelecetIcon){
           if(categoryTitle.text.isNotEmpty()&&categoryDesc.text.isNotEmpty()){
               val dialog=ProgressDialog(context)
               dialog.setTitle("Product Uploading.....")
               dialog.show()
               val category=Category(categoryTitle.text.toString(),categoryDesc.text.toString())
                viewModel.getMutableBoolean(category,filePath).observe(this, androidx.lifecycle.Observer {
                    if(it){
                        dialog.dismiss()
                    }else{
                        dialog.dismiss()
                        Toast.makeText(context, "Product Not Uploaded :(", Toast.LENGTH_SHORT).show()
                    }
                })
           }
       }else{
           Toast.makeText(context, "Please Select Picture First", Toast.LENGTH_SHORT).show()
       }

   }

//    private fun uploadImage() {
//
//        if (categoryTitle.text.trim().isNotEmpty() && categoryDesc.text.trim().isNotEmpty()) {
//            progressBar.setMessage("Uploading.....")
//            progressBar.show()
//            if (filePath != null) {
//                val fileRef = storageRef!!.child(System.currentTimeMillis().toString() + ".jpg")
//                var uploadTask: StorageTask<*>
//                uploadTask = fileRef.putFile(filePath!!)
//                uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
//                    if (!task.isSuccessful) {
//                        task.exception?.let {
//                            throw it
//                            progressBar.cancel()
//                            Toast.makeText(context, "task! success", Toast.LENGTH_SHORT).show()
//                        }
//
//                    }
//                    return@Continuation fileRef.downloadUrl
//                }).addOnCompleteListener { task ->
//
//                    if (task.isSuccessful) {
//                        progressBar.cancel()
//                        val downloadUrl = task.result
//                        val url = downloadUrl.toString()
//                        val category = Category(
//                            categoryTitle.text.toString(),
//                            categoryDesc.text.toString(),
//                            url
//                        )
//                        addCategory(category)
//
//                    } else {
//                        progressBar.cancel()
//                        Toast.makeText(
//                            context,
//                            "Complete Listener ${task.exception.toString()}",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//
//                }
//
//
//            } else {
//                Toast.makeText(context, "Select Image First", Toast.LENGTH_SHORT).show()
//                progressBar.cancel()
//            }
//
//        } else {
//            Toast.makeText(activity, "Fill Category Information", Toast.LENGTH_SHORT).show()
//        }
//
//
//    }

    private fun initUi() {
        categoryTitle = rootView.categoryTitle
        categoryDesc = rootView.categoryDesc
        imageButton = rootView.findViewById(R.id.uploadImage)
        uploadButton = rootView.findViewById(R.id.uploadCategory)
        storageRef = FirebaseStorage.getInstance().reference.child("Images")
        progressBar = ProgressDialog(context)
    }


}

 
