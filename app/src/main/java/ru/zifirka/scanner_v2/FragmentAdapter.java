package ru.zifirka.scanner_v2;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentAdapter extends RecyclerView.Adapter<FragmentAdapter.FragmentAdapterViewHolder> {

    private List<String> results;

    private FirebaseFirestore db;
    private LoginActivity activity;

    public FragmentAdapter(LoginActivity activity, List<String> results, FirebaseFirestore db) {
        this.results = results;
        this.activity = activity;
        this.db = db;
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
        holder.cancel_btn.setOnClickListener(v -> {
            results.remove(position);
            notifyDataSetChanged();
        });
        holder.imageButton.setOnClickListener(v -> {
            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);
            activity.button = holder.imageButton;
            activity.startActivityForResult(i, 443);
        });

        holder.sendButton.setOnClickListener(view1 -> {
            Map<String, Object> result = new HashMap<>();
            Date date = new Date();

            result.put("date", String.valueOf(date.getTime()));
            result.put("email", MainActivity.mAuth.getCurrentUser().getEmail());
            result.put("result", results.get(position));
            result.put("name", holder.editText.getText().toString());
            result.put("image", BitMapToString(((BitmapDrawable) holder.imageButton.getDrawable()).getBitmap()));


            db.collection("results")
                    .add(result)
                    .addOnSuccessListener(documentReference -> {
                        Log.d("a", "DocumentSnapshot added with ID: " + documentReference.getId());
                        results.remove(position);
                        notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> Log.w("a", "Error adding document", e));
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    private String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public class FragmentAdapterViewHolder extends RecyclerView.ViewHolder {

        final TextView text;
        final EditText editText;
        final View view;
        final ImageButton imageButton;
        final Button sendButton;
        final Button cancel_btn;

        public FragmentAdapterViewHolder(@NonNull View view) {
            super(view);
            this.view = view;
            text = view.findViewById(R.id.text_v);
            editText = view.findViewById(R.id.name_edittext);
            cancel_btn = view.findViewById(R.id.cancel_btn);
            imageButton = view.findViewById(R.id.imageButton);
            sendButton = view.findViewById(R.id.send_btn);
        }


    }
}