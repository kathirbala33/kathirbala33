package com.myconsole.app.KTSamples.MVVMViewModel;

import android.os.Handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Model extends ViewModel {

    public MutableLiveData<String> text = new MutableLiveData<>();
    private MutableLiveData<List<String>> shoppingList;
    private MutableLiveData<List<String>> addedList;

    public void setTextValues(String sequence) {
        text.setValue(sequence);
    }

    public MutableLiveData<String> getTextValue() {
        return text;
    }
    public LiveData<List<String>> getShoppingList() {
        if (shoppingList == null) {
            shoppingList = new MutableLiveData<>();
            loadShoppingList();
        }
        return shoppingList;
    }

    public LiveData<List<String>> getShoppingList1() {
        if (addedList == null) {
            addedList = new MutableLiveData<>();
            loadShoppingList1();
        }
        return addedList;
    }

    private void loadShoppingList1() {
        // 1
        Handler myHandler = new Handler();
        // 2
//        myHandler.postDelayed(() -> {
        // 3
        List<String> shoppingListSample = new ArrayList<>();
 /*       shoppingListSample.add("Bread");
        shoppingListSample.add("Bananas");
        shoppingListSample.add("Peanut Butter");*/
        shoppingListSample.add("12");
        shoppingListSample.add("12345");
        // 4
        long seed = System.nanoTime();
        Collections.shuffle(shoppingListSample, new Random(seed));
        // 5
//        addedList.setValue(shoppingListSample);
        addedList.postValue(shoppingListSample);
        // 6
//        }, 5000);
    }

    private void loadShoppingList() {
        // 1
        Handler myHandler = new Handler();
        // 2
//        myHandler.postDelayed(() -> {
            // 3
            List<String> shoppingListSample = new ArrayList<>();
            shoppingListSample.add("Bread");
            shoppingListSample.add("Bananas");
            shoppingListSample.add("Peanut Butter");
            shoppingListSample.add("Eggs");
            shoppingListSample.add("Chicken breasts");
            // 4
            long seed = System.nanoTime();
            Collections.shuffle(shoppingListSample, new Random(seed));
            // 5
            shoppingList.setValue(shoppingListSample);
            // 6
//        }, 5000);
    }
}
