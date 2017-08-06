package eu.nanooq.otkd.viewModels.chat

import android.os.Bundle
import com.androidhuman.rxfirebase2.database.dataChanges
import com.google.firebase.database.GenericTypeIndicator

import eu.nanooq.otkd.helpers.FirebaseHelper
import eu.nanooq.otkd.models.API.Message
import eu.nanooq.otkd.models.API.User
import eu.nanooq.otkd.models.UI.MessageItem
import eu.nanooq.otkd.toBase64
import eu.nanooq.otkd.viewModels.base.BaseViewModel
import eu.nanooq.otkd.viewModels.main.sections.ChatView
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.toFlowable
import timber.log.Timber

import java.util.*

/**
 * Created by jvolp on 04.08.2017.
 */

class ChatViewModel : BaseViewModel<ChatView>() {

    var mDisposable: Disposable? = null

    private lateinit var messagesFlowable: Flowable<ArrayList<Message>>

    override fun onCreate(arguments: Bundle?, savedInstanceState: Bundle?) {
        Timber.d("onCreate()")
        super.onCreate(arguments, savedInstanceState)


    }

    override fun onStart() {
        Timber.d("onStart()")
        super.onStart()
        val user = mPreferencesHelper.getUser()

        user?.let { loadUserData(user) }


    }

    override fun onStop() {
        Timber.d("onStop()")
        super.onStop()
    }

    override fun onSaveInstanceState(bundle: Bundle) {
        Timber.d("onSaveInstanceState()")
        super.onSaveInstanceState(bundle)
    }

    override fun onDestroy() {
        Timber.d("onDestroy()")
        super.onDestroy()
        mDisposable?.dispose()
    }

    private fun loadUserData(user: User) {
        Timber.i("loadUserData() ${user.team_name}")


        val messagesObservable = mFirebaseHelper.mFBDBReference
                .child(FirebaseHelper.CHAT)
                .child(user.team_name.toBase64())
                .child(FirebaseHelper.MESSAGES)
                .dataChanges()
                .map {

                    Timber.d("messages map() $it")
                    var array = ArrayList<Message>()
                    try {

                        val typeList = object : GenericTypeIndicator<HashMap<String, Message>>() {}
                        val message = it.getValue(typeList)
                        Timber.d("messages mapinTry() ${message}")
                        message?.forEach {
                            Timber.d("messages map2 ${it.value}")
                            array.add(it.value)
                        }

                    } catch (exc: Exception) {
                        Timber.d("messages mapException ${exc}")
                        val typeList = object : GenericTypeIndicator<ArrayList<Message>>() {}
                        array = it.getValue(typeList) ?: ArrayList()
                        Timber.d("messages mapException ${array}")

                    }
                    Timber.d("messages array ${array}")
                    array
                }
                .toFlowable(BackpressureStrategy.LATEST)

        val obsList = listOf(messagesObservable)
        obsList.toFlowable()


        // mDisposable = Flowable. combineLatest(messagesObservable, ArrayList<MessageItem> { messages ->
        mDisposable = messagesObservable.subscribe({
            Timber.d("Chat subscribe ${it}it")

            val messages = it
            val messageItems = ArrayList<MessageItem>()

            messages.forEach {
                val messageItem = MessageItem()
                with(messageItem) {
                    //val messageId = it.id ?: 0
                   //Id = messageId
                    captain = it.captain?: false
                    sender = it.sender?: ""
                    text = it.text?: ""
                    timestamp = (it.timestamp * 1000).toLong()
                }


                val date = Date((it.timestamp*1000).toLong())
                Timber.d("MessageTimestamp::${it.timestamp}:::${date}")
                messageItems.add(messageItem)
            }
            Timber.d("MessageArray: ${messageItems}")
            messageItems.sortBy { it.timestamp }
            Timber.d("MessageArray2: ${messageItems}")
            view?.updateAdapter(messageItems)

        }, {
            Timber.e("onError() ${it.message}")
        }, {
            Timber.e("onComplete()")
        })


    }

    fun  sendMessage(message: String) {
        val user = mPreferencesHelper.getUser()
        val keyMessaging = user?.team_name.toBase64()
        val keyMessage = mFirebaseHelper.mFBDBReference.child("posts").push().getKey()



        val messageToSave = Message()
                with(messageToSave) {
                    captain = false
                    text = message
                    timestamp = (Date().time).toDouble()
                    sender = "Ivo"
                    sender_id = "abc"
                }

        mFirebaseHelper.mFBDBReference
                .child(FirebaseHelper.CHAT)
                .child(keyMessaging)
                .child(FirebaseHelper.MESSAGES)
                .child(keyMessage)
                .setValue(messageToSave)


        Timber.d("SEND MESS: ${user?.team_name}")
        Timber.d("SEND MESS: ${keyMessage}")
        Timber.d("SEND MESS: ${message}")
    }
}
