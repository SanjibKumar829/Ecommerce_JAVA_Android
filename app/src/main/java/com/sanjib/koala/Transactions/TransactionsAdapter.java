package com.sanjib.koala.Transactions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.sanjib.koala.App.Constant;
import com.sanjib.koala.R;
import com.sanjib.koala.helper.Utils;
import com.sanjib.koala.networking.model.Transaction;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.ViewHolder> {
    private List<Transaction> transactions;
    private Context context;

    public TransactionsAdapter(Context context, List<Transaction> transactions) {
        this.context = context;
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.transaction_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Transaction transaction = transactions.get(position);
        if (transaction.order != null) {
            viewHolder.orderId.setText(context.getString(R.string.order_id, transaction.order.id));
            viewHolder.orderItems.setOrderItems(transaction.order.orderItems);

            if (transaction.order.status.equalsIgnoreCase(Constant.ORDER_STATUS_COMPLETED)) {
                // order is placed
                viewHolder.orderStatus.setText(context.getString(R.string.order_placed));
                viewHolder.orderStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGreen));
            } else {
                // order failed
                viewHolder.orderStatus.setText(context.getString(R.string.transaction_failed));
                viewHolder.orderStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.red_dull));
            }

            viewHolder.price.setText(context.getString(R.string.total_price_with_currency_string, transaction.order.amount));
        }

        viewHolder.timestamp.setText(Utils.getOrderTimestamp(transaction.created_at));
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.order_id)
        TextView orderId;

        @BindView(R.id.timestamp)
        TextView timestamp;

        @BindView(R.id.price)
        TextView price;

        @BindView(R.id.order_status)
        TextView orderStatus;

        @BindView(R.id.order_items)
        OrderItemsListView orderItems;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
