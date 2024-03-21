package com.musify.app.presentation.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musify.app.data.datastore.PreferenceDataStoreConstants.TOKEN_KEY
import com.musify.app.data.datastore.PreferenceDataStoreHelper
import com.musify.app.domain.models.User
import com.musify.app.domain.repository.UserRepository
import com.musify.app.ui.utils.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class VerificationViewModel @Inject constructor(
//    private val userRepository: UserRepository,
    private val preferenceDataStoreHelper: PreferenceDataStoreHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState


    fun loginUser(phone: String) {
        _uiState.update { it.updateToLoading() }
        viewModelScope.launch {
            try {
//                userRepository.login(phone)
                _uiState.update { it.updateToDefault() }
            } catch (e: Exception) {
                _uiState.update { it.updateToFailure(e.message.toString()) }
            }
        }
    }

    fun verify(phone: String, code: String) {

        _uiState.update { it.updateToLoading() }
        viewModelScope.launch {
            try {
                preferenceDataStoreHelper.putPreference(TOKEN_KEY, "user.token")

//                val res = userRepository.verifyOTPAndProceed(phone, code)
                _uiState.update { it.updateToLoaded() }
            } catch (e: Exception) {
                _uiState.update { it.updateToFailure(e.message.toString()) }
            }
        }
    }

    fun updateToDefault() {
        _uiState.update { it.updateToDefault() }
    }

    fun proceedUserData(user: User) {

        viewModelScope.launch {
//            preferenceDataStoreHelper.putPreference(TOKEN_KEY, user.token)
//            preferenceDataStoreHelper.putPreference(USER_ID_KEY, user.userId)
//            preferenceDataStoreHelper.putPreference(PHONE_KEY, user.phone)
//            preferenceDataStoreHelper.putPreference(NOTIFICATIONS_KEY, user.notifications)
        }

    }

}