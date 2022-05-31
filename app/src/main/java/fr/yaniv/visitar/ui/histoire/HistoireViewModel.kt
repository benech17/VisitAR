package fr.yaniv.visitar.ui.histoire

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HistoireViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is histoire Fragment"
    }
    val text: LiveData<String> = _text
}