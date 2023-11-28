package com.example.hltv.data.local

import com.example.hltv.data.remote.Team

data class avgStatsOfTeam(
    val avgAdr: Double? = 0.0,
    val avgAssists: Int? = 0,
    val avgDeaths: Int? = 0,
    val avgFirstKillsDiff: Int? = 0,
    val avgFlashAssists: Int? = 0,
    val avgHeadshots: Int? = 0,
    val avgKdDiff: Int? = 0,
    val avgKast: Int? = 0,
    val avgKills: Int? = 0,
)