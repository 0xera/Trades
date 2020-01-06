package ru.itbirds.trades.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.itbirds.data.model.Sticker;
import ru.itbirds.trades.common.IStickerSender;
import ru.itbirds.trades.databinding.StickerBinding;

public class StickersAdapter extends FirestoreRecyclerAdapter<Sticker, StickersAdapter.StickerHolder> {
    private IStickerSender mSender;

    public StickersAdapter(@NonNull FirestoreRecyclerOptions<Sticker> options, IStickerSender sender) {
        super(options);
        mSender = sender;
    }


    @Override
    protected void onBindViewHolder(@NonNull StickerHolder holder, int position, @NonNull Sticker model) {
        holder.bind(model);
    }

    @NonNull
    @Override
    public StickerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StickerHolder(StickerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }


    class StickerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final StickerBinding binding;

        StickerHolder(StickerBinding itemBinding) {
            super(itemBinding.getRoot());
            binding = itemBinding;
            itemView.setOnClickListener(this);

        }

        void bind(Sticker sticker) {
            binding.setSticker(sticker);
            binding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {
            mSender.sendSticker(binding.getSticker().getUrl());
        }
    }

}
