package com.example.albumphotos.ui.generic.extension

import android.content.res.Resources
import android.os.Build
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.WindowInsets
import androidx.annotation.Px
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams

fun View.adjustForInsets(
    leftInsets: Boolean? = null,
    topInsets: Boolean? = null,
    rightInsets: Boolean? = null,
    bottomInsets: Boolean? = null,
    checkSystemBarsInsets: Boolean? = null,
    checkGestureInsets: Boolean? = null,
    checkDisplayCutoutInsets: Boolean? = null,
    checkImeInsets: Boolean? = null,
    useMargin: Boolean? = null,
    @Px leftSpacing: Float? = null,
    @Px topSpacing: Float? = null,
    @Px rightSpacing: Float? = null,
    @Px bottomSpacing: Float? = null,
) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        useSystemBarDimensAdjustment(topInsets, bottomInsets, useMargin, topSpacing, bottomSpacing)
    } else {
        useInsetsForAdjustment(
            leftInsets,
            topInsets,
            rightInsets,
            bottomInsets,
            checkSystemBarsInsets,
            checkGestureInsets,
            checkDisplayCutoutInsets,
            checkImeInsets,
            useMargin,
            leftSpacing,
            topSpacing,
            rightSpacing,
            bottomSpacing
        )
    }
}

private fun View.useSystemBarDimensAdjustment(
    topInsets: Boolean?,
    bottomInsets: Boolean?,
    useMargin: Boolean?,
    topSpacing: Float?,
    bottomSpacing: Float?,
) {
    if (topInsets == true) {
        val statusBarHeight = resources.statusBarHeight
        val actualSpacing = statusBarHeight + (topSpacing ?: 0).toInt()
        if (useMargin == true) {
            updateLayoutParams<MarginLayoutParams> { topMargin = actualSpacing }
        } else {
            setPaddingTop(actualSpacing)
        }
    }

    if (bottomInsets == true) {
        val navBarHeight = resources.navigationBarHeight
        val actualSpacing = navBarHeight + (bottomSpacing ?: 0).toInt()
        if (useMargin == true) {
            updateLayoutParams<MarginLayoutParams> { bottomMargin = actualSpacing }
        } else {
            setPaddingBottom(actualSpacing)
        }
    }
}

private fun View.useInsetsForAdjustment(
    leftInsets: Boolean?,
    topInsets: Boolean?,
    rightInsets: Boolean?,
    bottomInsets: Boolean?,
    checkSystemBarsInsets: Boolean?,
    checkGestureInsets: Boolean?,
    checkDisplayCutoutInsets: Boolean?,
    checkImeInsets: Boolean?,
    useMargin: Boolean?,
    @Px leftSpacing: Float?,
    @Px topSpacing: Float?,
    @Px rightSpacing: Float?,
    @Px bottomSpacing: Float?,
) {
    val safeLeftSpacing = leftSpacing?.toInt() ?: 0
    val safeTopSpacing = topSpacing?.toInt() ?: 0
    val safeRightSpacing = rightSpacing?.toInt() ?: 0
    val safeBottomSpacing = bottomSpacing?.toInt() ?: 0

    withInsets(checkSystemBarsInsets, checkGestureInsets, checkDisplayCutoutInsets, checkImeInsets) { insets ->
        if (useMargin == true) {
            updateLayoutParams<MarginLayoutParams> {
                leftInsets?.let { leftMargin = if (it) insets.left + safeLeftSpacing else safeLeftSpacing }
                topInsets?.let { topMargin = if (it) insets.top + safeTopSpacing else safeTopSpacing }
                rightInsets?.let { rightMargin = if (it) insets.right + safeRightSpacing else safeRightSpacing }
                bottomInsets?.let { bottomMargin = if (it) insets.bottom + safeBottomSpacing else safeBottomSpacing }
            }
        } else {
            leftInsets?.let { setPaddingLeft(if (it) insets.left + safeLeftSpacing else safeLeftSpacing) }
            topInsets?.let { setPaddingTop(if (it) insets.top + safeTopSpacing else safeTopSpacing) }
            rightInsets?.let { setPaddingRight(if (it) insets.right + safeRightSpacing else safeRightSpacing) }
            bottomInsets?.let { setPaddingBottom(if (it) insets.bottom + safeBottomSpacing else safeBottomSpacing) }
        }
    }
    requestApplyInsetsWhenAttached()
}

fun View.withInsets(
    systemBarsInsets: Boolean? = null,
    gestureInsets: Boolean? = null,
    displayCutoutInsets: Boolean? = null,
    imeInsets: Boolean? = null,
    applier: (Insets) -> Unit,
) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { _, windowInsets ->
        windowInsets.getInsets(systemBarsInsets, gestureInsets, displayCutoutInsets, imeInsets)?.let(applier::invoke)
        windowInsets
    }
}

fun WindowInsetsCompat.getInsets(
    systemBarsInsets: Boolean? = null,
    gestureInsets: Boolean? = null,
    displayCutoutInsets: Boolean? = null,
    imeInsets: Boolean? = null,
): Insets? {
    var insetMask = 0
    if (systemBarsInsets == true) insetMask = insetMask or WindowInsetsCompat.Type.systemBars()
    if (gestureInsets == true) insetMask = insetMask or WindowInsetsCompat.Type.systemGestures()
    if (displayCutoutInsets == true) insetMask = insetMask or WindowInsetsCompat.Type.displayCutout()
    if (imeInsets == true) insetMask = insetMask or WindowInsetsCompat.Type.ime()

    return if (insetMask != 0) getInsets(insetMask) else null
}

fun WindowInsets.toCompat() = WindowInsetsCompat.toWindowInsetsCompat(this)

fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        requestApplyInsets()
    } else {
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {

            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) {
                // no-op
            }
        })
    }
}

/* Some resource extensions */

private const val DIMEN_STATUS_BAR_HEIGHT = "status_bar_height"
private const val DIMEN_NAV_BAR_HEIGHT = "navigation_bar_height"
private const val ANDROID_DIMEN = "dimen"
private const val ANDROID_PACKAGE = "android"

internal val Resources.statusBarHeight: Int
    get() = getAndroidDimensionPixelSize(DIMEN_STATUS_BAR_HEIGHT)

internal val Resources.navigationBarHeight: Int
    get() = getAndroidDimensionPixelSize(DIMEN_NAV_BAR_HEIGHT)

private fun Resources.getAndroidDimensionPixelSize(name: String): Int {
    return getIdentifier(name, ANDROID_DIMEN, ANDROID_PACKAGE)
        .takeIf { it > 0 }
        ?.let(::getDimensionPixelSize)
        ?: 0
}


/* Some view extensions */

fun View.setPaddingLeft(@Px padding: Int) {
    setPadding(padding, paddingTop, paddingRight, paddingBottom)
}

fun View.setPaddingTop(@Px padding: Int) {
    setPadding(paddingLeft, padding, paddingRight, paddingBottom)
}

fun View.setPaddingRight(@Px padding: Int) {
    setPadding(paddingLeft, paddingTop, padding, paddingBottom)
}

fun View.setPaddingBottom(@Px padding: Int) {
    setPadding(paddingLeft, paddingTop, paddingRight, padding)
}
