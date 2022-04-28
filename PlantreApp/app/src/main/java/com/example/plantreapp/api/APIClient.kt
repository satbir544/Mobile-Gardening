package com.example.plantreapp.api

import android.content.Context
import androidx.core.content.edit
import com.example.plantreapp.entities.PlantInfo
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLSession

data class  User(
    val username: String,
    val firstname: String?,
    val lastname: String?,
    val email: String,
    val password: String,
        )
data class ResponsePlants(
    val data: List<PlantInfo>
)

data class ResponseLoginUser (
    val secret_token: String,
    val user: User
        )

val JSON: MediaType = "application/json; charset=utf-8".toMediaType()

class APIClient(context: Context) {
    private val base_url = "https://plantre.azurewebsites.net"
    private var context : Context? = null;
    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    private val userJsonAdapter = moshi.adapter<User>(User::class.java)
    private val plantJsonAdapter = moshi.adapter<ResponsePlants>(ResponsePlants::class.java)
    private val loginJsonAdapter = moshi.adapter<ResponseLoginUser>(ResponseLoginUser::class.java)
    init {
        this.context = context
    }

    suspend fun loginUser(email: String, password: String) = withContext(Dispatchers.IO) {
        var isLoggedIn = CompletableDeferred<Boolean>()
        val json = "{\"email\":\"$email\",\"password\":\"$password\"}";
        val body = json.toRequestBody(JSON);
        val request = Request.Builder().url("$base_url/api/user/login").addHeader("Connection", "close").post(body).build()

        instance?.newCall(request)?.execute().use { response ->
            if (response != null) {
                if (response.isSuccessful) {
                    val res = loginJsonAdapter.fromJson(response.body!!.source())
                    if (res != null) {
                        val sharedPrefs = context?.getSharedPreferences("prefs", Context.MODE_PRIVATE)
                        if (sharedPrefs != null) {
                            sharedPrefs?.edit(){
                                putString("secret_token", res.secret_token)
                                commit()
                                isLoggedIn.complete(true)
                            }
                        } else {
                            isLoggedIn.complete(false)
                        }
                    } else {
                        isLoggedIn.complete(false)
                    }
                } else {
                    isLoggedIn.complete(false)
                }
            } else {
                isLoggedIn.complete(false)
            }
        }

        return@withContext isLoggedIn.await();
    }

    suspend fun registerUser(user: User) = withContext(Dispatchers.IO)  {
        var userComplete = CompletableDeferred<User?>()
        val body = userJsonAdapter.toJson(user).toRequestBody(JSON)
        val request = Request.Builder().url("$base_url/api/user/register").addHeader("Connection", "close").post(body).build()

        instance?.newCall(request)?.execute().use { response ->
            if (response != null) {
                if (response.isSuccessful) {
                    val res = userJsonAdapter.fromJson(response.body!!.source())
                    if (res != null) {
                        userComplete.complete(res);
                    }
                }
            }
        }

        return@withContext userComplete.await();
    }

    suspend fun logoutUser() {

    }

    suspend fun activeUser() {

    }



    suspend fun loadPlants() = withContext(Dispatchers.IO) {
        var list = CompletableDeferred<List<PlantInfo>>();

            val request = Request.Builder().url("$base_url/api/plant").build()
            try {
            instance?.newCall(request)?.execute().use { response ->
                if (response != null) {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                        val res = plantJsonAdapter.fromJson(response.body!!.source())
                        if (res != null) {

                            list.complete(res.data)
                            println("Great Success")

                        } else {
                            list.complete(emptyList())
                        }



                } else {
                    list.complete(emptyList())
                }
            }
            } catch (e: IOException) {
                list.complete(emptyList())
            }
        return@withContext list.await();
    }

    companion object {
        @Volatile private var instance: OkHttpClient? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildClient().also { instance = it}
        }

        private fun buildClient() = OkHttpClient.Builder().connectTimeout(5, TimeUnit.MINUTES) // connect timeout
            .writeTimeout(5, TimeUnit.MINUTES) // write timeout
            .readTimeout(5, TimeUnit.MINUTES)
            .hostnameVerifier(HostnameVerifier() {s: String?, sslSession: SSLSession? ->
                val hv: Boolean = HttpsURLConnection.getDefaultHostnameVerifier().verify("https://plantre.azurewebsites.net", sslSession)
                return@HostnameVerifier true // This needs to change before deploy app - Allows for man in the middle attacks
            })
            .build()
    }
}

//.hostnameVerifier(HostnameVerifier() {s: String?, sslSession: SSLSession? ->
//    val hv: HostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier()
//    return@HostnameVerifier true // This needs to change before deploy app - Allows for man in the middle attacks
//})