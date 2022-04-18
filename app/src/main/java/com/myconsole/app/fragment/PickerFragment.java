package com.myconsole.app.fragment;

import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;
import static android.provider.MediaStore.ACTION_VIDEO_CAPTURE;
import static com.myconsole.app.ListenerConstant.FILE_DELETE_POSITION;
import static com.myconsole.app.ListenerConstant.FILE_LISTENER;
import static com.myconsole.app.commonClass.Utils.createFolder;
import static com.myconsole.app.commonClass.Utils.getPath;
import static com.myconsole.app.commonClass.Utils.isImageFile;
import static com.myconsole.app.commonClass.Utils.isVideoFile;
import static com.myconsole.app.commonClass.Utils.saveFile;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myconsole.app.Listener;
import com.myconsole.app.R;
import com.myconsole.app.commonClass.CommonAdapter;
import com.myconsole.app.commonClass.Utils;
import com.myconsole.app.databinding.PickerFragmentBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PickerFragment extends Fragment implements View.OnClickListener, Listener {

    private PickerFragmentBinding binding;
    private final int REQ_CODE_DOCUMENT = 1;
    private final int REQ_CODE_GALLERY = 2;
    private final int REQ_CODE_WRITE_STOREAGE = 3;
    private final int REQ_CODE_CAMERA = 4;
    private final int REQ_CODE_VIDEO = 5;
    private Context mContext;
    private List<FileModel> fileList;
    private CommonAdapter commonAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = PickerFragmentBinding.inflate(getLayoutInflater());
        mContext = getActivity();
        init();
        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_CODE_WRITE_STOREAGE) {
            createFolder();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_DOCUMENT) {
            if (data != null) {
                Uri uri = data.getData();
                String path = getPath(mContext, uri);
                saveFile(path != null ? path : "", uri, mContext);
                displayFiles();
            }
        } else if (requestCode == REQ_CODE_GALLERY) {
            if (data != null) {
                Uri uri = data.getData();
                String path = getPath(mContext, uri);
                saveFile(path != null ? path : "", uri, mContext);
                displayFiles();
            }
        } else if (requestCode == REQ_CODE_CAMERA) {
            if (data != null) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                String paths = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), bitmap, "Title", null);
                Uri uri = Uri.parse(paths);
                String path = getPath(mContext, uri);
                saveFile(path != null ? path : "", uri, mContext);
                displayFiles();
            }
        } else if (requestCode == REQ_CODE_VIDEO) {
            if (data != null) {
                Uri uri = data.getData();
                String path = getPath(mContext, uri);
                saveFile(path != null ? path : "", uri, mContext);
                displayFiles();
            }
        }
    }


    private void init() {
        checkPermissionStoreage();
        displayFiles();
        binding.galleryRelativeLayout.setOnClickListener(this);
        binding.documentRelativeLayout.setOnClickListener(this);
        binding.cameraRelativeLayout.setOnClickListener(this);
        binding.videoRelativeLayout.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.fileRecyclerView.setLayoutManager(layoutManager);
        binding.fileRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(mContext);
        layoutManager1.setOrientation(RecyclerView.VERTICAL);
    }


    private void displayFiles() {
        fileList = new ArrayList<>();
        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyConsole";
        File directory = new File(fullPath);
        File[] files = directory.listFiles();
        Bitmap thumbnail;
        String fileSize;
        if (files != null) {
            for (File file : files) {
                String extension = file.getPath().substring(file.getPath().lastIndexOf("."));
                if (isImageFile(extension)) {
                    thumbnail = Utils.getThumbnailImage(file.getPath());
                } else {
                    thumbnail = null;
                }
                fileSize = String.valueOf(Utils.getFileSize(file.getPath()));
                fileList.add(new FileModel(file.getName(), file.getPath(), thumbnail, extension, fileSize));
            }
            commonAdapter = new CommonAdapter(mContext, fileList, this);
            binding.fileRecyclerView.setAdapter(commonAdapter);
        }
    }


    private void checkPermissionStoreage() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQ_CODE_WRITE_STOREAGE);
        } else {
            createFolder();
        }
    }


    private void openDocument() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQ_CODE_DOCUMENT);
    }

    private void openCamera() {
        Intent intent = new Intent(ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQ_CODE_CAMERA);
    }

    private void openVideo() {
        Intent intent = new Intent(ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, REQ_CODE_VIDEO);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQ_CODE_GALLERY);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.galleryRelativeLayout) {
            openGallery();
        } else if (v.getId() == R.id.documentRelativeLayout) {
            openDocument();
        } else if (v.getId() == R.id.cameraRelativeLayout) {
            openCamera();
        } else if (v.getId() == R.id.videoRelativeLayout) {
            openVideo();
        }
    }


    @Override
    public void listenerData(int action, Object data) {
        if (action == FILE_LISTENER) {
            int pos = (int) data;
            String path = fileList.get(pos).getFilePath();
            if (isImageFile(path)) {
                Dialog dialog = Utils.setDialogView(mContext, true, path);
                dialog.show();
            } else if (isVideoFile(path)) {
                Dialog dialog = Utils.setDialogView(mContext, false, path);
                dialog.show();
            } else {
                Intent open = new Intent(Intent.ACTION_VIEW);
                Uri apkURI = FileProvider.getUriForFile(
                        mContext,
                        mContext.getApplicationContext()
                                .getPackageName() + ".provider", new File(path));
                open.setDataAndType(apkURI, "application/*");
                open.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                mContext.startActivity(open);
            }
        } else if (action == FILE_DELETE_POSITION) {
            int pos = (int) data;
            File path = new File(fileList.get(pos).getFilePath());
            path.delete();
            displayFiles();
        }
    }
}
