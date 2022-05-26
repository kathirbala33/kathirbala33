package com.myconsole.app.KTSamples.encryptDecrypt;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


import com.myconsole.app.R;
import com.myconsole.app.databinding.ActivityEncryptDecryptBinding;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptDecryptActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityEncryptDecryptBinding binding;
    private static String key = "EncryptDecryptSampleKeyForThisApplication";
    byte[] encryptByte = null;
    String encryptMessage = "";
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEncryptDecryptBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initialView();
    }

    private void initialView() {
        binding.encryptButton.setOnClickListener(this);
        binding.decryptButton.setOnClickListener(this);
    }

    private void enCryptMethod() {
        String message = binding.enterMessageEditText.getText().toString();
        getEncryptMessage(message);
    }

    private void getEncryptMessage(String message) {
        byte[] encryptByte = null;
        Cipher cipher = null;
        try {
            Key key = getSecretKey();
            cipher = Cipher.getInstance("AES/CTR/NoPadding");
//            cipher = Cipher.getInstance("AES/CTR/PKCS7Padding");
            /*cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);*/
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(new byte[16]));
            encryptByte = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
            encryptMessage = bytesToHex(encryptByte);
            Log.d("##EncryptByte", String.valueOf(encryptByte));
            String encryptString = new String(encryptByte, StandardCharsets.UTF_8);
            Log.d("##EncryptString", encryptString);
            binding.displayEncryptMessage.setText(encryptMessage);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("##EncryptException", e.getMessage());
        }
//        return encryptByte;
    }

    private void decryptMethod() {
        Key key = getSecretKey();
        Cipher cipher = null;
        byte[] hexTobyteValue = HexToBytes(encryptMessage);
        try {
            cipher = Cipher.getInstance("AES/CTR/NoPadding");
//            cipher = Cipher.getInstance("AES/CTR/PKCS7Padding");
//            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(new byte[16]));
            byte[] decrypt = cipher.doFinal(hexTobyteValue);
            String decryptString = new String(decrypt, StandardCharsets.UTF_8);
            binding.displayDecryptMessage.setText(decryptString);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("##DecryptException", e.getMessage());
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.encryptButton) {
            enCryptMethod();
        } else if (view.getId() == R.id.decryptButton) {
            decryptMethod();
        }
    }


    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    private byte[] hexToByte(String encryptMessage) {
        byte[] b = new BigInteger(encryptMessage, 16).toByteArray();
        return b;
    }

    private static byte[] HexToBytes(String hex) {
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        for (int i = 0; i < hex.length(); i += 2) {
            int b = Integer.parseInt(hex.substring(i, i + 2), 16);
            bas.write(b);
        }
        return bas.toByteArray();
    }

    private Key getSecretKey() {
        String base64String = Base64.encodeToString(key.getBytes(), Base64.DEFAULT);
        base64String = base64String.substring(0, 32);
        Key k = new SecretKeySpec(base64String.getBytes(), "AES");
        return k;
    }


}