package com.example.bspeech;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity {

    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    private ListView lstPairedDevices;
    private TextToSpeech tts;

    private String[] questions = {"hello professor how are you", "can you tell us something about your research on brains", "how interesting", "what is next", "are you kidding", "so what is the program like", "so the worm eats all the time", "that sounds scary", "what about a mouse", "and what about a human brain", "so this would be too much for a computer", "thank you professor for coming"};
    private String[] patterns = {"hello professor( how are you)?", "research", "interesting", "what( is| was) next", "(are you )?kidding", "so what is the program like", "all the time", "that sounds scary", "mouse", "human brain", "computer", "thank you professor"};
    private String[] answers = {
            "Fine, thank you",
            "Of course We started controlling the brain of a dog, using eye contact and gestures",
            "it took a while to manipulate the dog",
            "The next step was actually scanning the brain of a worm and transfer this map into a computer",
            "Not at all! Look at our worm we just connected the computer with sensors, so the worm can move and eat",
            "There is no program. What you see is the worm. The worm acts like a real one would.",
            "Only when he is hungry. We feed him with the balls, but he eats just when he likes.",
            "A worm consists of 302 neurons",
            "A mouse consists of about 100 millions of Neurons",
            "Our brain consists of about 100 Billions of Neurons",
            "Are you sure? We started with Memory sizes of about 300 Bytes in the 1960s. Today 100 Gigabytes is a problem for you?",
            "i did not come personally. I am dead for one year now. But my students scaned my brain and transferred it into this computer. Now i can live forever! \n forever! \n forever!"
    };


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

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, questions);
        lstPairedDevices.setAdapter(adapter);
        lstPairedDevices.setOnItemClickListener(myListClickListener);

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

                    for (int i = 0; i < patterns.length; i++){
                        if (result.get(0).toLowerCase().matches(".*" + patterns[i] + ".*")){
                            tts.speak(answers[i], TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }

                }
                break;
            }

        }
    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            for (int i = 0; i < patterns.length; i++){
                if (((TextView) view).getText().toString().toLowerCase().matches(".*" + patterns[i] + ".*")){
                    tts.speak(answers[i], TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        }
    };
}
