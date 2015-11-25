package com.adobe.phonegap.push;

import android.app.IntentService;
import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class PostAnswerService extends IntentService {
    private static String LOG_TAG = "PushPlugin_PostAnswerService";

    public PostAnswerService() {
        super("PostAnswerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Log.d(LOG_TAG, "onHandleIntent PostAnswerService");

            String servicesUrl = intent.getStringExtra("servicesUrl");
            String servicesToken = intent.getStringExtra("servicesToken");
            String questionNid = intent.getStringExtra("questionNid");
            String answerTid = intent.getStringExtra("answerTid");
            String typeOfNotification = intent.getStringExtra("typeOfNotification");

            // Prepare JSON containing the answer message content.
            JSONObject jData = new JSONObject();
            jData.put("question_nid", questionNid);
            jData.put("answer_tid", answerTid);
            jData.put("type_of_notification", typeOfNotification);
            if (typeOfNotification.equals("medication")) {
                String data = intent.getStringExtra("data");
                jData.put("data", data);
            } else if (typeOfNotification.equals("appointment")) {

            }

            // Create connection to send GCM Message request.
            URL url = new URL(servicesUrl + "/service/patient_answer?services_token=" + servicesToken);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // Send answer message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jData.toString().getBytes());
            InputStream inputStream = conn.getInputStream();

            Log.d(LOG_TAG, "Successfully sent answer message.");
            super.stopService(intent);
        } catch (IOException e) {
            Log.d(LOG_TAG, "IOException: Unable to send answer message.");
            e.printStackTrace();
        } catch (JSONException e) {
            Log.d(LOG_TAG, "JSONException: Unable to send answer message.");
            e.printStackTrace();
        }
    }
}
