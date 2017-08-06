package eu.nanooq.otkd.viewModels.chat

import android.os.Bundle
import com.androidhuman.rxfirebase2.database.data
import com.androidhuman.rxfirebase2.database.dataChanges
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener

import eu.nanooq.otkd.helpers.FirebaseHelper
import eu.nanooq.otkd.models.API.*
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
import kotlin.collections.ArrayList

/**
 * Created by jvolp on 04.08.2017.
 */

class ChatViewModel : BaseViewModel<ChatView>() {

    var mDisposable: Disposable? = null
    var teamMembers: ArrayList<Member>?= null

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


        messagesFlowable = mFirebaseHelper.mFBDBReference
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

        val teamFlowable = mFirebaseHelper.mFBDBReference
                .child(FirebaseHelper.TEAM_MEMBERS)
                .child(user.team_name.toBase64())
                .child(FirebaseHelper.MEMBERS)
                .dataChanges()
                .map {
                    Timber.d("teamObservable map() $it")
                    var array = ArrayList<Member>()
                    try {
                        val typeList = object : GenericTypeIndicator<HashMap<String, Member>>() {}
                        val team = it.getValue(typeList)
                        team?.forEach {
                            array.add(it.value)
                        }

                    } catch (exc: Exception) {
                        val typeList = object : GenericTypeIndicator<ArrayList<Member>>() {}
                        array = it.getValue(typeList) ?: ArrayList()

                    }

                    array
                }
                .toFlowable(BackpressureStrategy.LATEST)

        teamFlowable.subscribe({
            teamMembers = it
        })



        mDisposable = messagesFlowable.subscribe({
            Timber.d("Chat subscribe ${it}it")
            val messages = it
            val messageItems = ArrayList<MessageItem>()

            messages.forEach {
                val messageItem = MessageItem()
                with(messageItem) {
                    captain = it.captain?: false
                    sender = it.sender?: ""
                    text = it.text?: ""
                    timestamp = (it.timestamp * 1000).toLong()
                }

                val date = Date((it.timestamp*1000).toLong())
                Timber.d("MessageTimestamp::${it.timestamp}:::${date}")
                messageItems.add(messageItem)
            }

            messageItems.sortBy { it.timestamp }

            view?.updateAdapter(messageItems)

        }, {
            Timber.e("onError() ${it.message}")
        }, {
            Timber.e("onComplete()")
        })
    }


    fun getCaptainDetails(): Member? {
        teamMembers?.forEach {
            if (it.is_captain) {
                return it
            }
        }
        return null
    }


    fun  sendMessage(message: String) {
        val user = mPreferencesHelper.getUser()
        val keyMessaging = user?.team_name.toBase64()
        val keyMessage = mFirebaseHelper.mFBDBReference.child("posts").push().getKey()
        val name: String
        if (user is UserCaptain) {
            val captain = getCaptainDetails()
            name = captain?.first_name + " " + captain?.last_name
        } else {
            val member = user as UserRunner
            name = member.first_name + " " + member.last_name
        }


        val messageToSave = Message()
                with(messageToSave) {
                    captain = user is UserCaptain
                    text = message
                    timestamp = (Date().time).toDouble()
                    sender = name
                    sender_id = name.toBase64()
                }

        Timber.d("MESS TO SEND: ${messageToSave.sender}")
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
