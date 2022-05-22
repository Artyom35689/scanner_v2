package ru.zifirka.scanner_v2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryAdapterViewHolder> {
    private List<String> history;
    private FirebaseFirestore db;

    public HistoryAdapter(List<String> history_content, FirebaseFirestore db) {
        this.db = db;
        this.history = history_content;
    }

    @NonNull
    @Override
    public HistoryAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int rec = R.layout.history_fragment;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(rec, parent, false);
        HistoryAdapterViewHolder a = new HistoryAdapterViewHolder(view);
        return a;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapterViewHolder holder, int position) {
        holder.history_id.setText(history.get(position));
        holder.history_name.setText(db.collection("results").whereEqualTo("email", Query.Direction.valueOf(MainActivity.mAuth.getCurrentUser().getEmail()))
                .orderBy("name")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("a", document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.d("A", "Error getting documents: ", task.getException());
                    }
                }).toString());
        holder.final_img.setImageBitmap(StringToBitMap(db.collection("results").whereEqualTo("email", Query.Direction.valueOf(MainActivity.mAuth.getCurrentUser().getEmail()))
                .orderBy("image")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("a", document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.d("A", "Error getting documents: ", task.getException());
                    }
                })
                .toString()));

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    private Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch (Exception e) {
            //Toast.makeText()
            return null;
        }
    }

    public class HistoryAdapterViewHolder extends RecyclerView.ViewHolder {
        final ImageView final_img;
        final TextView history_name;
        final TextView history_id;

        public HistoryAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            this.final_img = itemView.findViewById(R.id.final_img);
            this.history_id = itemView.findViewById(R.id.history_id);
            this.history_name = itemView.findViewById(R.id.history_name);


        }
    }
}
