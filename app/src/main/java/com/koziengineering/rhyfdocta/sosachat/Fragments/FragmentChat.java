package com.koziengineering.rhyfdocta.sosachat.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.koziengineering.rhyfdocta.sosachat.ObjectsModels.ChatMessage;
import com.example.rhyfdocta.sosachat.R;
import com.koziengineering.rhyfdocta.sosachat.adapters.AdapterChatMessages;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rhyfdocta on 11/18/17.
 */

public class FragmentChat extends Fragment {

    private ListView listView;
    private View btnSend;
    private EditText editText;
    boolean isMine = true;
    private List<ChatMessage> chatMessages;
    private ArrayAdapter<ChatMessage> adapter;
    private String TAG = "FRAG_TAG";

    public FragmentChat(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        chatMessages = new ArrayList<>();







    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: " );

        //set ListView adapter first
        adapter = new AdapterChatMessages(getActivity(), R.layout.item_chat_left, chatMessages);
        listView.setAdapter(adapter);

        //event for button SEND
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().trim().equals("")) {
                    Toast.makeText(getContext(), "Please input some text...", Toast.LENGTH_SHORT).show();
                } else {
                    //add message to list
                    ChatMessage chatMessage = new ChatMessage(editText.getText().toString(), isMine);
                    chatMessages.add(chatMessage);
                    adapter.notifyDataSetChanged();
                    editText.setText("");
                    isMine = !isMine;
                }
            }
        });

        Log.e(TAG, "onCreate: " );
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.frag_msgs_chat,null);

        listView = view.findViewById(R.id.list_msg);
        btnSend = view.findViewById(R.id.btn_chat_send);
        editText = view.findViewById(R.id.msg_type);



        return view;
    }
}
