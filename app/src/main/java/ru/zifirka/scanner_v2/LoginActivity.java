package ru.zifirka.scanner_v2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.widget.TextView;


public class LoginActivity extends AppCompatActivity {
    //this is activity that used for functional app
    private Button logout_btn;
    private FirebaseAuth mAuth;
    private DatabaseReference ref;
    private TextView textView;
    private String email;
    FirebaseFirestore db;
    Button scButton;
    List<String> results;
    private RecyclerView FragmentRes;
    FragmentAdapter adapter;

    // массив результов

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logout_btn = findViewById(R.id.logout_btn);
        mAuth = FirebaseAuth.getInstance();

        scButton = findViewById(R.id.sc_button);
        db = FirebaseFirestore.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("Users");

        scButton.setOnClickListener(v -> {
            IntentIntegrator intentIntegrator = new IntentIntegrator(LoginActivity.this);
            intentIntegrator.setPrompt("For flash use volume up key");
            intentIntegrator.setBeepEnabled(true);
            intentIntegrator.setOrientationLocked(true);
            intentIntegrator.setCaptureActivity(Capture.class);
            intentIntegrator.initiateScan();
        });

        logout_btn.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        });

        results=new ArrayList<>();
        FragmentRes = findViewById(R.id.recyclerview);
        adapter = new FragmentAdapter(results);
        FragmentRes.setAdapter(adapter);

        // оздаем рекуклер на основе массива
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult != null && intentResult.getContents() != null) {
            String resultString = intentResult.getContents();
            results.add(resultString);
            adapter.notifyDataSetChanged();
            // убираем транзакшн
            // вместо этого пихаем новый результ в массив и говорим рекуклер.нотифайдатачанжет
        } else {
            Toast.makeText(getApplicationContext(), "Oops", Toast.LENGTH_SHORT).show();
        }
    }
}