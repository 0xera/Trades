package ru.itbirds.data.repositories;

import android.net.Uri;

import com.github.tifezh.kchartlib.chart.utils.DateUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import ru.itbirds.data.model.Message;
import ru.itbirds.data.model.Sticker;

import static ru.itbirds.data.Constants.DATE_FIELD;

public class ChatRepositoryImpl implements ChatRepository {
    private FirebaseFirestore mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseStorage mStorage;

    public ChatRepositoryImpl() {
        mDatabase = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance();
    }

    public FirebaseUser getUser() {
        return mFirebaseAuth.getCurrentUser();
    }

    public Query getMessages(String symbol) {
        try {
            return mDatabase.collection("chat").document("symbols").collection(symbol).orderBy(DATE_FIELD, Query.Direction.ASCENDING).whereGreaterThan(DATE_FIELD, getTime());
        } catch (ParseException e) {
            return mDatabase.collection("chat").document("symbols").collection(symbol).orderBy(DATE_FIELD, Query.Direction.ASCENDING);
        }

    }

    private long getTime() throws ParseException {
        return Objects.requireNonNull(DateUtil.DateFormat.parse(DateUtil.DateFormat.format(new Date()))).getTime();
    }

    public void sendMessage(String symbol, String message) {
        mDatabase.collection("chat").document("symbols").collection(symbol).add(new Message(getUser().getUid(), getUser().getDisplayName(), message, Objects.requireNonNull(getUser().getPhotoUrl()).toString(), null, "text"));

    }

    @Override
    public void sendSticker(String url, String symbol) {
        mDatabase.collection("chat").document("symbols").collection(symbol).add(new Message(getUser().getUid(), getUser().getDisplayName(), null, Objects.requireNonNull(getUser().getPhotoUrl()).toString(), url, "sticker"));

    }

    @Override
    public void editMessage(String symbol, String message, String documentId) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("text", message);
        updates.put("edit", true);
        mDatabase.collection("chat").document("symbols").collection(symbol).document(documentId).update(updates);

    }

    @Override
    public void deleteMessage(String symbol, String documentId) {
        mDatabase.collection("chat").document("symbols").collection(symbol).document(documentId).delete();
    }

    @Override
    public Query getStickers() {
        return mDatabase.collection("stickers").orderBy("url");
    }

    @Override
    public void uploadSticker(Uri uri) {
        StorageReference storageReference = mStorage.getReference("stickers").child(UUID.randomUUID().toString());
        storageReference.putFile(uri)
                .addOnCompleteListener(task2 ->
                        addStickerToCollection(storageReference));
    }

    private void addStickerToCollection(StorageReference storageReference) {
        storageReference.getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if (task.getResult() != null)
                        mDatabase.collection("stickers").add(new Sticker(task.getResult().toString()));
                });
    }


}
