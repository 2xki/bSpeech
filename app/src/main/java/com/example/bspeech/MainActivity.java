package com.example.bspeech;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    private ListView lstPairedDevices;
    private TextToSpeech tts;
    /*
    private String device_address = "";
    private String device_name = "";

    private BluetoothAdapter btAdapter = null;
    private Set<BluetoothDevice> pairedDevices;
    */

    private final int REQ_CODE_SPEECH_INPUT = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        lstPairedDevices = (ListView) findViewById(R.id.lstPairedDevices);


        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR){
                    tts.setLanguage(Locale.US);
                }
            }
        });

        /*btAdapter = BluetoothAdapter.getDefaultAdapter();
        if(btAdapter == null)
        {
            //Show a mensag. that thedevice has no bluetooth adapter
            Toast.makeText(getApplicationContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();
            //finish apk
            finish();
        }
        else
        {
            if (btAdapter.isEnabled())
            { }
            else
            {
                //Ask to the user turn the bluetooth on
                Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(turnBTon,1);
            }
        }*/

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //if(device_address != ""){
                    promptSpeechInput();
                //}else{
                //    listPairedDevices();
                //}
            }
        });

    }

    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));

                    switch(result.get(0).toString().toLowerCase()) {
                        case "hello professor how are you":
                            tts.speak("Fine, thank you", TextToSpeech.QUEUE_FLUSH, null);
                            break;
                        case "can you tell us something about your research on brains":
                            tts.speak("Of course\n" +
                                    "We started controlling the\n" +
                                    "brain of a dog, using eye\n" +
                                    "contact and guestures", TextToSpeech.QUEUE_FLUSH, null);
                            break;
                        case "how interesting":
                            tts.speak("it took a while to manipulate\n" +
                                    "the dog, but then we won", TextToSpeech.QUEUE_FLUSH, null);
                            break;
                        case "what was next":
                            tts.speak("The next step was acutally\n" +
                                    "scanning the brain of a worm\n" +
                                    "and transfer this map into a\n" +
                                    "computer", TextToSpeech.QUEUE_FLUSH, null);
                            break;
                        case "you are kidding":
                            tts.speak("Not at all! Look at our worm we just connected the\n" +
                                    "computer with sensors, so the worm can see and eat", TextToSpeech.QUEUE_FLUSH, null);
                            break;
                        case "so what is the program like":
                            tts.speak("There is no program. What\n" +
                                    "you see IS the worm. We did\n" +
                                    "not write a program. The worm\n" +
                                    "acts like his own brain tells\n" +
                                    "him.", TextToSpeech.QUEUE_FLUSH, null);
                            break;
                        case "so the worm eats all the time":
                            tts.speak("Only when he is hungry, we do\n" +
                                    "not know before. We feed him\n" +
                                    "with the balls, but he eats just\n" +
                                    "when he likes.", TextToSpeech.QUEUE_FLUSH, null);
                            break;
                        case "that sounds scary":
                            tts.speak("A worm consists of 302\n" +
                                    "neurons", TextToSpeech.QUEUE_FLUSH, null);
                            break;
                        case "what about a mouse":
                            tts.speak("A mouse consists of about 100 millions of Neurons", TextToSpeech.QUEUE_FLUSH, null);
                            break;
                        case "and what about a human brain":
                            tts.speak("Our brain consists of about\n" +
                                    "100 Billions of Neurons", TextToSpeech.QUEUE_FLUSH, null);
                            break;
                        case "so this would be too much for a computer":
                            tts.speak("Are you sure?\n" +
                                    "We started with Memory sizes\n" +
                                    "of about 300 Bytes in the\n" +
                                    "1960s\n" +
                                    "Today – 100 Gigabytes is\n" +
                                    "problem for you?", TextToSpeech.QUEUE_FLUSH, null);
                            break;
                        case "thank your professor for comming":
                            tts.speak("i did not come personally. I am\n" +
                                    "dead for one year now. But my\n" +
                                    "students scaned my brain and\n" +
                                    "transferred it into this\n" +
                                    "computer.\n" +
                                    "Now i can live forever!", TextToSpeech.QUEUE_FLUSH, null);
                            break;
                        default:
                            tts.speak(result.get(0), TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
                break;
            }

        }
    }

    /*private void listPairedDevices()
    {
        pairedDevices = btAdapter.getBondedDevices();
        ArrayList list = new ArrayList();

        if (pairedDevices.size()>0)
        {
            for(BluetoothDevice bt : pairedDevices)
            {
                list.add(bt.getName() + "\n" + bt.getAddress()); //Get the device's name and the address
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        lstPairedDevices.setAdapter(adapter);
        lstPairedDevices.setOnItemClickListener(myListClickListener); //Method called when the device from the list is clicked
    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            device_name = ((TextView) view).getText().toString();
            device_address = device_name.substring(device_name.length() - 17);
        }
    };*/
}
