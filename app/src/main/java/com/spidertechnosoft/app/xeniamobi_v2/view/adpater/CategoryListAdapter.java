package com.spidertechnosoft.app.xeniamobi_v2.view.adpater;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.spidertechnosoft.app.xeniamobi_v2.R;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 11/24/2017.
 */

public class CategoryListAdapter extends BaseAdapter implements Filterable {

    private final Context context;
    private List<Category> originalData = null;
    private List<Category> filteredData = null;
    private LayoutInflater mInflater;

    private ItemFilter mFilter = new ItemFilter();

    private OperationCallback mOperationCallback;

    public CategoryListAdapter(@NonNull Context context, @NonNull List<Category> data, OperationCallback mOperationCallback) {
        this.context=context;

        this.mOperationCallback=mOperationCallback;

        this.originalData=data;
        this.filteredData=data;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return filteredData.size();
    }

    public Object getItem(int position) {
        return filteredData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View rowView = mInflater.inflate(R.layout.lay_category_list_item, parent, false);

        ImageView imgCategoryImage=(ImageView)rowView.findViewById(R.id.imgCategoryImage);

        TextView lblCategoryName=(TextView) rowView.findViewById(R.id.lblCategoryName);

        final ImageView btnItemEdit=(ImageView) rowView.findViewById(R.id.btnItemEdit);

        final ImageView btnItemDelete=(ImageView) rowView.findViewById(R.id.btnItemDelete);

        final Category category=filteredData.get(position);

        if(category!=null){

            lblCategoryName.setText(category.getName());

        }

        btnItemEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mOperationCallback.onEditClick(category);

            }
        });

        btnItemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               mOperationCallback.onDeleteClick(category);

            }
        });

        return rowView;
    }

    public Filter getFilter() {
        return mFilter;
    }

    public interface OperationCallback {
        void onEditClick(Category category);
        void onDeleteClick(Category category);
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Category> list = originalData;

            int count = list.size();
            final ArrayList<Category> nList = new ArrayList<Category>(count);

            Category filterableCategory ;

            for (int i = 0; i < count; i++) {
                filterableCategory = list.get(i);
                if (filterableCategory.getName()!=null&&filterableCategory.getName().toLowerCase().contains(filterString)) {
                    nList.add(filterableCategory);
                }
            }

            results.values = nList;
            results.count = nList.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<Category>) results.values;
            notifyDataSetChanged();
        }

    }


}
