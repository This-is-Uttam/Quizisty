package com.reward.quizisty.Utils;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.reward.quizisty.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadService extends Service {

    private static final String TAG = "DownloadService";
    private NotificationManager notificationManager;
    private int notificationId = 1;
    public static Call call;
    long fileSize;

    private volatile boolean isPaused = false;
    private volatile boolean isCanceled = false;
    private long totalBytesDowloaded = 0;

    IBinder binder = new LocalBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        startForeground(notificationId, createNotification("Download starting", 0));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String url = intent.getStringExtra("apkUrl");

        // Start the download in a background thread
        new Thread(() -> downloadAPKWithProgress(url)).start();

        return START_STICKY;  // Ensures the service is restarted if killed
    }

    public class LocalBinder extends Binder {
        public DownloadService getService(){
            return DownloadService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private void downloadAPKWithProgress(String url) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(chain -> {
                    Response originalResponse = chain.proceed(chain.request());

                    // Capture content-length in the same request
                    assert originalResponse.body() != null;
                    fileSize = originalResponse.body().contentLength();

                    return originalResponse.newBuilder()
                            .body(new ProgressResponseBody(originalResponse.body(), new DownloadProgressListener() {
                                @Override
                                public void update(long bytesRead, long contentLength, boolean done) {
                                    int progress = (int) ((100 * bytesRead) / contentLength);
                                    updateNotification(progress);  // Update notification with progress
                                    sendProgressBroadcast(progress);
                                }
                            }))
                            .build();
                })
                .build();

        Request.Builder requestBuilder = new Request.Builder().url(url);

//        todo: store incomplete download files in shared preference
//         Check if the file is partially downloaded.
        if (totalBytesDowloaded > 0) {
            requestBuilder.header("Range", "bytes=" + totalBytesDowloaded + "-");
        }

        Request request = requestBuilder.build();

        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: call on failure");
                e.printStackTrace();
                stopSelf();  // Stop the service if download fails
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse: dwapk");
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: success dwapk");
                    File apkFile = new File(getExternalFilesDir(null), "update1.apk");

                    if (response.body() != null) {

                        InputStream inputStream = response.body().byteStream();
                        FileOutputStream fos = new FileOutputStream(apkFile, true);

                        byte[] buffer = new byte[4096];
                        int bytesRead;

                        while ((bytesRead = inputStream.read(buffer)) != -1) {
//                            Delete the partially download file.
//                            Log.d(TAG, "onResponse: isCancelsed");
//                            if (isCanceled) {
//                                fos.close();
//                                call.cancel();
//                                apkFile.delete();
//                                stopSelf();
//                                Log.d(TAG, "onResponse: isCancelsed");
//                                return;
//                            }
//                            Log.d(TAG, "onResponse: isCancelsed after return");
//                            if (isPaused) {
//                                fos.close();
//                                return;
//                            }


                            fos.write(buffer, 0, bytesRead);
                            totalBytesDowloaded += bytesRead;
                        }

                        fos.close();
                        inputStream.close();

                        if (!isPaused && !isCanceled) { // Download complete, update the notification
                            updateNotification(100);
                            broadcastDownloadComplete(apkFile.getAbsolutePath(), fileSize);
                            stopSelf();  // Stop the service after the download completes
                        }
                    }
                }
            }
        });
    }

    public void pauseDownload() {
        isPaused = true;
    }

    public void resumeDownload(String url) {
        isPaused = false;
        new Thread(() -> downloadAPKWithProgress(url)).start();
    }

    public void stopDownload() {
        isCanceled = true;
    }

    private void broadcastDownloadComplete(String apkPath, long fileSize) {
        Intent intent = new Intent("DOWNLOAD_COMPLETED");
        intent.putExtra("apkSize", fileSize);
        intent.putExtra("apkPath", apkPath);
        intent.setPackage(this.getPackageName());
        sendBroadcast(intent);
    }


    private Notification createNotification(String message, int progress) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "download_channel")
                .setContentTitle("Downloading Update")
                .setContentText(message)
                .setSmallIcon(R.drawable.app_icon)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setProgress(100, progress, false)
                .setOngoing(true);  // Makes the notification non-dismissible

        return builder.build();
    }

    private void updateNotification(int progress) {
        Notification notification = createNotification("Download in progress", progress);
        notificationManager.notify(notificationId, notification);
    }

    private void sendProgressBroadcast(int progress) {
        Intent intent = new Intent("DOWNLOAD_PROGRESS");
        intent.putExtra("progress", progress);
        intent.setPackage(this.getPackageName());
        sendBroadcast(intent);
    }



}
