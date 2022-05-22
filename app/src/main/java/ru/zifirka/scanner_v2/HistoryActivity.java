package ru.zifirka.scanner_v2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private Button back_btn;
    private RecyclerView history_rec;
    private HistoryAdapter adapterH;
    private List<String> history_content;
    private FirebaseFirestore db1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        back_btn = findViewById(R.id.back_btn);
        history_rec = findViewById(R.id.history_recycler);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        history_rec.setLayoutManager(llm);
        db1 = FirebaseFirestore.getInstance();
        adapterH= new HistoryAdapter(history_content,db1);
        history_rec.setAdapter(adapterH);

        back_btn.setOnClickListener(v -> {
            Intent intent = new Intent(HistoryActivity.this, LoginActivity.class);
            startActivity(intent);
        });

    }
}