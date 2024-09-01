package com.example.spotted.backend.dataServices
import com.example.spotted.backend.dataModels.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object EventDataService {

    fun createEvent(event: Event, onResult: (Event?) -> Unit) {
        DataService.apiService.createEvent("Bearer ${DataService.authToken}", event).enqueue(object : Callback<Event> {
            override fun onResponse(call: Call<Event>, response: Response<Event>) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    DataService.extractMsg(response.errorBody())
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<Event>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun getJoinedEvents(onResult: (List<Event>?) -> Unit) {
        DataService.apiService.getJoinedEvents("Bearer ${DataService.authToken}").enqueue(object : Callback<List<Event>> {
            override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    DataService.extractMsg(response.errorBody())
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun respondInvitation(eventId: String, status: String, onResult: (Event?) -> Unit) {
        DataService.apiService.respondInvitation("Bearer ${DataService.authToken}", eventId, status).enqueue(object : Callback<Event> {
            override fun onResponse(call: Call<Event>, response: Response<Event>) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    DataService.extractMsg(response.errorBody())
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<Event>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun respondInvitation(eventId: Event,status: Boolean) {
        val statuss = if (status) "accept" else "reject"
        respondInvitation(eventId._id, statuss) { event ->
            if (event != null) {
                println("Event $status")
            }
        }
    }

    fun inviteAdminEvents(eventId: String, userId: String, mode: String, onResult: (String?) -> Unit) {
        DataService.apiService.inviteAdminEvents("Bearer ${DataService.authToken}", eventId, userId, mode).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    DataService.extractMsg(response.errorBody())
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun inviteAdminEvents(eventId: Event, userId: String, mode: String) {
        inviteAdminEvents(eventId._id, userId, mode) { response ->
            if (response != null) {
                println("Invited Admin")
            }
        }
    }

    fun inviteUserToEvent(invite: InviteRequest, onResult: (Invitation?) -> Unit){
        DataService.apiService.inviteUserToEvent("Bearer ${DataService.authToken}", invite).enqueue(object : Callback<Invitation> {
            override fun onResponse(call: Call<Invitation>, response: Response<Invitation>) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    DataService.extractMsg(response.errorBody())
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<Invitation>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun inviteUserToEvent(eventID: String, userID: String, onResult: (Invitation?) -> Unit){
        val invite = InviteRequest(eventID, userID)
        inviteUserToEvent(invite, onResult)
    }
}