package iwd.props.com.greeterapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Future;
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3jFactory
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.protocol.infura.InfuraHttpService
import java.math.BigInteger

class MainActivity : AppCompatActivity() {
    private val TAG = javaClass.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val contractAddress = "0x8394cDf176A4A52DA5889f7a99c4f7AD2BF59088"
        val url = "https://rinkeby.infura.io/v3/01eb8f7b5e514832af8e827c23784d23"
        val web3j = Web3jFactory.build(InfuraHttpService(url))
        val gasLimit: BigInteger = BigInteger.valueOf(20_000_000_000L)
        val gasPrice: BigInteger = BigInteger.valueOf(4300000)

        /*** HONOR SYSTEM TEST ETHER ACCOUNT (ಠ_ಠ) ***/
        val credentials = Credentials.create("f9319fe162c31947c0ca8fd649a536b7ca311b5f210afdc48b62fd7d18ce53e4")

        val greeter = Greeter.load(contractAddress, web3j, credentials, gasLimit, gasPrice)

        fab.setOnClickListener { _ ->
            /*** SIMPLE EXECUTION ONLY FOR DEMO PURPOSES.... THIS THREAD INITIALIZATION HERE IS BAD PRACTICE ***/
            val thread = Thread(Runnable {
                try {

                    // check contract validity
                    Log.d(TAG, " ${greeter.isValid}")

                    // read from contract
                    val greeting: Future<String>? = greeter.greet().sendAsync()
                    val convertToString: String? = greeting?.get()
                    Log.d(TAG, "greeting value returned: $convertToString")

                    // write to contract
                    val transactionReceipt: Future<TransactionReceipt>? = greeter.changeGreeting("Greeting changed from an Android App (ಠ_ಠ) ").sendAsync()
                    val result = "Successful transaction. Gas used: ${transactionReceipt?.get()?.blockNumber}  ${transactionReceipt?.get()?.gasUsed}"
                    Log.d(TAG, result)

                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.d(TAG, "error accessing contract: " + e.message)
                }
            })

            thread.start()
        }
    }

}
