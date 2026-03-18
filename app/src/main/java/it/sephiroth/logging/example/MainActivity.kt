package it.sephiroth.logging.example

import android.app.Activity
import android.os.Bundle
import android.util.Log
import it.sephiroth.android.library.asm.trunk.runtime.Trunk

class MainActivity : Activity() {
    class Test {
        fun test() {
            Trunk.d("test message")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val logger = Trunk.tag("myTag")
        logger.i("logger message")
        logger.i("logger message %s", "test")

        Trunk.v("test message")
        Trunk.d("Trunk version is: ${Trunk.BUILD_DATE}")
        Test().test()

        Log.v(TAG, "Log message")

        setContentView(R.layout.main)
    }

    companion object {
        const val TAG = "MainActivity"
    }
}
