package com.sanjib.koala.Ui.cart;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.sanjib.koala.Db.AppDatabase;
import com.sanjib.koala.Db.CartItem;
import com.sanjib.koala.Payment.PayTMActivity;
import com.sanjib.koala.R;
import com.sanjib.koala.helper.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class CartBottomSheetFragment extends BottomSheetDialogFragment
        implements CartProductsAdapter.CartProductsAdapterListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.btn_checkout)
    Button btnCheckout;

    private Realm realm;
    private CartProductsAdapter mAdapter;
    private RealmResults<CartItem> cartItems;
    private RealmChangeListener<RealmResults<CartItem>> cartItemRealmChangeListener;

    public CartBottomSheetFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Making bottom sheet expanding to full height by default
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(dialog1 -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog1;

            FrameLayout bottomSheet = d.findViewById(R.id.design_bottom_sheet);
            BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
        });
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart_bottom_sheet, container, false);
        ButterKnife.bind(this, view);
        realm = Realm.getDefaultInstance();
        cartItems = realm.where(CartItem.class).findAllAsync();

        cartItemRealmChangeListener = cartItems -> {
            mAdapter.setData(cartItems);
            setTotalPrice();
        };

        cartItems.addChangeListener(cartItemRealmChangeListener);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mAdapter = new CartProductsAdapter(getActivity(), this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        setTotalPrice();
    }

    private void setTotalPrice() {
        if (cartItems != null) {
            float price = Utils.getCartPrice(cartItems);
            if (price > 0) {
                btnCheckout.setText(getString(R.string.btn_checkout, getString(R.string.price_with_currency, price)));
            } else {
                // if the price is zero, dismiss the dialog
                dismiss();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cartItems != null) {
            cartItems.removeChangeListener(cartItemRealmChangeListener);
        }

        if (realm != null) {
            realm.close();
        }
    }

    @OnClick(R.id.ic_close)
    void onCloseClick() {
        dismiss();
    }

    @OnClick(R.id.btn_checkout)
    void onCheckoutClick() {
        startActivity(new Intent(getActivity(), PayTMActivity.class));
        dismiss();
    }

    @Override
    public void onCartItemRemoved(int index, CartItem cartItem) {
        AppDatabase.removeCartItem(cartItem);
    }

//    @Override
//    public void onCartItemRemoved(int index, CartItem cartItem) {
//        AppDatabase.removeCartItem(cartItem);
//    }
}
