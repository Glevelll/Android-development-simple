package com.project.foodapp.ui.Cookbook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.foodapp.R;
import com.project.foodapp.ui.Favorite.CardFav;
import com.project.foodapp.ui.Favorite.CardFavAdapter;

import java.util.List;

public class CardBookAdapter extends RecyclerView.Adapter<com.project.foodapp.ui.Cookbook.CardBookAdapter.ViewHolder> {
    private List<CardBook> cardList;
    private com.project.foodapp.ui.Cookbook.CardBookAdapter.OnItemClickListener onItemClickListener;
    private CardFavAdapter.OnDeleteInfoClickListener onDeleteInfoClickListener;

    public CardBookAdapter(List<CardBook> cards) {
        this.cardList = cards;
    }

    public void setOnClickListener(com.project.foodapp.ui.Cookbook.CardBookAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public void setOnDeleteInfoClickListener(CardFavAdapter.OnDeleteInfoClickListener listener) {
        this.onDeleteInfoClickListener = listener;
    }

    public interface OnDeleteInfoClickListener {
        void onDeleteInfoClick(int position);
    }

    @NonNull
    @Override
    public com.project.foodapp.ui.Cookbook.CardBookAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_other, parent, false);
        com.project.foodapp.ui.Cookbook.CardBookAdapter.ViewHolder viewHolder = new com.project.foodapp.ui.Cookbook.CardBookAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(com.project.foodapp.ui.Cookbook.CardBookAdapter.ViewHolder holder, int position) {
        CardBook card = cardList.get(position);
        holder.titleTextView.setText(card.getName());
        holder.imageView.setImageBitmap(card.getPhoto());
        // Дополнительные операции над элементами карточки
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public ImageView imageView;
        public Button deleteInfoButton;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.name_title);
            imageView = itemView.findViewById(R.id.imageView);
            deleteInfoButton = itemView.findViewById(R.id.delete);

            // Обработчик нажатия на кнопку addInfo
            deleteInfoButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onDeleteInfoClickListener != null) {
                    onDeleteInfoClickListener.onDeleteInfoClick(position);
                }
            });

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, position);
                }
            });
        }
    }
}
