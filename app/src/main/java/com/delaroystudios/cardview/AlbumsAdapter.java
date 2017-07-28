package com.delaroystudios.cardview;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder>  {
    public MainActivity main=new MainActivity();
    private Context mContext;
    private List<Album> albumList;
    private String send;
    private EnviarMensaje enviarMensaje;
    public static final boolean D = true;
    public static final String TAG = "LEDv0";
    public ConexionBT Servicio_BT = null;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }


    public AlbumsAdapter(Context mContext, List<Album> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Album album = albumList.get(position);
        final int pos = position;
        holder.title.setText(album.getName());
        holder.count.setText(album.getNumOfSongs() + " segundos");

        // loading album cover using Glide library
        Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);
        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow,position);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow,pos);
               /* if(pos == 0){
                    showPopupMenu(holder.overflow,pos);
                    // view.getContext().startActivity(new Intent(view.getContext(),Activity1.class));
                }else if(pos == 1){
                    showPopupMenu(holder.overflow,pos);
                   // view.getContext().startActivity(new Intent(view.getContext(),Activity2.class));
                }else if (pos == 2){
                    showPopupMenu(holder.overflow,pos);
                }*/
            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view, int position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_album, popup.getMenu());
        if (position==0) {
            popup.setOnMenuItemClickListener(new MyMenuItemClickListener0());
            popup.show();
        }
        if (position==1) {
            popup.setOnMenuItemClickListener(new MyMenuItemClickListener1());
            popup.show();
        }
        if (position==2) {
            popup.setOnMenuItemClickListener(new MyMenuItemClickListener2());
            popup.show();
        }
        if (position==3) {
            popup.setOnMenuItemClickListener(new MyMenuItemClickListener3());
            popup.show();
        }
        if (position==4) {
            popup.setOnMenuItemClickListener(new MyMenuItemClickListener4());
            popup.show();
        }
        if (position==5) {
            popup.setOnMenuItemClickListener(new MyMenuItemClickListener5());
            popup.show();
        }
    }


     // Click listener for popup menu items
    private class MyMenuItemClickListener0 implements PopupMenu.OnMenuItemClickListener {
        public MyMenuItemClickListener0() {
            ;
        }
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Preparando, porfavor espere...", Toast.LENGTH_SHORT).show();
                    main.sendMessage(mContext,"0");
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Preparando, porfavor espere...", Toast.LENGTH_SHORT).show();
                    main.sendMessage(mContext,"1");
                    return true;
                default:
            }
            return false;
        }
    }
    class MyMenuItemClickListener1 implements PopupMenu.OnMenuItemClickListener {
        public MyMenuItemClickListener1() {
        }
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Preparando, porfavor espere...", Toast.LENGTH_SHORT).show();
                    main.sendMessage(mContext,"2");
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Preparando, porfavor espere...", Toast.LENGTH_SHORT).show();
                    main.sendMessage(mContext,"3");
                    return true;
                default:
            }
            return false;
        }
    }
    class MyMenuItemClickListener2 implements PopupMenu.OnMenuItemClickListener {
        public MyMenuItemClickListener2() {
        }
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Preparando, porfavor espere..", Toast.LENGTH_SHORT).show();
                    main.sendMessage(mContext,"4");
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Preparando, porfavor espere..", Toast.LENGTH_SHORT).show();
                    main.sendMessage(mContext,"5");
                    return true;
                default:
            }
            return false;
        }
    }
    class MyMenuItemClickListener3 implements PopupMenu.OnMenuItemClickListener {
        public MyMenuItemClickListener3() {
        }
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Preparando, porfavor espere..", Toast.LENGTH_SHORT).show();
                    main.sendMessage(mContext,"6");
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Preparando, porfavor espere..", Toast.LENGTH_SHORT).show();
                    main.sendMessage(mContext,"7");
                    return true;
                default:
            }
            return false;
        }
    }
    class MyMenuItemClickListener4 implements PopupMenu.OnMenuItemClickListener {
        public MyMenuItemClickListener4() {
        }
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Preparando, porfavor espere...", Toast.LENGTH_SHORT).show();
                    main.sendMessage(mContext,"8");
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Preparando, porfavor espere..", Toast.LENGTH_SHORT).show();
                    main.sendMessage(mContext,"9");
                    return true;
                default:
            }
            return false;
        }
    }
    class MyMenuItemClickListener5 implements PopupMenu.OnMenuItemClickListener {
        public MyMenuItemClickListener5() {
        }
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Preparando, porfavor espere...", Toast.LENGTH_SHORT).show();
                    main.sendMessage(mContext,"a");
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Preparando, porfavor espere...", Toast.LENGTH_SHORT).show();
                    main.sendMessage(mContext,"b");
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

}
