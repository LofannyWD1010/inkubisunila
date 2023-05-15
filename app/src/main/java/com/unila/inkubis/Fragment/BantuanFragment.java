package com.unila.inkubis.Fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.unila.inkubis.BuildConfig;
import com.unila.inkubis.R;
import com.unila.inkubis.WebActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BantuanFragment extends Fragment {

    CardView cvBeli, cvPesanan;
    ExpandableListView lv;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;
    View rootView;
    private String[] groups;
    private String[][] children;

    public BantuanFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        groups = new String[] { "Bagaimana Cara Membeli Produk ?", "Bagaimana Cara Melihat Status Pesanan ?"};

        children = new String [][] {
                { "1.Pilih \"Pencarian\" untuk melihat produk yang ingin dibeli \n \n2.Pilih produk yang ingin dibeli \n \n3.Anda dapat melihat deskripsi produk dan anda juga dapat menghubungi penjual via chat maupun telpon \n \n4.Tekan \"Tambah Keranjang\" untuk menambahkan produk ke keranjang atau tekan \"Pesan Sekarang\" untuk beli produk tersebut saja \n \n5.Tentukan jumlah produk \n \n6.Tentukan lokasi pengiriman berdasarkan detail alamat \n \n7.Setelah menekan tombol selanjutnya maka akan muncul rekap pembayaran dan tombol \"Lanjut Bayar\" untuk menyelesaikan pesanan \n \n8.Melakukan pembayaran via transfer ATM ke rekening 0814014758 (BNI) a.n INKUBATOR BISNIS \n \n9.Anda tinggal menunggu status pesanan agar diproses oleh admin dan penjual" },
                { "1.Pilih menu \"Pesanan\" \n \n2.Lalu pilih produk pesanan yang ingin dilihat \n \n3.Anda dapat melihat \"status\" pesanan \n \n4.Untuk menghubungi penjual, anda dapat menghubungi penjual via chat maupun telpon yang ditampilkan logonya pada sub menu Identitas Penjual" },
        };
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_bantuan, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lv = (ExpandableListView) view.findViewById(R.id.expandableListView);
        lv.setAdapter(new ExpandableListAdapter(groups, children));
        lv.setGroupIndicator(null);

    }

    public class ExpandableListAdapter extends BaseExpandableListAdapter {

        private final LayoutInflater inf;
        private String[] groups;
        private String[][] children;

        public ExpandableListAdapter(String[] groups, String[][] children) {
            this.groups = groups;
            this.children = children;
            inf = LayoutInflater.from(getActivity());
        }

        @Override
        public int getGroupCount() {
            return groups.length;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return children[groupPosition].length;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groups[groupPosition];
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return children[groupPosition][childPosition];
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if (convertView == null) {
                convertView = inf.inflate(R.layout.item_bantuan, parent, false);
                holder = new ViewHolder();

                holder.text = (TextView) convertView.findViewById(R.id.expandedListItem);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.text.setText(getChild(groupPosition, childPosition).toString());

            return convertView;
        }
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = inf.inflate(R.layout.list_bantuan, parent, false);

                holder = new ViewHolder();
                holder.text = (TextView) convertView.findViewById(R.id.listTitle);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.text.setText(getGroup(groupPosition).toString());

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        private class ViewHolder {
            TextView text;
        }

    }

}


