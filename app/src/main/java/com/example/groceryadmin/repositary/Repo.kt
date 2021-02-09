package com.example.groceryadmin.repositary

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.groceryadmin.ModelClasses.Category
import com.example.groceryadmin.ModelClasses.Product
import com.example.groceryadmin.Util.CATEGORY_FIELD
import com.example.groceryadmin.Util.CATEGORY_TABLE
import com.example.groceryadmin.Util.PRODUCT_TABLE
import com.example.groceryadmin.callbackInterface.CallbackListener
import com.example.groceryadmin.callbackInterface.RepositoryCallback
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay

import kotlinx.coroutines.launch
import java.lang.reflect.Type
import java.net.URI


object Repo {
    private val db = FirebaseFirestore.getInstance()
    private val cloudStorage = FirebaseStorage.getInstance().reference.child("Images")
    private lateinit var context: Context
    private val categoryList = ArrayList<Category>()
    private val _productList=ArrayList<Product>()


    private fun addCategory(uri: Uri, category: Category, listener: NetworkRequest) {
        CoroutineScope(IO).launch {

            val fileRef = cloudStorage!!.child(System.currentTimeMillis().toString() + ".jpg")
            var uploadTask: StorageTask<*>
            uploadTask = fileRef.putFile(uri!!)
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                        listener.onSuccess(it.message.toString(), false)

                    }

                }
                return@Continuation fileRef.downloadUrl
            }).addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    val downloadUrl = task.result
                    val url = downloadUrl.toString()
                    category.imageUrl = url
                    db.collection(CATEGORY_TABLE)
                        .add(category).addOnSuccessListener {
                            db.collection(CATEGORY_TABLE).document(it.id)
                                .update(CATEGORY_FIELD, it.id)
                            listener.onSuccess("", true)
                        }
                        .addOnFailureListener {

                        }


                } else {


                }

            }

        }


    }


    fun getCategoryUploaded(category: Category, uri: Uri): MutableLiveData<Boolean> {
        val uploadAck = MutableLiveData<Boolean>()
        addCategory(uri, category, object : NetworkRequest {
            override fun onSuccess(message: String, isSuccess: Boolean) {
                if (isSuccess) {
                    uploadAck.value = isSuccess
                } else {
                    uploadAck.value = isSuccess
                }
            }

        })

        return uploadAck
    }


    fun getCategoryList(listener: CallbackListener): LiveData<List<Category>> {
        categoryList.clear()
        listener.onStart("s")
        val mutableList = MutableLiveData<List<Category>>()
        CoroutineScope(IO).launch {
            val categoryRef = db.collection(CATEGORY_TABLE)

            categoryRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    listener.onSuccess(mutableList)
                    for (document in task.result!!) {
                        val item: Category = document.toObject(Category::class.java)
                        categoryList.add(item)
                    }
                    mutableList.value = categoryList
                    Log.d("Data", "Run success")
                } else {
                    Log.d("Data", "Run fail")
                    categoryList.add(Category("Default"))
                    //  mutableList.value= emptyList()
                    listener.onFailuare(task.exception.toString())
                }

            }

        }




        return mutableList
    }

    fun removeCategory(categoryId: String, listener: RepositoryCallback<LiveData<Boolean>>): LiveData<Boolean> {
        val mutableBoolean = MutableLiveData<Boolean>()
        listener.onStart(mutableBoolean)
        CoroutineScope(IO).launch {

            val categoryRef = db.collection(CATEGORY_TABLE)
            categoryRef.document(categoryId).delete().addOnCompleteListener {
                if (it.isSuccessful) {
                    val ref = db.collection(PRODUCT_TABLE)
                    val query = ref.whereEqualTo(CATEGORY_FIELD, categoryId)
                        .get()
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                listener.onSuccess(mutableBoolean, "Sucess")
                                for (document in it.result!!) {
                                    ref.document(document.id).delete()
                                }
                                mutableBoolean.value = true
                            } else {
                                listener.onFailure(mutableBoolean, it.exception.toString())
                            }
                        }

                } else {
                    listener.onFailure(mutableBoolean, it.exception.toString())
                }

            }


        }

        return mutableBoolean

    }
   // Product Functions
    fun getProducts(listener: RepositoryCallback<LiveData<List<Product>>>):LiveData<List<Product>>{
        val mutabaleProduct=MutableLiveData<List<Product>>()
        listener.onStart(mutabaleProduct)
        CoroutineScope(IO).launch {
            _productList.clear()
            val productRef = db.collection(PRODUCT_TABLE)

            productRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    listener.onSuccess(mutabaleProduct,"Success")
                    for (document in task.result!!) {
                        val item: Product = document.toObject(Product::class.java)
                        _productList.add(item)
                    }
                    mutabaleProduct.value = _productList
                    Log.d("Data", "Run success")
                } else {
                    Log.d("Data", "Run fail")

                   listener.onFailure(mutabaleProduct,task.exception.toString())
                }

            }




        }

        return mutabaleProduct
    }

}

interface NetworkRequest {
    fun onSuccess(message: String, isSuccess: Boolean)
}