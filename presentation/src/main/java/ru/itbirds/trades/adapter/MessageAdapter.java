package ru.itbirds.trades.adapter;

import android.os.Bundle;
import android.os.Parcelable;
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
import ru.itbirds.trades.databinding.AnotherUserStickerMessageBinding;
import ru.itbirds.trades.databinding.CurrentUserMessageBinding;
import ru.itbirds.trades.databinding.CurrentUserStickerMessageBinding;

import static android.view.MotionEvent.ACTION_CANCEL;
import static android.view.MotionEvent.ACTION_OUTSIDE;
import static android.view.MotionEvent.ACTION_SCROLL;
import static android.view.MotionEvent.ACTION_UP;
import static ru.itbirds.data.Constants.STATE_CHAT;
import static ru.itbirds.data.Constants.STICKER_TYPE;

public class MessageAdapter extends FirestoreRecyclerAdapter<Message, MessageAdapter.MessageHolder> {
    private final int STICKER_ANOTHER_USER = 2;
    private final int MESSAGE_CURRENT_USER = 3;
    private final int STICKER_CURRENT_USER = 4;
    private Parcelable mState;
    private RecyclerView mRecyclerView;
    private String mUserId;
    private IRecyclerItemMenuClickListener mMenuClickListener;


    public MessageAdapter(@NonNull FirestoreRecyclerOptions<Message> options, String userId, IRecyclerItemMenuClickListener listener, RecyclerView recyclerView, Bundle state) {
        super(options);
        this.mUserId = userId;
        this.mMenuClickListener = listener;
        this.mRecyclerView = recyclerView;
        if (state != null && state.getParcelable(STATE_CHAT) != null)
            this.mState = state.getParcelable(STATE_CHAT);
    }

    @Override
    public void stopListening() {
        if (mRecyclerView != null && mRecyclerView.getLayoutManager() != null)
            mState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        super.stopListening();
    }

    @Override
    public void onDataChanged() {
        if (mState != null) {
            if (mRecyclerView != null && mRecyclerView.getLayoutManager() != null) {
                mRecyclerView.getLayoutManager().onRestoreInstanceState(mState);
                mState = null;
            }
        }

    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (mRecyclerView != null && mRecyclerView.getLayoutManager() != null)
            outState.putParcelable(STATE_CHAT, mRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    protected void onBindViewHolder(@NonNull MessageHolder holder, int position, @NonNull Message model) {
        holder.bind(model);
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case MESSAGE_CURRENT_USER:
                return new MessageCurrentHolder(CurrentUserMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
            case STICKER_CURRENT_USER:
                return new StickerCurrentHolder(CurrentUserStickerMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
            case STICKER_ANOTHER_USER:
                return new StickerAnotherHolder(AnotherUserStickerMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
            default:
                return new MessageAnotherHolder(AnotherUserMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));


        }
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).getId().equals(mUserId)) {
            if (getItem(position).getType().equals(STICKER_TYPE))
                return STICKER_CURRENT_USER;
            return MESSAGE_CURRENT_USER;
        }
        if (getItem(position).getType().equals(STICKER_TYPE))
            return STICKER_ANOTHER_USER;
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
            mPopupMenu.getMenuInflater().inflate(R.menu.menu_chat_text_message, mPopupMenu.getMenu());
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
                    binding.rlMessage.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), android.R.color.transparent));
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

    public class StickerCurrentHolder extends MessageHolder implements View.OnLongClickListener, PopupMenu.OnMenuItemClickListener, View.OnTouchListener {
        private final CurrentUserStickerMessageBinding binding;
        private final PopupMenu mPopupMenu;

        StickerCurrentHolder(CurrentUserStickerMessageBinding itemBinding) {
            super(itemBinding.getRoot());
            binding = itemBinding;
            itemView.setOnLongClickListener(this);
            itemView.setOnTouchListener(this);
            mPopupMenu = new PopupMenu(itemView.getContext(), itemView);
            mPopupMenu.getMenuInflater().inflate(R.menu.menu_chat_sticker_message, mPopupMenu.getMenu());
            mPopupMenu.setOnMenuItemClickListener(this);

        }

        void bind(Message message) {
            binding.setMessage(message);
            binding.executePendingBindings();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.delete_message) {
                mMenuClickListener.deleteMessage(binding.getMessage().getDocumentId());
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
                    binding.rlMessage.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), android.R.color.transparent));
            }
            return false;
        }
    }

    public class StickerAnotherHolder extends MessageHolder {
        private final AnotherUserStickerMessageBinding binding;

        StickerAnotherHolder(AnotherUserStickerMessageBinding itemBinding) {
            super(itemBinding.getRoot());
            binding = itemBinding;

        }

        void bind(Message message) {
            binding.setMessage(message);
            binding.executePendingBindings();
        }
    }

}
