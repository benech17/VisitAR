package fr.yaniv.visitar

data class ParcoursModel(
    val name: String = "Parcours 1 - Paris centre",
    val descrption: String = "Petite description",
    val imageUrl: String = "https://cdn.pixabay.com/photo/2017/04/23/20/36/tulips-2254970__480.jpg",
    var liked: Boolean = true
)