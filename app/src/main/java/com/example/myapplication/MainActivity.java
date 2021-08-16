package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.button.MaterialButtonToggleGroup;

import java.io.File;

public class MainActivity extends AppCompatActivity implements AddNewFolderDialog.AddNewFolderCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.iv_main_addNewFolder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddNewFolderDialog().show(getSupportFragmentManager(),null);
            }
        });

        if(StorageHelper.isExternalStorageReadable()){
            //getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            //masir poshe apk shoma dakhel poshe data namayesh mide
            File externalFilesDir = getExternalFilesDir(null);
            listFiles(externalFilesDir.getPath(),false);
        }

        EditText EtSearch = findViewById(R.id.et_main_search);
        EtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Fragment fragment =getSupportFragmentManager().findFragmentById(R.id.frame_main_fragmentContainer);
                if(fragment instanceof  FileListFragment){
                    ((FileListFragment) fragment).search(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        MaterialButtonToggleGroup toggleGroup = findViewById(R.id.toggleGroup_main);
        toggleGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if(checkedId == R.id.btn_mail_list && isChecked){
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_main_fragmentContainer);
                    if(fragment instanceof  FileListFragment){
                        ((FileListFragment) fragment).setViewType(ViewType.ROW);
                    }
                }else if(checkedId == R.id.btn_mail_grid && isChecked){
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_main_fragmentContainer);
                    if(fragment instanceof  FileListFragment){
                        ((FileListFragment) fragment).setViewType(ViewType.GRID);
                    }
                }
            }
        });


    }

    public void  listFiles(String path,boolean addToBackStack){

        FileListFragment fileListFragment = new FileListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("path",path);
        fileListFragment.setArguments(bundle);


        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main_fragmentContainer,fileListFragment);
        if(addToBackStack)
            transaction.addToBackStack(null);

        transaction.commit();
    }
    public void listFiles(String path){
        this.listFiles(path,true);
    }


    @Override
    public void onCreateFolderButtonClick(String folderName) {

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_main_fragmentContainer);
        if(fragment instanceof FileListFragment){
            ((FileListFragment) fragment).createNewFolder(folderName);
        }


    }
}