package com.myconsole.app.fragment.link;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myconsole.app.R;

import java.util.ArrayList;
import java.util.List;

public class LinkAdapter extends RecyclerView.Adapter<LinkAdapter.MyHolderView> {
    private List<String> link  = new ArrayList<>();
    private Context context;

    public LinkAdapter(Context context, List<String> linkList) {
        this.context = context;
        this.link = linkList;
    }

    @NonNull
    @Override
    public MyHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
View view= LayoutInflater.from(context).inflate(R.layout.)
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull LinkAdapter.MyHolderView holder, int position) {

    }


    @Override
    public int getItemCount() {
        return link.size();
    }

    public class MyHolderView extends RecyclerView.ViewHolder {
        public MyHolderView(@NonNull View itemView) {
            super(itemView);
        }
    }
}
