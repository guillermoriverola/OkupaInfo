package edu.upc.eetac.dsa.okupainfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import edu.upc.eetac.dsa.okupainfo.client.entity.CasalCollection;

/**
 * Created by Guillermo on 21/05/2016.
 */
public class CasalCollectionAdapter extends BaseAdapter {

        private CasalCollection casalCollection;
        private LayoutInflater layoutInflater;

        public CasalCollectionAdapter(Context context, CasalCollection casalCollection){
            layoutInflater = LayoutInflater.from(context);
            this.casalCollection = casalCollection;
        }

        class ViewHolder{
            TextView textViewAminid;
            TextView textViewName;
            TextView textViewDescription;

            ViewHolder(View row){
                this.textViewAminid = (TextView) row
                        .findViewById(R.id.textViewAdminid);
                this.textViewName = (TextView) row
                        .findViewById(R.id.textViewName);
                this.textViewDescription = (TextView) row
                        .findViewById(R.id.textViewDescription);
            }
        }

        @Override
        public int getCount() {
            return casalCollection.getCasals().size();
        }

        @Override
        public Object getItem(int position) {
            return casalCollection.getCasals().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.list_row_data, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            String adminid = casalCollection.getCasals().get(position).getAdminid();
            String name = casalCollection.getCasals().get(position).getName();
            String description = casalCollection.getCasals().get(position).getDescription();


            viewHolder.textViewAminid.setText(adminid);
            viewHolder.textViewName.setText(name);
            viewHolder.textViewDescription.setText(description);
            return convertView;
        }
    }

