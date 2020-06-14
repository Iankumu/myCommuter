package com.example.mycommuter.model;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

public class JSONObj {
    Tasks tasks;

    public Tasks getData(Tasks task) {
        return (Tasks) (this.tasks=task);
    }
}
