package ru.itbirds.trades.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.Objects;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.itbirds.data.model.Message;
import ru.itbirds.data.model.Sticker;
import ru.itbirds.trades.R;
import ru.itbirds.trades.adapter.MessageAdapter;
import ru.itbirds.trades.adapter.StickersAdapter;
import ru.itbirds.trades.common.App;
import ru.itbirds.trades.common.IRecyclerItemMenuClickListener;
import ru.itbirds.trades.common.IStickerSender;
import ru.itbirds.trades.common.ItemOffsetDecoration;
import ru.itbirds.trades.common.SingleActivity;
import ru.itbirds.trades.databinding.ChatBinding;
import ru.itbirds.trades.util.NavBarSizeUtil;
import ru.itbirds.trades.viewmodels.ChatViewModel;
import ru.itbirds.trades.viewmodels.ChatViewModelFactory;

import static android.app.Activity.RESULT_OK;
import static ru.itbirds.data.Constants.COMPANY_SYMBOL;
import static ru.itbirds.data.Constants.KEYBOARD_HEIGHT;
import static ru.itbirds.data.Constants.MESSAGE_KEY;
import static ru.itbirds.data.Constants.PREF_NAME;


public class ChatFragment extends Fragment implements IRecyclerItemMenuClickListener, IStickerSender {

    private ChatViewModel mViewModel;
    private String mSymbol;
    private Toolbar mToolbar;
    private ChatBinding mBinding;
    private MessageAdapter mAdapterMessage;
    private RecyclerView mRecyclerViewMessage;
    private int mKeyboardHeight;
    private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener;
    private View.OnClickListener onClickListenerSendMessage;
    private InputMethodManager mImm;
    private SharedPreferences mSharedPreferences;
    private static final int RESULT_LOAD_IMAGE = 101;
    @Inject
    ChatViewModelFactory chatViewModelFactory;
    private LinearLayoutManager mLinearLayoutManager;
    private int mLastVisibleItemPosition;
    private boolean mNeedToScroll = true;
    private String mEditMessageText;

    static ChatFragment newInstance(Bundle bundle) {
        ChatFragment chatFragment = new ChatFragment();
        chatFragment.setArguments(bundle);
        return chatFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        App.getAppComponent().inject(this);
        if (getArguments() != null) {
            mSymbol = getArguments().getString(COMPANY_SYMBOL);
        }
        mViewModel = ViewModelProviders.of(this, chatViewModelFactory).get(ChatViewModel.class);
        mSharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if (mSharedPreferences.contains(KEYBOARD_HEIGHT))
            mKeyboardHeight = mSharedPreferences.getInt(KEYBOARD_HEIGHT, 0);
        mImm = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = ChatBinding.inflate(inflater, container, false);
        mToolbar = mBinding.toolbar;
        configToolbar();
        mBinding.setVm(mViewModel);
        if (savedInstanceState != null) {
            if (savedInstanceState.getString(MESSAGE_KEY) != null)
                mBinding.etMessage.setText(savedInstanceState.getString(MESSAGE_KEY));
        }
        mBinding.etMessage.requestFocus();
        mBinding.setVisibleKeyboard(false);
        mBinding.setVisibleStickers(false);
        mBinding.setVisibleCancel(false);
        configRecyclerMessage(savedInstanceState);
        configRecyclerStickers();
        configGlobalLayoutListener();
        buttonsListener();
        return mBinding.getRoot();
    }

