package com.example.hltv.data.remote
import com.google.gson.annotations.SerializedName

//Classes for parsing this: https://rapidapi.com/fluis.lacasse/api/allsportsapi2/
//Most classes are auto-generated from https://json2kt.com/
//Kotlin docs saying this long thing is okay: https://kotlinlang.org/docs/coding-conventions.html


sealed class APIResponse{
    data class GameWrapper (
        @SerializedName("games") var games: List<Game>
    ) : APIResponse()
    data class EventsWrapper(

        @SerializedName("events") var events: List<Event>

        ) : APIResponse()
    data class Lineup (

        @SerializedName("confirmed" ) var confirmed : Boolean? = null,
        @SerializedName("home"      ) var home      : PlayerGroup?    = PlayerGroup(),
        @SerializedName("away"      ) var away      : PlayerGroup?    = PlayerGroup()

    ) : APIResponse()




    data class TeamContainer (
        @SerializedName("team" ) var team : Team
    ) : APIResponse()

    data class TournamentWrapper (
        @SerializedName("groups") var uniqueTournament: List<ExtraWrapper>
    ) : APIResponse()

    data class CategoryWrapper (

        @SerializedName("categories" ) var categories : ArrayList<Category> = arrayListOf()

    ) :APIResponse()

    data class ThirdTournamentWrapper (
        @SerializedName("uniqueTournament") var tournamentDetails : ThirdUniqueTournament
    ) : APIResponse()
}




////////////////////////////////////////////////////////////////////////
data class Sport (

    @SerializedName("name" ) var name : String? = null,
    @SerializedName("slug" ) var slug : String? = null,
    @SerializedName("id"   ) var id   : Int?    = null

)

data class Category (

    @SerializedName("name"  ) var name : String? = null,
    @SerializedName("slug"  ) var slug  : String? = null,
    @SerializedName("sport" ) var sport : Sport?  = Sport(),
    @SerializedName("priority" ) var priority : Int?    = null,
    @SerializedName("id"    ) var id    : Int?    = null,
    @SerializedName("flag"  ) var flag  : String? = null

    )

data class ExtraWrapper (

    @SerializedName("uniqueTournaments") var wrapper: List<SecondUniqueTournament>

)

data class Country (

    @SerializedName("alpha2" ) var alpha2 : String? = null,
    @SerializedName("name"   ) var name   : String? = null

)
data class ThirdUniqueTournament (

    @SerializedName("name"                        ) var name                        : String?           = null,
    @SerializedName("slug"                        ) var slug                        : String?           = null,
    @SerializedName("logo"                        ) var logo                        : Logo?             = Logo(),
    @SerializedName("category"                    ) var category                    : Category?         = Category(),
    @SerializedName("userCount"                   ) var userCount                   : Int?              = null,
    @SerializedName("mostTitles"                  ) var mostTitles                  : Int?              = null,
    @SerializedName("mostTitlesTeams"             ) var mostTitlesTeams             : ArrayList<String> = arrayListOf(),
    @SerializedName("linkedUniqueTournaments"     ) var linkedUniqueTournaments     : ArrayList<String> = arrayListOf(),
    @SerializedName("hasStandingsGroups"          ) var hasStandingsGroups          : Boolean?          = null,
    @SerializedName("hasRounds"                   ) var hasRounds                   : Boolean?          = null,
    @SerializedName("hasPlayoffSeries"            ) var hasPlayoffSeries            : Boolean?          = null,
    @SerializedName("upperDivisions"              ) var upperDivisions              : ArrayList<String> = arrayListOf(),
    @SerializedName("lowerDivisions"              ) var lowerDivisions              : ArrayList<String> = arrayListOf(),
    @SerializedName("crowdsourcingEnabled"        ) var crowdsourcingEnabled        : Boolean?          = null,
    @SerializedName("hasPerformanceGraphFeature"  ) var hasPerformanceGraphFeature  : Boolean?          = null,
    //@SerializedName("periodLength"                ) var periodLength                : PeriodLength?     = PeriodLength(),
    @SerializedName("id"                          ) var id                          : Int?              = null,
    @SerializedName("country"                     ) var country                     : Country?          = Country(),
    @SerializedName("startDateTimestamp"          ) var startDateTimestamp          : Int?              = null,
    @SerializedName("endDateTimestamp"            ) var endDateTimestamp            : Int?              = null,
    @SerializedName("displayInverseHomeAwayTeams" ) var displayInverseHomeAwayTeams : Boolean?          = null

)
data class Logo (

    @SerializedName("md5" ) var md5 : String? = null,
    @SerializedName("id"  ) var id  : Int?    = null

)
/*
data class PeriodLength(


)*/

