package vn.edu.ueh.thanhdnh.firebase_example;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ArticleViewAdapter extends RecyclerView.Adapter<ArticleViewHolder> {
  private LayoutInflater mInflater;
  private List<Article> articles;
  private Context context;

  public ArticleViewAdapter(Context context, List<Article> articles) {
    this.context = context;
    this.mInflater = LayoutInflater.from(context);
    this.articles = articles;
  }

  public void update(List<Article> articles){
    this.articles = articles;
  }

  @NonNull
  @Override
  public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View customView = mInflater.inflate(R.layout.contact_list, parent, false);
    return new ArticleViewHolder(customView);
  }

  @Override
  public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
    Article article = articles.get(position);
    holder.getTxtTitle().setText(article.getTitle());
    holder.getTxtContent().setText(article.getContent());

    String imageUrl = article.getImageUrl();
    if (imageUrl != null && !imageUrl.trim().isEmpty()) {
      Glide.with(context)
          .load(imageUrl)
          .placeholder(android.R.drawable.ic_menu_gallery)
          .error(android.R.drawable.ic_dialog_alert)
          .into(holder.getImgArticle());
    } else {
      holder.getImgArticle().setImageResource(android.R.drawable.ic_menu_gallery);
    }
  }

  @Override
  public int getItemCount() {
    return articles.size();
  }
}
