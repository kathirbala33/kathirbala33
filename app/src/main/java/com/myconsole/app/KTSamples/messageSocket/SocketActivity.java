package com.myconsole.app.KTSamples.messageSocket;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.myconsole.app.KTSamples.socketPojo.EnvDetailsSocket;
import com.myconsole.app.KTSamples.socketPojo.MetaDataItemsSocket;
import com.myconsole.app.KTSamples.socketPojo.MsgSendSocket;
import com.myconsole.app.KTSamples.socketPojo.SocketNewResponse;
import com.myconsole.app.application.MyApplication;
import com.myconsole.app.databinding.ActivitySocketBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketActivity extends AppCompatActivity {
    private ActivitySocketBinding binding;
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySocketBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initialView();
    }

    private void initialView() {
        socket = MyApplication.getSocket();
        socket.connect();
        socket.on("new-message-response", newResponse);
        binding.messageEditText.getText();
        binding.socketButton.setOnClickListener(view -> emitSocket());

    }

    private void emitSocket() {
        MetaDataItemsSocket metaDataModel = new MetaDataItemsSocket();
        DateFormat df = DateFormat.getDateInstance();
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        String time = getCurrentDateTimeAccordingToUTC("yyyy-MM-dd'T'HH:mm:ss'Z'");
        metaDataModel.setExtension("");
        metaDataModel.setFileName("");
        metaDataModel.setSourcefileName("");
        String encMsg = callEncryptMethod("your-unic-key", binding.messageEditText.getText().toString());
        Log.d("##ence", encMsg);
        MsgSendSocket msgSendSocket = new MsgSendSocket("your-token", "1792c0e5-cf9e-5d2b-e6e2-184fd4678262", Collections.singletonList("ce2d9447-c6dc-63a2-a31f-1b33dd62d2f2"), encMsg, "text", metaDataModel, getEnvDetailsForSocket(), "bse-url", time);
        String json = new Gson().toJson(msgSendSocket);
        try {
            Log.d("##socket", json);
            JSONObject jsonObj = new JSONObject(json);
            socket.emit("new-message", jsonObj);
        } catch (JSONException e) {
            Log.d("##socket", e.getMessage());
            e.printStackTrace();
        }
    }

    private final Emitter.Listener newResponse = args -> {
        JSONObject obj = (JSONObject) args[0];
        Gson gson = new GsonBuilder().create();
        String jsonData = gson.toJson(obj);
        Log.d("##socketresponse", "----->" + jsonData);
        SocketNewResponse socketNewResponse = gson.fromJson(jsonData, SocketNewResponse.class);
        String msg = callDecryptMethod("your-unic-key", socketNewResponse.getNameValuePairs().getContent());
        binding.messageGetTextView.setText(msg);
    };

    public static EnvDetailsSocket getEnvDetailsForSocket() {
        EnvDetailsSocket envDetailsSocket = new EnvDetailsSocket();
        envDetailsSocket.setDomain_key("alpha");
        envDetailsSocket.setTenant_key("alpha.1555884244");
        envDetailsSocket.setTenantId("1");
        return envDetailsSocket;
    }

    private String getCurrentDateTimeAccordingToUTC(String format) {
        Date date = new Date(System.currentTimeMillis());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(date);
    }

    public static String callEncryptMethod(String key, String password) {
        String encryptedStringData = "";
        try {
            //Generate Random IV - Hex format(32 char)
            String IV = getRandomHexString().toLowerCase();
            //byte[] ivBytes = new byte[16];
            byte[] ivBytes = HexToBytes(IV);
            //Initiate Cipher
            Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
            //Convert String to byte[]
            byte[] keyData = password.getBytes();
            byte[] passwordData = key.getBytes(StandardCharsets.UTF_8);
            //Convert Base64 to SHA-256 Hash-key in byte[]
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] passwordHash = digest.digest(passwordData);
            //Convert HashBytes to Base64
            String passHashBase64 = Base64.encodeToString(passwordHash, Base64.DEFAULT);
            // Checking Base64 length
            passHashBase64 = passHashBase64.length() > 32 ? passHashBase64.substring(0, 32) : passHashBase64;
            // Convert base64 in Data to SecretKey formatted key
            Key keySpec = new SecretKeySpec(passHashBase64.getBytes(), "AES");
            //Initiate Encrypt Mode
            //Initiate IVParameterSpec
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(ivBytes));
            //Encrypted Data in byte[]
            byte[] encryptedMessage = cipher.doFinal(keyData);
            //byte[] to Hex String
            String encryptedHex = bytesToHex(encryptedMessage).toLowerCase();
            encryptedStringData = IV.concat(":").concat(encryptedHex);
            Log.d("##ENCRYPTION##callEncryptMethod", "---------->" + encryptedStringData);
        } catch (Exception e) {
            Log.d("##ENCRYPTION##callEncryptMethod", "---------->" + e.getMessage());
            return encryptedStringData;
        }
        return encryptedStringData;
    }

    public static String bytesToHex(byte[] bytes) {
        char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    private static String getRandomHexString() {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        while (sb.length() < 32) {
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.substring(0, 32);
    }

    public static String callDecryptMethod(String key, String encryptedMsg) {
        String decryptedString = "";
        try {
            //Split IV and Epk
            String[] iVPrivateKeys = encryptedMsg.split(":");
            String IV = iVPrivateKeys[0];
            String encryptedMessage = iVPrivateKeys[1];
            // Hex IV to byte[]
            byte[] ivBytes = HexToBytes(IV);
            //Initiate Cipher fro Decryption
            Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
            //Convert Base64 to SHA-256 Hash_key in byte[]
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] passwordHash = digest.digest(key.getBytes(StandardCharsets.UTF_8));
            //Convert HashBytes to Base64
            String passHashBase64 = Base64.encodeToString(passwordHash, Base64.DEFAULT);
            // Checking Base64 length
            passHashBase64 = passHashBase64.length() > 32 ? passHashBase64.substring(0, 32) : passHashBase64;
            Key keySpec = new SecretKeySpec(passHashBase64.getBytes(), "AES");
            //Initiate Decrypt Mode
            //Initiate IVParameterSpec
            cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(ivBytes));
            //Decrypted Data in String
            byte[] ecBytes = HexToBytes(encryptedMessage);
            decryptedString = new String(cipher.doFinal(ecBytes));
            Log.d("##DECRYPTION##callDecryptMethod", "---------->" + decryptedString);
        } catch (Exception e) {
            Log.d("##DECRYPTION##callDecryptMethod", "---------->" + e.getMessage());
            return decryptedString;
        }
        return decryptedString;
    }

    private static byte[] HexToBytes(String hex) {
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        for (int i = 0; i < hex.length(); i += 2) {
            int b = Integer.parseInt(hex.substring(i, i + 2), 16);
            bas.write(b);
        }
        return bas.toByteArray();
    }
}