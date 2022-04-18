package com.myconsole.app.commonClass;

import static com.myconsole.app.commonClass.DateUtils.PATTERN_FULL_TIME;
import static com.myconsole.app.commonClass.DateUtils.PATTERN_SERVER_DATE;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.myconsole.app.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

public class Utils {
    public static void printLog(String tag, String msg) {
        Log.d(tag, msg);
    }

    public static String convertDateLongToString(long createdDateLong) {
        return DateFormat.format("MM/dd/yyyy HH:mm:ss", new Date(createdDateLong)).toString();
    }

 /*   public static String[] getVitalIDUsingName(String vitalName) {
String[] name =new  String[]{"BloodPressure","Heart Rate","Spo2","Weight","Temperature","Glucose"};
        return new String[name];
    }*/

    public static int getVitalIDUsingNames(String vitalName, Context context) {
        int vitalID = 0;
        if (vitalName.equals(context.getResources().getString(R.string.vital_title))) {
            vitalID = 0;
        } else if (vitalName.equals(context.getResources().getString(R.string.vital_heart_rate))) {
            vitalID = 2;
        } else if (vitalName.equals(context.getResources().getString(R.string.vital_spo2))) {
            vitalID = 1;
        } else if (vitalName.equals(context.getResources().getString(R.string.vital_weight))) {
            vitalID = 3;
        } else if (vitalName.equals(context.getResources().getString(R.string.vital_temp))) {
            vitalID = 4;
        } else if (vitalName.equals(context.getResources().getString(R.string.vital_glucose))) {
            vitalID = 5;
        }
        return vitalID;
    }

    /*
     * get thunbnail image from the path in Bitmap
     * */
    public static Bitmap getThumbnailImage(String path) {
        Bitmap bitmap;
        bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(path), 50, 50);
        return bitmap;
    }

    public static boolean isVideoFile(String extension) {
        return extension.contains(".mkv") || extension.contains(".mp4") || extension.contains(".mov");
    }

    public static boolean isImageFile(String extension) {
        return extension.contains(".jpg") || extension.contains(".jpeg") || extension.contains(".png");
    }

    public static int getFileSize(String path) {
        int file_size;
        File file = new File(path);
        file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
        return file_size;
    }

    public static String getPath(Context context, Uri uri) {
        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    final String id;
                    Cursor cursor = null;
                    try {
                        cursor = context.getContentResolver().query(uri, new String[]{MediaStore.MediaColumns.DISPLAY_NAME}, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            String fileName = cursor.getString(0);
                            String path = Environment.getExternalStorageDirectory().toString() + "/Download/" + fileName;
                            if (!TextUtils.isEmpty(path)) {
                                return path;
                            }
                        }
                    } finally {
                        if (cursor != null)
                            cursor.close();
                    }
                    id = DocumentsContract.getDocumentId(uri);
                    if (!TextUtils.isEmpty(id)) {
                        if (id.startsWith("raw:")) {
                            return id.replaceFirst("raw:", "");
                        }
                        String[] contentUriPrefixesToTry = new String[]{
                                "content://downloads/public_downloads",
                                "content://downloads/my_downloads"
                        };
                        for (String contentUriPrefix : contentUriPrefixesToTry) {
                            try {
                                final Uri contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.parseLong(id));


                                return getDataColumn(context, contentUri, null, null);
                            } catch (NumberFormatException e) {
                                //In Android 8 and Android P the id is not a number
                                return uri.getPath().replaceFirst("^/document/raw:", "").replaceFirst("^raw:", "");
                            }
                        }
                    }
                } else {
                    final String id = DocumentsContract.getDocumentId(uri);
                    Uri contentUri = null;
                    if (id.startsWith("raw:")) {
                        return id.replaceFirst("raw:", "");
                    }
                    try {
                        contentUri = ContentUris.withAppendedId(
                                Uri.parse("content://downloads/public_downloads"), Long.parseLong(id));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    if (contentUri != null) {
                        return getDataColumn(context, contentUri, null, null);
                    }
                }
            } else if (isMediaDocument(uri)) { // MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);

            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {// MediaStore (and general)
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);

        } else if ("file".equalsIgnoreCase(uri.getScheme())) {// File
            return uri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static void saveFile(String sourceuri, Uri uri, Context mContext) {
        String extension = sourceuri.substring(sourceuri.lastIndexOf("."));
        String fileName = sourceuri.substring(0, sourceuri.lastIndexOf("."));
        fileName = fileName.substring(fileName.lastIndexOf("/")+1);
        String fileURIPath = uri.getPath();
        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyConsole";
        File fullPathA = mContext.getFilesDir();
        String date = DateUtils.getCurrentDate();
        String dateq = DateUtils.changeDateFormat(date, PATTERN_FULL_TIME, PATTERN_SERVER_DATE);
        String saveFile = fileName.concat("-").concat(dateq);
        String fname = saveFile + extension;
        File file = new File(fullPath, fname);
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(sourceuri));
            bos = new BufferedOutputStream(new FileOutputStream(file, false));
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            } while (bis.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap getThumbnailVideo(String path) {
        Bitmap bitmap;
        bitmap = ThumbnailUtils.createVideoThumbnail(path, 0);
        return bitmap;
    }

    public static void createFolder() {
        File folder = new File(Environment.getExternalStorageDirectory(), "MyConsole");
        if (!folder.exists()) {
            folder.mkdir();
            Utils.printLog("##CreateNew", "yes");
        } else {
            Utils.printLog("##Created", "Already");
        }
    }

    public static Dialog setDialogView(Context mContext, boolean isImage, String path) {
        Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_popup);
        ImageView dialogImage = dialog.findViewById(R.id.dialogImageView);
        VideoView dialogVideo = dialog.findViewById(R.id.dialogVideoView);
        ImageView close = dialog.findViewById(R.id.closeImageView);
        if (isImage) {
            Bitmap myBitmap = BitmapFactory.decodeFile(path);
            dialogImage.setVisibility(View.VISIBLE);
            dialogVideo.setVisibility(View.GONE);
            dialogImage.setImageBitmap(myBitmap);
        } else {
            dialogVideo.setVideoPath(path);
            dialogImage.setVisibility(View.GONE);
            dialogVideo.setVisibility(View.VISIBLE);
            MediaController mediaController = new MediaController(mContext);
            mediaController.setAnchorView(dialogVideo);
            dialogVideo.setMediaController(mediaController);
            dialogVideo.requestFocus();
            dialogVideo.start();
            dialogVideo.start();
        }
        close.setOnClickListener(v -> dialog.dismiss());
        return dialog;
    }
}
