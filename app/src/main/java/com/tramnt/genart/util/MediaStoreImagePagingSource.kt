package com.tramnt.genart.util

import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.paging.PagingSource
import androidx.paging.PagingState

class MediaStoreImagePagingSource(
    private val context: Context
) : PagingSource<Int, Uri>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Uri> {
        val page = params.key ?: 0
        val pageSize = params.loadSize
        val offset = page * pageSize

        val uris = mutableListOf<Uri>()
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val query = context.contentResolver.query(
            collection,
            projection, null, null, sortOrder
        )
        query?.use { cursor ->
            if (offset > 0) {
                cursor.moveToPosition(offset - 1)
            } else {
                cursor.moveToPosition(-1)
            }
            var count = 0
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext() && count < pageSize) {
                val id = cursor.getLong(idColumn)
                val uri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL, id)
                uris.add(uri)
                count++
            }
        }

        return LoadResult.Page(
            data = uris,
            prevKey = if (page == 0) null else page - 1,
            nextKey = if (uris.size < pageSize) null else page + 1
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Uri>): Int? = 0
} 