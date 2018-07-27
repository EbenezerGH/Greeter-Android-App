package iwd.props.com.greeterapp

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3jFactory
import org.web3j.protocol.infura.InfuraHttpService
import java.math.BigInteger

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val contractAddress = "0x8394cDf176A4A52DA5889f7a99c4f7AD2BF59088"
        val url = "https://rinkeby.infura.io/v3/01eb8f7b5e514832af8e827c23784d23"
        val web3j = Web3jFactory.build(InfuraHttpService(url))
        val gasLimit: BigInteger = BigInteger.valueOf(21000)
        val gasPrice: BigInteger = BigInteger.valueOf(600000)

        val credentials: Credentials = Credentials.create(
                "f9319fe162c31947c0ca8fd649a536b7ca311b5f210afdc48b62fd7d18ce53e4","0x2Dd361B86bb18FDe3cdfDD11b9aC7461C0FB2Ea2")

        val greeter = Greeter(contractAddress, web3j, credentials ,gasLimit, gasPrice)

        fab.setOnClickListener { view ->

            val thread = Thread(Runnable {
                try {
                    /*** SIMPLE EXECUTION ONLY FOR DEMO PURPOSES.... THIS THREAD INITIALIZATION HERE IS BAD PRACTICE ***/
                    //Log.d("Ebenezer", " ${greeter.isValid}")
                    greeter.changeGreeting("Greeting changed from an Android App (ಠ_ಠ) ")
                    greeter.greet()
                    //greeter.kill()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            })

            thread.start()


        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
