package com.project.foodapp.ui.Recipes;

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

import java.util.List;

public class CardViewAdapter extends RecyclerView.Adapter<com.project.foodapp.ui.Recipes.CardViewAdapter.ViewHolder> {
    private List<CardView> cardList;
    private com.project.foodapp.ui.Recipes.CardViewAdapter.OnItemClickListener onItemClickListener;
    private OnAddInfoClickListener onAddInfoClickListener;


    public CardViewAdapter(List<CardView> cards) {
        this.cardList = cards;
    }

    public void setOnClickListener(com.project.foodapp.ui.Recipes.CardViewAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnAddInfoClickListener(OnAddInfoClickListener listener) {
        this.onAddInfoClickListener = listener;
    }

    public interface OnAddInfoClickListener {
        void onAddInfoClick(int position);
    }

    @NonNull
    @Override
    public com.project.foodapp.ui.Recipes.CardViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        com.project.foodapp.ui.Recipes.CardViewAdapter.ViewHolder viewHolder = new com.project.foodapp.ui.Recipes.CardViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(com.project.foodapp.ui.Recipes.CardViewAdapter.ViewHolder holder, int position) {
        CardView card = cardList.get(position);
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
        public Button addInfoButton;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.name_title);
            imageView = itemView.findViewById(R.id.imageView);
            addInfoButton = itemView.findViewById(R.id.addInfo);

            // Обработчик нажатия на кнопку addInfo
            addInfoButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onAddInfoClickListener != null) {
                    onAddInfoClickListener.onAddInfoClick(position);
                }
            });

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            });
        }
    }
}
