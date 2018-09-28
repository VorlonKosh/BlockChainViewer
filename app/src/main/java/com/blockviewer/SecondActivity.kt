package com.blockviewer

import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View

import kotlinx.android.synthetic.main.activity_second.*
import org.json.JSONObject
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.security.AccessController.getContext


/**
 *
 * Block List activity
 * most of the heavy lift is done here
 * we daisy chain some async task to populate the block list without stalling the main UI thread
 * its a bit cumbersome with works given the timeframe
 *
 */
class SecondActivity : AppCompatActivity() {
    //constants
    val BLOCKSTOSHOW:Int = 20  //how many blocks we wish to show

    //log tags....for logging/debugging
    val GETBLOCKINFO: String = "GetBlockInfo"
    val GETBLOCKS: String = "GetBlocks"

    //API url
    val baseApiUrl: String ="https://api.eosnewyork.io/v1/"

    //api commands
    val getInfoUrl: String = baseApiUrl + "chain/get_info"
    val getBlockUrl: String = baseApiUrl + "chain/get_block"
    val getBlockHeader: String = baseApiUrl + "chain/get_block_header_state"

    //list to hold last 20 blocks
    val blockList = ArrayList<Block>()

    var recursiveCount: Int = BLOCKSTOSHOW
    var currentBlockId: String = ""  //holds the current block num

