package com.reward.quizisty.Utils;

public interface DownloadProgressListener {
    void update(long bytesRead, long contentLength, boolean done);
}