data class SecondUniqueTournament (

    @SerializedName("name"                        ) var name                        : String?   = null,
    @SerializedName("slug"                        ) var slug                        : String?   = null,
    @SerializedName("category"                    ) var category                    : Category? = Category(),
    @SerializedName("userCount"                   ) var userCount                   : Int?      = null,
    @SerializedName("id"                          ) var id                          : Int?      = null,
    @SerializedName("displayInverseHomeAwayTeams" ) var displayInverseHomeAwayTeams : Boolean?  = null

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

/**
 * Does not hold players
 */
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



data class Score (

    @SerializedName("current" ) var current : Int? = null,
    @SerializedName("display" ) var display : Int? = null

)


data class Map(
    @SerializedName("id" ) var id : Int? = null,
    @SerializedName("name" ) var name : String? = null
)


data class Game (

    @SerializedName("length"                ) var length                : Int?       = null,
    @SerializedName("status"                ) var status                : Status?    = Status(),
    @SerializedName("winnerCode"            ) var winnerCode            : Int?       = null,
    @SerializedName("map"                   ) var map                   : Map?       = Map(),
    @SerializedName("homeScore"             ) var homeScore             : HomeScore? = HomeScore(),
    @SerializedName("awayScore"             ) var awayScore             : AwayScore? = AwayScore(),
    @SerializedName("homeTeamStartingSide"  ) var homeTeamStartingSide  : Int?       = null,
    @SerializedName("hasCompleteStatistics" ) var hasCompleteStatistics : Boolean?   = null,
    @SerializedName("id"                    ) var id                    : Int?       = null,
    @SerializedName("startTimestamp"        ) var startTimestamp        : Int?       = null

)
data class AwayScore (

    @SerializedName("display" ) var display : Int? = null,
    @SerializedName("period1" ) var period1 : Int? = null,
    @SerializedName("period2" ) var period2 : Int? = null

)

data class HomeScore (

    @SerializedName("display" ) var display : Int? = null,
    @SerializedName("period1" ) var period1 : Int? = null,
    @SerializedName("period2" ) var period2 : Int? = null

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
data class  Event (

    @SerializedName("tournament"                      ) var tournament                      : Tournament? = Tournament(),
    @SerializedName("customId"                        ) var customId                        : String?     = null,
    @SerializedName("status"                          ) var status                          : Status?     = Status(),
    @SerializedName("winnerCode"                      ) var winnerCode                      : Int?        = null,
    @SerializedName("homeTeam"                        ) var homeTeam                        : Team   = Team(),
    @SerializedName("awayTeam"                        ) var awayTeam                        : Team   = Team(),
    @SerializedName("homeScore"                       ) var homeScore                       : Score?  = Score(),
    @SerializedName("awayScore"                       ) var awayScore                       : Score?  = Score(),
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




////////////////////////////////////////////////////////////////////////////////////////////
//Below are for the "event lineup" API endpoint
//https://rapidapi.com/fluis.lacasse/api/allsportsapi2/
////////////////////////////////////////////////////////////////////////////////////////////



data class Player (

    @SerializedName("name"                 ) var name                 : String?  = null,
    @SerializedName("firstName"            ) var firstName            : String?  = null,
    @SerializedName("lastName"             ) var lastName             : String?  = null,
    @SerializedName("slug"                 ) var slug                 : String?  = null,
    @SerializedName("shortName"            ) var shortName            : String?  = null,
    @SerializedName("userCount"            ) var userCount            : Int?     = null,
    @SerializedName("id"                   ) var id                   : Int?     = null,
    @SerializedName("country"              ) var country              : Country? = Country(),
    @SerializedName("marketValueCurrency"  ) var marketValueCurrency  : String?  = null,
    @SerializedName("dateOfBirthTimestamp" ) var dateOfBirthTimestamp : Int?     = null

)

data class Player_orsub (

    @SerializedName("player"     ) var player     : Player?  = Player(),
    @SerializedName("substitute" ) var substitute : Boolean? = null

)

data class PlayerColor (

    @SerializedName("primary"     ) var primary     : String? = null,
    @SerializedName("number"      ) var number      : String? = null,
    @SerializedName("outline"     ) var outline     : String? = null,
    @SerializedName("fancyNumber" ) var fancyNumber : String? = null

)

/**
 * I have no idea how the goalkeeper is relevant to CSGO, but this is what it is
 */
data class PlayerGroup (

    @SerializedName("players"         ) var players         : ArrayList<Player_orsub> = arrayListOf(),
    @SerializedName("playerColor"     ) var playerColor     : PlayerColor?       = PlayerColor(),
    @SerializedName("goalkeeperColor" ) var goalkeeperColor : PlayerColor?       = PlayerColor()

)