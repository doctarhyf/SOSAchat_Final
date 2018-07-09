package com.koziengineering.rhyfdocta.sosachat.ObjectsModels;

import android.os.Bundle;

/**
 * Created by rhyfdocta on 11/9/17.
 */

public class TypesItem {

    private String typeName;
    private String typeImgPath;
    private String typeId;

    public static final String KEY_TYPE_ITEM_NAME = "items_types_name";
    public static final String KEY_TYPE_ITEM_IMG_PATH = "items_types_pic";
    public static final String KEY_TYPE_ITEM_ID = "items_types_id";
    //public static final String KEY_CAT_ICON_URI = "iconPath";

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeImgPath() {
        return typeImgPath;
    }

    public void setTypeImgPath(String typeImgPath) {
        this.typeImgPath = typeImgPath;
    }

    public TypesItem(String id, String name, String imgPath){

        this.typeId = id;
        this.typeName = name;
        this.typeImgPath = imgPath;

    }

    public Bundle toBundle(){
        Bundle b = new Bundle();

        b.putString(KEY_TYPE_ITEM_NAME, typeName);
        b.putString(KEY_TYPE_ITEM_IMG_PATH, typeImgPath);
        b.putString(KEY_TYPE_ITEM_ID, typeId);


        return b;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
}
