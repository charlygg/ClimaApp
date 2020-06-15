package com.clima.app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ClimaAdapter extends RecyclerView.Adapter<ClimaAdapter.NumberViewHolder> {

    private static final String TAG = ClimaAdapter.class.getSimpleName();

    private int mNumberItems;

    private static int viewHolderCount;

    private Toast mToast;

    final private ListItemClickListener mOnClickListener;

        public ClimaAdapter(int numberOfItems, ListItemClickListener listener){
        mNumberItems = numberOfItems;
        mOnClickListener = listener;
        viewHolderCount = 0;
    }

    @NonNull
    @Override
    public ClimaAdapter.NumberViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context c = viewGroup.getContext();
        int layoutIdForListItem = R.layout.number_list_item;
        LayoutInflater layoutInflater = LayoutInflater.from(c);
        boolean shouldAttach = false;

        View view = layoutInflater.inflate(layoutIdForListItem, viewGroup, shouldAttach);
        NumberViewHolder viewHolder = new NumberViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ClimaAdapter.NumberViewHolder numberViewHolder, int i) {
        Log.d(TAG, "#" + i);
        numberViewHolder.bind(i);
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    public interface ListItemClickListener{
        void onListItemClick(int clickedItemIndex);
    }

    class NumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView listItemNumberView;

        NumberViewHolder(@NonNull View itemView) {
            super(itemView);

            listItemNumberView = itemView.findViewById(R.id.tv_item_number);
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex){
            listItemNumberView.setText(String.valueOf(listIndex));
        }

        @Override
        public void onClick(View view) {
            int itemClicked = getAdapterPosition();
            mOnClickListener.onListItemClick(itemClicked);
        }
    }
}
