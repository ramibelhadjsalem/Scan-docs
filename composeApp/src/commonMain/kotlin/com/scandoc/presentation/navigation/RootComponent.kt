package com.scandoc.presentation.navigation

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    fun navigateToCamera()
    fun navigateToCrop(imageBytes: ByteArray)
    fun navigateToViewer(documentId: String)
    fun navigateBack()

    sealed interface Child {
        data object LibraryChild : Child
        data object CameraChild : Child
        data class CropChild(val imageBytes: ByteArray) : Child
        data class ViewerChild(val documentId: String) : Child
    }
}
