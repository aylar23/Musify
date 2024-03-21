package com.musify.app.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.musify.app.domain.models.CodeVerification
import com.musify.app.domain.models.User
import com.musify.app.domain.service.UserService
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject


class UserRepository @Inject constructor(
    private val userService: UserService,

) {
    suspend fun login(number: String) {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(PHONE, number)
            .build()
        return userService.login(requestBody)
    }

    suspend fun verifyOTPAndProceed(phone: String, otpCode: String): Boolean {

        val tokens = userService.verifyOtp(
            CodeVerification(
                phone = phone,
                otp = otpCode
            )
        )

//        with(sharedPreferenceHelper.sharedPreferences.edit()) {
//            putString(ACCESSTOKEN, tokens.access)
//            putString(REFRESHTOKEN, tokens.refresh)
//            apply()
//        }
        val userDetails = getAndSaveUserDetail()
        return !userDetails.firstLogin

    }


    suspend fun getAndSaveUserDetail(): User {

        val userDetails = userService.getProfileInfo()


//        with(sharedPreferenceHelper.sharedPreferences.edit()) {
//            putString(USERNAME, userDetails.fullName)
//            putString(PHONE, userDetails.phone)
//            putLong(BALANCE, userDetails.balance)
//            putString(INVITATION_ID, userDetails.invitationId)
//            apply()
//        }

        return userDetails
    }



    suspend fun profileUpdate(
        name: String,
        birthday:String,
    ): User {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(NAME, name)
            .addFormDataPart(BIRTHDAY, birthday)
            .build()
        return userService.profileUpdate(requestBody)
    }


    fun getCurrentUser(): User {

        return User(
            "Emir",
            "",
             true,
            true
        )

    }


    suspend fun logOutCurrentUser() {
//        val preferences = sharedPreferenceHelper.sharedPreferences
//        with(preferences.edit()) {
//            clear()
//            commit()
//        }

    }

    companion object {
        const val PHONE = "phone"
        const val NAME = "full_name"
        const val BIRTHDAY = "birth_day"


    }
}
