package br.ufc.insta.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import br.ufc.insta.MainActivity;
import br.ufc.insta.R;
import br.ufc.insta.models.Post;
import br.ufc.insta.utils.GlideApp;

public class GridAdapter extends BaseAdapter {

    List<Post> postList;
    private Context ctx;
    private LayoutInflater layoutInflater;

    public GridAdapter(Context context, List<Post> customizedListView) {
        this.ctx = context;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        postList = customizedListView;
    }

    @Override
    public int getCount() {
        return postList.size();
    }

    @Override
    public Object getItem(int position) {
        return postList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder listViewHolder;
        if(convertView == null){
            listViewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.singlegriditem, parent, false);
            listViewHolder.imageView = (ImageView)convertView.findViewById(R.id.singleimageitem);
            convertView.setTag(listViewHolder);
        }else{
            listViewHolder = (ViewHolder)convertView.getTag();
        }
        Post post = postList.get(position);

        GlideApp.with(listViewHolder.imageView.getContext())
                .load("http://www.pngmart.com/files/11/Versus-PNG-Clipart.png")
                .placeholder(R.drawable.usericon)
                .into(listViewHolder.imageView);
//        RequestOptions options = new RequestOptions()
//                .centerCrop()
//                .placeholder(R.drawable.usericon);
//        Glide.with(MainActivity.mainContext).load(post.getPhotourl()).apply(options).into(listViewHolder.imageView);

        return convertView;
    }

    public class ViewHolder
    {
        ImageView imageView;
    }

}
