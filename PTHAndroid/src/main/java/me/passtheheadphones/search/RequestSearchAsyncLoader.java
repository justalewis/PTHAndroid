package me.passtheheadphones.search;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import api.search.requests.RequestsSearch;

/**
 * AsyncTaskLoader to load some page of a request search with the desired terms & tags
 * bundle should contain at least one of search terms or tags. if no page number is
 * passed the first page will be loaded
 */
public class RequestSearchAsyncLoader extends AsyncTaskLoader<RequestsSearch> {
	private RequestsSearch search;
	private String terms, tags;
	private int page;

	public RequestSearchAsyncLoader(Context context, Bundle args){
		super(context);
		terms = args.getString(SearchActivity.TERMS, "");
		tags = args.getString(SearchActivity.TAGS, "");
		page = args.getInt(SearchActivity.PAGE, 1);
	}

	@Override
	public RequestsSearch loadInBackground(){
		if (search == null){
			while (true){
				search = RequestsSearch.search(terms, tags, page);
				//If we get rate limited wait and retry. It's very unlikely the user has used all 5 of our
				//requests per 10s so don't wait the whole time initially
				if (search != null && !search.getStatus() && search.getError() != null && search.getError().equalsIgnoreCase("rate limit exceeded")){
					try {
						Thread.sleep(3000);
					}
					catch (InterruptedException e){
						Thread.currentThread().interrupt();
					}
				}
				else {
					break;
				}
			}
		}
		return search;
	}

	@Override
	protected void onStartLoading(){
		if (search != null){
			deliverResult(search);
		}
		else {
			forceLoad();
		}
	}

	public String getTerms(){
		return terms;
	}

	public String getTags(){
		return tags;
	}
}
