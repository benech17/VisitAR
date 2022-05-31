package fr.yaniv.visitar.ui.information

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InformationViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is informations Fragment"
    }
    val text: LiveData<String> = _text
}