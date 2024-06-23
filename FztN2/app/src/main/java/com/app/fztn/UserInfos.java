package com.app.fztn;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.User;

import java.util.Calendar;

public class UserInfos extends AppCompatActivity {
    private DbAdapter db;

    private View view;

    private String[] arraySpinnerDTGün;
    private String[] arraySpinnerDTYıl = new String[101];

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_infos);

        this.arraySpinnerDTGün = new String[]{
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"
        };
        Spinner spinnerDTGün = findViewById(R.id.spinner4);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arraySpinnerDTGün);
        spinnerDTGün.setAdapter(adapter);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int end = year - 100;
        int index = 0;

        for (int x = year; x > end; x--) {
            this.arraySpinnerDTYıl[index] = "" + x;
            index++;
        }

        Spinner spinnerDTYıl = findViewById(R.id.spinner5);
        ArrayAdapter<String> adapterYear = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arraySpinnerDTYıl);
        spinnerDTYıl.setAdapter(adapterYear);

        Button buttonKaydet = findViewById(R.id.buttonKaydet);
        buttonKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Kaydet();
                Intent intent = new Intent(UserInfos.this,GirisActivity2.class);
                startActivity(intent);
                finish();
            }
        });

        TextView textViewErrorMessage = findViewById(R.id.textViewErrorMessage);
        textViewErrorMessage.setVisibility(View.GONE);

        ImageView imageViewError = findViewById(R.id.imageViewerrormessage);
        imageViewError.setVisibility(View.GONE);

        EditText editTextHeightInches = findViewById(R.id.editTextHeightInches);
        editTextHeightInches.setVisibility(View.GONE);

        Spinner spinnerOlcuBirimi = findViewById(R.id.spinnerHaftalıkKg);
        spinnerOlcuBirimi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                olcuDegistir();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });
    }
    private void olcuDegistir() {
        Spinner spinnerOlcuBirimi = findViewById(R.id.spinnerHaftalıkKg);
        String stringOlcuBirimi = spinnerOlcuBirimi.getSelectedItem().toString();

        EditText editTextHeightCm = findViewById(R.id.editTextHeightCm);
        EditText editTextHeightInches = findViewById(R.id.editTextHeightInches);
        String stringHeightCm = editTextHeightCm.getText().toString();
        String stringHeightInches = editTextHeightInches.getText().toString();

        double heightCm = 0;
        double heightFeet = 0;
        double heightInches = 0;

        TextView textViewCm = findViewById(R.id.textViewCm);
        TextView textViewKg = findViewById(R.id.textViewOlcuBirimi2);

        if (stringOlcuBirimi.startsWith("p")) {
            editTextHeightInches.setVisibility(View.VISIBLE);
            textViewCm.setText("feet and inches");
            textViewKg.setText("pound");

            try {
                heightCm = Double.parseDouble(stringHeightCm);
            } catch (NumberFormatException nfe) {
                // Handle the exception
            }

            if (heightCm != 0) {
                heightFeet = (heightCm * 0.3937008) / 12;
                int intHeightFeet = (int) heightFeet;
                editTextHeightCm.setText("" + intHeightFeet);
            }
        } else {
            editTextHeightInches.setVisibility(View.GONE);
            textViewCm.setText("cm");
            textViewKg.setText("kg");

            try {
                heightFeet = Double.parseDouble(stringHeightCm);
            } catch (NumberFormatException nfe) {
                // Handle the exception
            }

            try {
                heightInches = Double.parseDouble(stringHeightInches);
            } catch (NumberFormatException nfe) {
                // Handle the exception
            }

            if (heightFeet != 0 && heightInches != 0) {
                heightCm = ((heightFeet * 12) + heightInches) * 2.54;
                heightCm = Math.round(heightCm);
                editTextHeightCm.setText("" + heightCm);
            }
        }

        EditText editTextWeight = findViewById(R.id.editTextWeight);
        String stringWeight = editTextWeight.getText().toString();
        double doubleWeight = 0;

        try {
            doubleWeight = Double.parseDouble(stringWeight);
        } catch (NumberFormatException nfe) {
            // Handle the exception
        }

        if (doubleWeight != 0) {
            if (stringOlcuBirimi.startsWith("p")) {
                doubleWeight = Math.round(doubleWeight / 0.45359237);
            } else {
                doubleWeight = Math.round(doubleWeight * 0.45359237);
            }
            editTextWeight.setText("" + doubleWeight);
        }
    }


    private void Kaydet() {

        TextView textViewErrorMessage =findViewById(R.id.textViewErrorMessage);
        ImageView imageViewError = findViewById(R.id.imageViewerrormessage);

        String errorMessage = "";

        Spinner spinnerDTGün = findViewById(R.id.spinner4);
        String stringDTGün = spinnerDTGün.getSelectedItem().toString();
        int DTGün = 0;
        try {
            DTGün = Integer.parseInt(stringDTGün);
            if (DTGün < 10) {
                stringDTGün = "0" + stringDTGün;
            }
        } catch (NumberFormatException nfe) {
            errorMessage = "Lütfen doğum gününüzü girin";
        }

        Spinner spinnerDTAy = findViewById(R.id.spinner);
        String stringDTAy = spinnerDTAy.getSelectedItem().toString();
        if (stringDTAy.startsWith("Oc")) {
            stringDTAy = "01";
        } else if (stringDTAy.startsWith("Şu")) {
            stringDTAy = "02";
        } else if (stringDTAy.startsWith("Mar")) {
            stringDTAy = "03";
        } else if (stringDTAy.startsWith("Ni")) {
            stringDTAy = "04";
        } else if (stringDTAy.startsWith("May")) {
            stringDTAy = "05";
        } else if (stringDTAy.startsWith("Haz")) {
            stringDTAy = "06";
        } else if (stringDTAy.startsWith("Tem")) {
            stringDTAy = "07";
        } else if (stringDTAy.startsWith("Ağ")) {
            stringDTAy = "08";
        } else if (stringDTAy.startsWith("Ey")) {
            stringDTAy = "09";
        } else if (stringDTAy.startsWith("Ek")) {
            stringDTAy = "10";
        } else if (stringDTAy.startsWith("Ka")) {
            stringDTAy = "11";
        } else if (stringDTAy.startsWith("Ar")) {
            stringDTAy = "12";
        }

        Spinner spinnerDTYıl = findViewById(R.id.spinner5);
        String stringDTYıl = spinnerDTYıl.getSelectedItem().toString();
        int DTYıl = 0;
        try {
            DTYıl = Integer.parseInt(stringDTYıl);
        } catch (NumberFormatException nfe) {
            errorMessage = "Lütfen doğum yılınızı girin";
        }

        String dogumTarihi = stringDTGün + "-" + stringDTAy + "-" + DTYıl;

        RadioGroup radioGroupCinsiyet = findViewById(R.id.radioGrupCinsiyet);
        int selectedId = radioGroupCinsiyet.getCheckedRadioButtonId();
        RadioButton radioButtonCinsiyet = findViewById(selectedId);

        if (radioGroupCinsiyet.getCheckedRadioButtonId() == -1) {
            // Cinsiyet seçilmemişse
            errorMessage = "Cinsiyet seçiniz.";
            textViewErrorMessage.setText(errorMessage);
            textViewErrorMessage.setVisibility(View.VISIBLE);
            imageViewError.setVisibility(View.VISIBLE);
            return ;
        }

        String stringGender = radioButtonCinsiyet.getText().toString();

        EditText editTextHeightInches = findViewById(R.id.editTextHeightInches);
        EditText editTextHeightCm = findViewById(R.id.editTextHeightCm);
        String stringHeightCm = editTextHeightCm.getText().toString();
        String stringHeightInches = editTextHeightInches.getText().toString();

        double heightCm = 0;
        double heightFeet = 0;
        double heightInches = 0;
        boolean metric = true;

        Spinner spinnerOlcuBirimi = findViewById(R.id.spinnerHaftalıkKg);
        String stringOlcuBiirimi = spinnerOlcuBirimi.getSelectedItem().toString();

        int intOlcu = spinnerOlcuBirimi.getSelectedItemPosition();

        if (intOlcu == 0) {
            stringOlcuBiirimi = "kg_cm";
        } else {
            stringOlcuBiirimi = "pound_inch";
            metric = false;
        }

        if (stringOlcuBiirimi.startsWith("p")) {
            metric = false;
        }

        if (metric) {
            try {
                heightCm = Double.parseDouble(stringHeightCm);
            } catch (NumberFormatException nfe) {
                errorMessage = "Boy(cm) sayı olmalı";
            }
        } else {
            try {
                heightFeet = Double.parseDouble(stringHeightCm);
            } catch (NumberFormatException nfe) {
                errorMessage = "Boy(feet) sayı olmalı";
            }
            try {
                heightInches = Double.parseDouble(stringHeightInches);
            } catch (NumberFormatException nfe) {
                errorMessage = "Boy(inch) sayı olmalı";
            }
            heightCm = ((heightFeet * 12) + heightInches) * 2.54;
            heightCm = Math.round(heightCm);
        }

        EditText editTextWeight = findViewById(R.id.editTextWeight);
        String stringWeight = editTextWeight.getText().toString();
        double doubleWeight = 0;

        if (stringOlcuBiirimi.startsWith("p")) {
            metric = false;
        }

        if (metric) {
            try {
                doubleWeight = Double.parseDouble(stringWeight);
            } catch (NumberFormatException nfe) {
                errorMessage = "Kilo sayı olmalı";
            }
        } else {
            doubleWeight = Math.round(doubleWeight * 0.45359237);
        }

        editTextWeight.setText("" + doubleWeight);


        EditText editTextYas = findViewById(R.id.editTextYas);
        String stringYas = editTextYas.getText().toString();


        if (errorMessage.isEmpty()) {
            textViewErrorMessage.setVisibility(View.GONE);
            imageViewError.setVisibility(View.GONE);





            DbAdapter db = new DbAdapter(this) {
                @Override
                public void onCreate(SQLiteDatabase sqLiteDatabase) {
                }

                @Override
                public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
                }
            };
            db.open();

            String dogumTarihiSQL = db.quoteSmart(dogumTarihi);
            String stringCinsiyetSQL = db.quoteSmart(stringGender);
            double heightCmSQL = db.quoteSmart(heightCm);
            String doubleWeightSQL = db.quoteSmart(stringWeight);
            String stringOlcuSQL = db.quoteSmart(stringOlcuBiirimi);
            String doubleYasSql = db.quoteSmart(stringYas);


            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String email = user.getEmail();
                String userId = user.getUid();
                String stringInput =  dogumTarihiSQL + ",'" + userId+"',"+stringCinsiyetSQL + "," + heightCmSQL + ",'" + email + "'," + stringOlcuSQL+ ","+doubleWeightSQL+","+doubleYasSql;

                db.insert("USER", "user_dogum_tarih,user_id,user_cinsiyet,user_boy,user_email,user_olcu,user_kilo,user_yas", stringInput);



            }


        } else {
            textViewErrorMessage.setText(errorMessage);
            textViewErrorMessage.setVisibility(View.VISIBLE);
            imageViewError.setVisibility(View.VISIBLE);
        }
    }}



