/*Copyright 2015 Matthew Blau

        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.
        */

package com.xavierdarkness.tipcalculator;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import java.text.NumberFormat;

public class MainActivity extends ActionBarActivity implements OnItemSelectedListener {
    private Spinner guestsSpinner;
    //user enters tip
    private EditText tipEditText;
    //user enters total
    private EditText billTotalEditText;
    private TextView tipTotalTextView;
    private TextView billTotalTextView;
    private TextView individualAmountView;
    private TextView individualTipTextView;
    private SeekBar mySeekBar;

    // work variables
    int numberOfGuests;
    double tip;
    int tipAmount;
    double billAmount;
    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();


    String tipField = "";
    String billField = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        guestsSpinner = (Spinner) findViewById(R.id.guestsSpinner);
        guestsSpinner.setOnItemSelectedListener(this);
        tipEditText = (EditText) findViewById(R.id.tipEditText);
        tipTotalTextView = (TextView) findViewById(R.id.tipTotalTextView);
        billTotalEditText = (EditText) findViewById(R.id.billTotalEditText);
        billTotalTextView = (TextView) findViewById(R.id.billTotalTextView);
        individualAmountView = (TextView) findViewById(R.id.individualAmountView);
        individualTipTextView = (TextView) findViewById(R.id.individualTipTextView);
        mySeekBar = (SeekBar) findViewById(R.id.mySeekBar);


        /* set up tipEditText Listener */
        tipEditText.addTextChangedListener(tipEditTextWatcher);
        billTotalEditText.addTextChangedListener(billTotalWatcher);
        tipEditText.setText("15");


         //set up the spinner
        Spinner guestsSpinner = (Spinner) findViewById(R.id.guestsSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.guests_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        guestsSpinner.setAdapter(adapter);

    }
    private TextWatcher tipEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    tipAmount = mySeekBar.getProgress();
                    tipEditText.setText(String.valueOf(tipAmount));
//                    if(tipEditText.length() > 3){
//                        tipEditText.setText("100");
//                    }

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
//                    if(tipAmount > 100){
//                        tipAmount = 100;
//                    }
//
//                    tipEditText.setText(String.valueOf(tipAmount));

                }
            });
            if(s.toString().length() > 0) {
                int i = Integer.parseInt(s.toString());
                mySeekBar.setProgress(i);
            }
            tipField = s.toString();
            tipEditText.setSelection(tipEditText.getText().length());
            updateAndCalculate();
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private TextWatcher billTotalWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            billField = s.toString();
            updateAndCalculate();
        }

        @Override
        public void afterTextChanged(Editable s) {


        }
    };



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Object value = parent.getItemAtPosition(position);
        numberOfGuests = position + 1;
        calculateSplit();


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
        public void calculateTip(){

            double tipAmount = billAmount * tip;
           tipTotalTextView.setText(currencyFormat.format(tipAmount));
        }
        public void calculateTotal() {
               double subtotal = billAmount * tip;
               double total = billAmount + subtotal;
            billTotalTextView.setText(currencyFormat.format(total));
        }
    public void updateAndCalculate() {
        try {
            billAmount = Double.parseDouble((billField.length() > 0) ? billField : "0");
            tip = Double.parseDouble((tipField.length() > 0) ? tipField : "0") / 100.0;
        } catch (NumberFormatException e) {
        }

        calculateTotal();
        calculateTip();
        calculateSplit();
    }
    public void calculateSplit() {
        double individualAmount = (billAmount + (billAmount * tip)) / numberOfGuests;
        double individualTip = (billAmount * tip) / numberOfGuests;
        individualAmountView.setText(currencyFormat.format(individualAmount));
        individualTipTextView.setText(currencyFormat.format(individualTip));
    }
}


