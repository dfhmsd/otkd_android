package eu.nanooq.otkd.viewModels.chat

import android.os.Bundle
import com.androidhuman.rxfirebase2.database.dataChanges
import com.google.firebase.database.GenericTypeIndicator
import eu.nanooq.otkd.helpers.FirebaseHelper
import eu.nanooq.otkd.models.API.*
import eu.nanooq.otkd.models.UI.MessageItem
import eu.nanooq.otkd.toBase64
import eu.nanooq.otkd.viewModels.base.BaseViewModel
import eu.nanooq.otkd.viewModels.main.sections.IChatView
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

/**
 *
 * Created by jvolp on 04.08.2017.
 */

class ChatViewModel : BaseViewModel<IChatView>() {

    var mDisposable: Disposable? = null
    var teamMembers: ArrayList<Member>? = null

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

        mDisposable?.dispose()
    }

    override fun onSaveInstanceState(bundle: Bundle) {
        Timber.d("onSaveInstanceState()")
        super.onSaveInstanceState(bundle)
    }

    override fun onDestroy() {
        Timber.d("onDestroy()")
        super.onDestroy()
    }

    private fun loadUserData(user: User) {
        Timber.i("loadUserData() ${user.team_name}")
        val userId: String = when (user) {
            is UserCaptain -> user.email
            is UserRunner -> "${user.team_name} ${user.first_name} ${user.last_name}"
            else -> ""
        }


        messagesFlowable = mFirebaseHelper.mFBDBReference
                .child(FirebaseHelper.CHAT)
                .child(user.team_name.toBase64())
                .child(FirebaseHelper.MESSAGES)
                .dataChanges()
                .map {

                    var array = ArrayList<Message>()
                    try {

                        val typeList = object : GenericTypeIndicator<HashMap<String, Message>>() {}
                        val message = it.getValue(typeList)
                        message?.forEach {
                            array.add(it.value)
                        }

                    } catch (exc: Exception) {
                        Timber.d("messages mapException $exc")
                        val typeList = object : GenericTypeIndicator<ArrayList<Message>>() {}
                        array = it.getValue(typeList) ?: ArrayList()

                    }
                    array
                }
                .toFlowable(BackpressureStrategy.BUFFER)

        val teamFlowable = mFirebaseHelper.mFBDBReference
                .child(FirebaseHelper.TEAM_MEMBERS)
                .child(user.team_name.toBase64())
                .child(FirebaseHelper.MEMBERS)
                .dataChanges()
                .map {
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
                .toFlowable(BackpressureStrategy.BUFFER)

        teamFlowable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
            teamMembers = it
        })



        mDisposable = messagesFlowable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
            val messages = it
            val messageItems = ArrayList<MessageItem>()

            messages.forEach {
                val messageItem = MessageItem()
                with(messageItem) {
                    isCaptain = it.captain ?: false
                    sender = it.sender ?: ""
                    text = it.text ?: ""
                    timestamp = (it.timestamp * 1000).toLong()
                    isUser = it.sender_id == userId.toBase64()
                }

                val date = Date((it.timestamp * 1000).toLong())
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


    fun sendMessage(message: String) {
        val user = mPreferencesHelper.getUser()
        val keyMessaging = user?.team_name.toBase64()
        val keyMessage = mFirebaseHelper.mFBDBReference.child("posts").push().key
        val name: String
        val senderId: String
        if (user is UserCaptain) {
            val captain = getCaptainDetails()
            name = captain?.first_name + " " + captain?.last_name
            senderId = user.email.toBase64().toString()
        } else {
            val member = user as UserRunner
            name = member.first_name + " " + member.last_name
            senderId = (user.team_name + " " + member.first_name + " " + member.last_name)
                    .toBase64().toString()
        }

        val messageToSave = Message()
        with(messageToSave) {
            captain = user is UserCaptain
            text = message
            timestamp = System.currentTimeMillis().toDouble() / 1000


            sender = name
            sender_id = senderId
        }

        Timber.d("MESS TO SEND: ${messageToSave.sender}")
        mFirebaseHelper.mFBDBReference
                .child(FirebaseHelper.CHAT)
                .child(keyMessaging)
                .child(FirebaseHelper.MESSAGES)
                .child(keyMessage)
                .setValue(messageToSave)


        Timber.d("SEND MESS: ${user.team_name}")
        Timber.d("SEND MESS: $keyMessage")
        Timber.d("SEND MESS: $message")
    }
}
