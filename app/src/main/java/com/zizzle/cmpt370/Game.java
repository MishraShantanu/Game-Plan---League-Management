package com.zizzle.cmpt370;

import android.util.Pair;

import java.text.SimpleDateFormat;

/**
 * Parent class, stores generic information about sports games
 */
public class Game {

    /** teams who are in the game */
    private Pair<Team,Team> teams;

    /** date (year, month, day, time) the game is being/was played */
    private SimpleDateFormat date;

    /** name of the location the game is/was held at */
    private String location;

    /** sport of the game */
    private String sport;


}
