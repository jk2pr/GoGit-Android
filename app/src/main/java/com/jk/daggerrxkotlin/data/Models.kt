package com.jk.daggerrxkotlin.data

import android.os.Parcel
import android.os.Parcelable

data class NewsItem(
        val author: String,
        val title: String,
        val numComments: Int,
        val created: Long,
        val thumbnail: String,
        val url: String?
) : Parcelable {


    companion object {
        @JvmField val CREATOR: Parcelable.Creator<NewsItem> = object : Parcelable.Creator<NewsItem> {
            override fun createFromParcel(source: Parcel): NewsItem = NewsItem(source)
            override fun newArray(size: Int): Array<NewsItem?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(source.readString(), source.readString(), source.readInt(), source.readLong(), source.readString(), source.readString())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(author)
        dest?.writeString(title)
        dest?.writeInt(numComments)
        dest?.writeLong(created)
        dest?.writeString(thumbnail)
        dest?.writeString(url)
    }
}