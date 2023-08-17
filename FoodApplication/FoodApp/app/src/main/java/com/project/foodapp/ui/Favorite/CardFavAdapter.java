package com.project.foodapp.ui.Favorite;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.foodapp.R;
import com.project.foodapp.ui.Recipes.Card;
import com.project.foodapp.ui.Recipes.CardViewAdapter;

import java.util.List;

public class CardFavAdapter extends RecyclerView.Adapter<CardFavAdapter.ViewHolder> {
    private List<CardFav> cardList;
    private OnItemClickListener onItemClickListener;
    private OnDeleteInfoClickListener onDeleteInfoClickListener;

    public CardFavAdapter(List<CardFav> cards) {
        this.cardList = cards;
    }

    public void setOnClickListener(OnItemClickListener listener) {
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_other, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CardFav card = cardList.get(position);
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
