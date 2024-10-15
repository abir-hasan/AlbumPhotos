package com.example.albumphotos.presentation.generic

import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.ui.res.pluralStringResource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

/**
 * Representation of a piece of text that supports localisation and pluralisation. Allows for the combining of different
 * "types" of text. See implementations for more details.
 */
@Stable
sealed class StringUIModel {

    /**
     * Representation of an Android string resource. Expects a valid string resource identifier to be passed to [resId],
     * optionally supports formatting arguments via [formatArgs].
     */
    @Immutable
    data class Res(
        @StringRes val resId: Int,
        val formatArgs: ImmutableList<Any>,
    ) : StringUIModel() {

        constructor(
            @StringRes resId: Int,
            vararg formatArgs: Any,
        ) : this(resId = resId, formatArgs = formatArgs.toList().toImmutableList())
    }

    /**
     * Representation of plain text. Doesn't support localisation, pluralisation or arguments.
     */
    @Immutable
    data class Raw(val value: String) : StringUIModel()

    /**
     * Representation of text containing a [quantity]. [quantity] is only used to determine which plural form should be
     * selected, if your text also contains the [quantity] as a formatting arg then you should pass it to [formatArgs]
     * as well. Expects a valid plural resource identifier to be passed to [resId].
     */
    @Immutable
    data class Plural(
        @PluralsRes val resId: Int,
        val quantity: Int,
        val formatArgs: ImmutableList<Any>,
    ) : StringUIModel() {

        constructor(
            @PluralsRes resId: Int,
            quantity: Int,
            vararg formatArgs: Any,
        ) : this(resId = resId, quantity = quantity, formatArgs = formatArgs.toList().toImmutableList())
    }

    /**
     * Represents a collection of concatenated [StringUIModel]s. The order of the [sections] is the order in which they
     * will be placed in the final text.
     */
    @Immutable
    data class Combined internal constructor(
        val sections: ImmutableList<StringUIModel>,
    ) : StringUIModel()

    /**
     * Concatenate [other] to the end of `this`.
     */
    operator fun plus(other: StringUIModel): Combined = when {
        this is Combined -> Combined(
            (if (other is Combined) sections + other.sections else sections + other).toImmutableList()
        )
        other is Combined -> Combined((listOf(this) + other.sections).toImmutableList())
        else -> Combined(persistentListOf(this, other))
    }
}



/**
 * Convert a [StringUIModel] to a [String]. All types of [StringUIModel] are supported. See [StringUIModel] and its
 * implementations for more details.
 */
@Composable
@ReadOnlyComposable
fun stringResource(model: StringUIModel): String = when (model) {
    is StringUIModel.Combined -> buildString {
        for (section in model.sections) append(stringResource(section))
    }
    is StringUIModel.Plural -> {
        val args = resolveArguments(model.formatArgs)
        if (args.isEmpty()) {
            pluralStringResource(id = model.resId, count = model.quantity)
        } else {
            pluralStringResource(id = model.resId, count = model.quantity, formatArgs = args.toTypedArray())
        }
    }
    is StringUIModel.Raw -> model.value
    is StringUIModel.Res -> {
        val args = resolveArguments(model.formatArgs)
        if (args.isEmpty()) {
            androidx.compose.ui.res.stringResource(id = model.resId)
        } else {
            androidx.compose.ui.res.stringResource(id = model.resId, formatArgs = args.toTypedArray())
        }
    }
}

@Composable
@ReadOnlyComposable
private fun resolveArguments(args: ImmutableList<Any>): List<Any> = args.map {
    if (it is StringUIModel) stringResource(it) else it
}
