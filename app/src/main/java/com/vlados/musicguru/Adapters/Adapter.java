package com.vlados.musicguru.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.vlados.musicguru.R;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.vlados.musicguru.Model.Artist;

import java.util.List;
/*
 * Адаптер для json файла, чтобы переместить информацию в лист
 */
public class Adapter extends ArrayAdapter {
    private List<Artist> listArtist;
    private int resource;
    private LayoutInflater inflater;
    private Context myContext;

    public Adapter(Context context, int resource, List<Artist> objects) {
        super(context, resource, (List) objects);
        listArtist = objects;
        this.resource = resource;
        myContext = context;
        inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(resource, null);
            holder.ivIconSm = (ImageView) convertView.findViewById(R.id.ivIcon);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.tvGenres = (TextView) convertView.findViewById(R.id.tvGenres);
            holder.tvContent = (TextView) convertView.findViewById(R.id.tvContent);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (listArtist == null) {
            return convertView;
        }
        holder.tvName.setText(listArtist.get(position).getName());
        holder.tvGenres.setText(listArtist.get(position).getGenres());
        holder.tvContent.setText(listArtist.get(position).getAlbums() + " альбомов • "
                + listArtist.get(position).getTracks() + " песен");
        holder.ivIconSm.setImageURI(Uri.parse(listArtist.get(position).getCovers().getSmallCover()));
        if (listArtist.get(position).getCovers() != null) {
            ImageLoader.getInstance().displayImage(listArtist.get(position).getCovers().getSmallCover(), holder.ivIconSm);
        }
        return convertView;
    }

    public class ViewHolder {
        private ImageView ivIconSm;
        private TextView tvName;
        private TextView tvGenres;
        private TextView tvContent;
    }
}
