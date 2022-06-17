package com.myconsole.app.KTSamples.MVVMViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Model extends ViewModel {

    public MutableLiveData<String> text= new MutableLiveData<>();

    public void setTextValues(String sequence) {
        text.setValue(sequence);
    }

    public MutableLiveData<String> getTextValue(){
        return text;
    }
}
