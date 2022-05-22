package ru.zifirka.scanner_v2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    //this is activity that used for functional app
    private Button logout_btn;
    private FirebaseAuth mAuth;
    private DatabaseReference ref;
    public FirebaseFirestore db; ///
    Button scButton;
    List<String> results;
    private RecyclerView FragmentRes;
    FragmentAdapter adapter;
    private Button history_btn;

    // массив результов
    public ImageButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logout_btn = findViewById(R.id.logout_btn);
        mAuth = FirebaseAuth.getInstance();

        scButton = findViewById(R.id.sc_button);
        db = FirebaseFirestore.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("Users");
        history_btn = findViewById(R.id.history_btn);

        history_btn.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this,HistoryActivity.class );
            startActivity(intent);
        });

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
        adapter = new FragmentAdapter(this, results, db);
        FragmentRes.setAdapter(adapter);

        // оздаем рекуклер на основе массива
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 443:
                if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
                    Uri uri = data.getData();
                    ContentResolver contentResolver = getContentResolver();
                    try {
                        InputStream inputStream = contentResolver.openInputStream(uri);
                        Bitmap b = BitmapFactory.decodeStream(inputStream);
                        if (Math.max(b.getWidth(), b.getHeight()) >= 2048)
                            throw new Exception("Image is too large");

                        button.setImageBitmap(b);
                    } catch (FileNotFoundException e) {
                        Toast.makeText(this, "Failed to get image", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

                if (intentResult != null && intentResult.getContents() != null) {
                    String resultString = intentResult.getContents();
                    results.add(resultString);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), "Oops", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}