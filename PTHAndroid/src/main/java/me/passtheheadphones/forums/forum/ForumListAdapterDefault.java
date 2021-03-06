package me.passtheheadphones.forums.forum;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import api.forum.forum.ForumThread;
import api.soup.MySoup;
import me.passtheheadphones.R;

import java.util.Date;

/**
 * Adapter for showing a listing of forum threads, default version
 */
public class ForumListAdapterDefault extends ForumListAdapter {

	public ForumListAdapterDefault(Context context){
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		ViewHolder holder;
		if (convertView != null){
			holder = (ViewHolder)convertView.getTag();
		}
		else {
			convertView = inflater.inflate(R.layout.list_forum_thread, parent, false);
			holder = new ViewHolder();
			holder.name = (TextView)convertView.findViewById(R.id.thread_name);
			holder.replies = (TextView)convertView.findViewById(R.id.replies);
			holder.author = (TextView)convertView.findViewById(R.id.author_name);
			holder.lastPostAuthor = (TextView)convertView.findViewById(R.id.last_post_username);
			holder.lastPostTime = (TextView)convertView.findViewById(R.id.last_post_time);
			holder.jumpToLastRead = (ImageButton)convertView.findViewById(R.id.go_to_last_read);
			holder.jumpToLastRead.setImageResource(R.drawable.ic_forward_24dp);
			holder.sticky = (ImageView) convertView.findViewById(R.id.sticky_thread);
			holder.sticky.setImageResource(R.drawable.ic_star_24dp);
			holder.locked = (ImageView) convertView.findViewById(R.id.locked_thread);
			holder.locked.setImageResource(R.drawable.ic_lock_24dp);

			convertView.setTag(holder);
		}

		ForumThread thread = getItem(position);
		holder.name.setText(thread.getTitle());
		holder.author.setText(thread.getAuthorName());
		holder.lastPostAuthor.setText(thread.getLastAuthorName());
		Date lastPost = MySoup.parseDate(thread.getLastTime());
		holder.lastPostTime.setText(DateUtils.getRelativeTimeSpanString(lastPost.getTime(),
			new Date().getTime(), 0, DateUtils.FORMAT_ABBREV_ALL));

		holder.jumpToLastRead.setTag(position);
		holder.jumpToLastRead.setOnClickListener(this);

		if (thread.isRead()){
			holder.name.setTextColor(ContextCompat.getColor(getContext(), R.color.secondary_text));
		}
		else {
			holder.name.setTextColor(ContextCompat.getColor(getContext(), R.color.primary_text));
		}

		int replies = thread.getPostCount().intValue();
		if (replies == 0){
			holder.replies.setText("No replies");
		}
		else if (replies == 1){
			holder.replies.setText("1 reply");
		}
		else {
			holder.replies.setText(replies + " replies");
		}
		if (thread.isSticky()){
			holder.sticky.setVisibility(View.VISIBLE);
		}
		else {
			holder.sticky.setVisibility(View.GONE);
		}
		if (thread.isLocked()){
			holder.locked.setVisibility(View.VISIBLE);
		}
		else {
			holder.locked.setVisibility(View.GONE);
		}
		return convertView;
	}
}
