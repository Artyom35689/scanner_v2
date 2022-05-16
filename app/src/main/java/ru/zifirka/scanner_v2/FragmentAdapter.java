package ru.zifirka.scanner_v2;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentAdapter extends RecyclerView.Adapter<FragmentAdapter.FragmentAdapterViewHolder> {

    private List<String> results;
    private Button sendButton;
    private ImageButton imageButton;
    private Button cancel_btn;
    private int a = 1;

    private int SELECT_PICTURE = 200;
    private FirebaseFirestore db;

    public FragmentAdapter(List<String> results) {
        this.results = results;
    }

    @NonNull
    @Override
    public FragmentAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int recycler_h = R.layout.fragment_result_frahment;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(recycler_h, parent, false);
        FragmentAdapterViewHolder fragmentAdapterViewHolder = new FragmentAdapterViewHolder(view);

        return fragmentAdapterViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FragmentAdapterViewHolder holder, int position) {
        holder.text.setText(results.get(position));

    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class FragmentAdapterViewHolder extends RecyclerView.ViewHolder {

        final TextView text;
        final View view;

        public FragmentAdapterViewHolder(@NonNull View view) {
            super(view);
            this.view = view;
            text = view.findViewById(R.id.text_v);
            cancel_btn = view.findViewById(R.id.cancel_btn);
            //cancel_btn.setOnClickListener(v ->

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
        }

        private void imageChooser() {
            Log.d("TEST", "TEST");
            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);
            //resultLauncher.launch(i);
        }
    }
}
