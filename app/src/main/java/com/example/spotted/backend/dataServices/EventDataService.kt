package com.example.spotted.backend.dataServices
import com.example.spotted.backend.dataModels.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

object EventDataService {

    internal var currentEvent: Event? = null

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
    fun getEvents(getEventsRequest: GetEventsRequest, onResult: (List<Event>?) -> Unit) {
        DataService.apiService.getEvents("Bearer ${DataService.authToken}", getEventsRequest).enqueue(object : Callback<List<Event>> {
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

    fun getJoinedEvents(onResult: (List<JoinedEvent>?) -> Unit) {
        DataService.apiService.getJoinedEvents("Bearer ${DataService.authToken}").enqueue(object : Callback<List<JoinedEvent>> {
            override fun onResponse(call: Call<List<JoinedEvent>>, response: Response<List<JoinedEvent>>) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    DataService.extractMsg(response.errorBody())
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<List<JoinedEvent>>, t: Throwable) {
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

    fun getRole(eventId: String, onResult: (String?) -> Unit) {
        DataService.apiService.getRole(eventId, "Bearer ${DataService.authToken}").enqueue(object : Callback<String> {
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

    fun getEventInfo(eventId: String, onResult: (Event?) -> Unit) {
        DataService.apiService.getEventInfo(eventId, "Bearer ${DataService.authToken}").enqueue(object : Callback<Event> {
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

    fun getParticipants(eventId: String, onResult: (List<User>?) -> Unit) {
        DataService.apiService.getParticipants(eventId, "Bearer ${DataService.authToken}").enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    DataService.extractMsg(response.errorBody())
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun getAdmins(eventId: String, onResult: (List<Admin>?) -> Unit) {
        DataService.apiService.getAdmins(eventId, "Bearer ${DataService.authToken}").enqueue(object : Callback<List<Admin>> {
            override fun onResponse(call: Call<List<Admin>>, response: Response<List<Admin>>) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    DataService.extractMsg(response.errorBody())
                    onResult(null)
                }
            }
            override fun onFailure(call: Call<List<Admin>>, t: Throwable) {
                onResult(null)
            }
        })
    }

    fun requestJoinEvent(eventId: String, userId: String, onResult: (Event?) -> Unit) {
        val joinRequest = JoinRequest(eventId, listOf(userId))
        DataService.apiService.requestJoinEvent("Bearer ${DataService.authToken}", joinRequest).enqueue(object : Callback<Event> {
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



    fun getRequests(eventId: String, onResult: (List<RequestToJoin>?) -> Unit) {
        DataService.apiService.getRequests(eventId, "Bearer ${DataService.authToken}").enqueue(object : Callback<List<RequestToJoin>> {
            override fun onResponse(call: Call<List<RequestToJoin>>, response: Response<List<RequestToJoin>>) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    DataService.extractMsg(response.errorBody())
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<List<RequestToJoin>>, t: Throwable) {
                onResult(null)
            }
        })
    }

    // pass in event id and one user id
    fun requestJoinEvent(eventId: String, userId: String) {
        requestJoinEvent(eventId, userId) { event ->
            if (event != null) {
                println("Requested to join event")
            }
        }
    }

    fun responseToRequest(requestID: String, action: String, onResult: (ResultResponse?) -> Unit) {
        val response = ResponseRequest(action)
        DataService.apiService.respondToRequest("Bearer ${DataService.authToken}", requestID, response).enqueue(object : Callback<ResultResponse> {
            override fun onResponse(call: Call<ResultResponse>, response: Response<ResultResponse>) {
                if (response.isSuccessful) {
                    onResult(response.body())
                } else {
                    DataService.extractMsg(response.errorBody())
                    onResult(null)
                }
            }

            override fun onFailure(call: Call<ResultResponse>, t: Throwable) {
                onResult(null)
            }
        })
    }


    fun setCurrentEvent(event: Event){
        currentEvent = event
    }

    fun getCurrentEvent(): Event?{
        return currentEvent
    }
}