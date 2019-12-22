package ru.itbirds.trades.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.itbirds.trades.adapter.MessageAdapter;
import ru.itbirds.trades.databinding.ChatBinding;
import ru.itbirds.trades.viewmodels.ChatViewModel;

import static ru.itbirds.data.Constants.COMPANY_SYMBOL;


public class ChatFragment extends Fragment {

    private ChatViewModel mViewModel;
    private String mSymbol;
    private Toolbar mToolbar;
    private ChatBinding mBinding;
    private MessageAdapter mAdapter;

    public static ChatFragment newInstance() {
        return new ChatFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(this).get(ChatViewModel.class);
        if (getArguments() != null) {
            mSymbol = getArguments().getString(COMPANY_SYMBOL);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = ChatBinding.inflate(inflater, container, false);
        mToolbar = mBinding.toolbar;
        configToolbar();
        mBinding.setVm(mViewModel);
        mBinding.setClickListener(v -> {
            mViewModel.sendMessage(mSymbol, mBinding.etMessage.getText().toString());
            mBinding.etMessage.setText("");
        });

        mAdapter = new MessageAdapter(mViewModel.getMessages(mSymbol), mViewModel.getUser().getUid());
        mBinding.recyclerview.setAdapter(mAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mBinding.recyclerview.setLayoutManager(linearLayoutManager);
        mBinding.recyclerview.setItemAnimator(null);
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mBinding.recyclerview.scrollToPosition(positionStart);

            }
        });
        return mBinding.getRoot();
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
        if (mAdapter != null)
            mAdapter.startListening();
        super.onStart();
    }

    @Override
    public void onStop() {
        if (mAdapter != null)
            mAdapter.stopListening();
        super.onStop();
    }
}
