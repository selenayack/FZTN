package com.app.fztn;

import android.app.Dialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class RandevuFragment extends Fragment implements CalendarAdapter.OnItemListener {
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private Calendar selectedDate;

    private DbAdapter dbAdapter;
    private String selectedDateString;
    private String selectedTimeString;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_randevu, container, false);
        initWidgets(view);
        selectedDate = Calendar.getInstance();
        setMonthView();

        dbAdapter = new DbAdapter(getContext()) {
            @Override
            public void onCreate(SQLiteDatabase sqLiteDatabase) {

            }

            @Override
            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

            }
        };

        Button previousButton2 = view.findViewById(R.id.previous_button2);
        previousButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextMonthAction(v);
            }
        });

        Button previousButton = view.findViewById(R.id.previous_button);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousMonthAction(v);
            }
        });

        return view;
    }

    private void initWidgets(View view) {
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);
        monthYearText = view.findViewById(R.id.monthYearTV);
    }

    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    private ArrayList<String> daysInMonthArray(Calendar date) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        Calendar calendar = (Calendar) date.clone();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int startDayOfWeek = (dayOfWeek - calendar.getFirstDayOfWeek() + 7) % 7;

        for (int i = 1; i <= 42; i++) {
            if (i <= startDayOfWeek || i > daysInMonth + startDayOfWeek) {
                daysInMonthArray.add("");
            } else {
                daysInMonthArray.add(String.valueOf(i - startDayOfWeek));
            }
        }
        return daysInMonthArray;
    }

    private String monthYearFromDate(Calendar date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy", new Locale("tr"));
        return formatter.format(date.getTime());
    }

    public void previousMonthAction(View view) {
        selectedDate.add(Calendar.MONTH, -1);
        setMonthView();
    }

    public void nextMonthAction(View view) {
        selectedDate.add(Calendar.MONTH, 1);
        setMonthView();
    }

    @Override
    public void onItemClick(int position, String dayText) {
        if (!dayText.equals("")) {
            selectedDateString = dayText + " " + monthYearFromDate(selectedDate);
            showTimePickerDialog();
        }
    }

    private void showTimePickerDialog() {

            Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.dialog);

            TextView textViewTitle = dialog.findViewById(R.id.textViewTitle);
            textViewTitle.setText(selectedDateString);

            RecyclerView recyclerViewTimes = dialog.findViewById(R.id.recyclerViewTimes);
            recyclerViewTimes.setLayoutManager(new GridLayoutManager(getContext(), 4));

            ArrayList<String> timeList = new ArrayList<>();
            for (int hour = 9; hour <= 16; hour++) {
                timeList.add(String.format("%02d:00", hour));
            }

        ArrayList<Boolean> isTimeOccupiedList = new ArrayList<>(); // Dolu saatlerin listesi
        for (int hour = 9; hour <= 16; hour++) {
            // Saatin dolu olup olmadığını kontrol edin ve listeye ekleyin
            String timeString = String.format("%02d:00", hour);
            isTimeOccupiedList.add(dbAdapter.isDateTimeOccupied(selectedDateString, timeString));
        }

        TimeAdapter adapter = new TimeAdapter(timeList, isTimeOccupiedList, time -> {
            selectedTimeString = time;
            //Toast.makeText(getContext(), "Selected Time: " + time, Toast.LENGTH_SHORT).show();
        });

      // RecyclerView için adapteri ayarla
        recyclerViewTimes.setAdapter(adapter);


        recyclerViewTimes.setAdapter(adapter);

            Button buttonConfirm = dialog.findViewById(R.id.buttonConfirm);
            buttonConfirm.setOnClickListener(v -> {
                if (selectedTimeString != null) {
                    // Veritabanı kontrolü
                    DbAdapter dbAdapter = new DbAdapter(getContext()) {
                        @Override
                        public void onCreate(SQLiteDatabase sqLiteDatabase) {

                        }

                        @Override
                        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

                        }
                    };
                    dbAdapter.open();
                    if (dbAdapter.isDateTimeOccupied(selectedDateString, selectedTimeString)) {
                        Toast.makeText(getContext(), "Bu saat ve tarihte zaten bir randevu var.", Toast.LENGTH_SHORT).show();
                    } else {
                        saveAppointment(selectedDateString, selectedTimeString);
                        dialog.dismiss();
                    }
                    dbAdapter.close();
                } else {
                    Toast.makeText(getContext(), "Lütfen bir saat seçiniz.", Toast.LENGTH_SHORT).show();
                }
            });

            dialog.show();
        }


    private void saveAppointment(String date, String time) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Randevuyu veritabanına kaydetme işlemleri...
        DbAdapter dbAdapter = new DbAdapter(getContext()) {
            @Override
            public void onCreate(SQLiteDatabase sqLiteDatabase) {

            }

            @Override
            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

            }
        };
        dbAdapter.open();
        long result = dbAdapter.insertRandevu(userId, date, time);
        dbAdapter.close();

        // Kaydetme işleminin sonucuna göre kullanıcıya uygun bir mesaj gösterin
        if (result != -1) {
            Toast.makeText(getContext(), "Randevu başarıyla kaydedildi.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Randevu kaydedilirken bir hata oluştu.", Toast.LENGTH_SHORT).show();
        }
    }
}
