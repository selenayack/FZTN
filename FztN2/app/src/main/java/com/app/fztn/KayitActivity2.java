package com.app.fztn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

public class KayitActivity2 extends AppCompatActivity {
    private EditText yeniKullaniciMail,yeniKullanicisifre,yeniKullaniciİsim;
    private String txtEmail,txtSifre,txtİsim;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mReference;
    private HashMap<String,Object> mData;
    private FirebaseFirestore mFirestore;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit2);

        yeniKullaniciMail=findViewById(R.id.kayit_mail);
        yeniKullanicisifre=findViewById(R.id.kayit_parola);
        yeniKullaniciİsim=findViewById(R.id.kayit_kulad);

        mAuth = FirebaseAuth.getInstance();
        mReference= FirebaseDatabase.getInstance().getReference();
        mFirestore=FirebaseFirestore.getInstance();
    }

    //kayıt olma methodu
    public void kayitOl(View v){
        txtEmail=yeniKullaniciMail.getText().toString();
        txtSifre=yeniKullanicisifre.getText().toString();
        txtİsim=yeniKullaniciİsim.getText().toString();




        if(!TextUtils.isEmpty(txtİsim)&&!TextUtils.isEmpty(txtEmail)&&!TextUtils.isEmpty(txtSifre)){
            mAuth.createUserWithEmailAndPassword(txtEmail,txtSifre)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                mUser=mAuth.getCurrentUser();

                                // Kullanıcı adını Firebase Authentication'da ayarla
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(txtİsim)
                                        .build();

                                mUser.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> updateTask) {
                                                if (updateTask.isSuccessful()) {
                                                    // Kullanıcı adı başarıyla güncellendi
                                                }
                                            }
                                        });



                                mData=new HashMap<>();
                                mData.put("kullaniciAdı",txtİsim);
                                mData.put("kullaniciMail",txtEmail);
                                mData.put("kullaniciSifre",txtSifre);
                                mData.put("kullaniciId",mUser.getUid());
                                saveUserToFirebase();
                            }
                            else{
                                Toast.makeText(KayitActivity2.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }

        else{
            Toast.makeText(this,"E mail ve sifre bos olamaz",Toast.LENGTH_SHORT).show();
        }



    }


    private void saveUserToFirebase(){
        mFirestore.collection("Kullanıcılar").document(mUser.getUid())
                .set(mData)
                .addOnCompleteListener(KayitActivity2.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(KayitActivity2.this,"Kayit İslemi basarili ",Toast.LENGTH_SHORT).show();
                            Intent girisSayfasi=new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(girisSayfasi);
                        }
                        else{
                            Toast.makeText(KayitActivity2.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}