    private lateinit var blockAdapter: BlockAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)


        //list.set
        //val recyclerView = findViewById<View>(R.id.list) as RecyclerView

        //setup our RecyclerView
        list.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        list.addItemDecoration(dividerItemDecoration)
        list.layoutManager = layoutManager

        //recyclerview adapter
        blockAdapter = BlockAdapter(this, blockList, object : BlockAdapter.OnItemClickListener {
            override fun onItemClick(block: Block) {
                // Create an Intent to start the second activity
                val blockDetailsIntent = getBlockDetailsIntent()
                blockDetailsIntent.putExtra("blockJSON",block.raw_string)
                blockDetailsIntent.putExtra("blockID",block.block_id)
                // Start the new activity.
                startActivity(blockDetailsIntent)
            }
        })
        list.adapter = blockAdapter

        //this starts a daisychain of async tasks to populate the list
        //we get the chain info first then using the head_block_num recurse back
        //20 blocks
        AsyncTaskGetBlockChainInfo().execute()

    }

    fun getBlockDetailsIntent(): Intent {
        return Intent(this,DetailsActivity::class.java)
    }

    /**
     * Begins to populate the block chain list,
     * first gets chain info then proceeds to retrieve the last 20 blocks
     *
     * TODO: could of tried doAsync{} but time didn't permit
     *
     */


    inner class AsyncTaskGetBlockChainInfo : AsyncTask<String, String, String>() {
        override fun doInBackground(vararg p0: String?): String {

            //hold result
            var text: String

            //setup connects
            val url = URL(getInfoUrl)
            val connection = url.openConnection() as HttpURLConnection

            try {
                connection.connect()
                text = connection.inputStream.use { it.reader().use { reader -> reader.readText() } }

                if(BuildConfig.DEBUG) {
                    Log.e(GETBLOCKINFO, connection.getResponseCode().toString() )
                    Log.e(GETBLOCKINFO, text )
                }

            } finally {
                connection.disconnect()
            }

            return text
        }

        override fun onPostExecute(result: String?) {
            if(result != null){
                val chainInfo = processBlockInfo(result)

                //start building the block list...
                buildBlockChainList(chainInfo)
            }
            else{
                Log.e(GETBLOCKINFO, "ERROR: json result was null?" )
                //TODO display some toast error message?
            }
        }
    }


    /**
     * Process the json string get got from get_info and stick any info into
     * a data class. If we need more info we can always add fields to ChainInfo
     *
     */

    fun processBlockInfo(jsonString: String) : ChainInfo {

        val jsonObject = JSONObject(jsonString)

        return ChainInfo(jsonObject.getString("head_block_num"))

    }

    /**
     *
     * Build the block chain list
     *
     */

    fun buildBlockChainList(chainInfo: ChainInfo) {
        //recursively and async-ly  retreive the last 20 blocks
        //update list after each retreival and preforem checks
        //note: in UI thread.....

        //get head block info...
        recursiveCount = BLOCKSTOSHOW

        if(BuildConfig.DEBUG) {
            Log.e(GETBLOCKS, "begin recursive loop" )
        }

        getBlockAndAddToList(chainInfo.head_block_id)

    }


    /**
     *
     * Retreive a block and add it to the list and while count
     * greater than 0 recursively call self with prevBlock if not Genesis
     * block
     *
     */
    fun getBlockAndAddToList(block_id: String) {

        //
        recursiveCount--

        val blockAdapter: BlockAdapter = list.adapter as BlockAdapter

        blockAdapter.notifyDataSetChanged() //update list with new entry

        // count and id check
        if(recursiveCount >= 0){
            if(block_id != "0"){
                if(BuildConfig.DEBUG) {
                    Log.e(GETBLOCKS, "get block:" + block_id )
                }
                AsyncTaskGetBlockHeader().execute(block_id)
            }
        }
    }

    /**
     *
     * retreive block info
     *
     * again could rework to doAsync for more kotlin-spirted
     */


    inner class AsyncTaskGetBlock : AsyncTask<String, String, String>() {


        override fun doInBackground(vararg params: String?): String {

            var text: String

            //hackly build a quick json param to send
            val jsonString: String = """{ "block_num_or_id":""" + '"' + params[0] +"""" }"""
            val postData: ByteArray = jsonString.toByteArray(StandardCharsets.UTF_8)

            if(BuildConfig.DEBUG) {
                Log.e(GETBLOCKS, jsonString )
            }

            //build connection
            val url = URL(getBlockUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("charset", "utf-8")
            connection.setRequestProperty("Content-lenght", postData.size.toString()  )
            connection.setRequestProperty("Content-Type", "application/json")

            try {
                //try get responce
                val outputStream: DataOutputStream = DataOutputStream(connection.outputStream)
                outputStream.write(postData)

                connection.connect()
                text = connection.inputStream.use { it.reader().use { reader -> reader.readText() } }


            } finally {
                connection.disconnect()
            }

            return text

        }

        override fun onPostExecute(result: String?) {

            //add to list
            if(result != null) {
                val jsonObject = JSONObject(result)

                if(BuildConfig.DEBUG) {
                    Log.e(GETBLOCKS, result)
                    Log.e("transactions", jsonObject.getString("transactions"))
                }
                //block out block and add it to the list
                val block: Block = Block(
                        currentBlockId,
                        jsonObject.getString("timestamp"),
                        jsonObject.getString("previous"),
                        jsonObject.getString("producer"),
                        jsonObject.getString("producer_signature"),
                        result)

                blockList.add(block)
                //try next block
                getBlockAndAddToList(block.prev_block)
            }
        }
    }

    /**
     *
     * retrive some header info about a block
     * take header info and get block number to use instead of long id hash
     *
     */


    inner class AsyncTaskGetBlockHeader : AsyncTask<String, String, String>() {
        override fun doInBackground(vararg params: String?): String {

            var text: String

            val jsonString: String = """{ "block_num_or_id":""" + '"' + params[0] +"""" }"""
            val postData: ByteArray = jsonString.toByteArray(StandardCharsets.UTF_8)

            if(BuildConfig.DEBUG) {
                Log.e(GETBLOCKS, jsonString )
            }

            val url = URL("https://api.eosnewyork.io/v1/chain/get_block_header_state")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("charset", "utf-8")
            connection.setRequestProperty("Content-lenght", postData.size.toString()  )
            connection.setRequestProperty("Content-Type", "application/json")

            try {

                val outputStream: DataOutputStream = DataOutputStream(connection.outputStream)
                outputStream.write(postData)

                connection.connect()
                text = connection.inputStream.use { it.reader().use { reader -> reader.readText() } }

            } finally {
                connection.disconnect()
            }

            return text
        }

        override fun onPostExecute(result: String?) {

            val jsonObject =JSONObject(result)
            currentBlockId = jsonObject.getString("block_num")

            AsyncTaskGetBlock().execute(jsonObject.getString("block_num"))

        }
    }
}
