package fr.yaniv.visitar.data

data class WaypointData (
        val description: String,
        val horaires: String,
        val affluence: String,
        val tarif: String,
        val photos: List<Photo>
        )