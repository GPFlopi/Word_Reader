package edu.example.msc.word_reader;

//Importok
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.hardware.camera2.CameraManager;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import java.io.IOException;
import java.lang.reflect.Parameter;
import java.security.Policy;
import java.util.Locale;

public class CameraActivity extends AppCompatActivity {
    TextToSpeech toSpeech;
    int result;
    String text;
    TextView Text;
    SurfaceView cView;
    CameraSource cameraSource;
    CameraManager camger;
    int l=0;


    Button sw;


    final int RequestCameraPermissionID = 1001;
    @Override//Engedély kérés a kamerához
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        cameraSource.start(cView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
            break;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        //ID Csatolás

        cView = findViewById(R.id.SCam); //SurfaceView
        Text = findViewById(R.id.TEXT); //TextView

        sw= findViewById(R.id.button5);//Ideiglenes nyelv gomb

        SharedPreferences prefs2=PreferenceManager.getDefaultSharedPreferences(this);
        final String ny=prefs2.getString("example_list","0");





        //TTS+OCR

            toSpeech = new TextToSpeech(CameraActivity.this, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        result = toSpeech.setLanguage(Locale.UK);
                    } else {
                        Toast.makeText(getApplicationContext(), "Nem jó!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        final TextRecognizer textrecog = new TextRecognizer.Builder(getApplicationContext()).build();

        //Elérhető-e a Kamera
        if (!textrecog.isOperational()) {
            Log.w("CameraActivity", "A Szöveg felismerő nem elérhető!");
        } else
         {
            // A kamera beálítása!
            cameraSource = new CameraSource.Builder(getApplicationContext(), textrecog)

                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(854, 480)
                    .setRequestedFps(15.0f)
                    .setAutoFocusEnabled(true)
                    .build();
            cView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {

                    try {
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(CameraActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    RequestCameraPermissionID);
                            return;
                        }
                        cameraSource.start(cView.getHolder());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }//A Kamera ,képernyőre vetítése!
                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

                }
            });
            textrecog.setProcessor(new Detector.Processor<TextBlock>()
             {
                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(final Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if (items.size() != 0)
                    {
                        //Szöbeg adat kiadása, és beálítása!
                        Text.post(new Runnable()
                         {
                            @Override
                            public void run() {
                                cView.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View v) {

                                        if(l==1)
                                        {
                                            l=0;
                                        }else if(l==0)
                                        {
                                            if(!toSpeech.isSpeaking()) {
                                                StringBuilder stringBuilder = new StringBuilder();
                                                for (int i = 0; i < items.size(); ++i) {
                                                    TextBlock item = items.valueAt(i);
                                                    stringBuilder.append(item.getValue());
                                                    stringBuilder.append("\n");
                                                }
                                                switch (ny)
                                                {
                                                    case "Magyar":
                                                        toSpeech.setSpeechRate((float)0.8);
                                                        if (toSpeech.isLanguageAvailable(new Locale("hu_HU")) == 0)
                                                        {
                                                            toSpeech.setLanguage(new Locale("hu_HU"));
                                                        } else
                                                        {
                                                            toSpeech.setSpeechRate((float)0.8);
                                                            toSpeech.setLanguage(Locale.UK);
                                                        }
                                                        break;
                                                    case "Angol":
                                                        toSpeech.setSpeechRate((float)0.8);
                                                        toSpeech.setLanguage(Locale.UK);

                                                        break;
                                                    case "Szerb":
                                                        toSpeech.setSpeechRate((float)0.01);
                                                        if (toSpeech.isLanguageAvailable(new Locale("sr_SR")) == 0)
                                                        {
                                                            toSpeech.setLanguage(new Locale("sr_SR"));
                                                        }
                                                        else
                                                        {
                                                            toSpeech.setSpeechRate((float)0.8);
                                                            toSpeech.setLanguage(Locale.UK);
                                                            Toast.makeText(getApplicationContext(), "A kért nyelv nem elérhető!Angol nyelvre lett váltva!", Toast.LENGTH_SHORT).show();


                                                        }
                                                        break;
                                                    case "Francia":
                                                        if(toSpeech.isLanguageAvailable(Locale.FRANCE)== 0)
                                                        {
                                                            toSpeech.setSpeechRate((float) 0.8);
                                                            toSpeech.setLanguage(Locale.FRANCE);
                                                        }
                                                        else {
                                                            toSpeech.setSpeechRate((float)0.8);
                                                            toSpeech.setLanguage(Locale.UK);
                                                            Toast.makeText(getApplicationContext(), "A kért nyelv nem elérhető!Angol nyelvre lett váltva!", Toast.LENGTH_SHORT).show();

                                                        }

                                                        break;
                                                    case "Német":
                                                        if(toSpeech.isLanguageAvailable(Locale.GERMAN)== 0)
                                                        {
                                                            toSpeech.setSpeechRate((float) 0.8);
                                                            toSpeech.setLanguage(Locale.GERMAN);
                                                        }else {
                                                            toSpeech.setSpeechRate((float)0.8);
                                                            toSpeech.setLanguage(Locale.UK);
                                                            Toast.makeText(getApplicationContext(), "A kért nyelv nem elérhető!Angol nyelvre lett váltva!", Toast.LENGTH_SHORT).show();

                                                        }
                                                        break;
                                                    case "Olasz":
                                                        if(toSpeech.isLanguageAvailable(Locale.ITALIAN)== 0)
                                                        {
                                                            toSpeech.setSpeechRate((float) 0.8);
                                                            toSpeech.setLanguage(Locale.ITALIAN);
                                                        }else {
                                                            toSpeech.setSpeechRate((float)0.8);
                                                            toSpeech.setLanguage(Locale.UK);
                                                            Toast.makeText(getApplicationContext(), "A kért nyelv nem elérhető!Angol nyelvre lett váltva!", Toast.LENGTH_SHORT).show();
                                                        }
                                                        break;
                                                    case "Cseh":
                                                        toSpeech.setSpeechRate((float)0.8);
                                                        if (toSpeech.isLanguageAvailable(new Locale("cs_CS")) == 0)
                                                        {
                                                            toSpeech.setLanguage(new Locale("cs_CS"));
                                                        }
                                                        else
                                                        {
                                                            toSpeech.setSpeechRate((float)0.8);
                                                            toSpeech.setLanguage(Locale.UK);
                                                            Toast.makeText(getApplicationContext(), "A kért nyelv nem elérhető!Angol nyelvre lett váltva!", Toast.LENGTH_SHORT).show();

                                                        }
                                                        break;

                                                    default:
                                                        Toast.makeText(getApplicationContext(), "Hiba a változó nem megfelelő!", Toast.LENGTH_SHORT).show();
                                                        break;
                                                }
                                                l=1;
                                                Text.setText(stringBuilder.toString());
                                                text = Text.getText().toString();
                                                toSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                                                toSpeech.setSpeechRate((float) 0.5);


                                            }
                                        }
                                        return false;
                                    }
                                });
                                }
                         });
                    }
                }
             });
        }
    }
    public void onBackPressed() {

        cameraSource.stop();
        toSpeech.stop();
        Intent intent=new Intent(this, word_reader.class);
        startActivity(intent);
    }//Vissza ugrás a Kezdőfelületre


}

