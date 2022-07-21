package com.myconsole.app.KTSamples.MVVMViewModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myconsole.app.Listener;
import com.myconsole.app.R;
import com.myconsole.app.fragment.FileModel;

import java.util.List;

public class LifeCycleAdapter extends RecyclerView.Adapter<LifeCycleAdapter.ViewHolder> {
    private Context context;
    private List<FileModel> fileLists;
    private Listener listener;
    private List<String> shoppingLists;


    public LifeCycleAdapter(Context activity, List<String> shoppingList) {
        this.context = activity;
        this.shoppingLists = shoppingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.link_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String model = shoppingLists.get(position);
        holder.fileExtensionTextView.setText(model);
    }

    @Override
    public int getItemCount() {
        return shoppingLists.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView fileExtensionTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fileExtensionTextView = itemView.findViewById(R.id.fileExtensionTextView);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
