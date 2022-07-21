package com.myconsole.app.KTSamples;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.myconsole.app.R;

public class LinkDownloadActivity extends AppCompatActivity implements View.OnClickListener {
    Button link;
    EditText linkEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_download);
        link = findViewById(R.id.download);
        linkEditText = findViewById(R.id.linkEditText);
        link.setOnClickListener(this);
    }

    private void downloadLink() {
        String d = linkEditText.getText().toString();
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(d);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        long reference = manager.enqueue(request);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.download) {
            downloadLink();
        }
    }
}