package me.dio.soccernews.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import me.dio.soccernews.R;
import me.dio.soccernews.databinding.NewsItemBinding;
import me.dio.soccernews.domain.News;

public class NewsAdapters extends RecyclerView.Adapter<NewsAdapters.ViewHolder> {

    private final List<News> news;
    private final FavoriteListener favoriteListener;

    public NewsAdapters(List<News> news, FavoriteListener favoriteListener){
        this.news = news;
        this.favoriteListener = favoriteListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        NewsItemBinding binding = NewsItemBinding.inflate(layoutInflater,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Context context = holder.itemView.getContext();

        News news = this.news.get(position);
        holder.binding.tvTitle.setText(news.title);
        holder.binding.tvDescription.setText(news.description);
        Picasso.get()
                .load(news.image)
                .fit()
                .centerCrop()
                .into(holder.binding.ivThumbnail);

        //implementação da funcionalidade de abrir link
        holder.binding.btOpenLink.setOnClickListener(vew ->{
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(news.link));
            holder.itemView.getContext().startActivity(i);
        });
        //implementação da funcionalidade de compartilhar
        holder.binding.ivShare.setOnClickListener(view -> {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, news.title);
            i.putExtra(Intent.EXTRA_TEXT,news.link);
            holder.itemView.getContext().startActivity(Intent.createChooser(i,"Share"));
        });
        //implementação da funcionalidade de "Favoritar" (O evento ser instanciado pelo Fragment)
        holder.binding.ivFavorite.setOnClickListener(view ->{
            news.favorite = !news.favorite;
            this.favoriteListener.onFavorite(news);
            notifyItemChanged(position);
        });
        int favoriteColor = news.favorite ? R.color.gray_900:R.color.gray_200;
            holder.binding.ivFavorite.setColorFilter(context.getResources().getColor(favoriteColor));
        }

    @Override
    public int getItemCount() {
        return this.news.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final NewsItemBinding binding;

        public ViewHolder(NewsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public interface FavoriteListener {
       void  onFavorite(News news);
    }
}
