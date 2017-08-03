package com.jk.daggerrxkotlin.managers

import com.jk.daggerrxkotlin.api.NewsAPI
import com.jk.daggerrxkotlin.data.NewsItem
import rx.Observable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * News Manager allows you to request news from Reddit API.
 *
 * @author juancho
 */
@Singleton
class NewsManager @Inject constructor() {

    /**
     *
     * Returns Reddit News paginated by the given limit.
     *
     * @param after indicates the next page to navigate.
     * @param limit the number of news to request.
     */
    fun getNews(after: String, limit: String = "10"): Observable<NewsItem> {
        return Observable.create {
            subscriber ->
            val callResponse = NewsAPI.getRetrofit().getTop(after, limit)
            val response = callResponse.execute()

            if (response.isSuccessful) {
                val news = response.body() as NewsItem
                subscriber.onNext(news)
                subscriber.onCompleted()
            } else {
                subscriber.onError(Throwable(response.message()))
            }
        }
    }
}