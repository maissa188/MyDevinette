package com.example.mydevinette

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class MyListAdapter( var mCtx: Context, var resource: Int, var items: List<Joueur>)
    :ArrayAdapter<Joueur>(mCtx,resource,items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val layoutInflater:  LayoutInflater= LayoutInflater.from(mCtx)
        val view : View = layoutInflater.inflate(resource,null)
        val firstName: TextView= view.findViewById(R.id.tvFirstName)
        val score: TextView= view.findViewById(R.id.tvScoreGamer)
        val joueur: Joueur = items[position]
        firstName.text=joueur.firstName
        score.text= joueur.score

        return view
    }




}