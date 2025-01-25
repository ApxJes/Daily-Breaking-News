package com.example.retrofittutorial.ui.adapter

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

    lateinit var articleImage: ImageView
    lateinit var articleTItle: TextView
    lateinit var articleDescription: TextView
    lateinit var articleSource: TextView
    lateinit var articleDataAndTime: TextView

    val differCallBack = object: DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.news_article,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        articleImage = holder.itemView.findViewById(R.id.imvArticleImage)
        articleTItle = holder.itemView.findViewById(R.id.txvArticleTitle)
        articleDescription = holder.itemView.findViewById(R.id.txvArticleDescription)
        articleSource = holder.itemView.findViewById(R.id.txvArticleSource)
        articleDataAndTime = holder.itemView.findViewById(R.id.txvArticleDateAndTime)

        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(articleImage)
            articleTItle.text = article.title
            articleDescription.text = article.description
            articleSource.text = article.source?.name
            articleDataAndTime.text = article.publishedAt

            setOnItemClickListener {
                onItemClick?.let {
                    it(article)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClick: ((Article) -> Unit)? = null
    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClick = listener
    }
}