    private void buttonsListener() {
        onClickListenerSendMessage = v -> {
            mViewModel.sendMessage(mSymbol, mBinding.etMessage.getText().toString());
            mBinding.etMessage.setText("");
            mRecyclerViewMessage.scrollToPosition(mAdapterMessage.getItemCount() - 1);
        };
        mBinding.ivSend.setOnClickListener(onClickListenerSendMessage);
        mBinding.ivCancel.setOnClickListener(v -> {
            mBinding.setVisibleCancel(false);
            mBinding.etMessage.setText("");
            mBinding.ivSend.setOnClickListener(onClickListenerSendMessage);
        });
        mBinding.ivSticker.setOnClickListener(v -> {
            mBinding.setVisibleStickers(true);
            mBinding.rlStickers.getLayoutParams().height = mKeyboardHeight;
            if (!mBinding.getVisibleKeyboard()) {
                mBinding.rlStickers.requestLayout();
            }
            hideKeyboard(v);

        });
        mBinding.ivKeyboard.setOnClickListener(v -> {
            mBinding.rlStickers.getLayoutParams().height = 0;
            showKeyboard();
            mBinding.setVisibleStickers(false);
        });
        mBinding.etMessage.setOnClickListener(v -> {
            mBinding.rlStickers.getLayoutParams().height = 0;
            showKeyboard();
            mBinding.setVisibleStickers(false);
        });
        mBinding.ivAddSticker.setOnClickListener(v -> openGallery());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                && null != data) {
            uploadSticker(data.getData());

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadSticker(Uri uri) {
        mViewModel.uploadSticker(uri);
    }

    private void hideKeyboard(View v) {
        mBinding.setVisibleKeyboard(false);
        Objects.requireNonNull(mImm).hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    private void showKeyboard() {
        mBinding.setVisibleKeyboard(true);
        Objects.requireNonNull(mImm).showSoftInput(mBinding.etMessage, InputMethodManager.SHOW_IMPLICIT);
    }

    private void configGlobalLayoutListener() {
        mGlobalLayoutListener = () -> {
            if (!ChatFragment.this.isDetached()) {
                Rect r = new Rect();
                mBinding.getRoot().getWindowVisibleDisplayFrame(r);
                int screenHeight = mBinding.getRoot().getRootView().getHeight();
                int heightDiff = screenHeight - (r.bottom - r.top);
                int resId = getResources().getIdentifier("status_bar_height", "dimen", "android");
                if (resId > 0)
                    heightDiff -= getResources().getDimensionPixelSize(resId);
                int resIdNav = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
                if (resIdNav > 0 && NavBarSizeUtil.getNavigationBarSize(getContext()).x != 0)
                    heightDiff -= getResources().getDimensionPixelSize(resIdNav);

                if (heightDiff >= 150) {
                    scrollToLastMessage();
                    mNeedToScroll = false;
                    mBinding.rlStickers.getLayoutParams().height = 0;
                    mBinding.setVisibleStickers(false);
                    mBinding.setVisibleKeyboard(true);
                    if (mKeyboardHeight != heightDiff) {
                        mKeyboardHeight = heightDiff;
                        saveKeyboardHeight();
                    }
                } else {
                    mNeedToScroll = true;
                    mBinding.setVisibleKeyboard(false);

                }

            }
        };
    }


    private void saveKeyboardHeight() {
        mSharedPreferences.edit().putInt(KEYBOARD_HEIGHT, mKeyboardHeight).apply();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onActivityCreated(savedInstanceState);
    }

    private void configRecyclerStickers() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.columns_sticker));
        FirestoreRecyclerOptions<Sticker> stickersOptions = mViewModel.getStickers().setLifecycleOwner(this).build();
        StickersAdapter mAdapterStickers = new StickersAdapter(stickersOptions, this);
        mBinding.rvStickers.addItemDecoration(new ItemOffsetDecoration(getResources().getInteger(R.integer.columns_sticker)));
        mBinding.rvStickers.setAdapter(mAdapterStickers);
        mBinding.rvStickers.setLayoutManager(gridLayoutManager);
    }

    private void configRecyclerMessage(Bundle savedInstanceState) {
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mBinding.recyclerview.setLayoutManager(mLinearLayoutManager);
        mRecyclerViewMessage = mBinding.recyclerview;
        mRecyclerViewMessage.setItemAnimator(null);

        RecyclerView.AdapterDataObserver mAdapterObserver = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                scrollToNewMessage();

            }
        };
        FirestoreRecyclerOptions<Message> messagesOptions = mViewModel.getMessages(mSymbol).setLifecycleOwner(this).build();
        mViewModel.initSnapshot(messagesOptions.getSnapshots());
        mAdapterMessage = new MessageAdapter(messagesOptions, mViewModel.getUser().getUid(), this, mRecyclerViewMessage, savedInstanceState);
        mAdapterMessage.registerAdapterDataObserver(mAdapterObserver);
        mRecyclerViewMessage.setAdapter(mAdapterMessage);
        mRecyclerViewMessage.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                    mLastVisibleItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();
            }
        });

    }


    private void scrollToNewMessage() {
        if (mAdapterMessage.getItemCount() - 2 == mLinearLayoutManager.findLastVisibleItemPosition())
            mRecyclerViewMessage.scrollToPosition(mAdapterMessage.getItemCount() - 1);
    }

    private void scrollToLastMessage() {
        if (mNeedToScroll)
            mRecyclerViewMessage.scrollToPosition(mLastVisibleItemPosition);
    }

    private void configToolbar() {
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(mToolbar);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(view -> ((SingleActivity) Objects.requireNonNull(getActivity())).popBackStack(false));
        mToolbar.setTitle(mSymbol);
    }


    @Override
    public void onResume() {
        mBinding.getRoot().getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);
        super.onResume();
    }

    @Override
    public void onPause() {
        mBinding.getRoot().getViewTreeObserver().removeOnGlobalLayoutListener(mGlobalLayoutListener);
        super.onPause();
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(MESSAGE_KEY, mBinding.etMessage.getText().toString());
        mAdapterMessage.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void editMessage(Message message) {
        mBinding.setVisibleCancel(true);
        mBinding.etMessage.requestFocus();
        mEditMessageText = message.getText();
        mBinding.etMessage.setText(mEditMessageText);
        mBinding.etMessage.setSelection(message.getText().length());
        showKeyboard();
        View.OnClickListener onClickListenerEditMessage = v -> {
            if (!mEditMessageText.equals(mBinding.etMessage.getText().toString()))
                mViewModel.editMessage(mSymbol, mBinding.etMessage.getText().toString(), message.getDocumentId());
            mBinding.setVisibleCancel(false);
            mBinding.etMessage.setText("");
            mRecyclerViewMessage.scrollToPosition(mAdapterMessage.getItemCount() - 1);
            mBinding.ivSend.setOnClickListener(onClickListenerSendMessage);
        };
        mBinding.ivSend.setOnClickListener(onClickListenerEditMessage);
    }

    @Override
    public void sendSticker(String url) {
        mViewModel.sendSticker(url, mSymbol);
        mRecyclerViewMessage.scrollToPosition(mAdapterMessage.getItemCount() - 1);
    }

    @Override
    public void deleteMessage(String documentId) {
        mViewModel.deleteMessage(mSymbol, documentId);
    }
}
