package ru.itbirds.trades.adapter;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import ru.itbirds.data.model.Message;
import ru.itbirds.trades.R;
import ru.itbirds.trades.common.IRecyclerItemMenuClickListener;
import ru.itbirds.trades.databinding.AnotherUserMessageBinding;
import ru.itbirds.trades.databinding.CurrentUserMessageBinding;

import static android.view.MotionEvent.ACTION_CANCEL;
import static android.view.MotionEvent.ACTION_OUTSIDE;
import static android.view.MotionEvent.ACTION_SCROLL;
import static android.view.MotionEvent.ACTION_UP;

public class MessageAdapter extends FirestoreRecyclerAdapter<Message, MessageAdapter.MessageHolder> {
    private final int MESSAGE_CURRENT_USER = 2;
    private String userId;
    private IRecyclerItemMenuClickListener mMenuClickListener;

    public MessageAdapter(@NonNull FirestoreRecyclerOptions<Message> options, String userId, IRecyclerItemMenuClickListener listener) {
        super(options);
        this.userId = userId;
        this.mMenuClickListener = listener;
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

    public class MessageCurrentHolder extends MessageHolder implements View.OnLongClickListener, PopupMenu.OnMenuItemClickListener, View.OnTouchListener {
        private final CurrentUserMessageBinding binding;
        private final PopupMenu mPopupMenu;

        MessageCurrentHolder(CurrentUserMessageBinding itemBinding) {
            super(itemBinding.getRoot());
            binding = itemBinding;
            itemView.setOnLongClickListener(this);
            itemView.setOnTouchListener(this);
            mPopupMenu = new PopupMenu(itemView.getContext(), itemView);
            mPopupMenu.getMenuInflater().inflate(R.menu.menu_chat_item, mPopupMenu.getMenu());
            mPopupMenu.setOnMenuItemClickListener(this);

        }

        void bind(Message message) {
            binding.setMessage(message);
            binding.executePendingBindings();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.edit_message:
                    mMenuClickListener.editMessage(binding.getMessage());
                    break;
                case R.id.delete_message:
                    mMenuClickListener.deleteMessage(binding.getMessage().getDocumentId());
                    break;
            }
            return true;
        }

        @Override
        public boolean onLongClick(View v) {
            binding.rlMessage.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.chart_background_item_choose));
            mPopupMenu.show();
            return true;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case ACTION_SCROLL:
                case ACTION_CANCEL:
                case ACTION_UP:
                case ACTION_OUTSIDE:
                    binding.rlMessage.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.chart_background));
            }
            return false;
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
