package ru.zifirka.scanner_v2;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class ResultFragment extends Fragment {
    private String result;
    private TextView textV;
    private Button sendButton;
    private ImageButton imageButton;
    private Button cancel_btn;
    private int a = 1;

    private int SELECT_PICTURE = 200;
    private FirebaseFirestore db;

    public ResultFragment(String result, FirebaseFirestore db) {
        super(R.layout.fragment_result_frahment);
        this.result = result;
        this.db = db;
    }

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), intent -> {
        if(intent.getResultCode() == Activity.RESULT_OK && intent.getData() != null) {
            Uri uri = intent.getData().getData();
            ContentResolver contentResolver = requireActivity().getContentResolver();
            try {
                InputStream inputStream = contentResolver.openInputStream(uri);
                Bitmap b = BitmapFactory.decodeStream(inputStream);
                if(Math.max(b.getWidth(), b.getHeight()) >= 2048)
                    throw new Exception("Image is too large");
                imageButton.setImageBitmap(b);
            } catch (FileNotFoundException e) {
                Toast.makeText(requireContext(), "Failed to get image", Toast.LENGTH_SHORT);
            } catch (Exception e) {
                Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT);
            }
        }
    });
}