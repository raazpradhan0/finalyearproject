package com.example.restaurantlocator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataParser
{
    private HashMap<String,String> getSingleNearbyPlace(JSONObject googlePlaceJSON)
    {
        HashMap<String,String> googlePlacesMap = new HashMap<>();
        String  NameOfPlaces  = "-NA-";
        String  vicinity  = "-NA-";
        String  lattitude  = "";
        String  longitude  = "";
        String  reference  = "";

        try
        {
            if(!googlePlaceJSON.isNull( "name"))
            {
                NameOfPlaces = googlePlaceJSON.getString("name");
            }

            if(!googlePlaceJSON.isNull( "vicinity"))
            {
                vicinity = googlePlaceJSON.getString("vicinity");
            }

            lattitude = googlePlaceJSON.getJSONObject("geomerty").getJSONObject("location").getString("lat");
            longitude = googlePlaceJSON.getJSONObject("geomerty").getJSONObject("location").getString("lng");
            reference = googlePlaceJSON.getString("reference");

            googlePlacesMap.put("place_name", NameOfPlaces);
            googlePlacesMap.put("vicinity", vicinity);
            googlePlacesMap.put("lat", lattitude);
            googlePlacesMap.put("lng", longitude);
            googlePlacesMap.put("reference",reference);


        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return googlePlacesMap;

    }

    private List<HashMap<String, String>> getAllNearbyPlaces(JSONArray jsonArray)
        {
            int counter = jsonArray.length();
            List<HashMap<String, String>> NearbyPlacesList = new ArrayList<>();

            HashMap<String, String> NearbyPlaceMap = null;

            for(int i=0;i<counter;i++){
                try
                {
                    NearbyPlaceMap = getSingleNearbyPlace((JSONObject) jsonArray.get(i));
                    NearbyPlacesList.add(NearbyPlaceMap);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            return NearbyPlacesList;

        }

    public List<HashMap<String,String>> parse(String jSONdata)
    {
        JSONArray jsonArray =   null;
        JSONObject jsonObject;


        try
        {
            jsonObject = new JSONObject(jSONdata);
            jsonArray = jsonObject.getJSONArray("results");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return getAllNearbyPlaces(jsonArray);


    }


}
