package com.myconsole.app.KTSamples.MVVMViewModel;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myconsole.app.databinding.MvvmFragmentBinding;

import java.util.ArrayList;
import java.util.List;

public class MVVMFragment extends Fragment {
    private Model model;
    private MvvmFragmentBinding binding;
    private List<String> valueList = new ArrayList<>();
    private LifeCycleAdapter commonAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getModelView();
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = MvvmFragmentBinding.inflate(getLayoutInflater());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.fileRecyclerView.setLayoutManager(layoutManager);
        binding.fileRecyclerView.setHasFixedSize(true);

        Model viewModel = ViewModelProviders.of(this).get(Model.class);
        viewModel.getShoppingList().observe(getActivity(),
                new Observer<List<String>>() {
                    @Override
                    public void onChanged(List<String> shoppingList) {
                        valueList = shoppingList;
                        commonAdapter = new LifeCycleAdapter(MVVMFragment.this.getActivity(), shoppingList);
                        binding.fileRecyclerView.setAdapter(commonAdapter);
                    }
                });
        Handler myHandler = new Handler();
        // 2
        myHandler.postDelayed(() -> {
            viewModel.getShoppingList1().observe(getActivity(), new Observer<List<String>>() {
                @Override
                public void onChanged(List<String> add) {
                    valueList.addAll(add);
                    commonAdapter.notifyDataSetChanged();
                }
            });
        }, 5000);
        return binding.getRoot();
    }

    private void getModelView() {
//        model = new ViewModelProvider(this).get(Model.class);
        model = ViewModelProviders.of(this).get(Model.class);
        model.getTextValue();
    }
}
