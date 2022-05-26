package com.myconsole.app.commonClass;

import static com.myconsole.app.ListenerConstant.FILE_DELETE_POSITION;
import static com.myconsole.app.ListenerConstant.FILE_LISTENER;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.myconsole.app.Listener;
import com.myconsole.app.R;
import com.myconsole.app.fragment.FileModel;

import java.util.List;
import java.util.Objects;

public class CommonAdapter extends RecyclerView.Adapter<CommonAdapter.ViewHolder> {
    private final Context context;
    private List<FileModel> fileLists;
    private Listener listener;

    public CommonAdapter(Context mContext, List<FileModel> fileList, Listener listener) {
        this.context = mContext;
        this.fileLists = fileList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CommonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.link_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommonAdapter.ViewHolder holder, int position) {
        FileModel model = fileLists.get(position);
        if (model != null) {
            holder.titleTextView.setText(context.getResources().getString(R.string.file_name_with_colon).concat(" ").concat(model.getFileName()));
            holder.fileExtensionTextView.setText(context.getResources().getString(R.string.file_format_with_colon).concat(" ").concat(model.getExtension().toUpperCase()));
            holder.fileSizeTextView.setText(context.getResources().getString(R.string.file_size_with_colon).concat(" ").concat(model.getFileSize()).concat(" KB"));
            if (model.getThumbNail() != null) {
                holder.titleImageView.setVisibility(View.VISIBLE);
                holder.titleImageView.setImageBitmap(model.getThumbNail());
                holder.playImageView.setVisibility(View.GONE);
            } else if (Utils.isVideoFile(model.getFilePath())) {
                holder.titleCardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.black));
                holder.titleImageView.setVisibility(View.GONE);
                holder.playImageView.setVisibility(View.VISIBLE);
            } else {
                holder.titleImageView.setVisibility(View.VISIBLE);
                holder.playImageView.setVisibility(View.GONE);
                holder.titleImageView.setImageBitmap(((BitmapDrawable) Objects.requireNonNull(ContextCompat.getDrawable(context, R.drawable.document))).getBitmap());
            }
        }
        holder.titleCardView.setTag(position);
        holder.fileTotalLayout.setTag(position);
        holder.deleteImageView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return fileLists.size();
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
        private final LinearLayout fileTotalLayout;
        private final TextView titleTextView;
        private final TextView fileExtensionTextView;
        private final TextView fileSizeTextView;
        private final ImageView titleImageView;
        private final ImageView playImageView;
        private final ImageView deleteImageView;
        private final CardView titleCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            titleCardView = itemView.findViewById(R.id.titleCardView);
            fileTotalLayout = itemView.findViewById(R.id.fileTotalLayout);
            titleImageView = itemView.findViewById(R.id.titleImageView);
            fileExtensionTextView = itemView.findViewById(R.id.fileExtensionTextView);
            fileSizeTextView = itemView.findViewById(R.id.fileSizeTextView);
            playImageView = itemView.findViewById(R.id.playImageView);
            deleteImageView = itemView.findViewById(R.id.deleteImageView);
            fileTotalLayout.setOnClickListener(this);
            titleCardView.setOnClickListener(this);
            deleteImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.fileTotalLayout || v.getId() == R.id.titleCardView) {
                int position = (int) titleCardView.getTag();
                listener.listenerData(FILE_LISTENER, position);
            } else if (v.getId() == R.id.deleteImageView) {
                int position = (int) deleteImageView.getTag();
                listener.listenerData(FILE_DELETE_POSITION, position);
            }
        }
    }
}
