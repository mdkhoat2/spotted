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