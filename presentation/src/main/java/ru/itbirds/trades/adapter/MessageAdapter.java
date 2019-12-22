package ru.itbirds.trades.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.itbirds.data.model.Message;
import ru.itbirds.trades.databinding.AnotherUserMessageBinding;
import ru.itbirds.trades.databinding.CurrentUserMessageBinding;

public class MessageAdapter extends FirestoreRecyclerAdapter<Message, MessageAdapter.MessageHolder> {
    private String userId;
    private final int MESSAGE_CURRENT_USER = 2;

    public MessageAdapter(@NonNull FirestoreRecyclerOptions<Message> options, String userId) {
        super(options);
        this.userId = userId;
    }


    @Override
    protected void onBindViewHolder(@NonNull MessageHolder holder, int position, @NonNull Message model) {
        holder.bind(model);
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MESSAGE_CURRENT_USER) {
            return new MessageCurrentHolder(CurrentUserMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            return new MessageAnotherHolder(AnotherUserMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).getId().equals(userId)) {
            return MESSAGE_CURRENT_USER;
        }
        return 1;
    }

    abstract class MessageHolder extends RecyclerView.ViewHolder {

        MessageHolder(View view) {
            super(view);
        }

        abstract void bind(Message message);
    }

    public class MessageCurrentHolder extends MessageHolder {
        private final CurrentUserMessageBinding binding;

        MessageCurrentHolder(CurrentUserMessageBinding itemBinding) {
            super(itemBinding.getRoot());
            binding = itemBinding;

        }

        void bind(Message message) {
            binding.setMessage(message);
            binding.executePendingBindings();
        }
    }

    public class MessageAnotherHolder extends MessageHolder {
        private final AnotherUserMessageBinding binding;

        MessageAnotherHolder(AnotherUserMessageBinding itemBinding) {
            super(itemBinding.getRoot());
            binding = itemBinding;

        }

        void bind(Message message) {
            binding.setMessage(message);
            binding.executePendingBindings();
        }
    }

}
