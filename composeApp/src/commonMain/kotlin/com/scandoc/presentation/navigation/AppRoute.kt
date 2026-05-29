package com.scandoc.presentation.navigation

sealed interface AppRoute {
    val path: String

    data object Splash : AppRoute {
        override val path: String = "splash"
    }

    data object Library : AppRoute {
        override val path: String = "library"
    }

    data object Home : AppRoute {
        override val path: String = "home"
    }

    data object Tools : AppRoute {
        override val path: String = "tools"
    }

    data object Me : AppRoute {
        override val path: String = "me"
    }

    data object Camera : AppRoute {
        override val path: String = "camera"
    }

    data class Crop(val imageBytes: ByteArray) : AppRoute {
        override val path: String = "crop"

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Crop) return false
            return imageBytes.contentEquals(other.imageBytes)
        }

        override fun hashCode(): Int = imageBytes.contentHashCode()
    }

    data class Viewer(val documentId: String) : AppRoute {
        override val path: String = "viewer/$documentId"
    }
}
