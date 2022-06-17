package com.myconsole.app.KTSamples.MVVMViewModel;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.myconsole.app.commonClass.Utils;

public class LifeCycleFragment extends Fragment {
    private Model model = new Model();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getModelView();
        setModelView();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        Utils.printLog("##LifeCycleFragment", "---->onAttach");
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Utils.printLog("##LifeCycleFragment", "---->onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Utils.printLog("##LifeCycleFragment", "---->onViewCreated");

        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Utils.printLog("##LifeCycleFragment", "---->onCreateView");

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void setModelView() {
//        model = new ViewModelProvider(this).get(Model.class);
        model = ViewModelProviders.of(this).get(Model.class);
        model.setTextValues("hai");
    }

    private void getModelView() {
//        model = new ViewModelProvider(this).get(Model.class);
        model = ViewModelProviders.of(this).get(Model.class);
        model.getTextValue();
    }

    @Override
    public void onStart() {
        Utils.printLog("##LifeCycleFragment", "---->onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        Utils.printLog("##LifeCycleFragment", "---->onResume");
    }

    @Override
    public void onPause() {
        Utils.printLog("##LifeCycleFragment", "---->onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Utils.printLog("##LifeCycleFragment", "---->onStop");
        super.onStop();
    }


    @Override
    public void onDestroyView() {
        Utils.printLog("##LifeCycleFragment", "---->onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Utils.printLog("##LifeCycleFragment", "---->onDestroy");
        super.onDestroy();
    }


    @Override
    public void onDetach() {
        Utils.printLog("##LifeCycleFragment", "---->onDetach");
        super.onDetach();
    }
}
