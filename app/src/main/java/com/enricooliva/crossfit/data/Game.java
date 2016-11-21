package com.enricooliva.crossfit.data;

import org.json.JSONException;
import org.json.JSONObject;

public class Game {


	private String result;
	private String game;
	private String goals;
	private Boolean victory;
	
	public Game(JSONObject json_data)
	{
		
		try {
			result=json_data.getString("Result");
			game=json_data.getString("Game");
			goals=json_data.getString("Goals");
			String app=json_data.getString("Victory");
			if (app.contains("1"))
				victory=true;
			else
				victory = false;
			//born=  Date.valueOf(json_data.getString("Born"));
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	public Boolean getVictory()
	{
		return victory;
	}
	public String getResult()
	{
		return result;
	}
	
	public String getGame()
	{
		return game;
	}
	
	public String getGoals()
	{
		return goals;
	}

	
	
}
