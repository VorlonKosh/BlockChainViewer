package com.blockviewer

import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.V
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import kotlinx.android.synthetic.main.activity_details.*
import org.json.JSONObject
import kotlinx.android.synthetic.main.activity_details.view.*
import org.json.JSONArray
import android.widget.ArrayAdapter




class DetailsActivity : AppCompatActivity() {

    lateinit var blockJSON: JSONObject
    lateinit var jsonArray: JSONArray
    //val transactionList = ArrayList<JSONObject>()
    var transNum: Int  = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        //rebuild our json object from intent
        blockJSON = JSONObject(intent.extras.getString("blockJSON"))

        //display out data on the activity
        setupDetails(intent.extras.getString("blockID"))

        //
        //val jsonArray: JSONArray = JSONArray(blockJSON.getString("transactions"))

    }


    fun setupDetails(blockID: String){

        //populate some textviews...
        block_id_detail_text.text = "Block:  " + blockID;
        block_producer_detail_text.text = "Producer:  " + blockJSON.getString("producer")

        block_signature_detail_text.text = "Signature:  " + blockJSON.getString("producer_signature")

        //count transactions...
        jsonArray = JSONArray(blockJSON.getString("transactions"))
        val transCount: Int = jsonArray.length()
        block_transaction_detail_text.text = "Transaction Count: " + transCount.toString()

        // add some transactions to the list
        val tranStringList = ArrayList<String>()

        var i = 0
        while (i < transCount)
            tranStringList.add("Transaction # " + i++)

        val listAdapter = ArrayAdapter <String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, tranStringList);

        //show or hide the raw transaction data on demand...
        trans_list.adapter = listAdapter
        trans_list.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            transNum = position
            block_raw_content_text.text = jsonArray[transNum].toString()

            if (block_raw_content_text.getVisibility() == View.VISIBLE) {
                block_raw_content_text.setVisibility(View.GONE);
                trans_list.visibility = View.VISIBLE
                view_raw_button.text = getString(R.string.show_raw_contents)
            } else {
                block_raw_content_text.setVisibility(View.VISIBLE);
                view_raw_button.text = getString(R.string.hide_raw_contents)
                trans_list.visibility = View.GONE
            }
        }
    }

    /**
     * toggle raw data on/off manually with button click
     */
    fun toggleRaw(view: View) {
        if (block_raw_content_text.getVisibility() == View.VISIBLE) {
            block_raw_content_text.setVisibility(View.GONE);
            trans_list.visibility = View.VISIBLE
            view_raw_button.text = getString(R.string.show_raw_contents)
        } else {
            block_raw_content_text.setVisibility(View.VISIBLE);
            view_raw_button.text = getString(R.string.hide_raw_contents)
            trans_list.visibility = View.GONE
        }


        block_raw_content_text.text = jsonArray[transNum].toString()

    }
}
