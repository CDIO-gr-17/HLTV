package com.example.hltv.data.remote

//Classes for parsing this: https://rapidapi.com/fluis.lacasse/api/allsportsapi2/
//All classes are auto-generated from https://json2kt.com/
//Kotlin docs saying this long thing is okay: https://kotlinlang.org/docs/coding-conventions.html

//package com.example.example

import com.google.gson.annotations.SerializedName

data class Sport (

    @SerializedName("name" ) var name : String? = null,
    @SerializedName("slug" ) var slug : String? = null,
    @SerializedName("id"   ) var id   : Int?    = null

)

data class Category (

    @SerializedName("name"  ) var name  : String? = null,
    @SerializedName("slug"  ) var slug  : String? = null,
    @SerializedName("sport" ) var sport : Sport?  = Sport(),
    @SerializedName("id"    ) var id    : Int?    = null,
    @SerializedName("flag"  ) var flag  : String? = null

    )


data class Country (

    @SerializedName("alpha2" ) var alpha2 : String? = null,
    @SerializedName("name"   ) var name   : String? = null

)
data class UniqueTournament (

    @SerializedName("name"                        ) var name                        : String?   = null,
    @SerializedName("slug"                        ) var slug                        : String?   = null,
    @SerializedName("category"                    ) var category                    : Category? = Category(),
    @SerializedName("userCount"                   ) var userCount                   : Int?      = null,
    @SerializedName("id"                          ) var id                          : Int?      = null,
    @SerializedName("country"                     ) var country                     : Country?  = Country(),
    @SerializedName("hasEventPlayerStatistics"    ) var hasEventPlayerStatistics    : Boolean?  = null,
    @SerializedName("crowdsourcingEnabled"        ) var crowdsourcingEnabled        : Boolean?  = null,
    @SerializedName("hasPerformanceGraphFeature"  ) var hasPerformanceGraphFeature  : Boolean?  = null,
    @SerializedName("displayInverseHomeAwayTeams" ) var displayInverseHomeAwayTeams : Boolean?  = null

)


data class Tournament (

    @SerializedName("name"             ) var name             : String?           = null,
    @SerializedName("slug"             ) var slug             : String?           = null,
    @SerializedName("category"         ) var category         : Category?         = Category(),
    @SerializedName("uniqueTournament" ) var uniqueTournament : UniqueTournament? = UniqueTournament(),
    @SerializedName("priority"         ) var priority         : Int?              = null,
    @SerializedName("id"               ) var id               : Int?              = null

)



data class Status (

    @SerializedName("code"        ) var code        : Int?    = null,
    @SerializedName("description" ) var description : String? = null,
    @SerializedName("type"        ) var type        : String? = null

)

data class TeamColors (

    @SerializedName("primary"   ) var primary   : String? = null,
    @SerializedName("secondary" ) var secondary : String? = null,
    @SerializedName("text"      ) var text      : String? = null

)

data class Team (

    @SerializedName("name"       ) var name       : String?           = null,
    @SerializedName("slug"       ) var slug       : String?           = null,
    @SerializedName("shortName"  ) var shortName  : String?           = null,
    @SerializedName("sport"      ) var sport      : Sport?            = Sport(),
    @SerializedName("userCount"  ) var userCount  : Int?              = null,
    @SerializedName("nameCode"   ) var nameCode   : String?           = null,
    @SerializedName("national"   ) var national   : Boolean?          = null,
    @SerializedName("type"       ) var type       : Int?              = null,
    @SerializedName("id"         ) var id         : Int?              = null,
    @SerializedName("country"    ) var country    : Country?          = Country(),
    @SerializedName("subTeams"   ) var subTeams   : ArrayList<String> = arrayListOf(),
    @SerializedName("teamColors" ) var teamColors : TeamColors?       = TeamColors()

)



data class HomeScore (

    @SerializedName("current" ) var current : Int? = null,
    @SerializedName("display" ) var display : Int? = null

)


data class AwayScore (

    @SerializedName("current" ) var current : Int? = null,
    @SerializedName("display" ) var display : Int? = null

)


data class Time (

    @SerializedName("currentPeriodStartTimestamp" ) var currentPeriodStartTimestamp : Int? = null

)



data class Changes (

    @SerializedName("changes"         ) var changes         : ArrayList<String> = arrayListOf(),
    @SerializedName("changeTimestamp" ) var changeTimestamp : Int?              = null

)

/**
 * Is actually only one event?
 */
data class Event (

    @SerializedName("tournament"                      ) var tournament                      : Tournament? = Tournament(),
    @SerializedName("customId"                        ) var customId                        : String?     = null,
    @SerializedName("status"                          ) var status                          : Status?     = Status(),
    @SerializedName("winnerCode"                      ) var winnerCode                      : Int?        = null,
    @SerializedName("homeTeam"                        ) var homeTeam                        : Team?   = Team(),
    @SerializedName("awayTeam"                        ) var awayTeam                        : Team?   = Team(),
    @SerializedName("homeScore"                       ) var homeScore                       : HomeScore?  = HomeScore(),
    @SerializedName("awayScore"                       ) var awayScore                       : AwayScore?  = AwayScore(),
    @SerializedName("coverage"                        ) var coverage                        : Int?        = null,
    @SerializedName("time"                            ) var time                            : Time?       = Time(),
    @SerializedName("changes"                         ) var changes                         : Changes?    = Changes(),
    @SerializedName("hasGlobalHighlights"             ) var hasGlobalHighlights             : Boolean?    = null,
    @SerializedName("crowdsourcingDataDisplayEnabled" ) var crowdsourcingDataDisplayEnabled : Boolean?    = null,
    @SerializedName("id"                              ) var id                              : Int?        = null,
    @SerializedName("bestOf"                          ) var bestOf                          : Int?        = null,
    @SerializedName("eventType"                       ) var eventType                       : String?     = null,
    @SerializedName("startTimestamp"                  ) var startTimestamp                  : Int?        = null,
    @SerializedName("slug"                            ) var slug                            : String?     = null,
    @SerializedName("finalResultOnly"                 ) var finalResultOnly                 : Boolean?    = null,
    @SerializedName("isEditor"                        ) var isEditor                        : Boolean?    = null,
    @SerializedName("crowdsourcingEnabled"            ) var crowdsourcingEnabled            : Boolean?    = null

)

data class EventsWrapper(
    @SerializedName("events") var events: List<Event>
)