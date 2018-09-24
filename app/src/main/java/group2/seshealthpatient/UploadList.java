package group2.seshealthpatient;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import group2.seshealthpatient.Model.Upload;

public class UploadList extends ArrayAdapter<Upload>{
    private Activity context;
    private List<Upload> uploads;

    public UploadList(Activity context, List<Upload>uploads){
        super(context,R.layout.uploadlist_layout,uploads);
        this.context=context;
        this.uploads = uploads;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.uploadlist_layout,null,true);

        TextView textviewName = (TextView)listViewItem.findViewById(R.id.textViewName);
        TextView textviewType = (TextView)listViewItem.findViewById(R.id.textViewType);
        TextView textviewUrl = (TextView)listViewItem.findViewById(R.id.textViewUrl);

        Upload upload = uploads.get(position);
        textviewName.setText(upload.getName());
        textviewType.setText(upload.getType());
        textviewUrl.setText(upload.getUrl());

        return listViewItem;




    }
}
