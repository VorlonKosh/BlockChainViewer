package com.blockviewer

data class Block(val block_id: String,
                 val time_stamp: String,
                 val prev_block: String,
                 val producer: String,
                 val producer_signature: String,
                 val raw_string: String)