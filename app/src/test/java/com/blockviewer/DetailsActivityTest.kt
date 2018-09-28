package com.blockviewer

import android.content.Context
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import android.support.v4.app.NotificationCompat.getExtras
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_details.*
import org.json.JSONObject
import org.mockito.Mockito
import org.mockito.Mockito.*


class DetailsActivityTest {

    val blockJSON = """{"timestamp":"2018-09-27T22:06:32.000","producer":"eosbeijingbp","confirmed":0,"previous":"011d1c3336571632cdb7ab9ee6da4f70be97ac023eb62640b4a5df6425cd339e","transaction_mroot":"cdf16a1d57d0310d889565a4903277692004e1f835cc6cdb8ca1ea7a5c31445c","action_mroot":"b8cd2b376bee62a57c2485ad22a4ed166b76f50bdd36e7ef11b108d53052840e","schedule_version":372,"new_producers":null,"header_extensions":[],"producer_signature":"SIG_K1_KXxmUDwPF3Kt2yy96CmxHtSSuAMx82NYJKoYdba9cqcMSYPn6UxMr3wxJMuThfnRszzAmjW5dfYFfcTtvzLDrm744vUqCn","transactions":[{"status":"executed","cpu_usage_us":681,"net_usage_words":0,"trx":"8059c5cc750e0086283d58eb31dcde6b6161dddbf51287a13632f637cada97a7"},{"status":"executed","cpu_usage_us":102,"net_usage_words":0,"trx":"3dbd57244e99931c1a252102af793ead986600adeb6b43fd4ba24f034d0dcc7d"},{"status":"executed","cpu_usage_us":154,"net_usage_words":14,"trx":{"id":"a44ede2c7906068e4ca6121fb035bccf35b954c2f13e5823c5d27091a1c25fe0","signatures":["SIG_K1_KhCFRsvPfSGw1HmobrkAqUXZiVUPsqFujKr8C8dyskaKz76biT5dKtE8UWWG6wZT21UzjvXa9b5FmTLYFd2juYzazzCSs4"],"compression":"none","packed_context_free_data":"","context_free_data":[],"packed_trx":"9e54ad5bdf1a88f36737000000000190558c8663aaba4a00ae4a974685a64101a09863fa4a98866600000000a8ed323210a09863fa4a9886666efa01000000000000","transaction":{"expiration":"2018-09-27T22:07:26","ref_block_num":6879,"ref_block_prefix":929559432,"max_net_usage_words":0,"max_cpu_usage_ms":0,"delay_sec":0,"context_free_actions":[],"actions":[{"account":"dexeoswallet","name":"cancelorder","authorization":[{"actor":"gu3dkmrugige","permission":"active"}],"data":{"owner":"gu3dkmrugige","tradepk":129646},"hex_data":"a09863fa4a9886666efa010000000000"}],"transaction_extensions":[]}}},{"status":"executed","cpu_usage_us":1113,"net_usage_words":27,"trx":{"id":"23dbd81dada969250b614dca9091344a0aface0f8e46f7ed618d234b64713b28","signatures":["SIG_K1_KWu4JaqUkBBAfqNbj8wRmksf3Gbcicd7HEp3mSXyzWD74Saq3NDrWSm9dpH7nVPF9SCixKb7E4ovkcLYMFxGJaUHLY8Jgv"],"compression":"none","packed_context_free_data":"","context_free_data":[],"packed_trx":"a354ad5beb1a2f1b5d4500000000011082422e6575305500405647ed48b1ba0140a7c3066575305500000000489aa6b94a3ac45247b6c006e30020057eedbe0b932921f83b39f9e6c21402626cfc88a325051df42208947cbb823d35bc61ca0be4ba2329f4fe1730f183ce5ef125ce8b830619ff690316eeeb071000","transaction":{"expiration":"2018-09-27T22:07:31","ref_block_num":6891,"ref_block_prefix":1163729711,"max_net_usage_words":0,"max_cpu_usage_ms":0,"delay_sec":0,"context_free_actions":[],"actions":[{"account":"eosbetdice11","name":"resolvebet","authorization":[{"actor":"eosbetcasino","permission":"random"}],"data":{"bet_id":"16358974585583092794","sig":"SIG_K1_KVU27mqy7E7BmUrVHPnjRQjHR4fBbLzDwst3Vnh25kziEPyb4efgpJX1xqtU22h27uv78jHwWjMf3XLg38BkAHJZvUjTh6"},"hex_data":"3ac45247b6c006e30020057eedbe0b932921f83b39f9e6c21402626cfc88a325051df42208947cbb823d35bc61ca0be4ba2329f4fe1730f183ce5ef125ce8b830619ff690316eeeb0710"}],"transaction_extensions":[]}}}],"block_extensions":[],"id":"011d1c34c0ca2bd9c28dddaa783c76e76cb437df7b0687e9f9e3d563d8ad5b94","block_num":18684980,"ref_block_prefix":2866646466}"""
    val blockId = """18684980"""


    @Before
    fun setUp() {

    }


    //servere lack of unit testing documenation/literature for kotlin android
    @Test
    fun `should inflate layout`() {
        // tested = spy(DetailsActivity())

        //tested.setupDetails("0")
        //val extras = mock(Bundle::class.java)
        //.
        //tested.onCreate(extras)
        //verify(tested).setContentView(R.layout.activity_details)
    }

    @After
    fun tearDown() {
    }
}