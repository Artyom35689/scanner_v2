package ru.zifirka.scanner_v2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {             //this is the start activity
    private EditText email_field;
    private EditText password_field;
    private Button button_login;
    private TextView register_txt;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email_field = findViewById(R.id.email_field);
        password_field = findViewById(R.id.password_field);
        button_login= findViewById(R.id.button_login);
        register_txt = findViewById(R.id.register_txt);
        button_login = findViewById(R.id.button_login);

        mAuth = FirebaseAuth.getInstance();

        register_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email_field.getText().toString().isEmpty()||password_field.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this,"Fields are empty", Toast.LENGTH_SHORT).show();
                }else {
                    mAuth.signInWithEmailAndPassword(email_field.getText().toString().trim(), password_field.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class); //
                                startActivity(intent);
                            }else{
                                Log.d("tag", Objects.requireNonNull(task.getException()).toString());
                                Toast.makeText(MainActivity.this,"Error with password or email", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });

                }
            }
        });
    }
}