package com.example.synergyjavamod3;

import static android.hardware.Sensor.TYPE_ACCELEROMETER;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener{
    private final String DATA_STREAM = "http://ep128.hostingradio.ru:8030/ep128"; // ссылка на поток
    private final String DATA_SD = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC) + "/music.mp3"; // ссылка на сд карту устройства
    private String nameAudio = ""; // название контента

    private MediaPlayer mediaPlayer; // медиа плеер
    private AudioManager audioManager; // аудио менедржер
    private Toast toast; // поле тост
    private SeekBar seekBar; // ползунок для громкости

    private TextView textOut; // поле вывода информации фудио файла


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textOut = findViewById(R.id.textOut);

        // доступ к аудио менеджеру
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxValue = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int minValue = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        seekBar = findViewById(R.id.btnSeekBar);
        seekBar.setMax(maxValue);
        seekBar.setProgress(minValue);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // создание слушателя переключателя повтора
//        switchLoop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheacked) {
//                if (mediaPlayer != null) {
//                    mediaPlayer.setLooping(isCheacked); // включение и выключение повтора
//                }
//            }
//        });
    }
    // слушатель нажатия радио кнопок
    public void onClickSource(View view) {
        releaseMediaPlayer(); // освобождение ресурсов перед выбором источника контента
          try {
              switch (view.getId()) {
                  case R.id.btnStream:
                      toast = Toast.makeText(this, "Запуен поток аудио", Toast.LENGTH_SHORT);
                      toast.show();
                      mediaPlayer = new MediaPlayer(); // создание объекта меда плеера
                      mediaPlayer.setDataSource(DATA_STREAM);
                      mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                      mediaPlayer.setOnPreparedListener(this);
                      mediaPlayer.prepareAsync();
                      nameAudio = "Радио";
                      break;
                  case R.id.btnRAW:
                      toast = Toast.makeText(this, "Запущен аудио файл с памяти телефона", Toast.LENGTH_SHORT);
                      toast.show();
                      mediaPlayer = MediaPlayer.create(this, R.raw.survivor); // создание дорожки со звуком
                      mediaPlayer.start(); // старт данной дорожки
                      nameAudio = "Survivor - Eye of the Tiger";
                      break;
                  case R.id.btnSD:
                      toast = Toast.makeText(this, "Запуен аудио файл с SD-карты", Toast.LENGTH_SHORT);
                      toast.show();
                      mediaPlayer = new MediaPlayer();
                      mediaPlayer.setDataSource(DATA_SD); // источник аудио
                      mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                      mediaPlayer.prepare();
                      mediaPlayer.start();
                      break;
              }
          } catch (IOException e) { // исключение ввода и вывода
                e.printStackTrace();
                toast = Toast.makeText(this, "Источник информации не найден", Toast.LENGTH_LONG);
                toast.show();
          }

          if (mediaPlayer ==null) return;

//          mediaPlayer.setLooping(switchLoop.isChecked());
          mediaPlayer.setOnCompletionListener(this);

    }

    public void onClick(View view) {
        if (mediaPlayer == null) return;
        switch (view.getId()) {
            case R.id.btnResume:
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
                break;
            case R.id.btnPause:
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                break;
            case R.id.btnStop:
                mediaPlayer.stop();
                break;
            case R.id.btnForward:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 5000); // переемотка вперед на 5 сек
                break;
            case R.id.btnBack:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 5000); // перемотка назад на 5 сек
                break;
        }

        textOut.setText(nameAudio + "\n(проигрывание " + mediaPlayer.isPlaying() + ", время " + mediaPlayer.getCurrentPosition()
                + ",\nповтор " + mediaPlayer.isLooping() + ", громкость " + audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + ")");
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        toast = Toast.makeText(this, "Отключение меда плеера", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start(); // старт медиа плеера
        toast = Toast.makeText(this, "Старт медиа плеера", Toast.LENGTH_SHORT);
        toast.show();
    }

    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
    }

    // метод освобождения используемых проигрывателем ресурсов
    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
