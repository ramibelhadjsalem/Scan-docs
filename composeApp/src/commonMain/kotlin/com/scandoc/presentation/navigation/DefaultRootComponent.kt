package com.scandoc.presentation.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value

class DefaultRootComponent(
    componentContext: ComponentContext,
) : RootComponent, ComponentContext by componentContext {

    // Config is not @Serializable because Crop carries ByteArray — state is not persisted
    // across process death, which is acceptable for a scanner app.
    private sealed interface Config {
        data object Library : Config
        data object Camera : Config
        data class Crop(val imageBytes: ByteArray) : Config
        data class Viewer(val documentId: String) : Config
    }

    private val navigation = StackNavigation<Config>()

    private val _stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        serializer = null,
        initialConfiguration = Config.Library,
        handleBackButton = true,
        childFactory = ::createChild,
    )

    override val stack: Value<ChildStack<*, RootComponent.Child>> = _stack

    private fun createChild(
        config: Config,
        @Suppress("UNUSED_PARAMETER") context: ComponentContext,
    ): RootComponent.Child = when (config) {
        Config.Library -> RootComponent.Child.LibraryChild
        Config.Camera -> RootComponent.Child.CameraChild
        is Config.Crop -> RootComponent.Child.CropChild(config.imageBytes)
        is Config.Viewer -> RootComponent.Child.ViewerChild(config.documentId)
    }

    override fun navigateToCamera() {
        navigation.push(Config.Camera)
    }

    override fun navigateToCrop(imageBytes: ByteArray) {
        navigation.push(Config.Crop(imageBytes))
    }

    override fun navigateToViewer(documentId: String) {
        navigation.push(Config.Viewer(documentId))
    }

    override fun navigateBack() {
        navigation.pop()
    }
}
