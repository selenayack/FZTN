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

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HesabımFragment extends Fragment {

    private DbAdapter db;

    private View view;

    private String[] arraySpinnerDTGün;
    private String[] arraySpinnerDTYıl = new String[101];

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_hesabim, container, false);

        Button logoutButton=view.findViewById(R.id.buttonLogout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout(view);
            }
        });

        this.arraySpinnerDTGün = new String[]{
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"
        };
        Spinner spinnerDTGün = view.findViewById(R.id.spinner4);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, arraySpinnerDTGün);
        spinnerDTGün.setAdapter(adapter);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int end = year - 100;
        int index = 0;

        for (int x = year; x > end; x--) {
            this.arraySpinnerDTYıl[index] = "" + x;
            index++;
        }

        Spinner spinnerDTYıl = view.findViewById(R.id.spinner5);
        ArrayAdapter<String> adapterYear = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, arraySpinnerDTYıl);
        spinnerDTYıl.setAdapter(adapterYear);


        Button buttonGuncelle = view.findViewById(R.id.buttonGuncelle);
        buttonGuncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Guncelle();
            }
        });


        TextView textViewErrorMessage = view.findViewById(R.id.textViewErrorMessage);
        textViewErrorMessage.setVisibility(View.GONE);

        // Diğer bileşenlerin tanımlamaları da buraya eklenebilir
        ImageView imageViewError = view.findViewById(R.id.imageViewerrormessage);
        imageViewError.setVisibility(View.GONE);


        EditText editTextHeightInches = view.findViewById(R.id.editTextHeightInches);
        editTextHeightInches.setVisibility(View.GONE);


        Spinner spinnerOlcuBirimi = view.findViewById(R.id.spinnerHaftalıkKg);

        spinnerOlcuBirimi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long l) {
                olcuDegistir();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //olcuDegistir();

            }
        });

        return view;
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);

    }
    private void Guncelle() {

        EditText editTextHeightCm = view.findViewById(R.id.editTextHeightCm);
        EditText editTextHeightInches = view.findViewById(R.id.editTextHeightInches);
        Spinner spinnerOlcuBirimi = view.findViewById(R.id.spinnerHaftalıkKg);
        EditText editTextWeight = view.findViewById(R.id.editTextWeight);
        EditText editTextYas = view.findViewById(R.id.editTextYas);
        Spinner spinnerDTGün = view.findViewById(R.id.spinner4);
        String stringDTGün = spinnerDTGün.getSelectedItem().toString();
        int DTGün = 0;

        DTGün = Integer.parseInt(stringDTGün);
        if (DTGün < 10) {
            stringDTGün = "0" + stringDTGün;


            Spinner spinnerDTAy = view.findViewById(R.id.spinner);
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

            Spinner spinnerDTYıl = view.findViewById(R.id.spinner5);
            String stringDTYıl = spinnerDTYıl.getSelectedItem().toString();
            int DTYıl = 0;

            DTYıl = Integer.parseInt(stringDTYıl);


            String dogumTarihi = stringDTGün + "-" + stringDTAy + "-" + DTYıl;
            RadioGroup radioGroupCinsiyet = view.findViewById(R.id.radioGrupCinsiyet);

            String stringHeightCm = editTextHeightCm.getText().toString();
            String stringHeightInches = editTextHeightInches.getText().toString();
            String stringOlcuBirimi = spinnerOlcuBirimi.getSelectedItem().toString();
            String stringWeight = editTextWeight.getText().toString();
            String stringYas = editTextYas.getText().toString();

            int selectedId = radioGroupCinsiyet.getCheckedRadioButtonId();

            RadioButton radioButtonCinsiyet = view.findViewById(selectedId);
            String stringCinsiyet = radioButtonCinsiyet.getText().toString();

            Log.d("Guncelle", "Cinsiyet: " + stringCinsiyet);


            // Güncelleme işlemini gerçekleştir
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String userId = user.getUid();

                ContentValues values = new ContentValues();
                values.put("user_id", userId);
                values.put("user_dogum_tarih", dogumTarihi); // Güncellenecek doğum tarihi
                values.put("user_cinsiyet", stringCinsiyet);
                values.put("user_boy", stringHeightCm); // Güncellenecek boy
                values.put("user_kilo", stringWeight); // Güncellenecek kilo
                values.put("user_yas", stringYas); // Güncellenecek yaş
                values.put("user_olcu", stringOlcuBirimi); // Güncellenecek ölçü

                DbAdapter db = new DbAdapter(getActivity()) {
                    @Override
                    public void onCreate(SQLiteDatabase sqLiteDatabase) {

                    }

                    @Override
                    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

                    }
                };
                db.open();

                boolean success = db.updateUser("USER", values, "user_id=?", new String[]{userId});

                if (success) {
                    Toast.makeText(getActivity(), "Bilgileriniz güncellendi", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Bilgilerinizi güncellerken bir hata oluştu", Toast.LENGTH_SHORT).show();
                }

                db.close();
            }
            ;
        }
    }


    private void olcuDegistir() {
        Spinner spinnerOlcuBirimi = view.findViewById(R.id.spinnerHaftalıkKg);
        String stringOlcuBirimi = spinnerOlcuBirimi.getSelectedItem().toString();

        EditText editTextHeightCm = view.findViewById(R.id.editTextHeightCm);
        EditText editTextHeightInches = view.findViewById(R.id.editTextHeightInches);
        String stringHeightCm = editTextHeightCm.getText().toString();
        String stringHeightInches = editTextHeightInches.getText().toString();

        double heightCm = 0;
        double heightFeet = 0;
        double heightInches = 0;

        TextView textViewCm = view.findViewById(R.id.textViewCm);
        TextView textViewKg = view.findViewById(R.id.textViewOlcuBirimi2);

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

        EditText editTextWeight = view.findViewById(R.id.editTextWeight);
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


}











