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
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.NumberPicker.OnValueChangeListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.groceryadmin.ModelClasses.Category
import com.example.groceryadmin.ModelClasses.Product
import com.example.groceryadmin.R
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.fragment_add_product.view.*


class AddProduct : Fragment() {

    lateinit var filePath: Uri
    private val REQUESTCODE: Int = 8111
    private lateinit var storageRef: StorageReference
    private lateinit var progressBar: ProgressDialog
    private lateinit var documentList: ArrayList<String>
    private lateinit var wheelView: NumberPicker
    private lateinit var categories: List<Category>
    private lateinit var rootView: View
    private var isImageSelect: Boolean? = null
    private lateinit var titleEdittext: EditText
    private lateinit var descEdittext: EditText
    private var categoryId:String=""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_add_product, container, false)
        initUi()

        getCategoriesId()
        rootView.uploadImage.setOnClickListener {
            selectImage()
        }

        rootView.uploadProduct.setOnClickListener {
            progressBar.setTitle("Checking..")
            progressBar.show()
           if(isImageSelect==true){
               uploadImage(object : OnUpdate {
                   override fun onSucces(message: String) {
                       if (message == "Success") {
                           progressBar.cancel()
                           Toast.makeText(activity, "$message", Toast.LENGTH_SHORT).show()
                       } else {
                           progressBar.cancel()
                           Toast.makeText(activity, "There is Issue... $message", Toast.LENGTH_SHORT).show()
                       }
                   }

               })
           }else{
               progressBar.cancel()
               Toast.makeText(activity, "Select Product Image", Toast.LENGTH_SHORT).show()
           }



        }


        return rootView
    }

    private fun addProduct(product: Product, listener: OnUpdate) {
        val db = FirebaseFirestore.getInstance()
        db.collection("Product")
            .add(product).addOnSuccessListener {
                Toast.makeText(activity, "Its Done.....", Toast.LENGTH_SHORT).show()
                listener.onSucces("Success")
            }
            .addOnFailureListener {
                listener.onSucces("Error ${it.message.toString()}")
            }
    }

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
            var bitmap: Bitmap =
                MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, filePath)
            rootView.uploadImage.setImageBitmap(bitmap)
            isImageSelect = true
        }
    }

    private fun uploadImage(listener: OnUpdate) {

        if(titleEdittext.text.trim().isNotEmpty() && descEdittext.text.trim().isNotEmpty()){
            storageRef = FirebaseStorage.getInstance().reference.child("Images")
            progressBar.setMessage("Uploading.....")
            progressBar.show()
            if (filePath != null) {
                val fileRef = storageRef!!.child(System.currentTimeMillis().toString() + ".jpg")
                var uploadTask: StorageTask<*>
                uploadTask = fileRef.putFile(filePath!!)
                uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                            //progressBar.cancel()
                            Toast.makeText(context, "task! success", Toast.LENGTH_SHORT).show()
                            listener.onSucces("Error")

                        }

                    }
                    return@Continuation fileRef.downloadUrl
                }).addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        progressBar.cancel()
                        val downloadUrl = task.result
                        val url = downloadUrl.toString()
                        addProduct(
                            Product(titleEdittext.text.toString(), descEdittext.text.toString(), url, categoryId, false, 0),
                            listener
                        )

                    } else {
                        listener.onSucces("Error")
                        Toast.makeText(
                            context,
                            "Complete Listener ${task.exception.toString()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }


            } else {
                Toast.makeText(context, "Select Image First", Toast.LENGTH_SHORT).show()
                progressBar.cancel()
            }


        } else{
            Toast.makeText(activity, "Enter Product Details", Toast.LENGTH_SHORT).show()
            listener.onSucces("Error")
        }



    }

    private fun getCategoriesId() {
        documentList = ArrayList()
        categories = ArrayList()
        val db = FirebaseFirestore.getInstance()
        val categoryRef = db.collection("Category")
        categoryRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    val item = document.toObject(Category::class.java)
                    (categories as ArrayList<Category>).add(item)
                    documentList.add(item.title)


                }


            }
            setWheelView(documentList)
        }

    }

    private fun setWheelView(items: ArrayList<String>) {
        categoryId=categories[0].categoryId
        val values = arrayOfNulls<String>(items.size)
        items.toArray(values)
        wheelView.minValue = 0
        wheelView.value = 2
        wheelView.maxValue = (values.size - 1)
        wheelView.wrapSelectorWheel = false
        wheelView.displayedValues = values
        val myValChangedListener =
            OnValueChangeListener { picker, oldVal, newVal ->
                Toast.makeText(
                    activity,
                    "Selected: ${categories.get(newVal).categoryId}",
                    Toast.LENGTH_SHORT
                )
                    .show()
                categoryId=categories[newVal].categoryId
            }

        wheelView.setOnValueChangedListener(myValChangedListener);
    }


    private interface OnUpdate {
        fun onSucces(message: String)
    }

    private fun initUi() {
        wheelView = rootView.NumberPicker
        isImageSelect = false
        titleEdittext=rootView.title
        descEdittext=rootView.description
        progressBar = ProgressDialog(context)
    }

}


