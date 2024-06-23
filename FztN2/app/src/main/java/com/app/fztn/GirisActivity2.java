package com.app.fztn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class GirisActivity2 extends AppCompatActivity {



        private BottomNavigationView bottomNavigationView;
        private AnasayfaFragment anasayfaFragment = new AnasayfaFragment();
        private HesabımFragment hesabımFragment = new HesabımFragment();
        private ProgramFragment  programFragment = new  ProgramFragment();
        private  RandevuFragment  randevuFragment = new  RandevuFragment();


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_giris2);
            bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
            anasayfaFragment = new AnasayfaFragment();
            hesabımFragment = new HesabımFragment();
            programFragment = new ProgramFragment();
            randevuFragment = new RandevuFragment();
            setFragment(anasayfaFragment);


            bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int itemId = item.getItemId();
                    if (itemId == R.id.Anasayfa_bottom) {
                        setFragment(anasayfaFragment);
                        return true;
                    } else if (itemId == R.id.Programlarım_bottom) {
                        setFragment(programFragment);
                        return true;
                    } else if (itemId == R.id.Randevularım_bottom) {
                        setFragment(randevuFragment);
                        return true;
                    } else if (itemId == R.id.hesabım_bottom) {
                        setFragment(hesabımFragment);
                        return true;
                    }
                    return false;
                }

            });

        }

        private void setFragment(Fragment fragment){
            FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container,fragment);
            transaction.commit();



        }
    }
