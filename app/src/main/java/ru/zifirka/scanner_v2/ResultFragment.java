package ru.zifirka.scanner_v2;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class ResultFragment extends Fragment {
    private String result;
    private TextView textV;
    private Button sendButton;
    private ImageButton imageButton;

    private int SELECT_PICTURE = 200;
    private FirebaseFirestore db;

    public ResultFragment(String result, FirebaseFirestore db) {
        super(R.layout.fragment_result_frahment);
        this.result = result;
        this.db = db;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result_frahment, container, false);
        textV = view.findViewById(R.id.text_v);
        textV.setText(result);
        imageButton = view.findViewById(R.id.imageButton);
        imageButton.setOnClickListener(v -> imageChooser());
        sendButton = view.findViewById(R.id.send_btn);
        sendButton.setOnClickListener(view1 -> {
            Map<String, Object> result = new HashMap<>();
            Date date = new Date();
            result.put(String.valueOf(date.getTime()), result);
            db.collection("results")
                    .add(result)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("a", "DocumentSnapshot added with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("a", "Error adding document", e);
                        }
                    });
        });

        return view;
    }

    private void imageChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    imageButton.setImageURI(selectedImageUri);
                }
            }
        }
    }
}