package com.example.rhyfdocta.sosachat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.rhyfdocta.sosachat.Fragments.FragmentChat;
import com.example.rhyfdocta.sosachat.Fragments.FragmentContactsList;
import com.example.rhyfdocta.sosachat.HelperObjects.HelperMethods;
import com.example.rhyfdocta.sosachat.ObjectsModels.Contact;

public class ActivityMessages extends AppCompatActivity implements FragmentContactsList.FragmentContactsListListener {


    FragmentChat fragmentChat;
    FragmentContactsList fragmentContactsList;
    //FragmentManager fragmentManager;
    Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        getSupportActionBar().setTitle(HelperMethods.getStringResource(getBaseContext(),R.string.contacts));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fragmentChat = new FragmentChat();
        fragmentContactsList = new FragmentContactsList();
        //fragmentContactsList.setArguments();
        //fragmentManager = getSupportFragmentManager();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.msgsFragsCont, fragmentContactsList)
                .commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;

            case R.id.menuContactList:
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.msgsFragsCont, fragmentContactsList)
                        .commit();

                item.setVisible(false);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                getSupportActionBar().setTitle(HelperMethods.getStringResource(getBaseContext(),R.string.contacts));

                break;


        }



        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chats, menu);
        this.menu = menu;
        menu.getItem(0).setVisible(false);
        return true;
    }



    @Override
    protected void onStop() {
        super.onStop();
        menu = null;
        //fragmentManager = null;
    }

    @Override
    public void onItemClicked(Contact contact) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                .replace(R.id.msgsFragsCont, fragmentChat)
                .commit();

        menu.getItem(0).setVisible(true);

        getSupportActionBar().setTitle(contact.getContactName());
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }
}
