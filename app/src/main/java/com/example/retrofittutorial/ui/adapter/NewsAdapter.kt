package com.example.retrofittutorial.ui.adapter

import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.retrofittutorial.R
import com.example.retrofittutorial.ui.model.Article

class NewsAdapter: RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {
    inner class ArticleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private lateinit var articleImage: ImageView
    private lateinit var articleSource: TextView
    private lateinit var articleTitle: TextView
    private lateinit var articleDescription: TextView
    private lateinit var articleDataAndTime: TextView

    private var differCallBack = object: DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    var differ = AsyncListDiffer(this, differCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.news_article, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]

        articleImage = holder.itemView.findViewById(R.id.imvArticleImage)
        articleSource = holder.itemView.findViewById(R.id.txvArticleSource)
        articleTitle = holder.itemView.findViewById(R.id.txvArticleTitle)
        articleDescription = holder.itemView.findViewById(R.id.txvArticleDescription)
        articleDataAndTime = holder.itemView.findViewById(R.id.txvArticleDateAndTime)

        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(articleImage)
            articleSource.text = article.source?.name
            articleTitle.text = article.title
            articleDescription.text = article.description
            articleDataAndTime.text = article.publishedAt

            setOnClickListener {
               onItemClick?.let { it(article) }
            }
        }
    }

    private var onItemClick: ((Article) -> Unit)? = null
    fun onItemClickListener(listener: (Article) -> Unit) {
        onItemClick = listener
    }
}