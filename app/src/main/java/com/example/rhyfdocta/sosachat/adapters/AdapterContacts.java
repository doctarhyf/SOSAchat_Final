package com.example.rhyfdocta.sosachat.adapters;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rhyfdocta.sosachat.ObjectsModels.Contact;
import com.example.rhyfdocta.sosachat.R;


import java.util.List;

public class AdapterContacts extends ArrayAdapter<Contact> {

    private Activity activity;
    private List<Contact> contactsList;
    ContactsListener listener;

    public AdapterContacts(Activity context, int resource, List<Contact> objects, ContactsListener listener) {
        super(context, resource, objects);
        this.activity = context;
        this.contactsList = objects;
        this.listener = listener;
    }

    static class ViewHolderContactItem {

        TextView tvChatMsgFromName, tvChatMsgLastMsg, tvChatMsgLastMsgDate;
        ImageView ivChatMsg, ivRmChatMsg;


    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolderContactItem viewHolderWishListItem;

        if (view == null) {

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_contact, null);

            //view = inflater.inflate(R.)
            viewHolderWishListItem = new ViewHolderContactItem();
            viewHolderWishListItem.tvChatMsgFromName = view.findViewById(R.id.tvChatMsgFromName);
            viewHolderWishListItem.tvChatMsgLastMsg = view.findViewById(R.id.tvChatMsgLastMsg);
            viewHolderWishListItem.tvChatMsgLastMsgDate = view.findViewById(R.id.tvChatMsgLastMsgDate);
            viewHolderWishListItem.ivChatMsg = view.findViewById(R.id.ivChatMsg);
            viewHolderWishListItem.ivRmChatMsg = view.findViewById(R.id.ivRmChatMsg);
            view.setTag(viewHolderWishListItem);


        } else {
            viewHolderWishListItem = (ViewHolderContactItem) view.getTag();
        }

        final Contact contact = contactsList.get(position);
        viewHolderWishListItem.tvChatMsgFromName.setText(contact.getContactName());
        viewHolderWishListItem.tvChatMsgLastMsg.setText(contact.getLastMsg());
        viewHolderWishListItem.tvChatMsgLastMsgDate.setText(contact.getLastMsgDate());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onContactClicked(contact);
            }
        });

        viewHolderWishListItem.ivRmChatMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRemoveContactClicked(contact);
            }
        });

        return view;

    }

    @Override
    public int getViewTypeCount() {
        // return the total number of view types. this value should never change
        // at runtime
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        // return a value between 0 and (getViewTypeCount - 1)
        return position % 2;
    }

    private class ViewHolder {
        private TextView msg;

        public ViewHolder(View v) {
            msg = v.findViewById(R.id.txt_msg);
        }
    }

    public interface ContactsListener{
        void onContactClicked(Contact contact);

        void onRemoveContactClicked(Contact contact);
    }
}
