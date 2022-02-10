package com.myconsole.app.fragment.link;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myconsole.app.R;
import com.myconsole.app.fragment.link.linkPreview.LinkModel;
import com.myconsole.app.fragment.link.linkPreview.LinkPreview;

import java.util.ArrayList;
import java.util.List;

public class LinkAdapter extends RecyclerView.Adapter<LinkAdapter.MyHolderView> {
    private List<LinkModel> link = new ArrayList<>();
    private Context context;
    private LinkPreview richPreview;

    public LinkAdapter(Context context, List<LinkModel> linkList) {
        this.context = context;
        this.link = linkList;
    }

    @NonNull
    @Override
    public MyHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.link_adapter, parent, false);
        return new MyHolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolderView holder, int position) {
        if (link != null) {
            holder.title.setText(link.get(position).getTitle());
           /* richPreview = new LinkPreview(new ResponseListener() {
                @Override
                public void onData(MetaData metaData) {
                    holder.title.setText(metaData.getTitle());
                    holder.titleImageView.setImageResource(Integer.parseInt(metaData.getImageurl()));
                }

                @Override
                public void onError(Exception e) {

                }
            });
            richPreview.getPreview(link.get(position));*/
        }

    }


    @Override
    public int getItemCount() {
        return link.size();
    }

    public class MyHolderView extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView titleImageView;

        public MyHolderView(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTextView);
            titleImageView = itemView.findViewById(R.id.titleImageView);
        }
    }
}
