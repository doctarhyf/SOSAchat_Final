package com.koziengineering.rhyfdocta.sosachat.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.koziengineering.rhyfdocta.sosachat.API.SOS_API;
import com.koziengineering.rhyfdocta.sosachat.ObjectsModels.Contact;
import com.example.rhyfdocta.sosachat.R;
import com.koziengineering.rhyfdocta.sosachat.adapters.AdapterContacts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rhyfdocta on 11/18/17.
 */

public class FragmentContactsList extends Fragment implements
        AdapterContacts.ContactsListener
{


    private static final String TAG = "TAG_FRAG_CONT";

    List<Contact> contactList = new ArrayList<>();
    AdapterContacts adapterContacts;
    JsonArrayRequest request;
    private String contactsURL;
    private ListView lvContactsList;
    private TextView tvContactsListEmpty;
    //RequestQueue queue;


    public interface FragmentContactsListListener{
        void onItemClicked(Contact contact);
    }

    FragmentContactsListListener listListener;

    public FragmentContactsList(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //queue = Volley.newRequestQueue(getContext());

    }

    private void loadItems() {



        //Log.e(TAG, "loadItems: " );
        //getActivity().getSupportActionBar().setSubtitle(products.size() + " item(s).");
        adapterContacts = new AdapterContacts(getActivity(), R.layout.list_item_contact, contactList, this);
        lvContactsList.setAdapter(adapterContacts);



            tvContactsListEmpty.setVisibility(View.GONE);
            lvContactsList.setVisibility(View.VISIBLE);


    }

    @Override
    public void onStart() {
        super.onStart();

        contactList.clear();

        contactsURL = SOS_API.API_URL + "act=" + SOS_API.ACTION_LOAD_CHAT_CONTACTS;
        Log.e(TAG, "onCreate: CHAT URL : " + contactsURL);
        request = new JsonArrayRequest(
                contactsURL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        Log.e(TAG, "onResponse: " + jsonArray.toString() );
                        // Toast.makeText(getContext(), "Loaded : " + jsonArray.toString(), Toast.LENGTH_SHORT).show();

                        if (jsonArray.length() > 0){


                            try {
                                for(int i =0; i < jsonArray.length(); i++) {

                                    JSONObject jo = jsonArray.getJSONObject(i);
                                    //public Message(int fromID, int toID, int type, String msgTitle, String content, Date date)
                                    Contact contact = new Contact(
                                        jo.getInt(Contact.KEY_CONTACT_ID),
                                        jo.getString(Contact.KEY_CONTACT_NAME),
                                        jo.getString(Contact.KEY_LAST_MESSAGE),
                                        jo.getString(Contact.KEY_LAST_MESSAGE_DATE)
                                    );


                                    contactList.add(contact);

                                }

                                loadItems();

                            } catch (JSONException e) {

                                //Toast.makeText(getApplicationContext(), "Sorry this cat is still empty" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                onEmptyList();
                            }


                        }else{

                            onEmptyList();

                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e(TAG, "onErrorResponse: " + volleyError.getLocalizedMessage() );
                        Toast.makeText(getContext(), "Error loading messages. Error : " + volleyError.toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                }

        );

        Volley.newRequestQueue(getContext()).add(request);

    }

    private void onEmptyList() {


        String msg = getResources().getString(R.string.msgContactsListEmpty);
        Toast.makeText(getContext(), "Sorry! you contacts list is empty." , Toast.LENGTH_SHORT).show();
        Log.e(TAG, "onNoProdsInCategory: \n" +  msg);

        lvContactsList.setVisibility(View.GONE);
        tvContactsListEmpty.setVisibility(View.VISIBLE);
        //pbWli.setVisibility(View.GONE);

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_msgs_contacts_list,null);

        lvContactsList = view.findViewById(R.id.lvContactsList);
        tvContactsListEmpty = view.findViewById(R.id.tvContactsListEmpty);
        return view;
    }

    @Override
    public void onContactClicked(Contact contact) {
        //Toast.makeText(getContext(), "CONTACT CLICKED", Toast.LENGTH_SHORT).show();
        listListener.onItemClicked(contact);
    }

    @Override
    public void onRemoveContactClicked(Contact contact) {
        Toast.makeText(getContext(), "WILL REMOVE ITEM", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.listListener = (FragmentContactsListListener) context;
        Log.e(TAG, "onAttach: " );
    }
}

