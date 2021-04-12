package com.sanjib.koala.Db;

import com.sanjib.koala.networking.model.Order;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Transaction extends RealmObject {
    @PrimaryKey
    int id;
}