package ru.itbirds.trades.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import java.util.Objects;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.itbirds.data.model.Message;
import ru.itbirds.trades.adapter.MessageAdapter;
import ru.itbirds.trades.common.App;
import ru.itbirds.trades.common.IRecyclerItemMenuClickListener;
import ru.itbirds.trades.databinding.ChatBinding;
import ru.itbirds.trades.viewmodels.ChatViewModel;
import ru.itbirds.trades.viewmodels.ChatViewModelFactory;

import static ru.itbirds.data.Constants.COMPANY_SYMBOL;
import static ru.itbirds.data.Constants.MESSAGE_KEY;


public class ChatFragment extends Fragment implements IRecyclerItemMenuClickListener {


    private ChatViewModel mViewModel;
    private String mSymbol;
    private Toolbar mToolbar;
    private ChatBinding mBinding;
    private MessageAdapter mAdapter;
    private RecyclerView.AdapterDataObserver mAdapterObserver;
    private RecyclerView mRecyclerView;
    @Inject
    ChatViewModelFactory chatViewModelFactory;
    private View.OnClickListener onClickListenerSendMessage;

    public static ChatFragment newInstance() {
        return new ChatFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        App.getAppComponent().inject(this);
        if (getArguments() != null) {
            mSymbol = getArguments().getString(COMPANY_SYMBOL);
        }
        mViewModel = ViewModelProviders.of(this, chatViewModelFactory).get(ChatViewModel.class);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = ChatBinding.inflate(inflater, container, false);
        mToolbar = mBinding.toolbar;
        configToolbar();
        mBinding.setVm(mViewModel);
        onClickListenerSendMessage = v -> {
            mViewModel.sendMessage(mSymbol, mBinding.etMessage.getText().toString());
            mBinding.etMessage.setText("");
        };
        mBinding.setClickListener(onClickListenerSendMessage);
        if (savedInstanceState != null) {
            if (savedInstanceState.getString(MESSAGE_KEY) != null)
                mBinding.etMessage.setText(savedInstanceState.getString(MESSAGE_KEY));
        }
        mBinding.ivCancel.setOnClickListener(v -> {
            mBinding.setVisibleCanel(false);
            mBinding.etMessage.setText("");
            mBinding.setClickListener(onClickListenerSendMessage);
        });
        configRecycler();
        return mBinding.getRoot();
    }

    private void configRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mBinding.recyclerview.setLayoutManager(linearLayoutManager);
        mRecyclerView = mBinding.recyclerview;
        mRecyclerView.setItemAnimator(null);
        mAdapterObserver = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mBinding.recyclerview.scrollToPosition(positionStart);

            }
        };
        mRecyclerView.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1));
        mAdapter = new MessageAdapter(mViewModel.getMessages(mSymbol), mViewModel.getUser().getUid(), this);
        mBinding.recyclerview.setAdapter(mAdapter);
        registerForContextMenu(mBinding.recyclerview);
    }

    private void configToolbar() {
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(mToolbar);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(view -> Navigation.findNavController(view).navigateUp());
        mToolbar.setTitle(mSymbol);
    }

    @Override
    public void onStart() {
        if (mAdapter != null) {

            mAdapter.registerAdapterDataObserver(mAdapterObserver);
            mAdapter.startListening();
        }
        super.onStart();
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(MESSAGE_KEY, mBinding.etMessage.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        if (mAdapter != null) {
            mAdapter.unregisterAdapterDataObserver(mAdapterObserver);
            mAdapter.stopListening();
        }
        super.onStop();
    }

    @Override
    public void editMessage(Message message) {
        InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
        mBinding.etMessage.setText(message.getText());
        mBinding.etMessage.setSelection(message.getText().length());
        Objects.requireNonNull(imm).showSoftInput(mBinding.etMessage, InputMethodManager.SHOW_IMPLICIT);
        mBinding.ivCancel.setVisibility(View.VISIBLE);
        View.OnClickListener onClickListenerEditMessage = v -> {
            mViewModel.editMessage(mSymbol, mBinding.etMessage.getText().toString(), message.getDocumentId());
            mBinding.setVisibleCanel(false);
            mBinding.etMessage.setText("");
            mBinding.setClickListener(onClickListenerSendMessage);
        };
        mBinding.setClickListener(onClickListenerEditMessage);
    }

    @Override
    public void deleteMessage(String documentId) {
        mViewModel.deleteMessage(mSymbol, documentId);
    }
}
