package com.kuyuzhiqi.testdemo.audio;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AudioRecordManager {

    public static final int MIN_RECORD_TIME = 5 * 1000;
    public static final int MAX_RECORD_TIME = 60 * 1000;
    public static final int MSG_PLAY_END = 101;
    //16K采集率
    private static final int FREQUENCY = 16000;
    //格式
    private static final int CHANNEL_CONFIGURATION = AudioFormat.CHANNEL_IN_STEREO;
    //16Bit
    private static final int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    public static final String AUDIO_SUFFIX = ".pcm";
    private static AudioRecordManager instance;
    private volatile boolean isRecording;
    private volatile boolean isPlaying;
    private AudioTrack mAudioTrack;
    private short[] buffer;
    private File mRecordFile;
    private Handler mAudioHandler;
    private long totalTime = -1l, startTime, endTime;

    public static AudioRecordManager getInstance() {
        if (instance == null) {
            instance = new AudioRecordManager();
        }
        return instance;
    }

    public void init() {
    }

    public void startRecord(final String fileName) {
        final Thread thread = new Thread(new Runnable() {
            @Override public void run() {
                try {
                    mRecordFile = new File(fileName);
                    if (!mRecordFile.exists()) {
                        mRecordFile.createNewFile();
                    }
                    isRecording = true;
                    OutputStream os = new FileOutputStream(mRecordFile);
                    BufferedOutputStream bos = new BufferedOutputStream(os);
                    DataOutputStream dos = new DataOutputStream(bos);
                    int bufferSize = AudioRecord.getMinBufferSize(FREQUENCY, CHANNEL_CONFIGURATION, AUDIO_ENCODING);
                    AudioRecord audioRecord =
                            new AudioRecord(MediaRecorder.AudioSource.MIC, FREQUENCY, CHANNEL_CONFIGURATION,
                                    AUDIO_ENCODING, bufferSize);
                    buffer = new short[bufferSize];
                    audioRecord.startRecording();
                    startTime = System.currentTimeMillis();
                    isRecording = true;
                    while (isRecording) {
                        int bufferedReadResult = audioRecord.read(buffer, 0, bufferSize);
                        for (int i = 0; i < bufferedReadResult; i++) {
                            dos.writeShort(buffer[i]);
                        }
                        long v = 0;
                        for (int i = 0; i < buffer.length; i++) {
                            v += buffer[i] * buffer[i];
                        }
                        double mean = v / (double) bufferedReadResult;
                        double volume = 10 * Math.log10(mean);
                        //获取录音的声音分贝值
                        Log.i("tag", "分贝值:" + volume);
                        totalTime = System.currentTimeMillis() - startTime;
                        Log.i("time", "totalTime:" + totalTime);
                    }
                    audioRecord.stop();
                    dos.close();
                    audioRecord.release();
                    endTime = System.currentTimeMillis();
                    Log.i("time", "endTime:" + endTime);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void playRecord() {
        if (mRecordFile == null || !mRecordFile.exists() || isPlaying) {
            return;
        }
        Thread thread = new Thread(new Runnable() {
            @Override public void run() {
                isPlaying = true;
                int musicLength = (int) (mRecordFile.length() / 2);
                short[] music = new short[musicLength];
                try {
                    InputStream is = new FileInputStream(mRecordFile);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    DataInputStream dis = new DataInputStream(bis);
                    int i = 0;
                    while (dis.available() > 0) {
                        music[i] = dis.readShort();
                        i++;
                    }
                    dis.close();
                    mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, FREQUENCY,
                            CHANNEL_CONFIGURATION, AUDIO_ENCODING, musicLength * 2, AudioTrack.MODE_STREAM);
                    mAudioTrack.setNotificationMarkerPosition(musicLength / 2);
                    mAudioTrack.setPlaybackPositionUpdateListener(new AudioTrack.OnPlaybackPositionUpdateListener() {
                        @Override public void onMarkerReached(AudioTrack track) {
                            isPlaying = false;
                            if (mAudioHandler != null) {
                                Message msg = new Message();
                                msg.what = MSG_PLAY_END;
                                mAudioHandler.sendMessage(msg);
                            }
                        }

                        @Override public void onPeriodicNotification(AudioTrack track) {
                        }
                    });
                    mAudioTrack.play();
                    mAudioTrack.write(music, 0, musicLength);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void playRecord(String fileName) {
        mRecordFile = new File(fileName);
        playRecord();
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public Boolean isRecording() {
        if (mRecordFile == null || !mRecordFile.exists()) {
            return null;
        }
        return isRecording;
    }

    public void stopRecording() {
        isRecording = false;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setAudioHandler(Handler audioHandler) {
        mAudioHandler = audioHandler;
    }

    public void stopPlaying() {
        isPlaying = false;
        if (mAudioTrack == null || mAudioTrack.getState() != AudioTrack.STATE_INITIALIZED) {
            return;
        }
        if (mAudioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
            mAudioTrack.pause();
            mAudioTrack.flush();
        }
    }

    public void deleteRecordFile() {
        if (mRecordFile == null || mRecordFile.exists()) {
            mRecordFile.delete();
        }
        mRecordFile = null;
    }

    public void release() {
        stopRecording();
        stopPlaying();
        if (mAudioTrack != null) {
            mAudioTrack.release();
            mAudioTrack = null;
        }
        if (mAudioHandler != null) {
            mAudioHandler.removeCallbacksAndMessages(null);
        }
        mRecordFile = null;
    }
}
